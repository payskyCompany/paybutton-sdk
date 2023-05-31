package io.paysky.ui.fragment.listcards

import io.paysky.data.model.response.CardItem
import io.paysky.ui.mvp.BaseView

interface ListCardsView : BaseView {
    fun sessionIdFetchedSuccessfully()
    fun showToastError(message: String)
    fun showSavedCards(cardsLists: List<CardItem>)
}