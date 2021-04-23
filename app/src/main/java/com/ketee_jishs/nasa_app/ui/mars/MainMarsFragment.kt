package com.ketee_jishs.nasa_app.ui.mars

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.interactors.strings_interactor.StringsInteractorImpl
import kotlinx.android.synthetic.main.main_mars_fragment.*

class MainMarsFragment : Fragment(R.layout.main_mars_fragment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        marsViewPager.adapter = MainMarsViewPagerAdapter(
            activity?.supportFragmentManager!!,
            StringsInteractorImpl(context!!)
        )
        marsTabLayout.setupWithViewPager(marsViewPager)
    }

    companion object {
        fun newInstance() = MainMarsFragment()
        var camera = "FHAZ"
    }
}