package com.quanticheart.monitor.system

import android.content.Context
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Context.getConnection(baseUrl: String, headers: Map<String, String>? = null): Retrofit {
    val interceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
        headers?.let {
            newRequest.headers(Headers.of(it))
        }
        chain.proceed(newRequest.build())
    }

    val builder = OkHttpClient.Builder()
    builder.interceptors().add(interceptor)
    val client = builder.build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}