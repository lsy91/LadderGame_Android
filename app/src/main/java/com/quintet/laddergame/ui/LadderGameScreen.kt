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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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

        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = gameElementsPadding)
        ) {
            drawLadder(playerInfo.playerCount)
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

fun DrawScope.drawLadder(playerCount: Int) {
    val ladderWidth = size.width / (playerCount + 1)
    val stroke = Stroke(4.dp.toPx())
    val ladderHeight = size.height

    // Draw vertical lines
    for (i in 0 until playerCount) {
        val x = (i + 1) * ladderWidth
        drawLine(
            Color.White,
            start = Offset(x, 0f),
            end = Offset(x, ladderHeight),
            strokeWidth = stroke.width
        )
    }
}
