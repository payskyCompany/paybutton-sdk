package io.paysky.ui.fragment.listcards

import androidx.recyclerview.widget.DiffUtil
import io.paysky.data.model.response.CardItem

class CardsCallback(
    private val mOldCardItems: List<CardItem>,
    private val mNewCardItems: List<CardItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldCardItems.size
    }

    override fun getNewListSize(): Int {
        return mNewCardItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldCardItems[oldItemPosition].cardId == mNewCardItems[newItemPosition].cardId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCard = mOldCardItems[oldItemPosition];
        val newCard = mNewCardItems[newItemPosition];

        return oldCard == newCard
    }
}