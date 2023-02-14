package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResponsePercakapan
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PercakapanViewModel : ViewModel() {

    private var _percakapan: MutableLiveData<ResponsePercakapan?>? = null

    fun getPercakapan(): LiveData<ResponsePercakapan?> {
        _percakapan = MutableLiveData()
        percakapan()
        return _percakapan as MutableLiveData<ResponsePercakapan?>
    }

    fun storePercakapan(): LiveData<ResponsePercakapan?> {
        _percakapan = MutableLiveData()
        percakapan()
        return _percakapan as MutableLiveData<ResponsePercakapan?>
    }

    fun updatePercakapan(): LiveData<ResponsePercakapan?> {
        _percakapan = MutableLiveData()
        percakapan()
        return _percakapan as MutableLiveData<ResponsePercakapan?>
    }

    fun deletePercakapan(): LiveData<ResponsePercakapan?> {
        _percakapan = MutableLiveData()
        percakapan()
        return _percakapan as MutableLiveData<ResponsePercakapan?>
    }

    private fun percakapan() {
        val client = ApiConfig.getApiService().getPercakapan()
        client.enqueue(object : Callback<ResponsePercakapan> {
            override fun onResponse(
                call: Call<ResponsePercakapan>,
                response: Response<ResponsePercakapan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _percakapan!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponsePercakapan::class.java
                        )
                    _percakapan!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponsePercakapan>, t: Throwable) {
                _percakapan!!.postValue(null)
            }
        })
    }
}