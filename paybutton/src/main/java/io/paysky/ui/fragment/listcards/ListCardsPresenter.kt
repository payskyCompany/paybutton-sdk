package io.paysky.ui.fragment.listcards

import android.os.Bundle
import io.paysky.data.model.PaymentData
import io.paysky.data.model.request.GetSessionRequest
import io.paysky.data.model.request.ListSavedCardsRequest
import io.paysky.data.model.response.GetSessionResponse
import io.paysky.data.model.response.ListSavedCardsResponse
import io.paysky.data.network.ApiConnection
import io.paysky.data.network.ApiResponseListener
import io.paysky.ui.mvp.BasePresenter
import io.paysky.util.AppConstant
import io.paysky.util.AppUtils.getDateTimeLocalTrxn
import io.paysky.util.HashGenerator
import io.paysky.util.parcelable

class ListCardsPresenter(
    arguments: Bundle?,
    view: ListCardsView

) : BasePresenter<ListCardsView>() {
    val paymentData: PaymentData?

    init {
        paymentData = arguments?.parcelable(AppConstant.BundleKeys.PAYMENT_DATA)
        attachView(view)
        getSessionToken()
    }

    private fun getSessionToken() {
        paymentData?.let { payment ->
            getSession(
                terminalId = payment.terminalId,
                merchantId = payment.merchantId,
                amount = payment.amount,
                customerId = payment.customerId
            )
        }
    }

    private fun getSession(
        terminalId: String,
        merchantId: String,
        amount: Double,
        customerId: String
    ) {
        // check internet.
        if (!view.isInternetAvailable) {
            view.showNoInternetDialog()
            return
        }
        view.showProgress()
        // create request body.

        val dateTimeLocalTrxn = getDateTimeLocalTrxn()
        val request = GetSessionRequest(
            customerId = customerId,
            merchantId = merchantId,
            terminalId = terminalId,
            dateTimeLocalTrxn = dateTimeLocalTrxn,
            amount = amount,
            secureHash = HashGenerator.encode(
                paymentData?.secureHashKey,
                dateTimeLocalTrxn,
                merchantId,
                terminalId
            )
        )

        ApiConnection.getSession(
            request,
            object : ApiResponseListener<GetSessionResponse?> {
                override fun onSuccess(response: GetSessionResponse?) {
                    view.dismissProgress()
                    if (response != null) {
                        if (response.success) {
                            paymentData?.customerSession = response.sessionId!!
                            view.sessionIdFetchedSuccessfully()
                        } else {
                            view.showToastError(response.message!!)
                        }
                    } else {
                        view.showToastError("Errorrrr")
                    }
                }

                override fun onFail(error: Throwable) {
                    error.printStackTrace()
                    view.dismissProgress()
                }
            }
        )
    }

    fun listCustomerCards() {
        paymentData?.let { payment ->
            // check internet.
            if (!view.isInternetAvailable) {
                view.showNoInternetDialog()
                return
            }
            view.showProgress()
            val dateTimeLocalTrxn = getDateTimeLocalTrxn()
            val request = ListSavedCardsRequest(
                sessionId = payment.customerSession,
                customerId = payment.customerId,
                amount = payment.amount,
                terminalId = payment.terminalId,
                merchantId = payment.merchantId,
                dateTimeLocalTrxn = dateTimeLocalTrxn
            )
            ApiConnection.listSavedCards(request,
                object : ApiResponseListener<ListSavedCardsResponse?> {
                    override fun onSuccess(response: ListSavedCardsResponse?) {
                        view.dismissProgress()
                        response?.let {
                            if (it.success) {
                                view.showSavedCards(it.cardsLists)
                            } else {
                                view.showToastError(response.message!!)
                            }
                        } ?: {
                            view.showToastError("Errorrrr")
                        }
                    }

                    override fun onFail(error: Throwable) {
                        error.printStackTrace()
                        view.dismissProgress()
                    }
                }
            )
        }
    }
}