package com.quintet.laddergame.ui

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.Winner

/**
 * Ladder Game Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun LadderGameScreen(
    playerInfo: Player,
    winnerInfo: Winner
) {
    val shuffledPlayerNames = remember { playerInfo.playerNames.shuffled() }
    val shuffledWinnerTitles = remember { winnerInfo.winnerPrizes.shuffled() }

    val backgroundColor = Color(0xFFFFC107) // 더 진한 노란색
    val gameElementsTextColor = Color.Black
    val gameElementsTextSize = 48f // 텍스트 크기
    val gameElementsPadding = 16.dp // 패딩

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(gameElementsPadding)
        ) {
            val ladderWidth = size.width / (playerInfo.playerCount + 1)
            val ladderHeight = size.height / (winnerInfo.winnerCount + 1)
            val stroke = Stroke(4.dp.toPx())

            // Draw vertical lines and player names
            for (i in 0 until playerInfo.playerCount) {
                val x = (i + 1) * ladderWidth

                // Measure player name text width
                val playerTextWidth = android.graphics.Paint().apply {
                    textSize = this@Canvas.drawContext.density.density * gameElementsTextSize
                }.measureText(shuffledPlayerNames.getOrElse(i) { "" })

                val playerTextMinWidth = 120f // 플레이어 텍스트 최소 너비 설정

                // Draw player name background box
                drawRoundRect(
                    color = backgroundColor,
                    topLeft = Offset(x - (playerTextWidth / 2).coerceAtLeast(playerTextMinWidth / 2) - 8f, 10f),
                    size = Size(playerTextWidth.coerceAtLeast(playerTextMinWidth) + 16f, 80f),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )

                // Draw player name
                drawContext.canvas.nativeCanvas.drawText(
                    shuffledPlayerNames.getOrElse(i) { "" },
                    x,
                    20f + 40f, // 박스의 중앙에 위치하도록 조정
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = this@Canvas.drawContext.density.fontScale * gameElementsTextSize
                        color = gameElementsTextColor.toArgb()
                        typeface = Typeface.DEFAULT_BOLD
                    }
                )

                // Draw vertical lines
                drawLine(
                    Color.White,
                    start = Offset(x, 120f),
                    end = Offset(x, size.height - 120f),
                    strokeWidth = stroke.width
                )
            }

            // Draw horizontal lines
            for (i in 0 until winnerInfo.winnerCount) {
                val y = (i + 1) * ladderHeight
                drawLine(
                    Color.White,
                    start = Offset(ladderWidth, y),
                    end = Offset(size.width - ladderWidth, y),
                    strokeWidth = stroke.width
                )

                // Draw additional horizontal line in the middle for each column if playerCount > 1
                if (playerInfo.playerCount > 1 && i < winnerInfo.winnerCount - 1) {
                    drawLine(
                        Color.White,
                        start = Offset(ladderWidth, y + ladderHeight / 2),
                        end = Offset(size.width - ladderWidth, y + ladderHeight / 2),
                        strokeWidth = stroke.width
                    )
                }
            }

            // Draw winner titles below the ladder
            for (i in 0 until playerInfo.playerCount) {
                val x = (i + 1) * ladderWidth

                // Measure winner title text width
                val winnerTextWidth = android.graphics.Paint().apply {
                    textSize = this@Canvas.drawContext.density.fontScale * gameElementsTextSize
                }.measureText(shuffledWinnerTitles.getOrElse(i) { "꽝" })

                val winnerTextMinWidth = 120f // 당첨 텍스트 최소 너비 설정

                // Draw winner title background box
                drawRoundRect(
                    color = backgroundColor,
                    topLeft = Offset(x - (winnerTextWidth / 2).coerceAtLeast(winnerTextMinWidth / 2) - 8f, size.height - 110f),
                    size = Size(winnerTextWidth.coerceAtLeast(winnerTextMinWidth) + 16f, 80f),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )

                // Draw winner title
                drawContext.canvas.nativeCanvas.drawText(
                    shuffledWinnerTitles.getOrElse(i) { "꽝" },
                    x,
                    size.height - 100f + 40f, // 박스의 중앙에 위치하도록 조정
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = this@Canvas.drawContext.density.fontScale * gameElementsTextSize
                        color = gameElementsTextColor.toArgb()
                        typeface = Typeface.DEFAULT_BOLD
                    }
                )
            }
        }

        Button(
            onClick = { /* navigate back or perform action */ },
            modifier = Modifier
                .padding(gameElementsPadding)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Start Game")
        }
    }
}
