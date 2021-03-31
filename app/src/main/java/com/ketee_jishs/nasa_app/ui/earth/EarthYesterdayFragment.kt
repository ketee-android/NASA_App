package com.ketee_jishs.nasa_app.ui.earth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.ketee_jishs.nasa_app.BuildConfig
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.getThirdDate
import kotlinx.android.synthetic.main.fragment_earth.*
import java.text.SimpleDateFormat

class EarthYesterdayFragment : Fragment() {
    private val viewModel: EarthViewModel by lazy {
        ViewModelProviders.of(this).get(EarthViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private val formatterImage = SimpleDateFormat("yyyy/MM/dd")

    @SuppressLint("SimpleDateFormat")
    private val formatterUrl = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_earth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData(formatterUrl.format(getThirdDate()))
    }

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@EarthYesterdayFragment, Observer<EarthData> { renderData(it) })
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(data: EarthData) {
        when (data) {
            is EarthData.Success -> {
                val serverResponseData = data.serverResponseData[0]
                val date = formatterImage.format(getThirdDate())
                val url = serverResponseData.image
                val image = "https://api.nasa.gov/EPIC/archive/natural/$date/png/$url.png?api_key=${BuildConfig.NASA_API_KEY}"
                if (image.isEmpty()) {
                    toast("Link is empty")
                } else {
                    earthImageView.load(image) {
                        lifecycle(this@EarthYesterdayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    captionText.text = serverResponseData.caption
                    dateText.text = "Дата съемки: ${serverResponseData.date}"
                    earthProgressBar.visibility = View.GONE
                }
            }
            is EarthData.Loading -> {
                earthProgressBar.visibility = View.VISIBLE
            }
            is EarthData.Error -> {
                toast(data.error.message)
                earthProgressBar.visibility = View.GONE
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}