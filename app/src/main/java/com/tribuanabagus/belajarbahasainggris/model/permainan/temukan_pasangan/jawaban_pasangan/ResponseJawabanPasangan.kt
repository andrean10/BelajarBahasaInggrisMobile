package com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.jawaban_pasangan

import com.google.gson.annotations.SerializedName

data class ResponseJawabanPasangan(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsJawabanPasangan>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsJawabanPasangan(

    @field:SerializedName("kata")
    val kata: String? = null,

    @field:SerializedName("temukanPasanganId")
    val temukanPasanganId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null
)
