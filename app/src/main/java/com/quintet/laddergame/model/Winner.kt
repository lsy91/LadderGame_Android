package com.quintet.laddergame.model

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset

/**
 * Winner Data Class
 *
 * 2024-07-07 Created by sy.lee
 */
@Keep
data class Winner(
    val isWinner: Boolean = false,
    val point: Offset? = Offset(0f,0f)
)
