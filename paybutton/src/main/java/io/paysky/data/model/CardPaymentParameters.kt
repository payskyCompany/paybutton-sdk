package io.paysky.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardPaymentParameters(
    val cardNumber: String,
    val cardOwnerName: String,
    val expireDate: String,
    val cvv: String,
    val isSaveCard:Boolean,
    val isDefault:Boolean
) : Parcelable