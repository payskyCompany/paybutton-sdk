package io.paysky.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardPaymentParameters(
    val cardNumber: String,
    val cardOwnerName: String,
    val expireDate: String,
    val cvv: String
) : Parcelable
