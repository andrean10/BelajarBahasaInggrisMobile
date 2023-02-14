package com.tribuanabagus.belajarbahasainggris.model.permainan.tebak_gambar

import com.google.gson.annotations.SerializedName

data class ResponseTebakGambar(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsTebakGambar>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsTebakGambar(

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("opsi1")
    val opsi1: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("opsi2")
    val opsi2: String? = null,

    @field:SerializedName("opsi3")
    val opsi3: String? = null,

    @field:SerializedName("kunci_jawaban")
    val kunciJawaban: String? = null
)
