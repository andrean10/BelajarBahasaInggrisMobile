package com.tribuanabagus.belajarbahasainggris.model.auth

import com.google.gson.annotations.SerializedName
import com.tribuanabagus.belajarbahasainggris.model.auth.role.Role

data class ResponseRegister(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("results")
    val results: ResultsRegister? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ResultsRegister(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("role")
    val role: Role? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)