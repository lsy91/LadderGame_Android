package com.quintet.laddergame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Select Player Count Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun SelectPlayerCountScreen(
    onSelectedPlayerInfo: (Int, List<String>) -> Unit // 플레이어 정보를 전달하기 위한 콜백
) {
    // 플레이어 인원 수
    var playerCount by remember { mutableStateOf("") }
    // 플레이어 이름 리스트
    var playerNames by remember { mutableStateOf<List<String>>(listOf()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 타이틀
        Text(
            text = "게임 설정",
            fontSize = 34.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 50.dp, bottom = 20.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "게임 인원 수",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .wrapContentSize()
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(
                        value = playerCount,
                        onValueChange = { inputPlayerCount ->
                            val count = inputPlayerCount.toIntOrNull()
                            if (inputPlayerCount.isBlank() || (count != null && count in 1..10)) {
                                playerCount = inputPlayerCount
                                count?.let {
                                    playerNames = List(it) { index -> playerNames.getOrNull(index) ?: "" }
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
                            unfocusedContainerColor = Color.Transparent // 포커스 해제된 배경색 투명으로 설정
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // 플레이어 이름 입력 필드
            val count = playerCount.toIntOrNull() ?: 0
            if (count > 0) {
                itemsIndexed(playerNames) { index, playerName ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "플레이어 ${index + 1}",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = playerName,
                            onValueChange = { inputPlayerName ->
                                playerNames = playerNames.toMutableList().apply {
                                    this[index] = inputPlayerName
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = playerCount.isNotBlank() && playerNames.all { it.isNotBlank() },
                    onClick = {
                        // 설정한 플레이어 수를 콜백으로 NavHost에 전달
                        onSelectedPlayerInfo(playerCount.toInt(), playerNames)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}