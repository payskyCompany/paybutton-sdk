package io.paysky.ui.fragment.listcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paybutton.R
import io.paysky.data.model.TokenizedCardPaymentParameters
import io.paysky.data.model.response.CardItem
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.ui.fragment.manualpayment.ManualPaymentFragment
import io.paysky.ui.fragment.paymentprocessing.PaymentProcessingFragment
import io.paysky.util.AppConstant
import io.paysky.util.ToastUtils
import io.paysky.util.hideSoftKeyboard

class ListCardsFragment : BaseFragment(), ListCardsView {
    private lateinit var presenter: ListCardsPresenter
    private lateinit var addNewCardButton: Button
    private lateinit var proceedButton: Button
    private lateinit var backButton: Button
    private lateinit var cardsList: RecyclerView
    private lateinit var adapter: SavedCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as PaymentActivity?
        activity.setHeaderIconClickListener { activity.finish() }

        presenter = ListCardsPresenter(arguments, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        addNewCardButton = view.findViewById(R.id.add_new_card_button)
        addNewCardButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, presenter.paymentData)
            activity.replaceFragmentAndAddOldToBackStack(ManualPaymentFragment::class.java, bundle)
        }

        adapter = SavedCardsAdapter(
            onSubmitDataValid = {
                moveToPaymentProcessing(it.cardId, it.cvv!!)
            },
            onChangeItem = {
                val mHandler = activity.window.decorView.handler
                mHandler.post {
                    adapter.notifyItemChanged(it)
                }
            }
        )
        cardsList = view.findViewById(R.id.cards_list)
        cardsList.layoutManager = LinearLayoutManager(this.context)
        cardsList.adapter = adapter

        proceedButton = view.findViewById(R.id.proceed_button)
        proceedButton.setOnClickListener {
            adapter.submit()
        }

        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            activity.finish()
        }
    }

    private fun moveToPaymentProcessing(cardId: Int, cvv: String) {
        hideSoftKeyboard(activity)
        val tokenizedCardPaymentParameters = TokenizedCardPaymentParameters(cardId, cvv)
        val bundle = Bundle()
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, presenter.paymentData)
        bundle.putParcelable(AppConstant.BundleKeys.TOKENIZED_CARD, tokenizedCardPaymentParameters)
        activity.replaceFragmentAndRemoveOldFragment(PaymentProcessingFragment::class.java, bundle)
    }

    override fun sessionIdFetchedSuccessfully() {
        presenter.listCustomerCards()
    }

    override fun showToastError(message: String) {
        ToastUtils.showLongToast(context, message)
    }

    override fun showSavedCards(cardsLists: List<CardItem>) {
        adapter.setItems(cardsLists)
    }
}