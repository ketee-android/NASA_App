package com.ketee_jishs.nasa_app.ui.sun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.fragment_sun.*

class SunFragment : Fragment() {

    private val viewModel: SunViewModel by lazy {
        ViewModelProviders.of(this).get(SunViewModel::class.java)
    }

    private val sunAdapter = SunAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sunRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        sunRecyclerView.adapter = sunAdapter
        getData()
    }

    private fun getData() {
        viewModel.getData()
            .observe(this@SunFragment, Observer<SunData> { renderData(it) })
    }

    private fun renderData(data: SunData) {
        when (data) {
            is SunData.Success -> {
                sunAdapter.replaceData(data.serverResponseData)
                cmeHeader.visibility = View.VISIBLE
                sunProgressBar.visibility = View.GONE
            }
            is SunData.Loading -> {
                cmeHeader.visibility = View.GONE
                sunProgressBar.visibility = View.VISIBLE
            }
            is SunData.Error -> {
                toast(data.error.message)
                cmeHeader.visibility = View.GONE
                sunProgressBar.visibility = View.GONE
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = SunFragment()
    }
}