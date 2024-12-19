package com.quintet.laddergame.ui.winner

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quintet.laddergame.R
import com.quintet.laddergame.ui.LadderGameDestinations.GAME_ROUTE
import com.quintet.laddergame.ui.utils.BaseText

/**
 * Select Winner Count Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun WinnerScreen(
    uiState: WinnerState,
    playerCount: Int,
    navigateToScreen: (String) -> Unit,
    onEvent: (WinnerIntent) -> Unit
) {
    // 당첨 수
    var winnerCount by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BaseText(
            text = "당첨 개수는 플레이어 수보다 적어야 합니다.",
            fontSize = 12
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BaseText(
                        text = "당첨 개수",
                        fontSize = 20,
                        modifier = Modifier
                            .wrapContentSize()
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(
                        value = winnerCount,
                        onValueChange = { inputWinnerCount ->
                            if (inputWinnerCount.isEmpty()) {
                                winnerCount = ""
                                onEvent(WinnerIntent.ClearWinners)
                            } else {
                                val count = inputWinnerCount.toIntOrNull()
                                if (count != null) {
                                    winnerCount = count.toString()

                                    // 플레이어 수보다 적은 경우에만 Event 호출
                                    if (count in 1 until playerCount) {
                                        onEvent(WinnerIntent.LoadWinners(count, playerCount - count))
                                    } else {
                                        // TODO 플레이어 수보다 많습니다. 당첨 개수는 1개 이상이여야 합니다. SnackBar 오류 호출
                                    }
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, // 포커스된 배경색 투명으로 설정
                            unfocusedContainerColor = Color.Transparent, // 포커스 해제된 배경색 투명으로 설정
                            focusedIndicatorColor = Color.Red,
                            unfocusedIndicatorColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            items(uiState.winners.filter { winner -> winner.isWinner }.size) { winnerIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BaseText(
                        text = "winner ${winnerIndex + 1}",
                        fontSize = 20,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_winner),
                        contentDescription = "Winner Icon",
                        modifier = Modifier.size(30.dp).weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    enabled = uiState.winners.isNotEmpty() && uiState.winners.filter { winner -> winner.isWinner }.size < playerCount,
                    onClick = {
                        // 사다리게임 화면으로 이동
                        navigateToScreen(GAME_ROUTE)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BaseText(
                        text = "Next",
                        fontSize = 20
                    )
                }
            }
        }
    }
}