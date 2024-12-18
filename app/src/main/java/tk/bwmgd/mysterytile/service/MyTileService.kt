package tk.bwmgd.mysterytile.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tk.bwmgd.mysterytile.network.MysteryApi

class MyTileService : TileService() {
    override fun onClick() {
        super.onClick()
        qsTile.apply {
            CoroutineScope(Dispatchers.IO).launch {
                val temp = state
                state = Tile.STATE_UNAVAILABLE
                updateTile()
                state = when {
                    temp == Tile.STATE_INACTIVE && MysteryApi.start() -> Tile.STATE_ACTIVE
                    temp == Tile.STATE_ACTIVE && MysteryApi.stop() -> Tile.STATE_INACTIVE
                    else -> temp
                }
                updateTile()
            }
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        CoroutineScope(Dispatchers.IO).launch {
            qsTile.state = if (MysteryApi.status()) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            }
            qsTile.updateTile()
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        qsTile.updateTile()
    }
}