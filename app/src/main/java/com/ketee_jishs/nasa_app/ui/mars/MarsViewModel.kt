package com.ketee_jishs.nasa_app.ui.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketee_jishs.nasa_app.BuildConfig
import com.ketee_jishs.nasa_app.ui.NasaRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsViewModel(
    private val liveDataForViewToObserve: MutableLiveData<MarsData> = MutableLiveData(),
    private val nasaRetrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()
) : ViewModel() {

    fun getData(date: String, camera: String): LiveData<MarsData> {
        sendServerRequest(date, camera)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String, camera: String) {
        liveDataForViewToObserve.value = MarsData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            MarsData.Error(Throwable("You need API key"))
        } else {
            nasaRetrofitImpl.getMarsRetrofitImpl().getMarsPicture(date, camera, apiKey).enqueue(object :
                Callback<MarsServerResponseData> {
                override fun onResponse(
                    call: Call<MarsServerResponseData>,
                    response: Response<MarsServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            MarsData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                MarsData.Error(Throwable("Unidentified error"))
                        }
                    }
                }

                override fun onFailure(call: Call<MarsServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = MarsData.Error(t)
                }
            })
        }
    }
}