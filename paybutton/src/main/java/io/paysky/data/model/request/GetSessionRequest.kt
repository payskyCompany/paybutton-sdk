package io.paysky.data.model.request

import com.google.gson.annotations.SerializedName

data class GetSessionRequest(
    @SerializedName("DateTimeLocalTrxn") val dateTimeLocalTrxn: String,
    @SerializedName("Amount") val amount: Double,
    @SerializedName("TerminalId") val terminalId: String,
    @SerializedName("CustomerId") val customerId: String,
    @SerializedName("MerchantId") val merchantId: String,
    @SerializedName("SecureHash") val secureHash: String
) : BaseRequest()

