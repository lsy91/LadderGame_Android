package com.quintet.laddergame.ui.player

sealed class PlayerIntent {
    data class LoadPlayers(val inputPlayerCount: Int): PlayerIntent()
    data object ClearPlayers: PlayerIntent()
}
