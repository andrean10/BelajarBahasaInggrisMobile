package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.permainan.pair.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.ResponseTemukanPasangan
import com.tribuanabagus.belajarbahasainggris.model.permainan.temukan_pasangan.jawaban_pasangan.ResponseJawabanPasangan
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PairViewModel : ViewModel() {

    private var _pair: MutableLiveData<ResponseTemukanPasangan?>? = null
    private var _guess: MutableLiveData<ResponseJawabanPasangan?>? = null

    fun getTemukanPasangan(): LiveData<ResponseTemukanPasangan?> {
        _pair = MutableLiveData()
        temukanPasangan()
        return _pair as MutableLiveData<ResponseTemukanPasangan?>
    }

    fun getJawabanPasangan(idTemukanPasangan: Int): LiveData<ResponseJawabanPasangan?> {
        _guess = MutableLiveData()
        jawabanPasangan(idTemukanPasangan)
        return _guess as MutableLiveData<ResponseJawabanPasangan?>
    }

//    fun storeTemukanPasangan(params: HashMap<String, String>): LiveData<ResponseTemukanPasangan?> {
//        _pair = MutableLiveData()
//        temukanPasangan(UtilsCode.STORE, params = params)
//        return _pair as MutableLiveData<ResponseTemukanPasangan?>
//    }
//
//    fun updateTemukanPasangan(
//        id: Int,
//        params: HashMap<String, String>
//    ): LiveData<ResponseTemukanPasangan?> {
//        _pair = MutableLiveData()
//        temukanPasangan(UtilsCode.UPDATE, id, params)
//        return _pair as MutableLiveData<ResponseTemukanPasangan?>
//    }
//
//    fun deleteTemukanPasangan(id: Int): LiveData<ResponseTemukanPasangan?> {
//        _pair = MutableLiveData()
//        temukanPasangan(UtilsCode.DELETE, id)
//        return _pair as MutableLiveData<ResponseTemukanPasangan?>
//    }

    private fun temukanPasangan(
//        type: Int,
//        id: Int? = null,
//        params: HashMap<String, String>? = null
    ) {
//        val client = when (type) {
//            UtilsCode.STORE -> ApiConfig.getApiService().storeTemukanPasangan(params!!)
//            UtilsCode.UPDATE -> ApiConfig.getApiService().updateTemukanPasangan(id!!, params!!)
//            UtilsCode.DELETE -> ApiConfig.getApiService().deleteTemukanPasangan(id!!)
//            else -> ApiConfig.getApiService().getTemukanPasangan()
//        }

        val client = ApiConfig.getApiService().getSoalTemukanPasangan()
        client.enqueue(object : Callback<ResponseTemukanPasangan> {
            override fun onResponse(
                call: Call<ResponseTemukanPasangan>,
                response: Response<ResponseTemukanPasangan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _pair!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseTemukanPasangan::class.java
                        )
                    _pair!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseTemukanPasangan>, t: Throwable) {
                _pair!!.postValue(null)
            }
        })
    }

    private fun jawabanPasangan(
//        type: Int,
        idTemukanPasangan: Int,
//        params: HashMap<String, String>? = null
    ) {
//        val client = when (type) {
//            UtilsCode.STORE -> ApiConfig.getApiService().storeTemukanPasangan(params!!)
//            UtilsCode.UPDATE -> ApiConfig.getApiService().updateTemukanPasangan(id!!, params!!)
//            UtilsCode.DELETE -> ApiConfig.getApiService().deleteTemukanPasangan(id!!)
//            else -> ApiConfig.getApiService().getTemukanPasangan()
//        }

        val client = ApiConfig.getApiService().getSoalJawabanPasangan(idTemukanPasangan)
        client.enqueue(object : Callback<ResponseJawabanPasangan> {
            override fun onResponse(
                call: Call<ResponseJawabanPasangan>,
                response: Response<ResponseJawabanPasangan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _guess!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseJawabanPasangan::class.java
                        )
                    _guess!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseJawabanPasangan>, t: Throwable) {
                _guess!!.postValue(null)
            }
        })
    }
}