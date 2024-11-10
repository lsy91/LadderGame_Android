package com.quintet.laddergame.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.Winner
import kotlinx.coroutines.launch

/**
 * Ladder Game Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun LadderGameScreen(playerInfo: Player, winnerInfo: Winner) {
    val shuffledPlayerNames = remember { playerInfo.playerNames.shuffled() }
    val shuffledWinnerTitles = remember { winnerInfo.winnerPrizes.shuffled() }
    val gameElementsPadding = 16.dp

    var gameInProgress by remember { mutableStateOf(false) }
    val animatedPositions = remember(playerInfo.playerCount) {
        List(playerInfo.playerCount) { Animatable(0f) }
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.dp), // 좌우 마진 20dp 설정
        verticalArrangement = Arrangement.Center
    ) {
        // 플레이어, 세로선, 당첨을 세로로 정렬
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
        ) {
            // 플레이어 이름 표시 (가로로 균등 정렬)
            DrawPlayers(playerInfo.playerCount, shuffledPlayerNames)

            Spacer(modifier = Modifier.height(gameElementsPadding))

            // 사다리 그리기 (세로줄을 선 형태로)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(200.dp) // 사다리 영역 높이
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // 세로선 균등 배치
                ) {
                    for (columnIndex in 0 until playerInfo.playerCount) {
                        // 세로선 그리기
                        Column(
                            modifier = Modifier
                                .width(2.dp) // 세로줄의 두께 설정
                                .fillMaxHeight()
                                .background(Color.White) // 세로줄 색상
                                .offset(y = animatedPositions[columnIndex].value.dp) // 애니메이션 적용
                        ) {
                            // Spacer로 세로선의 중앙 정렬
                            Spacer(modifier = Modifier.weight(1f)) // 위쪽 여백
                            for (rowIndex in 0..10) {
                                if (shouldDrawHorizontalLine(columnIndex, rowIndex)) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth() // 가로줄의 길이 설정
                                            .height(2.dp) // 가로줄의 두께 설정
                                            .background(Color.White)
                                            .align(Alignment.Start) // 가로줄 위치
                                    )
                                }
                                Spacer(modifier = Modifier.height(30.dp)) // 각 셀 간 간격
                            }
                            Spacer(modifier = Modifier.weight(1f)) // 아래쪽 여백
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(gameElementsPadding))

            // 당첨 결과 표시 (가로로 균등 정렬)
            DrawWinners(playerInfo.playerCount, shuffledWinnerTitles)
        }

        Spacer(modifier = Modifier.height(gameElementsPadding))

        // 시작 버튼
        Button(
            onClick = {
                if (!gameInProgress) {
                    gameInProgress = true
                    scope.launch {
                        animatedPositions.forEachIndexed { index, animatable ->
                            animatable.snapTo(0f) // 초기 위치로 설정
                            animatable.animateTo(
                                targetValue = 300f, // 애니메이션의 최종 위치
                                animationSpec = tween(durationMillis = 1000 + index * 200)
                            )
                        }
                        gameInProgress = false
                    }
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // 균등한 간격 유지
    ) {
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
}

@Composable
fun DrawWinners(playerCount: Int, shuffledWinnerTitles: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // 균등한 간격 유지
    ) {
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
}

@Composable
fun shouldDrawHorizontalLine(columnIndex: Int, rowIndex: Int): Boolean {
    // 임의의 로직으로 수평 막대를 그릴지 여부 결정 (예: 특정 열과 행에서만 그리기)
    return (columnIndex % 2 == 0 && rowIndex % 3 == 0)
}
