package com.quintet.laddergame.model

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset

/**
 *  Player Info Data Class
 *
 * 2024-07-07 Created by sy.lee
 */
@Keep
data class Player(
    val playerIndex: Int,
    val playerId: Int,
    val startPoint: Offset? = Offset(0f,0f),  // 각 플레이어별 vertical startPoint
    val endPoint: Offset? = Offset(0f,0f),    // 각 플레이어별 vertical endPoint
)