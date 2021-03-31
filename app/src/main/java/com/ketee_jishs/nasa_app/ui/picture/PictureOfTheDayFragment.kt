package com.ketee_jishs.nasa_app.ui.picture

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
@SuppressLint("FragmentLiveDataObserve")
class PictureOfTheDayFragment : Fragment() {
    private val sharedPrefs by lazy {
        activity?.getSharedPreferences(CHIPS_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            getData(formatter.format(getCurrentDate()))
        }

        initChips()
        checkChip()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${inputEditText.text.toString()}")
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkChip() {
        chipMainGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                todayChip.id -> setData(
                    formatter.format(getCurrentDate()),
                    CHECKED_TODAY
                )
                yesterdayChip.id -> setData(
                    formatter.format(getYesterdayDate()),
                    CHECKED_YESTERDAY
                )
                dayBeforeYesterdayChip.id -> setData(
                    formatter.format(getDayBeforeYesterdayDate()),
                    CHECKED_DAY_BEFORE_YESTERDAY
                )
            }
        }
    }

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@PictureOfTheDayFragment, Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    progressBar.visibility = View.GONE
                }
            }
            is PictureOfTheDayData.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    private fun initChips() {
        when (getChipsPrefs()) {
            CHECKED_TODAY -> {
                todayChip.isChecked = true
                getData(formatter.format(getCurrentDate()))
            }
            CHECKED_YESTERDAY -> {
                yesterdayChip.isChecked = true
                getData(formatter.format(getYesterdayDate()))
            }
            CHECKED_DAY_BEFORE_YESTERDAY -> {
                dayBeforeYesterdayChip.isChecked = true
                getData(formatter.format(getDayBeforeYesterdayDate()))
            }
        }
    }

    private fun getChipsPrefs() = sharedPrefs?.getInt(KEY_CHIP, CHECKED_TODAY)

    private fun setData(date: String, prefsMode: Int) {
        getData(date)
        saveChipSettings(prefsMode)
    }

    private fun saveChipSettings(prefsMode: Int) =
        sharedPrefs?.edit()?.putInt(KEY_CHIP, prefsMode)?.apply()

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        var isMain = true
    }
}