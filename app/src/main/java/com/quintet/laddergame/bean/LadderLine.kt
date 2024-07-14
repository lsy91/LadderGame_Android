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
    val start: Offset,
    val end: Offset
)
