package com.quintet.laddergame.bean

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset

/**
 * Ladder Line Data Class
 *
 * 2024-07-14 Created by sy.lee
 */
@Keep
data class LadderLine(
    val playerIndex: Int? = null, // 각 플레이어의 인덱스
    val startPoint: Offset? = null,  // 각 플레이어별 vertical startPoint
    val endPoint: Offset? = null,     // 각 플레이어별 vertical endPoint
    val vertical: VerticalLine? = null,  // 세로줄
    val horizontal: HorizontalLine? = null, // 가로줄
)

// 세로줄을 나타내는 데이터 클래스
@Keep
data class VerticalLine(
    val start: Offset, // 시작점
    val end: Offset    // 끝점
)

// 가로줄을 나타내는 데이터 클래스
@Keep
data class HorizontalLine(
    val start: Offset, // 시작점
    val end: Offset    // 끝점
)
