package io.paysky.ui.fragment.managecards

import android.os.Bundle
import io.paysky.data.model.request.UpdateCardsRequest
import io.paysky.data.model.response.CardItem
import io.paysky.data.model.response.UpdateCardsResponse
import io.paysky.data.network.ApiConnection
import io.paysky.data.network.ApiResponseListener
import io.paysky.ui.fragment.cardsOperations.CardsPresenter
import io.paysky.util.AppUtils
import io.paysky.util.HashGenerator

class ManageCardsPresenter(
    arguments: Bundle?,
    view: ManageCardsView
) : CardsPresenter<ManageCardsView>(arguments, view) {
    init {
        attachView(view)
    }

    fun changeDefaultItem(cardItem: CardItem, position: Int) {
        paymentData?.let { payment ->
            // check internet.
            if (!view.isInternetAvailable) {
                view.showNoInternetDialog()
                return
            }
            view.showProgress()
            val dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
            val secureHash = HashGenerator.encode(
                paymentData.secureHashKey,
                dateTimeLocalTrxn,
                payment.merchantId,
                payment.terminalId
            )
            val request = UpdateCardsRequest(
                cardToken = cardItem.token,
                customerId = payment.customerId,
                dateTimeLocalTrxn = dateTimeLocalTrxn,
                merchantId = payment.merchantId,
                terminalId = payment.terminalId,
                secureHash = secureHash
            )
            ApiConnection.changeDefaultToken(request,
                object : ApiResponseListener<UpdateCardsResponse> {
                    override fun onSuccess(response: UpdateCardsResponse?) {
                        view.dismissProgress()
                        response?.let {
                            if (it.success) {
                                view.setCardAsDefault(position)
                            } else {
                                view.revertDefaultSelect(position)
                                val error = response.message ?: (response.errorDetail ?: "")
                                view.showToastError(error)
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

    fun deleteItem(cardItem: CardItem, position: Int) {
        paymentData?.let { payment ->
            // check internet.
            if (!view.isInternetAvailable) {
                view.showNoInternetDialog()
                return
            }
            view.showProgress()
            val dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn()
            val secureHash = HashGenerator.encode(
                paymentData.secureHashKey,
                dateTimeLocalTrxn,
                payment.merchantId,
                payment.terminalId
            )
            val request = UpdateCardsRequest(
                cardToken = cardItem.token,
                customerId = payment.customerId,
                dateTimeLocalTrxn = dateTimeLocalTrxn,
                merchantId = payment.merchantId,
                terminalId = payment.terminalId,
                secureHash = secureHash
            )
            ApiConnection.deleteTokenizedCard(request,
                object : ApiResponseListener<UpdateCardsResponse> {
                    override fun onSuccess(response: UpdateCardsResponse?) {
                        view.dismissProgress()
                        response?.let {
                            if (it.success) {
                                view.deleteCard(position)
                            } else {
                                val error = response.message ?: (response.errorDetail ?: "")
                                view.showToastError(error)
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
}