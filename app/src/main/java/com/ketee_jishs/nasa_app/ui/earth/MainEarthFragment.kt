package com.ketee_jishs.nasa_app.ui.earth

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ketee_jishs.nasa_app.R
import kotlinx.android.synthetic.main.main_earth_fragment.*

class MainEarthFragment : Fragment(R.layout.main_earth_fragment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        earthViewPager.adapter = MainEarthViewPagerAdapter(activity?.supportFragmentManager!!)
        earthTabLayout.setupWithViewPager(earthViewPager)
    }

    companion object {
        fun newInstance() = MainEarthFragment()
    }
}