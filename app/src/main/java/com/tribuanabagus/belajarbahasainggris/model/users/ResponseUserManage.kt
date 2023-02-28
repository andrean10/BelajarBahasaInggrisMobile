package com.tribuanabagus.belajarbahasainggris.model.users

import com.google.gson.annotations.SerializedName

data class ResponseUserManage(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("results")
	val results: ResultsUserManage? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ResultsUserManage(

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
)
