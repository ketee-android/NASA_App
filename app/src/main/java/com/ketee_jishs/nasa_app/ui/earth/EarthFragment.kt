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
import com.ketee_jishs.nasa_app.interactors.colors_interactor.ColorsInteractorImpl
import kotlinx.android.synthetic.main.fragment_earth.*
import java.text.SimpleDateFormat
import java.util.*

class EarthFragment(var date: Date) : Fragment(R.layout.fragment_earth) {
    private lateinit var earthAdapter: EarthAdapter
    private val viewModel: EarthViewModel by lazy {
        ViewModelProviders.of(this).get(EarthViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private val formatterUrl = SimpleDateFormat("yyyy-MM-dd")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        earthAdapter = EarthAdapter(arrayListOf(), date, ColorsInteractorImpl(context!!))
        earthRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        earthRecyclerView.adapter = earthAdapter
        getData(formatterUrl.format(date))
    }

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@EarthFragment, Observer<EarthData> { renderData(it) })
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