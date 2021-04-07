package com.ketee_jishs.nasa_app.ui.sun

import com.google.gson.annotations.SerializedName

data class SunServerResponseData(
    @field:SerializedName("startTime") val startTime: String?,
    @field:SerializedName("note") val note: String?,
    @field:SerializedName("cmeAnalyses") val cmeAnalyses: ArrayList<CMEAnalysesData>
)

data class CMEAnalysesData(
    @field:SerializedName("note") val note: String?
)