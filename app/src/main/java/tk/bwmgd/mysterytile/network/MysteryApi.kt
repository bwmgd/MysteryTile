package tk.bwmgd.mysterytile.network

import fuel.Fuel
import fuel.get
import fuel.post
import kotlinx.io.readString

object MysteryApi {
    private const val HOST = "127.0.0.1"
    private const val PORT = "23333"
    private const val PATH = "/api/kernel"

    const val URL = "http://$HOST:$PORT"

    private const val AUTH = "node"

    private const val METHOD_START = "start"
    private const val METHOD_STOP = "stop"

    private val headers = mapOf("authorization" to AUTH, "Content-Type" to "application/json")

    suspend fun status(): Boolean {
        return resolveResult(Fuel.get("$URL$PATH", headers = headers).source.readString())
    }

    suspend fun start(): Boolean {
        postNetwork(METHOD_START)
        return status()
    }

    suspend fun stop(): Boolean {
        postNetwork(METHOD_STOP)
        return !status()
    }

    private fun resolveResult(result: String): Boolean {
        return result.contains("working")
    }

    private suspend fun postNetwork(method: String): String = Fuel.post(
        "$URL$PATH",
        body = "{ \"method\" : \"$method\" }",
        headers = headers
    ).source.readString()
}