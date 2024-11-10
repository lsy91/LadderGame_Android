package com.quintet.laddergame.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quintet.laddergame.bean.LadderLine
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.Winner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

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
    val gameElementsPadding = 16.dp

    var gameInProgress by remember { mutableStateOf(false) }
    val animatedPosition = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var ladderData by remember { mutableStateOf<List<List<LadderLine>>>(emptyList()) }

    LaunchedEffect(playerInfo.playerCount) {
        // Flow를 통해 ladderData를 비동기적으로 가져오기
        val flowResult = flow {
            emit(generateLadderData(playerInfo.playerCount))
        }

        // collect 함수를 사용하여 Flow의 데이터를 받아옴
        flowResult.collect { newData ->
            ladderData = newData
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawPlayers(playerInfo.playerCount, shuffledPlayerNames)
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격

        if (ladderData.isEmpty()) {
            // 로딩 중일 때 보여질 로딩 바
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            // 사다리 그리기
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = gameElementsPadding)
            ) {
                ladderData.forEach { column ->
                    drawLadder(animatedPosition.value, column)
                }
            }
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawWinners(playerInfo.playerCount, shuffledWinnerTitles)
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격

        Button(
            onClick = {
                if (!gameInProgress) {
                    scope.launch {
                        animatedPosition.snapTo(0f)
                        animatedPosition.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 1000)
                        )
                        gameInProgress = false
                    }
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

@Composable
fun DrawPlayers(playerCount: Int, shuffledPlayerNames: List<String>) {
    for (i in 0 until playerCount) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = shuffledPlayerNames.getOrElse(i) { "" },
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun DrawWinners(playerCount: Int, shuffledWinnerTitles: List<String>) {
    for (i in 0 until playerCount) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = shuffledWinnerTitles.getOrElse(i) { "꽝" },
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

suspend fun generateLadderData(playerCount: Int): List<List<LadderLine>> = withContext(Dispatchers.Default) {
    val ladderData = mutableListOf<List<LadderLine>>()
    val ladderWidth = 1f / (playerCount + 1)
    val stepHeight = 1f / 11  // 총 11개 구간으로 나눔
    val random = Random

    // List to store horizontal lines for each column
    val horizontalLinesForEachColumn = MutableList(playerCount) { mutableSetOf<Int>() }

    // Generate horizontal lines for each column
    for (i in 0 until playerCount - 1) { // 마지막 세로줄에서는 가로줄을 그리지 않음
        val x = (i + 1) * ladderWidth

        // Check previous column's horizontal lines
        val previousHorizontalLines = horizontalLinesForEachColumn.getOrNull(i - 1) ?: emptySet()

        // Generate random number of horizontal lines (1 to 9)
        val horizontalLineCount = random.nextInt(9) + 1
        val horizontalLines = mutableSetOf<Int>()

        // Generate unique y coordinates for horizontal lines
        while (horizontalLines.size < horizontalLineCount) {
            val y = random.nextInt(10) + 1 // 1 to 10
            // Check if y coordinate is already used by the previous column's lines
            val overlapping = previousHorizontalLines.any { it == y }
            if (!overlapping) {
                horizontalLines.add(y)
            }
        }

        // Add generated horizontal lines to ladderLines
        val columnLadderLines = mutableListOf<LadderLine>()
        for (line in horizontalLines) {
            val y = line * stepHeight
            columnLadderLines.add(LadderLine(Offset(x, y), Offset(x + ladderWidth, y)))
            horizontalLinesForEachColumn[i].add(line)
        }
        ladderData.add(columnLadderLines)
    }

    // Generate vertical lines
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth
        ladderData.add(listOf(LadderLine(Offset(x, 0f), Offset(x, 1f))))
    }

    return@withContext ladderData
}

fun DrawScope.drawLadder(animatedValue: Float, ladderLines: List<LadderLine>) {
    val stroke = Stroke(4.dp.toPx())
    val ladderHeight = size.height
    val animatedHeight = ladderHeight * animatedValue

    // Draw ladder lines from precomputed data
    ladderLines.forEach { line ->
        val start = Offset(line.start.x * size.width, line.start.y * ladderHeight)
        val end = Offset(line.end.x * size.width, line.end.y * ladderHeight)

        drawLine(
            Color.White,
            start = start,
            end = end,
            strokeWidth = stroke.width
        )

        if (line.start.y == 0f && line.end.y == 1f) {
            // Draw animated red line for vertical lines
            val animatedEnd = Offset(line.end.x * size.width, animatedHeight)
            drawLine(
                Color.Red,
                start = start,
                end = animatedEnd,
                strokeWidth = stroke.width
            )
        }
    }
}