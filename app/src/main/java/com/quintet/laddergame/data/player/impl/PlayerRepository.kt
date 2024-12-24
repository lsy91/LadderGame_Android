package com.quintet.laddergame.data.player.impl

import com.quintet.laddergame.R
import com.quintet.laddergame.data.player.IPlayerRepository
import com.quintet.laddergame.model.Player
import javax.inject.Inject

/**
 * Player Repository Interface 구현체
 *
 * Player Model 에 data 를 전달하는 동작 일체를 구현한다.
 */
class PlayerRepository @Inject constructor(): IPlayerRepository {

    override suspend fun loadPlayers(inputPlayerCount: Int): List<Player> {

        val playerIconList = listOf(
            R.drawable.ic_player_bear,
            R.drawable.ic_player_cat,
            R.drawable.ic_player_dog,
            R.drawable.ic_player_panda,
            R.drawable.ic_player_tiger,
            R.drawable.ic_player_cow,
            R.drawable.ic_player_pig,
            R.drawable.ic_player_giraffe,
            R.drawable.ic_player_chicken,
            R.drawable.ic_player_gorilla,
        )

        // playerIconList 를 섞고 inputPlayerCount 만큼 추출
        val randomIcons = playerIconList.shuffled().take(inputPlayerCount)

        // Player List 생성
        val playerList = randomIcons.mapIndexed { playerIndex, playerId ->
            Player(
                playerIndex = playerIndex,
                playerId = playerId
            )
        }

        return playerList
    }

}