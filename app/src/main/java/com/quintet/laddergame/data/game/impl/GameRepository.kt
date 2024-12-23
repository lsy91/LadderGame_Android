package com.quintet.laddergame.data.game.impl

import androidx.compose.ui.geometry.Offset
import com.quintet.laddergame.data.game.IGameRepository
import com.quintet.laddergame.model.HorizontalLine
import com.quintet.laddergame.model.LadderLine
import com.quintet.laddergame.model.VerticalLine
import javax.inject.Inject
import kotlin.random.Random

/**
 * Game Repository Interface 구현체
 *
 * Game Model 에 data 를 전달하는 동작 일체를 구현한다.
 */
class GameRepository @Inject constructor(): IGameRepository {

    override suspend fun generateLadders(playerCount: Int): List<LadderLine> {
        val ladderData = mutableListOf<LadderLine>()
        val ladderWidth = 1f / (playerCount + 1)
        val stepHeight = 1f / 11  // 총 11개 구간으로 나눔
        val random = Random

        // List to store horizontal lines for each column
        val horizontalLinesForEachColumn = MutableList(playerCount) { mutableSetOf<Int>() }

        // 세로줄마다 가로줄을 생성하는 부분 최적화
        for (i in 0 until playerCount - 1) {
            val x = (i + 1) * ladderWidth

            // 이전 세로줄과 겹치지 않도록 랜덤한 가로줄 생성
            val previousHorizontalLines = horizontalLinesForEachColumn.getOrNull(i - 1) ?: emptySet()

            // 1~5개 가로줄을 랜덤하게 생성
            val horizontalLineCount = random.nextInt(5) + 1
            val horizontalLines = mutableSetOf<Int>()

            // 중복을 최소화하는 방식으로 가로줄 생성
            while (horizontalLines.size < horizontalLineCount) {
                val y = random.nextInt(10) + 1
                if (!previousHorizontalLines.contains(y)) {
                    horizontalLines.add(y)
                }
            }

            // 생성된 가로줄을 ladderData에 추가
            for (line in horizontalLines) {
                val y = line * stepHeight
                ladderData.add(LadderLine(horizontal = HorizontalLine(Offset(x, y), Offset(x + ladderWidth, y))))
                horizontalLinesForEachColumn[i].add(line)
            }
        }

        // 세로줄 추가 (플레이어마다 한 개의 세로줄)
        for (i in 0 until playerCount) {
            val x = (i + 1) * ladderWidth
            val startPoint = Offset(x, 0f)
            val endPoint = Offset(x, 1f)

            // 세로줄과 함께 startPoint와 endPoint를 설정하여 추가
            ladderData.add(
                LadderLine(
                    vertical = VerticalLine(start = startPoint, end = endPoint)
                )
            )
        }

        return ladderData
    }
}