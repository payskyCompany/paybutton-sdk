package io.paysky.ui.fragment.listcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.paybutton.R
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.ui.fragment.manualpayment.ManualPaymentFragment
import io.paysky.util.AppConstant
import io.paysky.util.ToastUtils

class ListCardsFragment : BaseFragment(), ListCardsView {
    private lateinit var presenter: ListCardsPresenter
    private lateinit var addNewCardButton: Button

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
    }

    override fun sessionIdFetchedSuccessfully() {
        presenter.listCustomerCards()
    }

    override fun showToastError(message: String) {
        ToastUtils.showLongToast(context, message)
    }
}