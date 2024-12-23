package com.quintet.laddergame.ui.game

import com.quintet.laddergame.model.LadderLine

sealed class GameIntent {
    data class LoadLadders(val playerCount: Int): GameIntent()
    data class SetPlayerPosition(val ladders: List<LadderLine>): GameIntent()
}