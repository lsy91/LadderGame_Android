package com.quintet.laddergame.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.Winner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    // State to track the selected player index
    var selectedPlayerIndex by remember { mutableStateOf(-1) }

    // State to track the current position in ladder simulation
    var currentPosition by remember { mutableStateOf(0) }
    var gameInProgress by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(gameElementsPadding)
        ) {
            drawLadder(playerInfo.playerCount, winnerInfo.winnerCount)
            drawPlayers(density, playerInfo.playerCount, shuffledPlayerNames)
            drawWinners(density, playerInfo.playerCount, shuffledWinnerTitles, size.height)
        }

        Button(
            onClick = {
                if (!gameInProgress) {
                    startGame(playerInfo.playerCount)
                    gameInProgress = true
                }
            },
            modifier = Modifier
                .padding(gameElementsPadding)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Start Game")
        }
    }
}

fun DrawScope.drawLadder(playerCount: Int, winnerCount: Int) {
    val ladderWidth = size.width / (playerCount + 1)
    val ladderHeight = size.height / (winnerCount + 1)
    val stroke = Stroke(4.dp.toPx())

    // Draw vertical lines
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth
        drawLine(
            Color.White,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = stroke.width
        )
    }

    // Draw horizontal lines
    for (i in 0 until winnerCount) {
        val y = (i + 1) * ladderHeight
        drawLine(
            Color.White,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = stroke.width
        )
    }
}

fun DrawScope.drawPlayers(density: Density, playerCount: Int, shuffledPlayerNames: List<String>) {
    val ladderWidth = size.width / (playerCount + 1)
    val ladderHeight = size.height

    // Draw player names
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth

        // Measure player name text width
        val playerTextWidth = with(density) {
            android.graphics.Paint().apply {
                textSize = this@drawPlayers.density * 48f // gameElementsTextSize
            }.measureText(shuffledPlayerNames.getOrElse(i) { "" })
        }

        val playerTextMinWidth = 120f // 플레이어 텍스트 최소 너비 설정

        // Draw player name background box
        drawRoundRect(
            color = Color.Red,
            topLeft = Offset(x - (playerTextWidth / 2).coerceAtLeast(playerTextMinWidth / 2) - 8f, 10f),
            size = androidx.compose.ui.geometry.Size(playerTextWidth.coerceAtLeast(playerTextMinWidth) + 16f, 80f),
            cornerRadius = CornerRadius(16.dp.toPx())
        )

        // Draw player name
        drawContext.canvas.nativeCanvas.drawText(
            shuffledPlayerNames.getOrElse(i) { "" },
            x,
            20f + 40f, // 박스의 중앙에 위치하도록 조정
            android.graphics.Paint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = this@drawPlayers.density * 48f // gameElementsTextSize
                color = Color.Black.toArgb()
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
        )
    }
}

fun DrawScope.drawWinners(density: Density, playerCount: Int, shuffledWinnerTitles: List<String>, height: Float) {
    val ladderWidth = size.width / (playerCount + 1)
    val ladderHeight = size.height

    // Draw winner titles below the ladder
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth

        // Measure winner title text width
        val winnerTextWidth = with(density) {
            android.graphics.Paint().apply {
                textSize = this@drawWinners.density * 48f // gameElementsTextSize
            }.measureText(shuffledWinnerTitles.getOrElse(i) { "꽝" })
        }

        val winnerTextMinWidth = 120f // 당첨 텍스트 최소 너비 설정

        // Draw winner title background box
        drawRoundRect(
            color = Color.Red,
            topLeft = Offset(x - (winnerTextWidth / 2).coerceAtLeast(winnerTextMinWidth / 2) - 8f, height - 110f),
            size = androidx.compose.ui.geometry.Size(winnerTextWidth.coerceAtLeast(winnerTextMinWidth) + 16f, 80f),
            cornerRadius = CornerRadius(16.dp.toPx())
        )

        // Draw winner title
        drawContext.canvas.nativeCanvas.drawText(
            shuffledWinnerTitles.getOrElse(i) { "꽝" },
            x,
            height - 100f + 40f, // 박스의 중앙에 위치하도록 조정
            android.graphics.Paint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = this@drawWinners.density * 48f // gameElementsTextSize
                color = Color.Black.toArgb()
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
        )
    }
}

fun startGame(playerCount: Int) {
    // Simulate ladder climbing with a delay
    var currentPosition = 0
    CoroutineScope(Dispatchers.Default).launch {
        repeat(playerCount - 1) {
            delay(200) // Delay for animation effect
            currentPosition++
        }
    }
}
