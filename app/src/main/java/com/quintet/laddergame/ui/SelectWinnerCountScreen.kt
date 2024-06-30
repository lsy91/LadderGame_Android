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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Select Winner Count Screen
 *
 * 2024-06-16 Created by sy.lee
 */
@Composable
fun SelectWinnerCountScreen(
    playerCount: Int,
    onSelectedGameInfo: (Int, List<String>) -> Unit
) {
    // 당첨 수
    var winnerCount by rememberSaveable { mutableStateOf("") }
    // 당첨 제목 리스트
    var winnerTitles by remember { mutableStateOf<List<String>>(listOf()) }

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "당첨 개수",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(
                        value = winnerCount,
                        onValueChange = { inputWinnerCount ->
                            val count = inputWinnerCount.toIntOrNull()
                            if (inputWinnerCount.isBlank() || (count != null && count in 1..playerCount)) {
                                winnerCount = inputWinnerCount
                                count?.let {
                                    winnerTitles = List(it) { index -> winnerTitles.getOrNull(index) ?: "꽝" }
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 당첨 제목 입력 필드
            val count = winnerCount.toIntOrNull() ?: 0
            if (count > 0) {
                itemsIndexed(winnerTitles) { index, winnerTitle ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "당첨 ${index + 1}",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = winnerTitle,
                            onValueChange = { inputWinnerTitle ->
                                winnerTitles = winnerTitles.toMutableList().apply {
                                    this[index] = inputWinnerTitle
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = winnerCount.isNotBlank() && winnerTitles.all { it.isNotBlank() },
                    onClick = {
                        // 설정한 당첨 수를 콜백으로 NavHost에 전달
                        onSelectedGameInfo(winnerCount.toInt(), winnerTitles)
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