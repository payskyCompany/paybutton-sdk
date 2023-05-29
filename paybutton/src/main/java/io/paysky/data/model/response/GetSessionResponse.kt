package io.paysky.data.model.response

import com.google.gson.annotations.SerializedName

data class GetSessionResponse(
    @SerializedName("SecureHashData") val secureHashData: String?,
    @SerializedName("TransactionId") val secureHash: String?,
    @SerializedName("Message") val message: String?,
    @SerializedName("ReferenceId") val referenceId: String?,
    @SerializedName("StatusCode") val statusCode: Int,
    @SerializedName("SessionId") val sessionId: String?,
    @SerializedName("Success") val success: Boolean,
    @SerializedName("TransactionId") val transactionId: String?
)

