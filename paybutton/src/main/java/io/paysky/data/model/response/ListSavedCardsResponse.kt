package io.paysky.data.model.response

import com.google.gson.annotations.SerializedName

data class ListSavedCardsResponse(
    @SerializedName("Message")
    var message: String?,

    @SerializedName("ReferenceId")
    var referenceId: String?,

    @SerializedName("SecureHash")
    var secureHash: String?,

    @SerializedName("SecureHashData")
    var secureHashData: String?,

    @SerializedName("StatusCode")
    var statusCode: Int,

    @SerializedName("Success")
    var success: Boolean,

    @SerializedName("TransactionId")
    var transactionId: String?,

    @SerializedName("cardsLists")
    var cardsLists: List<CardItem>
)
