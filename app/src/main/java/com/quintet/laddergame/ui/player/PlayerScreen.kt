package com.quintet.laddergame.ui.player

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
import com.quintet.laddergame.ui.LadderGameDestinations.SET_WINNER_ROUTE
import com.quintet.laddergame.ui.utils.BaseText

/**
 * Select Player Count Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun PlayerScreen(
    uiState: PlayerState,
    navigateToScreen: (String) -> Unit,
    onEvent: (PlayerIntent) -> Unit
) {
    // 플레이어 인원 수
    var playerCount by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BaseText(
            text = "플레이어는 2명 이상, 10명 이하로 설정해주세요.",
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
                        text = "게임 인원 수",
                        fontSize = 20,
                        modifier = Modifier
                            .wrapContentSize()
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(
                        value = playerCount,
                        onValueChange = { inputPlayerCount ->

                            if (inputPlayerCount.isEmpty()) {
                                playerCount = ""
                                onEvent(PlayerIntent.ClearPlayers)
                            } else {
                                val count = inputPlayerCount.toIntOrNull()
                                if (count != null) {
                                    playerCount = count.toString()

                                    // 2~10명 사이일 때만 Event 호출
                                    if (count in 2..10) {
                                        onEvent(PlayerIntent.LoadPlayers(count))
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

            items(uiState.players.size) { playerIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BaseText(
                        text = "player ${playerIndex + 1}",
                        fontSize = 20,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )

                    Image(
                        painter = painterResource(uiState.players[playerIndex].player),
                        contentDescription = "Player Icon",
                        modifier = Modifier.size(30.dp).weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    enabled = uiState.players.size > 1,
                    onClick = {
                        // 당첨 수 설정 화면으로 이동
                        navigateToScreen(SET_WINNER_ROUTE)
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