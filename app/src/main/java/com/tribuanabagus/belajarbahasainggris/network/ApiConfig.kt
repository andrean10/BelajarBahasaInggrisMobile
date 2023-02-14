package com.tribuanabagus.belajarbahasainggris.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        const val URL = "http://192.168.202.33:3000" // local  WIFI HOME
//        const val URL = "http://192.168.100.215:8000" // local WIFI KANTOR

        //        const val URL = "https://belajar-bahasa-inggris.sahamsmart.com" //RELEASE
//        const val URL = "https://test-belajar-bahasa-inggris.sahamsmart.com" //RELEASE
        const val URL_IMAGE = "$URL/app-api/public/images/"
        const val URL_SOUNDS = "$URL/app-api/public/sounds/"
        const val URL_FILES = "$URL/files"
        const val URL_IMAGES = "$URL/images"

        private const val ENDPOINT = "$URL/api/"

        //ALLOWED RETROFIT TO ACCESS LARAVEL API
        //laravel protect unauthorized access and prevents from unknown client request. and for this case, it was Retrofit
        private var allowedClient = Interceptor { chain: Interceptor.Chain ->
            val response: Response
            val newRequest = chain.request().newBuilder()
                .addHeader("User-Agent", System.getProperty("http.agent") ?: "") //IMPORTANT
                .addHeader(
                    "Accept",
                    "application/json"
                )
                .method(chain.request().method, chain.request().body)
                .build()
            newRequest.headers["Cookie"]
            response = chain.proceed(newRequest)
            response.headers["Set-Cwhookie"]
            response
        }

//        private fun client(): OkHttpClient {
//            val loggingInterceptor = HttpLoggingInterceptor()
//                .setLevel(
//                    HttpLoggingInterceptor.Level.BODY
//                )
//            val builder = OkHttpClient.Builder().also {
//                it.readTimeout(200, TimeUnit.SECONDS)
//                it.writeTimeout(600, TimeUnit.SECONDS)
//                it.connectTimeout(600, TimeUnit.SECONDS)
//                it.addInterceptor(loggingInterceptor)
//                it.addInterceptor(allowedClient)
//            }
//
//            return builder.build()
//        }

        private fun client(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}