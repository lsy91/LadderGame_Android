package com.quintet.laddergame.ui.game

import androidx.annotation.Keep
import com.quintet.laddergame.model.LadderLine
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner

@Keep
data class GameState(
    val isLoading: Boolean = true,
    val ladders: List<LadderLine> = emptyList(),
    val players: List<Player> = emptyList(),
    val winners: List<Winner> = emptyList(),
    val errorMessages: List<String> = emptyList(),
)