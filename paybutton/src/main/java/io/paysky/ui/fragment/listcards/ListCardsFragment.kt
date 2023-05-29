package io.paysky.ui.fragment.listcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paybutton.R
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.util.ToastUtils

class ListCardsFragment : BaseFragment(), ListCardsView {
    private lateinit var presenter: ListCardsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as PaymentActivity?
        // get data from bundle.
        presenter = ListCardsPresenter(arguments, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_cards, container, false)
    }

    override fun sessionIdFetchedSuccessfully() {
        presenter.listCustomerCards()
    }

    override fun showToastError(message: String) {
        ToastUtils.showLongToast(context, message)
    }
}