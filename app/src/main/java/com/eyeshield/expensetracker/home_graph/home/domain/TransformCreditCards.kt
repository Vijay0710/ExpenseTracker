package com.eyeshield.expensetracker.home_graph.home.domain

import com.eyeshield.expensetracker.home_graph.home.data.CardInfo
import javax.inject.Inject

class TransformCreditCards @Inject constructor() {

    fun getTransformedCardsForSelectedIndex(
        cardInfoList: MutableList<CardInfo>,
        selectedCard: CardInfo
    ): List<CardInfo> {
        val getCardDataForMaximumCard = cardInfoList.maxByOrNull {
            it.zIndex
        }

        val maximumItemIndex = cardInfoList.indexOf(
            getCardDataForMaximumCard
        )

        val temp = selectedCard.copy()

        val selectedCardIndex = cardInfoList.indexOfLast {
            it == selectedCard
        }

        cardInfoList[selectedCardIndex] =
            cardInfoList[maximumItemIndex].copy(
                id = cardInfoList[selectedCardIndex].id,
                zIndex = cardInfoList[maximumItemIndex].zIndex,
                offsetY = cardInfoList[maximumItemIndex].offsetY,
                cardColor = cardInfoList[selectedCardIndex].cardColor,
                position = cardInfoList[maximumItemIndex].position
            )

        cardInfoList[maximumItemIndex] = temp.copy(
            id = cardInfoList[maximumItemIndex].id,
            zIndex = temp.zIndex,
            offsetY = temp.offsetY,
            cardColor = cardInfoList[maximumItemIndex].cardColor,
            position = temp.position
        )

        return cardInfoList
    }
}