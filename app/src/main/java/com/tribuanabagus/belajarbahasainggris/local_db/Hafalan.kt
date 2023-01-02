package com.tribuanabagus.belajarbahasainggris.local_db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hafalan(
    val img: String? = null,
    val title: String,
    val description: String,
) : Parcelable
