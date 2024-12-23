package com.quintet.laddergame.ui.game

import androidx.annotation.Keep
import com.quintet.laddergame.model.LadderLine

@Keep
data class GameState(
    val isLoading: Boolean = true,
    val ladders: List<LadderLine> = emptyList(),
    val errorMessages: List<String> = emptyList(),
)