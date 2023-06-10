package io.paysky.ui.fragment.cardsOperations

import android.os.Bundle
import io.paysky.data.model.PaymentData
import io.paysky.data.model.request.GetSessionRequest
import io.paysky.data.model.request.ListSavedCardsRequest
import io.paysky.data.model.response.CardItem
import io.paysky.data.model.response.GetSessionResponse
import io.paysky.data.model.response.ListSavedCardsResponse
import io.paysky.data.network.ApiConnection
import io.paysky.data.network.ApiResponseListener
import io.paysky.ui.mvp.BasePresenter
import io.paysky.util.AppConstant
import io.paysky.util.AppUtils
import io.paysky.util.HashGenerator
import io.paysky.util.parcelable

open class CardsPresenter<V : CardsView>(arguments: Bundle?, view: V) :
    BasePresenter<V>() {
    val cardsList = mutableListOf<CardItem>()
    val paymentData: PaymentData?

    init {
        attachView(view)
        paymentData = arguments?.parcelable(AppConstant.BundleKeys.PAYMENT_DATA)
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

        val dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
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
                    //view.dismissProgress()
                    if (response != null) {
                        if (response.success) {
                            paymentData?.customerSession = response.sessionId!!
                            listCustomerCards()
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


    private fun listCustomerCards() {
        paymentData?.let { payment ->
            // check internet.
            if (!view.isInternetAvailable) {
                view.showNoInternetDialog()
                return
            }
            //view.showProgress()
            val dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
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
                                cardsList.clear()
                                cardsList.addAll(it.cardsLists)
                                setDefaultCardSelected()
                                view.showSavedCards(cardsList)
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

    private fun setDefaultCardSelected() {
        cardsList.find { it.isDefaultCard }?.isSelected = true
    }
}