package com.ketee_jishs.nasa_app.ui.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketee_jishs.nasa_app.BuildConfig
import com.ketee_jishs.nasa_app.ui.NasaRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthViewModel (
    private val liveDataForViewToObserve: MutableLiveData<EarthData> = MutableLiveData(),
    private val nasaRetrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()
) : ViewModel() {

    fun getData(date: String): LiveData<EarthData> {
        sendServerRequest(date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = EarthData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            EarthData.Error(Throwable("You need API key"))
        } else {
            nasaRetrofitImpl.getEarthRetrofitImpl().getEarthPicture(date, apiKey).enqueue(object :
                Callback<ArrayList<EarthServerResponseData>> {
                override fun onResponse(
                    call: Call<ArrayList<EarthServerResponseData>>,
                    response: Response<ArrayList<EarthServerResponseData>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            EarthData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                EarthData.Error(Throwable("Unidentified error"))
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<EarthServerResponseData>>, t: Throwable) {
                    liveDataForViewToObserve.value = EarthData.Error(t)
                }
            })
        }
    }
}