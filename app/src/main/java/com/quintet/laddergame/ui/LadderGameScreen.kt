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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Ladder Game Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun LadderGameScreen(
    navController: NavController,
    playerCount: Int,
    playerNames: List<String>,
    winnerCount: Int,
    winnerTitles: List<String>
) {
    val shuffledPlayerNames = remember { playerNames.shuffled() }
    val shuffledWinnerTitles = remember { winnerTitles.shuffled() }

    Column(modifier = Modifier.fillMaxSize().background(Color.Gray)) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val ladderWidth = size.width / (playerCount + 1)
            val ladderHeight = size.height / (winnerCount + 1)
            val stroke = Stroke(4.dp.toPx())

            // Draw vertical lines
            for (i in 0 until playerCount) {
                drawLine(
                    Color.White,
                    start = Offset((i + 1) * ladderWidth, 0f),
                    end = Offset((i + 1) * ladderWidth, size.height),
                    strokeWidth = stroke.width
                )
                val playerName = shuffledPlayerNames.getOrElse(i) { "" }
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        playerName,
                        (i + 1) * ladderWidth - ladderWidth / 2,
                        16f,
                        Paint().asFrameworkPaint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 16f
                            color = android.graphics.Color.BLACK
                            typeface = Typeface.DEFAULT
                        }
                    )
                }
            }

            // Draw horizontal lines
            for (i in 0 until winnerCount) {
                drawLine(
                    Color.White,
                    start = Offset(ladderWidth, (i + 1) * ladderHeight),
                    end = Offset(size.width - ladderWidth, (i + 1) * ladderHeight),
                    strokeWidth = stroke.width
                )
                val winnerTitle = shuffledWinnerTitles.getOrElse(i) { "" }
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        winnerTitle,
                        (i + 1) * ladderWidth - ladderWidth / 2,
                        size.height - 8f,
                        Paint().asFrameworkPaint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 16f
                            color = android.graphics.Color.BLACK
                            typeface = Typeface.DEFAULT
                        }
                    )
                }

                // Draw additional horizontal line in the middle for each column if playerCount > 1
                if (playerCount > 1 && i < winnerCount - 1) {
                    drawLine(
                        Color.White,
                        start = Offset(ladderWidth, (i + 1) * ladderHeight + ladderHeight / 2),
                        end = Offset(size.width - ladderWidth, (i + 1) * ladderHeight + ladderHeight / 2),
                        strokeWidth = stroke.width
                    )
                }
            }
        }

        Button(
            onClick = { /* navigate back or perform action */ },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Start Game")
        }
    }
}