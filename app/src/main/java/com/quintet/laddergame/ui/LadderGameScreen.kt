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
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(playerInfo.playerCount) {
        // 비동기적으로 사다리 데이터를 가져옴
        ladderData = withContext(Dispatchers.Default) {
            generateLadderData(playerInfo.playerCount)
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawPlayers(playerInfo.playerCount, shuffledPlayerNames)
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격

        if (isLoading) {
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
                .fillMaxWidth().padding(horizontal = 20.dp),
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

fun generateLadderData(playerCount: Int): List<List<LadderLine>> {
    val ladderData = mutableListOf<List<LadderLine>>()
    val ladderWidth = 1f / (playerCount + 1)
    val stepHeight = 1f / 11  // 총 11개 구간으로 나눔
    val random = Random

    // List to store horizontal lines for each column
    val horizontalLinesForEachColumn = MutableList(playerCount) { mutableSetOf<Int>() }

    // 세로줄마다 가로줄을 생성하는 부분 최적화
    for (i in 0 until playerCount - 1) {
        val x = (i + 1) * ladderWidth

        // 이전 세로줄과 겹치지 않도록 랜덤한 가로줄 생성
        val previousHorizontalLines = horizontalLinesForEachColumn.getOrNull(i - 1) ?: emptySet()

        // 1~5개 가로줄을 랜덤하게 생성
        val horizontalLineCount = random.nextInt(5) + 1
        val horizontalLines = mutableSetOf<Int>()

        // 중복을 최소화하는 방식으로 가로줄 생성
        while (horizontalLines.size < horizontalLineCount) {
            val y = random.nextInt(10) + 1
            if (!previousHorizontalLines.contains(y)) {
                horizontalLines.add(y)
            }
        }

        // 생성된 가로줄을 ladderData에 추가
        val columnLadderLines = mutableListOf<LadderLine>()
        for (line in horizontalLines) {
            val y = line * stepHeight
            columnLadderLines.add(LadderLine(Offset(x, y), Offset(x + ladderWidth, y)))
            horizontalLinesForEachColumn[i].add(line)
        }
        ladderData.add(columnLadderLines)
    }

    // 세로줄 추가 (플레이어마다 한 개의 세로줄)
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth
        ladderData.add(listOf(LadderLine(Offset(x, 0f), Offset(x, 1f))))
    }

    return ladderData
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
