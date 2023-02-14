package com.tribuanabagus.belajarbahasainggris.model.soal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class ResponseSoal(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("results")
	val results: List<ResultsSoal>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

@Parcelize
data class ResultsSoal(

	@field:SerializedName("sounds")
	val sounds: String? = null,

	@field:SerializedName("jawaban_soal")
	val jawabanSoal: String? = null,

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("percakapan")
	val percakapan: @RawValue Percakapan? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("hafalan")
	val hafalan: @RawValue Hafalan? = null
) : Parcelable

data class Hafalan(

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Percakapan(

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

