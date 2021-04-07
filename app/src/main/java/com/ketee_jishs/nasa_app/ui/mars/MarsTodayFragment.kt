package com.ketee_jishs.nasa_app.ui.mars

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.getDayBeforeYesterdayDate
import kotlinx.android.synthetic.main.fragment_mars.*
import java.text.SimpleDateFormat

class MarsTodayFragment : Fragment() {
    private val viewModel: MarsViewModel by lazy {
        ViewModelProviders.of(this).get(MarsViewModel::class.java)
    }
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData(formatter.format(getDayBeforeYesterdayDate()), MainMarsFragment.camera)
    }

    private fun getData(date: String, camera: String) {
        viewModel.getData(date, camera)
            .observe(this@MarsTodayFragment, Observer<MarsData> { renderData(it) })
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(data: MarsData) {
        try {
            when (data) {
                is MarsData.Success -> {
                    val serverResponseData = data.serverResponseData.photos[0]
                    val image = serverResponseData.image
                    if (image.isNullOrEmpty()) {
                        toast("Link is empty")
                    } else {
                        marsImageView.load(image) {
                            lifecycle(this@MarsTodayFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                        cameraName.text = serverResponseData.camera.cameraName
                        dateMarsView.text = "Date: ${serverResponseData.earthDate}"
                        marsProgressBar.visibility = View.GONE
                        expandPicture()
                    }
                }
                is MarsData.Loading -> {
                    marsProgressBar.visibility = View.VISIBLE
                }
                is MarsData.Error -> {
                    toast(data.error.message)
                    marsProgressBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            marsImageView.load(R.drawable.ic_load_error_vector)
            cameraName.text = "The data has not yet arrived"
            dateMarsView.text = "Try to choose a different camera or see photos from other days"
            marsProgressBar.visibility = View.GONE
        }
    }

    private fun expandPicture() {
        var isExpanded = false

        marsImageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(fragment_mars, TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = marsImageView.layoutParams
            params.height =
                if (isExpanded) 1500 else ViewGroup.LayoutParams.WRAP_CONTENT
            marsImageView.scaleType = if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}