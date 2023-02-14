package com.tribuanabagus.belajarbahasainggris.model.nilai.permainan

import com.google.gson.annotations.SerializedName

data class ResponseNilaiPermainan(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: ResultsNilaiPermainan? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsNilaiPermainan(

    @field:SerializedName("totalNilai")
    val totalNilai: Float? = null
)
