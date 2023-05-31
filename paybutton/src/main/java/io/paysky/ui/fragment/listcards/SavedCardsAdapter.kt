package io.paysky.ui.fragment.listcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paybutton.R
import io.paysky.data.model.response.CardItem

class SavedCardsAdapter : RecyclerView.Adapter<SavedCardViewHolder>() {
    private val savedCardsList = mutableListOf<CardItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_card, parent, false)

        return SavedCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedCardsList.size
    }

    override fun onBindViewHolder(holder: SavedCardViewHolder, position: Int) {
        holder.setCardData(savedCardsList[position])
    }

    fun setItems(cardsLists: List<CardItem>) {
        savedCardsList.clear()
        savedCardsList.addAll(cardsLists)
        notifyDataSetChanged()
    }

}

class SavedCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setCardData(cardItem: CardItem) {
        maskedCardNumber.text = cardItem.maskedCardNumber
        cardName.text = cardItem.displayName
        if (cardItem.isDefultCard) {
            selectCard.isChecked = true
        }

    }

    private val cvv: EditText = itemView.findViewById(R.id.ccv_editText)
    private val maskedCardNumber: TextView =
        itemView.findViewById(R.id.masked_card_number_text_view)
    private val cardName: TextView = itemView.findViewById(R.id.card_name_textview)
    private val selectCard: RadioButton = itemView.findViewById(R.id.card_selected_radio_button)

}