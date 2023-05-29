package io.paysky.ui.fragment.paymentprocessing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paybutton.R
import io.paysky.data.model.PaymentData
import io.paysky.data.model.ReceiptData
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.ui.fragment.paymentfail.PaymentFailedFragment
import io.paysky.ui.fragment.paymentsuccess.PaymentApprovedFragment
import io.paysky.ui.fragment.webview.WebPaymentFragment
import io.paysky.util.AppConstant
import io.paysky.util.ToastUtils

class PaymentProcessingFragment : BaseFragment(), PaymentProcessingView {
    private lateinit var presenter: PaymentProcessingPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as PaymentActivity?
        // get data from bundle.
        presenter = PaymentProcessingPresenter(arguments, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_processing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initView(view)
    }

    private fun initView(view: View) {
        activity.setHeaderIcon(R.drawable.ic_back)
        activity.setHeaderIconClickListener { activity.finish() }
    }

    override fun showTransactionApprovedFragment(
        transactionNo: String,
        authCode: String,
        receiptNumber: String,
        cardHolder: String,
        cardNumber: String,
        systemReference: String,
        paymentData: PaymentData?
    ) {
        val bundle = Bundle()
        val receiptData = ReceiptData()
        receiptData.rrn = transactionNo
        receiptData.authNumber = authCode
        receiptData.channelName = AppConstant.TransactionChannelName.CARD
        receiptData.refNumber = receiptNumber
        receiptData.receiptNumber = receiptNumber
        receiptData.amount = paymentData?.amountFormatted
        receiptData.cardHolderName = cardHolder
        receiptData.cardNumber = cardNumber
        receiptData.merchantName = paymentData?.merchantName
        receiptData.merchantId = paymentData?.merchantId
        receiptData.terminalId = paymentData?.terminalId
        receiptData.paymentType = ReceiptData.PaymentDoneBy.MANUAL.toString()
        receiptData.stan = systemReference
        receiptData.transactionType = ReceiptData.TransactionType.SALE.name
        receiptData.secureHashKey = paymentData?.secureHashKey
        bundle.putParcelable(AppConstant.BundleKeys.RECEIPT, receiptData)
        activity.replaceFragmentAndRemoveOldFragment(PaymentApprovedFragment::class.java, bundle)
        activity.hidePaymentOptions()
    }

    override fun showErrorInServerToast() {
        ToastUtils.showLongToast(activity, getString(R.string.error_try_again))
    }

    override fun showPaymentFailedFragment(bundle: Bundle) {
        activity.replaceFragmentAndRemoveOldFragment(PaymentFailedFragment::class.java, bundle)
    }

    override fun show3dpWebView(paymentServerURL: String, paymentData: PaymentData?) {
        val bundle = Bundle()
        bundle.putString("url", paymentServerURL)
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData)
        activity.replaceFragmentAndRemoveOldFragment(WebPaymentFragment::class.java, bundle)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}