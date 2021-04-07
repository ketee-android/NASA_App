package com.ketee_jishs.nasa_app.ui.sun

sealed class SunData {
    data class Success(val serverResponseData: ArrayList<SunServerResponseData>) : SunData()
    data class Error(val error: Throwable) : SunData()
    data class Loading(val progress: Int?) : SunData()
}