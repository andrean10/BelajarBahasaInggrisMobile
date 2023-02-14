package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.nilai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.nilai.belajar.ResponseNilaiBelajar
import com.tribuanabagus.belajarbahasainggris.model.nilai.permainan.ResponseNilaiPermainan
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NilaiStudentViewModel : ViewModel() {

    private var _nilaiBelajar: MutableLiveData<ResponseNilaiBelajar?>? = null
    private var _nilaiPermainan: MutableLiveData<ResponseNilaiPermainan?>? = null

    fun getNilaiBelajar(queryParams: HashMap<String, Any>): LiveData<ResponseNilaiBelajar?> {
        _nilaiBelajar = MutableLiveData()
        nilaiBelajar(-1, queryParams = queryParams)
        return _nilaiBelajar as MutableLiveData<ResponseNilaiBelajar?>
    }

    fun getNilaiPermainan(queryParams: HashMap<String, Any>): LiveData<ResponseNilaiPermainan?> {
        _nilaiPermainan = MutableLiveData()
        nilaiPermainan(-1, queryParams = queryParams)
        return _nilaiPermainan as MutableLiveData<ResponseNilaiPermainan?>
    }

    fun storeNilaiBelajar(params: HashMap<String, Any>): LiveData<ResponseNilaiBelajar?> {
        _nilaiBelajar = MutableLiveData()
        nilaiBelajar(UtilsCode.STORE, params = params)
        return _nilaiBelajar as MutableLiveData<ResponseNilaiBelajar?>
    }

    fun storeNilaiPermainan(params: HashMap<String, Any>): LiveData<ResponseNilaiPermainan?> {
        _nilaiPermainan = MutableLiveData()
        nilaiPermainan(UtilsCode.STORE, params = params)
        return _nilaiPermainan as MutableLiveData<ResponseNilaiPermainan?>
    }

    fun updateNilaiBelajar(id: Int, params: HashMap<String, Any>): LiveData<ResponseNilaiBelajar?> {
        _nilaiBelajar = MutableLiveData()
        nilaiBelajar(UtilsCode.UPDATE, id = id, params = params)
        return _nilaiBelajar as MutableLiveData<ResponseNilaiBelajar?>
    }

    fun updateNilaiPermainan(
        id: Int,
        params: HashMap<String, Any>
    ): LiveData<ResponseNilaiPermainan?> {
        _nilaiPermainan = MutableLiveData()
        nilaiPermainan(UtilsCode.UPDATE, id = id, params = params)
        return _nilaiPermainan as MutableLiveData<ResponseNilaiPermainan?>
    }

    private fun nilaiBelajar(
        type: Int,
        id: Int? = null,
        queryParams: HashMap<String, Any>? = null,
        params: HashMap<String, Any>? = null
    ) {
        val client = when (type) {
            UtilsCode.STORE -> ApiConfig.getApiService().storeNilaiBelajarSiswa(params!!)
//            UtilsCode.UPDATE -> ApiConfig.getApiService().updateNilaiBelajar(id!!, params!!)
            else -> ApiConfig.getApiService().getNilaiBelajarSiswa(queryParams!!)
        }

        client.enqueue(object : Callback<ResponseNilaiBelajar> {
            override fun onResponse(
                call: Call<ResponseNilaiBelajar>,
                response: Response<ResponseNilaiBelajar>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _nilaiBelajar!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseNilaiBelajar::class.java
                        )
                    _nilaiBelajar!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseNilaiBelajar>, t: Throwable) {
                _nilaiBelajar!!.postValue(null)
            }
        })
    }

    private fun nilaiPermainan(
        type: Int,
        id: Int? = null,
        queryParams: HashMap<String, Any>? = null,
        params: HashMap<String, Any>? = null
    ) {
        val client = when (type) {
            UtilsCode.STORE -> ApiConfig.getApiService().storeNilaiPermainanSiswa(params!!)
//            UtilsCode.UPDATE -> ApiConfig.getApiService().upda(id!!, params!!)
            else -> ApiConfig.getApiService().getNilaiPermainanSiswa(queryParams!!)
        }

        client.enqueue(object : Callback<ResponseNilaiPermainan> {
            override fun onResponse(
                call: Call<ResponseNilaiPermainan>,
                response: Response<ResponseNilaiPermainan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _nilaiPermainan!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseNilaiPermainan::class.java
                        )
                    _nilaiPermainan!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseNilaiPermainan>, t: Throwable) {
                _nilaiPermainan!!.postValue(null)
            }
        })
    }

}