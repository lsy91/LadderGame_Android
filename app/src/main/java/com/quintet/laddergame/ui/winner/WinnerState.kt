package com.quintet.laddergame.ui.winner

import androidx.annotation.Keep
import com.quintet.laddergame.model.Winner

@Keep
data class WinnerState(
    val winners: List<Winner> = emptyList(),
    val errorMessages: List<String> = emptyList(),
)