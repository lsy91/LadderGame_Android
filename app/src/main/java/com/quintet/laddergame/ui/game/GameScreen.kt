package com.quintet.laddergame.ui.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quintet.laddergame.R
import com.quintet.laddergame.model.LadderLine
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor

/**
 * Ladder Game Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun GameScreen(
    uiState: GameState,
    players: List<Player>,
    winners: List<Winner>,
    navigateToScreen: (String) -> Unit,
    onEvent: (GameIntent) -> Unit
) {
    val gameElementsPadding = 16.dp

    val animatedX = remember { Animatable(0f) }  // x 애니메이션
    val animatedY = remember { Animatable(0f) }  // y 애니메이션

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

    LaunchedEffect(players) {
        onEvent(GameIntent.InitData(players, winners))

        // 사다리 좌표값을 비동기로 생성
        withContext(Dispatchers.Default) {
            onEvent(GameIntent.LoadLadders(players.size))
        }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawPlayers(
                players = players,
                onPlayerSelected = { index ->
                    // 플레이어가 클릭되었을 때 해당 플레이어 경로를 설정
                    val path = players.getOrNull(index)?.let {
                        calculatePlayerPathForIndex(index, uiState.ladders, players)
                    }
                    if (path != null) {
                        selectedPlayerPath = path
                        isAnimating = true
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격

        if (uiState.isLoading) {
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
                drawLadder(
                    ladders = uiState.ladders,
                    onEvent = onEvent
                )

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
            DrawWinners(winners)
        }

        Spacer(modifier = Modifier.height(gameElementsPadding)) // 세로 요소 간 간격
    }
}

@Composable
fun DrawPlayers(
    players: List<Player>,
    onPlayerSelected: (Int) -> Unit // 플레이어가 선택되었을 때 콜백 추가
) {
    players.forEach { player ->
        Image(
            painter = painterResource(player.player),
            contentDescription = "Player Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onPlayerSelected(player.playerIndex)
                }
        )
    }
}

@Composable
fun DrawWinners(winners: List<Winner>) {
    winners.forEach { winner ->
        if (winner.isWinner) {
            Image(
                painter = painterResource(R.drawable.ic_winner),
                contentDescription = "Winner Icon",
                modifier = Modifier.size(30.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .background(Color.Red, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "꽝",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

fun DrawScope.drawLadder(ladders: List<LadderLine>, onEvent: (GameIntent) -> Unit) {
    val stroke = Stroke(4.dp.toPx())
    val ladderHeight = size.height

    // Draw ladder lines from precomputed data
    ladders.forEach { line ->
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

    // 그려진 사다리를 사용하여 Player & Winner Position 설정
    onEvent(GameIntent.SetPlayerAndWinnerPosition(ladders))
}

/**
 * 플레이어별 이동경로
 */
fun calculatePlayerPathForIndex(
    playerIndex: Int,
    ladderData: List<LadderLine>,
    playersInfo: List<Player>
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
