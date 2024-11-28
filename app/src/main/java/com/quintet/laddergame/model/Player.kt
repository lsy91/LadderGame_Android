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
    val playerCount: Int = 0,
    val playerNames: List<String> = listOf()
)

@Keep
data class PlayerGameInfo(
    val playerIndex: Int? = null,      // 각 플레이어의 인덱스
    val startPoint: Offset? = null,  // 각 플레이어별 vertical startPoint
    val endPoint: Offset? = null,     // 각 플레이어별 vertical endPoint
)
