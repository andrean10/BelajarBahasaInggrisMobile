package com.tribuanabagus.belajarbahasainggris.model.percakapan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponsePercakapan(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsPercakapan>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

@Parcelize
data class ResultsPercakapan(

    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null
) : Parcelable
