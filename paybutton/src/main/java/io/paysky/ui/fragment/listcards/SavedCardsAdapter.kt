package io.paysky.ui.fragment.listcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.paybutton.R
import io.paysky.data.model.response.CardItem

class SavedCardsAdapter(val onSubmitDataValid: (CardItem) -> Unit) :
    RecyclerView.Adapter<SavedCardsAdapter.SavedCardViewHolder>() {
    private val savedCardsList = mutableListOf<CardItem>()
    private var selectedItemPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_card, parent, false)

        return SavedCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedCardsList.size
    }

    override fun onBindViewHolder(holder: SavedCardViewHolder, position: Int) {
        holder.setCardData(savedCardsList[position], position)
    }

    fun setItems(cardsLists: List<CardItem>) {
        savedCardsList.clear()
        savedCardsList.addAll(cardsLists)
        notifyDataSetChanged()
    }

    fun submit() {
        if (selectedItemPosition != -1) {
            if (savedCardsList[selectedItemPosition].cvv.isEmpty()) {
                savedCardsList[selectedItemPosition].isError = true
            } else {
                onSubmitDataValid(savedCardsList[selectedItemPosition])
            }
        }
    }

    inner class SavedCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var position: Int = -1
        fun setCardData(cardItem: CardItem, position: Int) {
            maskedCardNumber.text = cardItem.maskedCardNumber
            cardName.text = cardItem.displayName
            if (cardItem.isSelected) {
                selectedItemPosition = position
                selectCard.isChecked = true
            }
        }

        private val cvv: EditText = itemView.findViewById(R.id.ccv_editText)
        private val maskedCardNumber: TextView =
            itemView.findViewById(R.id.masked_card_number_text_view)
        private val cardName: TextView = itemView.findViewById(R.id.card_name_textview)
        private val selectCard: RadioButton = itemView.findViewById(R.id.card_selected_radio_button)

        init {
            selectCard.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    cvv.visibility = View.VISIBLE
                } else {
                    updateSelectedCard(position)
                    cvv.visibility = View.INVISIBLE
                }
            }

            cvv.doAfterTextChanged {
                savedCardsList[position].cvv = it.toString()
            }
        }
    }

    private fun updateSelectedCard(position: Int) {
        savedCardsList[selectedItemPosition] =
            savedCardsList[selectedItemPosition].copy(isSelected = false, isError = false)

        savedCardsList[position] =
            savedCardsList[position].copy(isSelected = true)
        this.selectedItemPosition = position
    }
}