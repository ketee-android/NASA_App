package com.ketee_jishs.nasa_app.ui.sun

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SunAPI {
    @GET("DONKI/CME")
    fun getSunData(
        @Query("api_key") apiKey: String
    ): Call<ArrayList<SunServerResponseData>>
}