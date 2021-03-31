package com.ketee_jishs.nasa_app.ui.earth

sealed class EarthData {
    data class Success(val serverResponseData: ArrayList<EarthServerResponseData>) : EarthData()
    data class Error(val error: Throwable) : EarthData()
    data class Loading(val progress: Int?) : EarthData()
}