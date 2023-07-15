package io.paysky.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateCardsRequest(
    @SerializedName("Token") val cardToken: String,
    @SerializedName("CustomerId") val customerId: String,
    @SerializedName("DateTimeLocalTrxn") val dateTimeLocalTrxn: String,
    @SerializedName("MerchantId") val merchantId: String,
    @SerializedName("TerminalId") val terminalId: String,
    @SerializedName("SecureHash") var secureHash: String
)
