package com.tribuanabagus.belajarbahasainggris.local_db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoPembelajaran(
    val title: String,
    val description: String,
    val url: String,
) : Parcelable
