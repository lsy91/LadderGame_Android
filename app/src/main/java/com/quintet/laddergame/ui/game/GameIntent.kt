package com.quintet.laddergame.ui.game

import com.quintet.laddergame.model.LadderLine

sealed class GameIntent {
    data class LoadLadders(val ladderLines: List<LadderLine>): GameIntent()
}