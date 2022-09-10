package com.quanticheart.monitor.system

import android.content.Context
import com.quanticheart.monitor.BuildConfig
import okhttp3.Headers.Companion.toHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun Context.getConnection(baseUrl: String, headers: Map<String, String>? = null): Retrofit {
    val client = createClient(headers)

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())

        .client(client)
        .build()
}

private fun createClient(headers: Map<String, String>? = null): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(LoggingInterceptor().create())

    headers?.let {
        val interceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
            newRequest.headers(it.toHeaders())
            chain.proceed(newRequest.build())
        }
        builder.interceptors().add(interceptor)
    }

    return builder.build()
}

class LoggingInterceptor {
    private val log = HttpLoggingInterceptor()

    init {
        when (BuildConfig.DEBUG) {
            true -> log.level = HttpLoggingInterceptor.Level.BODY
            false -> log.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    fun create() = log
}