package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResponseHafalan
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HafalanViewModel : ViewModel() {

    private var _hafalan: MutableLiveData<ResponseHafalan?>? = null

    fun getHafalan(): LiveData<ResponseHafalan?> {
        _hafalan = MutableLiveData()
        hafalan()
        return _hafalan as MutableLiveData<ResponseHafalan?>
    }

    private fun hafalan() {
        val client = ApiConfig.getApiService().getHafalan()
        client.enqueue(object : Callback<ResponseHafalan> {
            override fun onResponse(
                call: Call<ResponseHafalan>,
                response: Response<ResponseHafalan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _hafalan!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseHafalan::class.java
                        )
                    _hafalan!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseHafalan>, t: Throwable) {
                _hafalan!!.postValue(null)
            }
        })
    }
}