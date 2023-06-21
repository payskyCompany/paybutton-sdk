package io.paysky.data.model.response

import com.google.gson.annotations.SerializedName

data class UpdateCardsResponse(
    @SerializedName("Message") val message: String?,
    @SerializedName("ReferenceId") val referenceId: String?,
    @SerializedName("SecureHash") val secureHash: String?,
    @SerializedName("SecureHashData") val secureHashData: String?,
    @SerializedName("StatusCode") val statusCode: Int,
    @SerializedName("Success") val success: Boolean,
    @SerializedName("TransactionId") val transactionId: String?,
    @SerializedName("ErrorDetail") val errorDetail: String?,
    @SerializedName("ErrorDescription") val ErrorDescription: String?
)

