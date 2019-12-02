package com.example.numberslight.service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NumberDataDetails(val name: String?, val text: String?, val image: String?) : Parcelable