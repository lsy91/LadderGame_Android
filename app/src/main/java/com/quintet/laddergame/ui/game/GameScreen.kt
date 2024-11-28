package com.quintet.laddergame.ui.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quintet.laddergame.model.HorizontalLine
import com.quintet.laddergame.model.LadderLine
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.PlayerGameInfo
import com.quintet.laddergame.model.VerticalLine
import com.quintet.laddergame.model.Winner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.random.Random

/**
 * Ladder Game Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun GameScreen(
    playerInfo: Player,
    winnerInfo: Winner
) {
    val shuffledPlayerNames = remember { playerInfo.playerNames.shuffled() }
    val shuffledWinnerTitles = remember { winnerInfo.winnerPrizes.shuffled() }
    val gameElementsPadding = 16.dp

//    var gameInProgress by remember { mutableStateOf(false) }
    val animatedX = remember { Animatable(0f) }  // x 애니메이션
    val animatedY = remember { Animatable(0f) }  // y 애니메이션
//    val scope = rememberCoroutineScope()

    val ladderData = remember { mutableStateOf<List<LadderLine>>(emptyList()) }
    var playerGameInfo by remember { mutableStateOf<List<PlayerGameInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 선택된 플레이어 경로를 저장할 상태
    var selectedPlayerPath by remember { mutableStateOf<List<Offset>?>(null) }
    var isAnimating by remember { mutableStateOf(false) }

    // 선택된 플레이어 경로에 따른 애니메이션 처리
    selectedPlayerPath?.let { path ->
        if (isAnimating) {
            // 애니메이션이 진행 중일 때는 플레이어가 이동하는 것을 그리기
            LaunchedEffect(path) {

                // 경로에서 첫 번째 위치를 시작점으로 설정
                val startOffset = path.first()
                animatedX.snapTo(startOffset.x)  // 초기 x 값 설정
                animatedY.snapTo(startOffset.y)  // 초기 y 값 설정

                // 경로에서 x, y 값을 애니메이션 진행
                path.forEach { pathPoint ->
                    animatedX.animateTo(
                        targetValue = pathPoint.x,
                        animationSpec = tween(durationMillis = 500)
                    )
                    animatedY.animateTo(
                        targetValue = pathPoint.y,
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            }
        }
    }

    LaunchedEffect(playerInfo.playerCount) {
        // ladderData를 비동기로 생성
        ladderData.value = withContext(Dispatchers.Default) {
            generateLadderData(playerInfo.playerCount)
        }

        // ladderData가 완료된 후 결과를 사용하여 playerGameInfo 생성
        playerGameInfo = generatePlayerGameInfo(ladderData.value)

        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = Color.Black
                )
            }
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "플레이어를 클릭하면 게임이 시작됩니다.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawPlayers(
                playerCount = playerInfo.playerCount,
                shuffledPlayerNames = shuffledPlayerNames,
                onPlayerSelected = { index ->
                    // 플레이어가 클릭되었을 때 해당 플레이어 경로를 설정
                    val path = playerGameInfo.getOrNull(index)?.let {
                        calculatePlayerPathForIndex(index, ladderData.value, playerGameInfo)
                    }
                    if (path != null) {
                        selectedPlayerPath = path
                        isAnimating = true
                    }
                }
            )
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
                // ladderDrawn이 true 일 때만 사다리를 그리도록 함
                drawLadder(ladderData.value)

                // 애니메이션을 그릴 플레이어의 경로
                selectedPlayerPath?.let {
                    // 경로에 따라 애니메이션된 위치 그리기
                    drawCircle(
                        color = Color.Red,
                        radius = 30f,
                        center = Offset(
                            x = animatedX.value * size.width,  // x 값 애니메이션
                            y = animatedY.value * size.height  // y 값 애니메이션
                        )
                    )
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

        /** TODO 전체 플레이어 동시 시작 버튼은 추후에 구현 예정 **/
//        Button(
//            onClick = {
//                if (!gameInProgress) {
//                    scope.launch {
//                        // 애니메이션 초기화
//                        animatedX.snapTo(0f)
//                        animatedY.snapTo(0f)
//                        // 애니메이션 시작
//                        animatedX.animateTo(
//                            targetValue = 1f,  // 예시로 x, y가 모두 1로 끝까지 이동
//                            animationSpec = tween(durationMillis = 1000)
//                        )
//                        animatedY.animateTo(
//                            targetValue = 1f,
//                            animationSpec = tween(durationMillis = 1000)
//                        )
//                        gameInProgress = false
//                    }
//                    gameInProgress = true
//                }
//            },
//            modifier = Modifier
//                .padding(gameElementsPadding)
//                .align(Alignment.CenterHorizontally)
//        ) {
//            Text("Start Game")
//        }
    }
}

@Composable
fun DrawPlayers(
    playerCount: Int,
    shuffledPlayerNames: List<String>,
    onPlayerSelected: (Int) -> Unit // 플레이어가 선택되었을 때 콜백 추가
) {
    for (i in 0 until playerCount) {
        Box(
            modifier = Modifier
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable { onPlayerSelected(i) } // 클릭 이벤트 처리
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

/**
 * 플레이어별 이동경로
 */
fun calculatePlayerPathForIndex(
    playerIndex: Int,
    ladderData: List<LadderLine>,
    playersInfo: List<PlayerGameInfo>
): List<Offset>? {
    // 해당 인덱스의 플레이어 정보를 가져와서 경로를 계산
    val player = playersInfo.getOrNull(playerIndex) ?: return null

    // 기존 경로 계산 로직을 재사용
    val canMovePointList = ladderData.flatMap { line ->
        listOfNotNull(
            line.horizontal?.start,
            line.horizontal?.end,
            line.vertical?.start,
            line.vertical?.end
        )
    }.distinct()

    val path = mutableListOf<Offset>()
    var currentPosition = player.startPoint
    path.add(currentPosition!!)

    while (currentPosition?.y!! < 1f) {
        // 수직 이동 (y 값 증가)
        val nextVerticalPoint = canMovePointList
            .filter {
                // y 축으로 소수점 네번째 자리 이하를 버리고 비교
                // (플레이어가 많아질 경우 y 축 좌표값이 소수점 여섯째 자리까지 가므로, 그 오차를 감안)
                floor(it.x * 10) == floor(currentPosition?.x!! * 10) &&
                        floor(it.y * 1000) > floor(currentPosition?.y!! * 1000)
            }
            .minByOrNull {
                // 소수점 네번째 자리 이하를 버리고 차이를 계산
                // (플레이어가 많아질 경우 y 축 좌표값이 소수점 여섯째 자리까지 가므로, 그 오차를 감안)
                floor(it.y * 1000) - floor(currentPosition?.y!! * 1000)
            }

        if (nextVerticalPoint != null) {
            path.add(nextVerticalPoint)
            currentPosition = nextVerticalPoint
        }

        if (currentPosition.y == 1.0f) break

        // 수평 이동 로직
        val nextHorizontalPoint = ladderData
            .filter { it.horizontal != null }
            .firstNotNullOfOrNull { horizontalLine ->
                val start = horizontalLine.horizontal?.start
                val end = horizontalLine.horizontal?.end

                when {
                    // currentPosition과 start가 일치하면 end로 이동 (소수점 첫째자리까지만 비교)
                    floor(start?.x!! * 10) == floor(currentPosition?.x!! * 10) && floor(start.y * 10) == floor(currentPosition?.y!! * 10) -> end
                    // currentPosition과 end가 일치하면 start로 이동 (소수점 첫째자리까지만 비교)
                    floor(end?.x!! * 10) == floor(currentPosition?.x!! * 10) && floor(end.y * 10) == floor(currentPosition?.y!! * 10) -> start
                    else -> null
                }
            }

        // nextHorizontalPoint가 null이 아니면 경로에 추가하고 currentPosition을 업데이트
        nextHorizontalPoint?.let {
            path.add(it)
            currentPosition = it
        }
    }

    return path
}
