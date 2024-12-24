package com.quintet.laddergame.ui.game

import com.quintet.laddergame.model.LadderLine
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner

sealed class GameIntent {
    data class InitData(val players: List<Player>, val winners: List<Winner>): GameIntent()
    data class LoadLadders(val playerCount: Int): GameIntent()
    data class SetPlayerAndWinnerPosition(val ladders: List<LadderLine>): GameIntent()
}