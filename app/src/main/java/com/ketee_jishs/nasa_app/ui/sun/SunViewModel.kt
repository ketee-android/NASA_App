package com.ketee_jishs.nasa_app.ui.sun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketee_jishs.nasa_app.BuildConfig
import com.ketee_jishs.nasa_app.ui.NasaRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SunViewModel (
    private val liveDataForViewToObserve: MutableLiveData<SunData> = MutableLiveData(),
    private val nasaRetrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<SunData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = SunData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            SunData.Error(Throwable("You need API key"))
        } else {
            nasaRetrofitImpl.getSunRetrofitImpl().getSunData(apiKey).enqueue(object :
                Callback<ArrayList<SunServerResponseData>> {
                override fun onResponse(
                    call: Call<ArrayList<SunServerResponseData>>,
                    response: Response<ArrayList<SunServerResponseData>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            SunData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                SunData.Error(Throwable("Unidentified error"))
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<SunServerResponseData>>, t: Throwable) {
                    liveDataForViewToObserve.value = SunData.Error(t)
                }
            })
        }
    }
}