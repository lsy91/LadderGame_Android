package com.quintet.laddergame.data

import com.quintet.laddergame.data.game.IGameRepository
import com.quintet.laddergame.data.game.impl.GameRepository
import com.quintet.laddergame.data.player.IPlayerRepository
import com.quintet.laddergame.data.player.impl.PlayerRepository
import com.quintet.laddergame.data.winner.IWinnerRepository
import com.quintet.laddergame.data.winner.impl.WinnerRepository

/**
 * App Repository 들을 하나의 Container 에 담아 관리하기 위한 Container
 *
 */
interface AppContainer {
    val playerRepository: IPlayerRepository
    val winnerRepository: IWinnerRepository
    val gameRepository: IGameRepository
}

class AppContainerImpl : AppContainer {
    override val playerRepository: IPlayerRepository by lazy {
        PlayerRepository()
    }

    override val winnerRepository: IWinnerRepository by lazy {
        WinnerRepository()
    }

    override val gameRepository: IGameRepository by lazy {
        GameRepository()
    }
}

