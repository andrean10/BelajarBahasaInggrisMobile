package com.tribuanabagus.belajarbahasainggris.model.nilai.belajar

import com.google.gson.annotations.SerializedName

data class ResponseNilaiBelajar(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsNilaiBelajar>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsNilaiBelajar(

    @field:SerializedName("soal")
    val soal: Soal? = null,

    @field:SerializedName("nilai")
    val nilai: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("userId")
    val userId: Int? = null
)

data class Soal(

    @field:SerializedName("hafalanId")
    val hafalanId: Int? = null,

    @field:SerializedName("sounds")
    val sounds: String? = null,

    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("percakapanId")
    val percakapanId: Int? = null
)
