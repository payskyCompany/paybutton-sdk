package io.paysky.ui.fragment.listcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paybutton.R
import io.paysky.data.model.response.CardItem
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.util.hideSoftKeyboard


class SavedCardsAdapter(
    val onSubmitDataValid: (CardItem) -> Unit,
    val onChangeItem: (Int) -> Unit,
    val activity: PaymentActivity
) : RecyclerView.Adapter<SavedCardsAdapter.SavedCardViewHolder>() {
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
        val diffCallback = CardsCallback(this.savedCardsList, cardsLists)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        savedCardsList.clear()
        savedCardsList.addAll(cardsLists)
        diffResult.dispatchUpdatesTo(this)
    }

    fun submit() {
        if (selectedItemPosition != -1) {
            if (savedCardsList[selectedItemPosition].cvv.isNullOrEmpty()) {
                savedCardsList[selectedItemPosition].isError = true
                notifyItemChanged(selectedItemPosition)
            } else {
                savedCardsList[selectedItemPosition].isError = false
                onSubmitDataValid(savedCardsList[selectedItemPosition])
                notifyItemChanged(selectedItemPosition)
            }
        }
    }

    inner class SavedCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var position: Int = -1
        private val cvv: EditText = itemView.findViewById(R.id.ccv_editText)
        private val maskedCardNumber: TextView =
            itemView.findViewById(R.id.masked_card_number_text_view)
        private val cardName: TextView = itemView.findViewById(R.id.card_name_textview)
        private val selectCard: RadioButton = itemView.findViewById(R.id.card_selected_radio_button)
        private val cvvRequired: View = itemView.findViewById(R.id.cvv_required)

        fun setCardData(cardItem: CardItem, position: Int) {
            selectCard.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    updateSelectedCard(position)
                }
            }

            cvv.doAfterTextChanged {
                savedCardsList[position].cvv = it.toString()
                if (!it.isNullOrEmpty()) {
                    savedCardsList[position].isError = false
                }
                updateCvvBackground(cardItem)
            }

            this.position = position
            maskedCardNumber.text = cardItem.maskedCardNumber
            cardName.text = cardItem.displayName
            if (cardItem.isSelected) {
                selectedItemPosition = position
                selectCard.isChecked = true
                cvv.visibility = View.VISIBLE
                cvv.setText(cardItem.cvv ?: "")
            } else {
                selectCard.isChecked = false
                cvv.visibility = View.INVISIBLE
                cvv.setText("")
            }
            cvv.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard(activity = activity)
                    true
                } else false
            }
        }

        private fun updateCvvBackground(cardItem: CardItem) {
            if (cardItem.isError) {
                cvv.setBackgroundResource(R.drawable.error_border_background)
                cvvRequired.visibility = View.VISIBLE
            } else {
                cvv.setBackgroundResource(R.drawable.cvv_border_background)
                cvvRequired.visibility = View.GONE
            }
        }
    }

    private fun updateSelectedCard(position: Int) {
        if (selectedItemPosition != -1) {
            savedCardsList[selectedItemPosition] =
                savedCardsList[selectedItemPosition].copy(isSelected = false, isError = false)
        }
        savedCardsList[position] =
            savedCardsList[position].copy(isSelected = true)

        onChangeItem(position)
        if (selectedItemPosition != -1) {
            onChangeItem(selectedItemPosition)
        }

        this.selectedItemPosition = position
    }
}