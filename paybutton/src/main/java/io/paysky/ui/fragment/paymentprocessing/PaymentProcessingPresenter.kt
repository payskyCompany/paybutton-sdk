package io.paysky.ui.fragment.paymentprocessing

import android.os.Bundle
import android.util.Log
import io.paysky.data.model.CardPaymentParameters
import io.paysky.data.model.PaymentData
import io.paysky.data.model.SuccessfulCardTransaction
import io.paysky.data.model.request.ManualPaymentRequest
import io.paysky.data.model.response.ManualPaymentResponse
import io.paysky.data.network.ApiConnection
import io.paysky.data.network.ApiLinks
import io.paysky.data.network.ApiResponseListener
import io.paysky.exception.TransactionException
import io.paysky.ui.mvp.BasePresenter
import io.paysky.util.AppConstant
import io.paysky.util.AppUtils
import io.paysky.util.HashGenerator
import io.paysky.util.TransactionManager
import io.paysky.util.parcelable

class PaymentProcessingPresenter(
    arguments: Bundle?,
    view: PaymentProcessingView
) :
    BasePresenter<PaymentProcessingView>() {
    private val paymentData: PaymentData?
    private val cardPayment: CardPaymentParameters?

    init {
        paymentData = arguments?.parcelable(AppConstant.BundleKeys.PAYMENT_DATA)
        cardPayment = arguments?.parcelable(AppConstant.BundleKeys.CARD_DATA)
        TransactionManager.setTransactionType(TransactionManager.TransactionType.MANUAL)
        attachView(view)
        makePayment()
    }

    private fun makePayment() {
        paymentData?.let { payment ->
            cardPayment?.let { cardData ->
                executeManualPayment(
                    payment.secureHashKey,
                    payment.currencyCode,
                    payment.amountFormatted,
                    payment.merchantId,
                    payment.terminalId,
                    cardData.cvv,
                    cardData.expireDate,
                    cardData.cardOwnerName,
                    cardData.cardNumber
                )
            } ?: {
                Log.d("Make Payment", "makePayment: card payment null")
            }
        } ?: {
            Log.d("Make Payment", "makePayment: payment data null")
        }
    }


    private fun executeManualPayment(
        secureHash: String,
        currencyCode: String,
        payAmount: String,
        merchantId: String,
        terminalId: String,
        ccv: String,
        expiryDate: String,
        cardHolder: String,
        cardNumber: String
    ) {
        // check internet.
        if (!view.isInternetAvailable) {
            view.showNoInternetDialog()
            return
        }
        view.showProgress()
        // create request.
        val paymentRequest = ManualPaymentRequest()
        val amount = AppUtils.formatPaymentAmountToServer(payAmount)
        paymentRequest.amountTrxn = amount.toString() + ""
        paymentRequest.cardAcceptorIDcode = merchantId
        paymentRequest.cardAcceptorTerminalID = terminalId
        paymentRequest.currencyCodeTrxn = currencyCode
        paymentRequest.cvv2 = ccv
        paymentRequest.dateExpiration = expiryDate
        paymentRequest.iSFromPOS = true
        paymentRequest.pAN = cardNumber
        paymentRequest.systemTraceNr = paymentData?.transactionReferenceNumber
        paymentRequest.MerchantReference = paymentData?.transactionReferenceNumber
        paymentRequest.dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
        paymentRequest.merchantId = merchantId
        paymentRequest.terminalId = terminalId
        paymentRequest.returnURL = ApiLinks.PAYMENT_LINK
        // create secure hash.
        paymentRequest.secureHash = HashGenerator.encode(
            secureHash,
            paymentRequest.dateTimeLocalTrxn,
            merchantId,
            terminalId
        )
        // make transaction.
        ApiConnection.executePayment(
            paymentRequest,
            object : ApiResponseListener<ManualPaymentResponse?> {
                override fun onSuccess(response: ManualPaymentResponse?) {
                    if (isViewDetached) return
                    // server make response.
                    view.dismissProgress()
                    if (response?.challengeRequired == true) {
                        view.show3dpWebView(response.threeDSUrl, paymentData)
                    } else {
                        if (response?.mWActionCode != null) {
                            val transactionException = TransactionException()
                            transactionException.errorMessage = response.mWMessage
                            TransactionManager.setTransactionException(transactionException)
                            val bundle = Bundle()
                            bundle.putString("decline_cause", response.mWMessage)
                            bundle.putString("opened_by", "manual_payment")
                            view.showPaymentFailedFragment(bundle)
                        } else {
                            if (response?.actionCode == null || response.actionCode.isEmpty() || response.actionCode != "00") {
                                val transactionException = TransactionException()
                                transactionException.errorMessage = response?.message
                                TransactionManager.setTransactionException(transactionException)
                                val bundle = Bundle()
                                bundle.putString("decline_cause", response?.message)
                                bundle.putString("opened_by", "manual_payment")
                                view.showPaymentFailedFragment(bundle)
                            } else {
                                // transaction success.
                                val cardTransaction = SuccessfulCardTransaction()
                                cardTransaction.ActionCode = response.actionCode
                                cardTransaction.AuthCode = response.authCode
                                cardTransaction.MerchantReference = response.merchantReference
                                cardTransaction.Message = response.message
                                cardTransaction.NetworkReference = response.networkReference
                                cardTransaction.ReceiptNumber = response.receiptNumber
                                cardTransaction.SystemReference =
                                    response.systemReference.toString() + ""
                                cardTransaction.Success = response.success
                                cardTransaction.merchantId = paymentData?.merchantId
                                cardTransaction.terminalId = paymentData?.terminalId
                                cardTransaction.amount = paymentData?.executedTransactionAmount
                                TransactionManager.setCardTransaction(cardTransaction)
                                view.showTransactionApprovedFragment(
                                    transactionNo = response.transactionNo,
                                    authCode = response.authCode,
                                    receiptNumber = response.receiptNumber,
                                    cardHolder = cardHolder,
                                    cardNumber = cardNumber,
                                    systemReference = response.systemReference.toString() + "",
                                    paymentData = paymentData
                                )
                            }
                        }
                    }
                }

                override fun onFail(error: Throwable) {
                    // payment failed.
                    if (isViewDetached) return
                    //view.dismissProgress()
                    val transactionException = TransactionException()
                    transactionException.errorMessage = error.message
                    TransactionManager.setTransactionException(transactionException)
                    error.printStackTrace()
                    view.showErrorInServerToast()
                }
            })
    }
}