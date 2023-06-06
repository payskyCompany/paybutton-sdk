package io.paysky.ui.fragment.listcards

import io.paysky.ui.mvp.BaseView

interface ListCardsView : BaseView {
    fun sessionIdFetchedSuccessfully()
    fun showToastError(message: String)
}