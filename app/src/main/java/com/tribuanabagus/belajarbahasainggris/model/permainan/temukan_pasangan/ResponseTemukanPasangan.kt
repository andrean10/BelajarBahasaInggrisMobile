package com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan

import com.google.gson.annotations.SerializedName
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.jawaban_pasangan.ResultsJawabanPasangan

data class ResponseTemukanPasangan(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsTemukanPasangan>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsTemukanPasangan(

    @field:SerializedName("suara")
    val suara: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("jawaban_pasangan")
    val jawabanPasangan: List<ResultsJawabanPasangan>? = null
)
