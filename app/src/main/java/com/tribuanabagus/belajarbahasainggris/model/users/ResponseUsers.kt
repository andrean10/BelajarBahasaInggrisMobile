package com.tribuanabagus.belajarbahasainggris.model.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseUsers(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("results")
    val results: List<ResultsUsers>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

@Parcelize
data class ResultsUsers(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("roleId")
    val roleId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable