package com.ketee_jishs.nasa_app.ui.earth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ketee_jishs.nasa_app.interactors.strings_interactor.StringsInteractor
import com.ketee_jishs.nasa_app.util.*

@Suppress("DEPRECATION")
class MainEarthViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val stringsInteractor: StringsInteractor
) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        EarthFragment(getYesterdayDate()),
        EarthFragment(getDayBeforeYesterdayDate()),
        EarthFragment(getThirdDate())
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[TODAY_EARTH_FRAGMENT]
            1 -> fragments[YESTERDAY_EARTH_FRAGMENT]
            2 -> fragments[DAY_BEFORE_YESTERDAY_EARTH_FRAGMENT]
            else -> fragments[TODAY_EARTH_FRAGMENT]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> stringsInteractor.viewPagerTodayText
            1 -> stringsInteractor.viewPagerYesterdayText
            2 -> stringsInteractor.viewPagerDayBeforeYDText
            else -> stringsInteractor.viewPagerTodayText
        }
    }
}