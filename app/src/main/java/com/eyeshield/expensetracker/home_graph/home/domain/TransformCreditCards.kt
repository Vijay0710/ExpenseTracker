package com.eyeshield.expensetracker.home_graph.home.domain

import com.eyeshield.expensetracker.home_graph.home.data.CardInfo
import javax.inject.Inject

class TransformCreditCards @Inject constructor() {

    fun getTransformedCardsForSelectedIndex(
        cardInfoList: MutableList<CardInfo>,
        index: Int,
        selectedCard: CardInfo
    ): CardInfo {
        val getCardDataForMaximumCard = cardInfoList.maxByOrNull {
            it.zIndex
        }

        val maximumItemIndex = cardInfoList.indexOf(
            getCardDataForMaximumCard
        )

        val temp = selectedCard.copy()

        cardInfoList[index] =
            cardInfoList[maximumItemIndex].copy(
                zIndex = cardInfoList[maximumItemIndex].zIndex,
                offsetY = cardInfoList[maximumItemIndex].offsetY,
                cardColor = cardInfoList[index].cardColor,
                position = cardInfoList[maximumItemIndex].position
            )

        cardInfoList[maximumItemIndex] = temp.copy(
            zIndex = temp.zIndex,
            offsetY = temp.offsetY,
            cardColor = cardInfoList[maximumItemIndex].cardColor,
            position = temp.position
        )

        return selectedCard
    }
}