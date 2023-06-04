package io.paysky.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenizedCardPaymentParameters(
    val TokenCardId: Int,
    val cvv: String
) : Parcelable
