package io.paysky.data.model.response

import com.google.gson.annotations.SerializedName

data class CardItem(
    @SerializedName("Brand")
    val brand: String?,

    @SerializedName("CardId")
    val cardId: Int,

    @SerializedName("DisplayName")
    val displayName: String,

    @SerializedName("IsDefaultCard")
    val isDefaultCard: Boolean,

    @SerializedName("MaskedCardNumber")
    val maskedCardNumber: String,

    @SerializedName("Postfix")
    val postfix: String?,

    @SerializedName("Token")
    val token: String,

    var isSelected: Boolean = false,
    var isError: Boolean = false,
    var cvv: String?
)
