package io.paysky.data.model.response

import com.google.gson.annotations.SerializedName

data class CardItem(
    @SerializedName("Brand")
    var brand: String?,

    @SerializedName("CardId")
    var cardId: String,

    @SerializedName("DisplayName")
    var displayName: String,

    @SerializedName("IsDefultCard")
    var isDefultCard: Boolean,

    @SerializedName("MaskedCardNumber")
    var maskedCardNumber: String,

    @SerializedName("Postfix")
    var postfix: String?,

    @SerializedName("Token")
    var token: String
)
