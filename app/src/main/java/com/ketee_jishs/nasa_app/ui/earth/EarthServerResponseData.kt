package com.ketee_jishs.nasa_app.ui.earth

import com.google.gson.annotations.SerializedName

data class EarthServerResponseData(
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("caption") val caption: String?,
    @field:SerializedName("date") val date: String?
)