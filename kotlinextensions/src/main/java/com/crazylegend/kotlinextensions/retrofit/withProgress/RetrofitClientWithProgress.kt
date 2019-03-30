package com.crazylegend.kotlinextensions.retrofit.withProgress

import android.content.Context
import com.crazylegend.kotlinextensions.isNull
import com.crazylegend.kotlinextensions.retrofit.ConnectivityInterceptor
import com.crazylegend.kotlinextensions.retrofit.RetryRequestInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */
object RetrofitClientWithProgress {

    private var retrofit: Retrofit? = null

    var progressListenerDownload: OnAttachmentDownloadListener? = null


    fun moshiInstance(context: Context, baseUrl: String, enableInterceptor: Boolean = false): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(RetryRequestInterceptor(context))
            addInterceptor(ConnectivityInterceptor(context))
            addInterceptor(OnProgressDownloadInterceptor(context, progressListenerDownload))
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(100, TimeUnit.SECONDS)
            writeTimeout(100, TimeUnit.SECONDS)
        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder)

        } else {
            retrofit?.baseUrl()?.let {

                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder)
                }
            }
        }

        return retrofit

    }

    fun gsonInstance(context: Context, baseUrl: String, enableInterceptor: Boolean = false): Retrofit? {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (enableInterceptor) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


        clientBuilder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(RetryRequestInterceptor(context))
            addInterceptor(ConnectivityInterceptor(context))
            addInterceptor(OnProgressDownloadInterceptor(context, progressListenerDownload))
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(100, TimeUnit.SECONDS)
            writeTimeout(100, TimeUnit.SECONDS)
        }

        if (retrofit.isNull) {
            retrofit = buildRetrofit(baseUrl, clientBuilder)

        } else {
            retrofit?.baseUrl()?.let {

                if (it.toString() != baseUrl) {
                    retrofit = buildRetrofit(baseUrl, clientBuilder)

                }
            }
        }

        return retrofit

    }


    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient.Builder): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}

