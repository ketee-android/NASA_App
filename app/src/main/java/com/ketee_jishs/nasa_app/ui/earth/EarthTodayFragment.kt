package com.ketee_jishs.nasa_app.ui.earth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.getDayBeforeYesterdayDate
import kotlinx.android.synthetic.main.fragment_earth.*
import java.text.SimpleDateFormat

class EarthTodayFragment : Fragment(R.layout.fragment_earth) {

    private val viewModel: EarthViewModel by lazy {
        ViewModelProviders.of(this).get(EarthViewModel::class.java)
    }

    private var earthAdapter = EarthAdapter(arrayListOf(), getDayBeforeYesterdayDate())

    @SuppressLint("SimpleDateFormat")
    private val formatterUrl = SimpleDateFormat("yyyy-MM-dd")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        earthRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        earthRecyclerView.adapter = earthAdapter
        getData(formatterUrl.format(getDayBeforeYesterdayDate()))
    }

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@EarthTodayFragment, Observer<EarthData> { renderData(it) })
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(data: EarthData) {
        when (data) {
            is EarthData.Success -> {
                earthAdapter.replaceData(data.serverResponseData)
                earthProgressBar.visibility = View.GONE
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