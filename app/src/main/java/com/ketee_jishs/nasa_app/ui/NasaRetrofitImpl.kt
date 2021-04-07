package com.ketee_jishs.nasa_app.ui

import com.google.gson.GsonBuilder
import com.ketee_jishs.nasa_app.ui.earth.EarthAPI
import com.ketee_jishs.nasa_app.ui.mars.MarsAPI
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayAPI
import com.ketee_jishs.nasa_app.ui.sun.SunAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class NasaRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"

    fun getPODRetrofitImpl(): PictureOfTheDayAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return podRetrofit.create(PictureOfTheDayAPI::class.java)
    }

    fun getEarthRetrofitImpl(): EarthAPI {
        val earthRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return earthRetrofit.create(EarthAPI::class.java)
    }

    fun getMarsRetrofitImpl(): MarsAPI {
        val earthRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return earthRetrofit.create(MarsAPI::class.java)
    }

    fun getSunRetrofitImpl(): SunAPI {
        val sunRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return sunRetrofit.create(SunAPI::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}