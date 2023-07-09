package io.paysky.ui.fragment.managecards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paybutton.R
import io.paysky.data.model.response.CardItem
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.util.ToastUtils

class ManageCardsFragment : BaseFragment(), ManageCardsView {
    private lateinit var adapter: CardsAdapter
    private lateinit var presenter: ManageCardsPresenter
    private lateinit var cardsList: RecyclerView
    private lateinit var backButton: Button
    private lateinit var title: View
    private lateinit var setAsDefaultTitle: View
    private lateinit var cardDetailsTitle: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as PaymentActivity?
        presenter = ManageCardsPresenter(arguments, this)
        activity.setHeaderIconClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        activity.hidePaymentInfoAndOptions()
        initView(view)
    }

    private fun initView(view: View) {
        title = view.findViewById(R.id.title)
        setAsDefaultTitle = view.findViewById(R.id.set_as_default_title)
        cardDetailsTitle = view.findViewById(R.id.card_details_title)

        cardsList = view.findViewById(R.id.cards_list)
        adapter = CardsAdapter(
            onChangeDefaultItem = { cardItem, position ->
                presenter.changeDefaultItem(cardItem, position)
            }, onDeleteCard = { cardItem, position ->
                presenter.deleteItem(cardItem, position)
            }
        )
        cardsList.layoutManager = LinearLayoutManager(this.context)
        cardsList.adapter = adapter

        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun showToastError(message: String) {
        ToastUtils.showLongToast(context, message)
    }

    override fun showToastErrorAndFinish(error: Int) {
        ToastUtils.showLongToast(context, getString(error))
        activity.finish()
    }

    override fun showSavedCards(cardsLists: List<CardItem>) {
        title.visibility = View.VISIBLE
        setAsDefaultTitle.visibility = View.VISIBLE
        cardDetailsTitle.visibility = View.VISIBLE
        adapter.setItems(cardsLists)
    }

    override fun setCardAsDefault(position: Int) {
        adapter.setItemAsDefault(position)
    }

    override fun deleteCard(position: Int) {
        adapter.deleteCard(position)
    }

    override fun revertDefaultSelect(position: Int) {
        adapter.revertDefault(position)
    }
}