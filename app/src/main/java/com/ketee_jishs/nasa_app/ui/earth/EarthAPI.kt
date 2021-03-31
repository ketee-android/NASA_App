package com.ketee_jishs.nasa_app.ui.earth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EarthAPI {
    @GET("EPIC/api/natural/date/{picture_date}")
    fun getEarthPicture(
        @Path("picture_date") picture_date: String,
        @Query("api_key") apiKey: String
    ): Call<ArrayList<EarthServerResponseData>>
}