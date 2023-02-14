package com.tribuanabagus.belajarbahasainggris.view.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tribuanabagus.belajarbahasainggris.model.auth.ResponseLogin
import com.tribuanabagus.belajarbahasainggris.model.auth.ResponseRegister
import com.tribuanabagus.belajarbahasainggris.model.users.ResponseUserManage
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.user.UserResponse
import com.tribuanabagus.belajarbahasainggris.utils.UtilsCode.TAG
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private var _auth: MutableLiveData<ResponseLogin?>? = null
    private var _register: MutableLiveData<ResponseRegister?>? = null
    private var _authOld = MutableLiveData<UserResponse>()

    private var _user: MutableLiveData<ResponseUserManage?>? = null

    private val RESPONSE_CLASS = UserResponse::class.java
    private val RESPONSE_LOGIN = ResponseLogin::class.java

    fun login(params: HashMap<String, Any>): LiveData<ResponseLogin?> {
        _auth = MutableLiveData()
        getLogin(params)
        return _auth as MutableLiveData<ResponseLogin?>
    }

    fun register(params: HashMap<String, String>): LiveData<ResponseRegister?> {
        _register = MutableLiveData()
        storeUser(params)
        return _register as MutableLiveData<ResponseRegister?>
    }

    fun updateProfile(
        id: Int,
        params: HashMap<String, RequestBody>,
        image: MultipartBody.Part?,
    ): LiveData<ResponseUserManage?> {
        _user = MutableLiveData()
        updateUser(id, params, image)
        return _user as MutableLiveData<ResponseUserManage?>
    }

    fun updatePassword(id: Int, newPassword: String): LiveData<ResponseUserManage?> {
        _user = MutableLiveData()
        updateUserPassword(id, newPassword)
        return _user as MutableLiveData<ResponseUserManage?>
    }

//    fun userDetail(userId: Int): LiveData<UserResponse> {
//        _authOld = getUserDetail(userId)
//        return _authOld
//    }

    private fun getLogin(params: HashMap<String, Any>) {
        val client = ApiConfig.getApiService().login(params)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _auth?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            RESPONSE_LOGIN
                        )
                    _auth?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun updateUserPassword(id: Int, newPassword: String) {
        val client = ApiConfig.getApiService().updatePassword(id, newPassword)
        client.enqueue(object : Callback<ResponseUserManage> {
            override fun onResponse(
                call: Call<ResponseUserManage>,
                response: Response<ResponseUserManage>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _user!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseUserManage::class.java
                        )
                    _user!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseUserManage>, t: Throwable) {
                _user!!.postValue(null)
            }
        })
    }

    private fun getUserDetail(userId: Int): MutableLiveData<UserResponse> {
        val client = ApiConfig.getApiService().detailUser(userId)
        val gson = Gson()
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _authOld.postValue(result!!)
                } else {
                    val errResult = gson.fromJson(response.errorBody()?.string(), RESPONSE_CLASS)
                    val msg = response.message()
                    Log.e(TAG, "onFailure: $errResult")
                    Log.e(TAG, "onFailure: $msg")
                    _authOld.postValue(errResult)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return _authOld
    }

    private fun storeUser(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().register(params)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    _register!!.postValue(result)
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseRegister::class.java
                        )
                    _register!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _register!!.postValue(null)
            }
        })
    }

    private fun updateUser(
        id: Int,
        params: HashMap<String, RequestBody>,
        image: MultipartBody.Part? = null
    ) {
        val client = ApiConfig.getApiService().updateUser(id, params, image)
        client.enqueue(object : Callback<ResponseUserManage> {
            override fun onResponse(
                call: Call<ResponseUserManage>,
                response: Response<ResponseUserManage>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _user!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseUserManage::class.java
                        )
                    _user!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseUserManage>, t: Throwable) {
                _user!!.postValue(null)
            }
        })
    }
}