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
import io.paysky.ui.fragment.listcards.ListCardsFragment
import io.paysky.util.AppConstant
import io.paysky.util.ToastUtils

class ManageCardsFragment : BaseFragment(), ManageCardsView {
    private lateinit var adapter: CardsAdapter
    private lateinit var presenter: ManageCardsPresenter
    private lateinit var cardsList: RecyclerView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as PaymentActivity?
        presenter = ManageCardsPresenter(arguments, this)
        activity.setHeaderIconClickListener {
            val bundle = Bundle()
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, presenter.paymentData)
            activity.replaceFragmentAndRemoveOldFragment(ListCardsFragment::class.java, bundle)
        }
        activity.hidePaymentInfoAndOptions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
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
            val bundle = Bundle()
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, presenter.paymentData)
            activity.replaceFragmentAndRemoveOldFragment(ListCardsFragment::class.java, bundle)
        }
    }

    override fun showToastError(message: String) {
        ToastUtils.showLongToast(context, message)
    }

    override fun showSavedCards(cardsLists: List<CardItem>) {
        adapter.setItems(cardsLists)
    }

    override fun setCardAsDefault(position: Int) {
        adapter.setItemAsDefault(position)
    }

    override fun deleteCard(position: Int) {
        adapter.deleteCard(position)
    }
}