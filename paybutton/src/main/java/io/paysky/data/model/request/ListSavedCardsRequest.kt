package io.paysky.data.model.request

import com.google.gson.annotations.SerializedName


data class ListSavedCardsRequest(
    @SerializedName("SessionId")
    val sessionId: String,
    @SerializedName("CustomerId")
    val customerId: String,
    @SerializedName("Amount")
    val amount: Double,
    @SerializedName("TerminalId")
    val terminalId: String,
    @SerializedName("MerchantId")
    val merchantId: String,
    @SerializedName("DateTimeLocalTrxn")
    val dateTimeLocalTrxn: String,
    @SerializedName("SecureHash")
    var secureHash: String
)