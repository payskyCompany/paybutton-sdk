package io.paysky.ui.fragment.managecards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.paybutton.R
import io.paysky.data.model.response.CardItem
import io.paysky.ui.fragment.listcards.CardsCallback

class CardsAdapter(
    val onDeleteCard: (CardItem, Int) -> Unit,
    val onChangeDefaultItem: (CardItem, Int) -> Unit
) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {
    private val cardsList = mutableListOf<CardItem>()
    private var selectedItemPosition = -1

    inner class CardViewHolder(itemView: View) : ViewHolder(itemView) {
        private var itemPosition: Int = -1
        private val maskedCardNumber: TextView =
            itemView.findViewById(R.id.masked_card_number_text_view)
        private val cardName: TextView = itemView.findViewById(R.id.card_name_textview)
        private val selectCard: RadioButton = itemView.findViewById(R.id.card_selected_radio_button)
        private val deleteCard: ImageView = itemView.findViewById(R.id.delete_card)

        fun setCardData(cardItem: CardItem, position: Int) {
            this.itemPosition = position
            selectCard.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!cardItem.isDefaultCard) {
                        cardsList[selectedItemPosition] =
                            cardsList[selectedItemPosition].copy(isDefaultCard = false)
                        notifyItemChanged((selectedItemPosition))
                        onChangeDefaultItem(cardItem, position)
                    }
                }
            }
            maskedCardNumber.text = cardItem.maskedCardNumber
            cardName.text = cardItem.displayName
            if (cardItem.isDefaultCard) {
                selectedItemPosition = position
                selectCard.isChecked = true
            } else {
                selectCard.isChecked = false
            }
            deleteCard.setOnClickListener {
                onDeleteCard(cardItem, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_manage, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardsList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.setCardData(cardsList[position], position)
    }

    fun setItems(cardsLists: List<CardItem>) {
        val diffCallback = CardsCallback(this.cardsList, cardsLists)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.cardsList.clear()
        this.cardsList.addAll(cardsLists)
        diffResult.dispatchUpdatesTo(this)
    }

    fun deleteCard(position: Int) {
        cardsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItemAsDefault(position: Int) {
        cardsList[position] = cardsList[position].copy(isDefaultCard = true)
        cardsList[selectedItemPosition] =
            cardsList[selectedItemPosition].copy(isDefaultCard = false)
        notifyItemChanged(position)
        notifyItemChanged(selectedItemPosition)
    }

    fun revertDefault(position: Int) {
        cardsList[selectedItemPosition] = cardsList[selectedItemPosition].copy(isDefaultCard = true)
        cardsList[position] =
            cardsList[position].copy(isDefaultCard = false)
        notifyItemChanged(position)
        notifyItemChanged(selectedItemPosition)
    }
}