package io.paysky.ui.fragment.cardsOperations

import io.paysky.data.model.response.CardItem
import io.paysky.ui.mvp.BaseView

interface CardsView : BaseView {
    fun showToastError(message: String)
    fun showToastErrorAndFinish(error: Int)
    fun showSavedCards(cardsLists: List<CardItem>)
}