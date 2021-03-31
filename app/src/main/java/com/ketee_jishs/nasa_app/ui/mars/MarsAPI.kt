package com.ketee_jishs.nasa_app.ui.mars

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPicture(
        @Query("earth_date") picture_date: String,
        @Query("camera") camera: String,
        @Query("api_key") apiKey: String
    ): Call<MarsServerResponseData>
}