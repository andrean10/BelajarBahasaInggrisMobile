package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.students.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.student.StudentsResultResponse
import com.tribuanabagus.belajarbahasainggris.model.users.ResponseUsers
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentsViewModel : ViewModel() {
    private var _students = MutableLiveData<StudentsResultResponse>()
    private var _siswa: MutableLiveData<ResponseUsers?>? = null
    private val TAG = StudentsViewModel::class.java.simpleName
    private val RESPONSE_CLASS = StudentsResultResponse::class.java

    fun students(): LiveData<StudentsResultResponse> {
        _students = getStudents()
        return _students
    }

    fun siswa(): LiveData<ResponseUsers?> {
        _siswa = MutableLiveData()
        getSiswa()
        return _siswa as MutableLiveData<ResponseUsers?>
    }

    private fun getSiswa() {
        val client = ApiConfig.getApiService().getAllUser("siswa")
        client.enqueue(object : Callback<ResponseUsers> {
            override fun onResponse(
                call: Call<ResponseUsers>,
                response: Response<ResponseUsers>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _siswa!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseUsers::class.java
                        )
                    _siswa!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                _siswa!!.postValue(null)
            }
        })
    }

    private fun getStudents(): MutableLiveData<StudentsResultResponse> {
        val client = ApiConfig.getApiService().getStudents()
        val gson = Gson()
        client.enqueue(object : Callback<StudentsResultResponse> {
            override fun onResponse(
                call: Call<StudentsResultResponse>,
                response: Response<StudentsResultResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _students.postValue(result!!)
                } else {
                    val errResult = gson.fromJson(response.errorBody()?.string(), RESPONSE_CLASS)
                    _students.postValue(errResult)
                    Log.e(TAG, "onFailure: $errResult")
                }
            }

            override fun onFailure(call: Call<StudentsResultResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        });
        return _students
    }
}