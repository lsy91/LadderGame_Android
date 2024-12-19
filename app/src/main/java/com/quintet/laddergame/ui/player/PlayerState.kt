package com.quintet.laddergame.ui.player

import androidx.annotation.Keep
import com.quintet.laddergame.model.Player

@Keep
data class PlayerState(
    val players: List<Player> = emptyList(),
    val errorMessages: List<String> = emptyList(),
)