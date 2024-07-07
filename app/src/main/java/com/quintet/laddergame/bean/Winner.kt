package com.quintet.laddergame.bean

import androidx.annotation.Keep

/**
 * Winner Data Class
 *
 * 2024-07-07 Created by sy.lee
 */
@Keep
data class Winner(
    val winnerCount: Int,
    val winnerPrizes: List<String>
)
