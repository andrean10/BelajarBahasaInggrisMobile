package com.tribuanabagus.belajarbahasainggris.model.auth.role

import com.google.gson.annotations.SerializedName

data class Role(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)

