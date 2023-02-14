package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.soal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.soal.ResponseSoal
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoalViewModel : ViewModel() {

    private var _soal: MutableLiveData<ResponseSoal?>? = null

    fun getSoal(queryParams: HashMap<String, Any>): LiveData<ResponseSoal?> {
        _soal = MutableLiveData()
        soal(-1, queryParams = queryParams)
        return _soal as MutableLiveData<ResponseSoal?>
    }

    fun storeSoal(params: HashMap<String, String>): LiveData<ResponseSoal?> {
        _soal = MutableLiveData()
        soal(UtilsCode.STORE, params = params)
        return _soal as MutableLiveData<ResponseSoal?>
    }

    fun updateSoal(id: Int, params: HashMap<String, String>): LiveData<ResponseSoal?> {
        _soal = MutableLiveData()
        soal(UtilsCode.UPDATE, id = id, params = params)
        return _soal as MutableLiveData<ResponseSoal?>
    }

    fun deleteSoal(id: Int): LiveData<ResponseSoal?> {
        _soal = MutableLiveData()
        soal(UtilsCode.DELETE, id = id)
        return _soal as MutableLiveData<ResponseSoal?>
    }

    private fun soal(
        type: Int,
        id: Int? = null,
        queryParams: HashMap<String, Any>? = null,
        params: HashMap<String, String>? = null
    ) {
        val client = when (type) {
            UtilsCode.STORE -> ApiConfig.getApiService().storeSoal(params!!)
            UtilsCode.UPDATE -> ApiConfig.getApiService().updateSoal(id!!, params!!)
            UtilsCode.DELETE -> ApiConfig.getApiService().deleteSoal(id!!)
            else -> ApiConfig.getApiService().getSoal(queryParams!!)
        }

        client.enqueue(object : Callback<ResponseSoal> {
            override fun onResponse(
                call: Call<ResponseSoal>,
                response: Response<ResponseSoal>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _soal!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseSoal::class.java
                        )
                    _soal!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseSoal>, t: Throwable) {
                _soal!!.postValue(null)
            }
        })
    }

}