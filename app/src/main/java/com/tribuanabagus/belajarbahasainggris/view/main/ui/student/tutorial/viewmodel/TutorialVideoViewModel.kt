package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.video_pembelajaran.ResponseVideoPembelajaran
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorialVideoViewModel : ViewModel() {

    private var _videoPembelajaran: MutableLiveData<ResponseVideoPembelajaran?>? = null

    fun getVideoPembelajaran(): LiveData<ResponseVideoPembelajaran?> {
        _videoPembelajaran = MutableLiveData()
        videoPembelajaran()
        return _videoPembelajaran as MutableLiveData<ResponseVideoPembelajaran?>
    }

    fun storeVideoPembelajaran(): LiveData<ResponseVideoPembelajaran?> {
        _videoPembelajaran = MutableLiveData()
        videoPembelajaran()
        return _videoPembelajaran as MutableLiveData<ResponseVideoPembelajaran?>
    }

    fun updateVideoPembelajaran(): LiveData<ResponseVideoPembelajaran?> {
        _videoPembelajaran = MutableLiveData()
        videoPembelajaran()
        return _videoPembelajaran as MutableLiveData<ResponseVideoPembelajaran?>
    }

    fun deleteVideoPembelajaran(): LiveData<ResponseVideoPembelajaran?> {
        _videoPembelajaran = MutableLiveData()
        videoPembelajaran()
        return _videoPembelajaran as MutableLiveData<ResponseVideoPembelajaran?>
    }

    private fun videoPembelajaran() {
        val client = ApiConfig.getApiService().getVideoPembelajaran()
        client.enqueue(object : Callback<ResponseVideoPembelajaran> {
            override fun onResponse(
                call: Call<ResponseVideoPembelajaran>,
                response: Response<ResponseVideoPembelajaran>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _videoPembelajaran!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseVideoPembelajaran::class.java
                        )
                    _videoPembelajaran!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseVideoPembelajaran>, t: Throwable) {
                _videoPembelajaran!!.postValue(null)
            }
        })
    }

}