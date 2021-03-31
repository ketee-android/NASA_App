package com.ketee_jishs.nasa_app.ui.mars

import com.google.gson.annotations.SerializedName

data class MarsServerResponseData(
    val photos: ArrayList<Photos>
)

data class Photos(
    @field:SerializedName("img_src") val image: String?,
    @field:SerializedName("earth_date") val earthDate: String?,
    val camera: Camera
)

data class Camera(
    @field:SerializedName("full_name") val cameraName: String?
)