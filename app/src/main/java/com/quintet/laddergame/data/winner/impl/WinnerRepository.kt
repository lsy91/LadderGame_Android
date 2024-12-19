package com.quintet.laddergame.data.winner.impl

import com.quintet.laddergame.data.winner.IWinnerRepository
import com.quintet.laddergame.model.Winner
import javax.inject.Inject

/**
 * Winner Repository Interface 구현체
 *
 * Winner Model 에 data 를 전달하는 동작 일체를 구현한다.
 */
class WinnerRepository @Inject constructor(): IWinnerRepository {

    override suspend fun loadWinners(inputPlayerCount: Int, nothingCount: Int): List<Winner> {

        val winnerList = mutableListOf<Winner>()

        // inputPlayerCount 만큼 추가
        repeat(inputPlayerCount) {
            winnerList.add(Winner(isWinner = true))
        }

        // nothingCount 만큼 추가
        repeat(nothingCount) {
            winnerList.add(Winner(isWinner = false))
        }

        // 리스트를 셔플
        winnerList.shuffle()

        return winnerList
    }

}