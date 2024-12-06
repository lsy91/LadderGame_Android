package com.quintet.laddergame.ui.player

sealed class PlayerIntent {
    data class LoadPlayers(val playerCount: Int): PlayerIntent()
}
