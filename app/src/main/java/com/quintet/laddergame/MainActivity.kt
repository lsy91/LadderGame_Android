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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quintet.laddergame.ui.LadderGameScreen
import com.quintet.laddergame.ui.SelectPlayerCountScreen
import com.quintet.laddergame.ui.SelectWinnerCountScreen
import com.quintet.laddergame.ui.theme.LadderGameTheme
import java.util.ArrayList

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

                    val currentBackStackEntry = navController.currentBackStackEntry
                    val args = currentBackStackEntry?.arguments ?: Bundle()
                    args.apply {
                        putInt("playerCount", playerCount)
                        putStringArrayList("playerNames", playerNames as? ArrayList<String> ?: arrayListOf())
                    }

                    // 당첨 수 화면으로 이동
                    navController.navigate("SelectWinnerCount")
                }
            )
        }
        composable("SelectWinnerCount") { navBackStackEntry ->
            val playerCount = navBackStackEntry.arguments?.getInt("playerCount") ?: 0
            val playerNames = navBackStackEntry.arguments?.getStringArrayList("playerNames") ?: arrayListOf<String>()

            SelectWinnerCountScreen(
                playerCount = playerCount,
                onSelectedGameInfo = { winnerCount, winnerTitles ->

                    val currentBackStackEntry = navController.currentBackStackEntry
                    val args = currentBackStackEntry?.arguments ?: Bundle()
                    args.apply {
                        putInt("playerCount", playerCount)
                        putStringArrayList("playerNames", playerNames)
                        putInt("winnerCount", winnerCount)
                        putStringArrayList("winnerTitles", winnerTitles as? ArrayList<String> ?: arrayListOf())
                    }

                    navController.navigate("LadderGameView")
                }
            )
        }
        composable("LadderGameView") { navBackStackEntry ->

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