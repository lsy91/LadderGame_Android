package com.quintet.laddergame.ui.winner

sealed class WinnerIntent {
    data class LoadWinners(val inputWinnerCount: Int, val nothingCount: Int): WinnerIntent()
    data object ClearWinners: WinnerIntent()
}