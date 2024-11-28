package com.quintet.laddergame.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.reflect.TypeToken
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner
import com.quintet.laddergame.ui.game.GameScreen
import com.quintet.laddergame.ui.player.PlayerScreen
import com.quintet.laddergame.ui.theme.LadderGameTheme
import com.quintet.laddergame.ui.winner.WinnerScreen
import com.quintet.laddergame.utils.LadderGameUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LadderGameTheme {
                // NavHost 로 첫번째 Composable 화면으로 이동
                LadderGameContent()
            }
        }
    }
}

@Composable
fun LadderGameContent() {

    // TODO ViewModel 과 NavHost 를 이용해서 화면 이동 및 데이터 이동을 재구성 해보자.
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "SelectPlayerCount"
    ) {
        composable("SelectPlayerCount") {
            PlayerScreen(
                onSelectedPlayerInfo = { playerCount, playerNames ->

                    val playerInfo = Player(
                        playerCount = playerCount,
                        playerNames = playerNames
                    )

                    // 당첨 수 화면으로 이동
                    navController.navigate("SelectWinnerCount" + "/" + "${LadderGameUtils.convertObjToJSON(playerInfo)}")
                }
            )
        }

        composable(
            route = "SelectWinnerCount" + "/" + "{playerInfo}",
            arguments = listOf( navArgument("playerInfo") { type = NavType.StringType; defaultValue = ""} )
        ) { navBackStackEntry ->

            val playerInfoJson = navBackStackEntry.arguments?.getString("playerInfo")
            val playerInfoToken = object : TypeToken<Player>() {}.type
            val selectedPlayerInfo = LadderGameUtils.convertJSONToObj<Player>(playerInfoJson, playerInfoToken)

            WinnerScreen(
                playerCount = selectedPlayerInfo?.playerCount ?: 0,
                onSelectedGameInfo = { winnerCount, winnerTitles ->

                    val winnerInfo = Winner(
                        winnerCount = winnerCount,
                        // 당첨 설정된 만큼을 제외하고, 플레이어 수 차이만큼 남는 Prize 요소는 "꽝" 으로 미리 만들어둔다. 그래야 그릴 때 shuffle 이 먹힌다.
                        winnerPrizes = (winnerTitles + List((selectedPlayerInfo?.playerCount ?: 0) - winnerTitles.size) { "꽝" })
                    )

                    navController.navigate("LadderGameView"
                            + "/"
                            + "${LadderGameUtils.convertObjToJSON(selectedPlayerInfo)}"
                            + "/"
                            + "${LadderGameUtils.convertObjToJSON(winnerInfo)}")
                }
            )
        }

        composable(
            route = "LadderGameView" + "/" + "{playerInfo}" + "/" + "{winnerInfo}",
            arguments = listOf(
                navArgument("playerInfo") { type = NavType.StringType; defaultValue = ""},
                navArgument("winnerInfo") { type = NavType.StringType; defaultValue = ""},
            )
        ) { navBackStackEntry ->

            val playerInfoJson = navBackStackEntry.arguments?.getString("playerInfo")
            val playerInfoToken = object : TypeToken<Player>() {}.type
            val selectedPlayerInfo = LadderGameUtils.convertJSONToObj<Player>(playerInfoJson, playerInfoToken)

            val winnerInfoJson = navBackStackEntry.arguments?.getString("winnerInfo")
            val winnerInfoToken = object : TypeToken<Winner>() {}.type
            val selectedWinnerInfo = LadderGameUtils.convertJSONToObj<Winner>(winnerInfoJson, winnerInfoToken)

            GameScreen(
                playerInfo = selectedPlayerInfo ?: Player(),
                winnerInfo = selectedWinnerInfo ?: Winner()
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LadderGameViewPreview() {
    LadderGameTheme {
        LadderGameContent()
    }
}