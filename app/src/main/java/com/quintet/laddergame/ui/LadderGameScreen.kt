package com.quintet.laddergame.ui

import android.util.Log
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
import com.quintet.laddergame.bean.HorizontalLine
import com.quintet.laddergame.bean.LadderLine
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.PlayerGameInfo
import com.quintet.laddergame.bean.VerticalLine
import com.quintet.laddergame.bean.Winner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
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

    var ladderData by remember { mutableStateOf<List<LadderLine>>(emptyList()) }
    var playerGameInfo by remember { mutableStateOf<List<PlayerGameInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(playerInfo.playerCount) {
        // ladderData를 비동기로 생성
        ladderData = withContext(Dispatchers.Default) {
            generateLadderData(playerInfo.playerCount)
        }

        // ladderData가 완료된 후 결과를 사용하여 playerGameInfo 생성
        playerGameInfo = generatePlayerGameInfo(ladderData)

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
                drawLadder(ladderData)

                calculatePlayerPaths(ladderData = ladderData, playersInfo = playerGameInfo)
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
                        // 애니메이션 초기화
                        animatedPosition.snapTo(0f)
                        // 애니메이션 시작
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

fun calculatePlayerPaths(
    ladderData: List<LadderLine>,
    playersInfo: List<PlayerGameInfo>
) {
    // ladderData 에서 이동 가능한 점들만 모은 리스트 생성 (중복 제거)
    val canMovePointList = ladderData.flatMap { line ->
        listOfNotNull(
            line.horizontal?.start,
            line.horizontal?.end,
            line.vertical?.start,
            line.vertical?.end
        )
    }.distinct()

    // 각 플레이어에 대해 경로 계산
    playersInfo.forEach { player ->
        var currentPosition = player.startPoint
        val playerPath = mutableListOf<Offset?>()
        playerPath.add(currentPosition)

        while (currentPosition?.y!! < 1f) {
            // 다음 수직 이동할 점 찾기
            val nextVerticalPoint = canMovePointList
                .filter { it.x == currentPosition?.x && it.y > currentPosition?.y!! }
                .minByOrNull { it.y - currentPosition?.y!! }

            // 이동한 위치를 경로에 추가
            playerPath.add(nextVerticalPoint)
            currentPosition = nextVerticalPoint

            // y 좌표가 1.0인 경우 이동 종료
            if (currentPosition?.y == 1.0f) break

            // 같은 y 좌표에 있는 다른 x 좌표의 점을 찾아서 수평 이동
            val nextHorizontalPoint = canMovePointList
                .filter { it.y == currentPosition?.y && it.x != currentPosition?.x }
                .minByOrNull { abs(it.x - currentPosition?.x!!) }

            // 수평 이동할 점이 있다면 이동
            if (nextHorizontalPoint != null) {
                playerPath.add(nextHorizontalPoint)
                currentPosition = nextHorizontalPoint
            }
        }

        // 경로 출력 또는 저장
        Log.e("[sy.lee] Player ${player.playerIndex} Path", playerPath.toString())
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

fun generateLadderData(playerCount: Int): List<LadderLine> {
    val ladderData = mutableListOf<LadderLine>()
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
        for (line in horizontalLines) {
            val y = line * stepHeight
            ladderData.add(LadderLine(horizontal = HorizontalLine(Offset(x, y), Offset(x + ladderWidth, y))))
            horizontalLinesForEachColumn[i].add(line)
        }
    }

    // 세로줄 추가 (플레이어마다 한 개의 세로줄)
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth
        val startPoint = Offset(x, 0f)
        val endPoint = Offset(x, 1f)

        // 세로줄과 함께 startPoint와 endPoint를 설정하여 추가
        ladderData.add(
            LadderLine(
                vertical = VerticalLine(start = startPoint, end = endPoint)
            )
        )
    }

    return ladderData
}

fun generatePlayerGameInfo(ladderData: List<LadderLine>): List<PlayerGameInfo> {
    // PlayerGameInfo를 저장할 리스트
    val playerGameInfoList = mutableListOf<PlayerGameInfo>()

    // ladderData에서 각 플레이어의 세로줄을 추출하여 PlayerGameInfo 생성
    ladderData.filter { it.vertical != null } // 세로줄만 선택
        .forEachIndexed { index, ladderLine ->
            // 각 세로줄의 시작점과 끝점 정보를 가져옴
            val startPoint = ladderLine.vertical?.start
            val endPoint = ladderLine.vertical?.end

            // PlayerGameInfo 생성하여 리스트에 추가
            if (startPoint != null && endPoint != null) {
                playerGameInfoList.add(
                    PlayerGameInfo(
                        playerIndex = index,
                        startPoint = startPoint,
                        endPoint = endPoint
                    )
                )
            }
        }

    return playerGameInfoList
}

fun DrawScope.drawLadder(ladderData: List<LadderLine>) {
    val stroke = Stroke(4.dp.toPx())
    val ladderHeight = size.height

    Log.e("[sy.lee] Ladder Data", ladderData.toString())

    // Draw ladder lines from precomputed data
    ladderData.forEach { line ->
        // 세로줄 그리기
        line.vertical?.let {
            val startX = it.start.x * size.width
            val endX = it.end.x * size.width
            val startY = it.start.y * ladderHeight
            val endY = it.end.y * ladderHeight

            val start = Offset(startX, startY)
            val end = Offset(endX, endY)

            drawLine(
                Color.White,
                start = start,
                end = end,
                strokeWidth = stroke.width
            )
        }

        // 가로줄 그리기
        line.horizontal?.let {
            val startX = it.start.x * size.width
            val endX = it.end.x * size.width
            val startY = it.start.y * ladderHeight
            val endY = it.end.y * ladderHeight

            val start = Offset(startX, startY)
            val end = Offset(endX, endY)

            drawLine(
                Color.White,
                start = start,
                end = end,
                strokeWidth = stroke.width
            )
        }
    }
}
