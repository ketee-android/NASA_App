package com.ketee_jishs.nasa_app.ui.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.main_mars_fragment.*

class MainMarsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_mars_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        marsViewPager.adapter = MainMarsViewPagerAdapter(activity?.supportFragmentManager!!)
        marsTabLayout.setupWithViewPager(marsViewPager)
    }

    companion object {
        fun newInstance() = MainMarsFragment()
        var camera = "FHAZ"
    }
}