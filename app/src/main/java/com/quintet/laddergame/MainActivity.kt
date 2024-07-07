package com.quintet.laddergame

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.reflect.TypeToken
import com.quintet.laddergame.bean.Player
import com.quintet.laddergame.bean.Winner
import com.quintet.laddergame.ui.LadderGameScreen
import com.quintet.laddergame.ui.SelectPlayerCountScreen
import com.quintet.laddergame.ui.SelectWinnerCountScreen
import com.quintet.laddergame.ui.theme.LadderGameTheme
import com.quintet.laddergame.utils.LadderGameUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LadderGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavHost 로 첫번째 Composable 화면으로 이동
                    LadderGameContent()
                }
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
            SelectPlayerCountScreen(
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

            SelectWinnerCountScreen(
                playerCount = selectedPlayerInfo?.playerCount ?: 0,
                onSelectedGameInfo = { winnerCount, winnerTitles ->

                    val winnerInfo = Winner(
                        winnerCount = winnerCount,
                        winnerPrizes = winnerTitles
                    )

                    navController.navigate("LadderGameView" + "/" + "${LadderGameUtils.convertObjToJSON(winnerInfo)}")
                }
            )
        }

        composable(
            route = "LadderGameView",
            arguments = listOf(
                navArgument("LadderGameView") { type = NavType.StringType; defaultValue = "SelectPlayerCount" }
            )
        ) { navBackStackEntry ->

            val playerCount = navBackStackEntry.arguments?.getInt("playerCount") ?: 0
            val playerNames = navBackStackEntry.arguments?.getStringArrayList("playerNames") ?: arrayListOf<String>()
            val winnerCount = navBackStackEntry.arguments?.getInt("winnerCount") ?: 0
            val winnerTitles = navBackStackEntry.arguments?.getStringArrayList("winnerTitles") ?: arrayListOf<String>()

            LadderGameScreen(
                navController = navController,
                playerCount = playerCount,
                playerNames = playerNames,
                winnerCount = winnerCount,
                winnerTitles = winnerTitles
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