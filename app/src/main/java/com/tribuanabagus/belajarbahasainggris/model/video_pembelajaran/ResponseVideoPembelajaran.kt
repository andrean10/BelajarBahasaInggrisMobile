package com.tribuanabagus.belajarbahasainggris.model.video_pembelajaran

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseVideoPembelajaran(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsVideoPembelajaran>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

@Parcelize
data class ResultsVideoPembelajaran(

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("video")
    val video: String? = null,

    @field:SerializedName("title")
    val title: String? = null
) : Parcelable