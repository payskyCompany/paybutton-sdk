package io.paysky.ui.fragment.cardsOperations

import android.os.Bundle
import com.example.paybutton.R
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
        val secureHash = HashGenerator.encode(
            paymentData?.secureHashKey,
            dateTimeLocalTrxn,
            merchantId,
            terminalId
        )
        if (secureHash != null) {
            val request = GetSessionRequest(
                customerId = customerId,
                merchantId = merchantId,
                terminalId = terminalId,
                dateTimeLocalTrxn = dateTimeLocalTrxn,
                amount = amount,
                secureHash = secureHash
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
                                view.dismissProgress()
                                view.showToastError(response.message!!)
                            }
                        } else {
                            view.dismissProgress()
                            view.showToastError("Something went wrong")
                        }
                    }

                    override fun onFail(error: Throwable) {
                        error.printStackTrace()
                        view.dismissProgress()
                    }
                }
            )
        } else {
            view.showToastErrorAndFinish(R.string.something_went_wrong)
        }
    }


    private fun listCustomerCards() {
        paymentData?.let { payment ->
            // check internet.
            if (!view.isInternetAvailable) {
                view.showNoInternetDialog()
                return
            }
            // create request body.
            val dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
            val secureHash = HashGenerator.encode(
                paymentData.secureHashKey,
                dateTimeLocalTrxn,
                payment.merchantId,
                payment.terminalId
            )
            val request = ListSavedCardsRequest(
                sessionId = payment.customerSession,
                customerId = payment.customerId,
                amount = payment.amount,
                terminalId = payment.terminalId,
                merchantId = payment.merchantId,
                dateTimeLocalTrxn = dateTimeLocalTrxn,
                secureHash = secureHash
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
                            view.showToastError("Something went wrong")
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