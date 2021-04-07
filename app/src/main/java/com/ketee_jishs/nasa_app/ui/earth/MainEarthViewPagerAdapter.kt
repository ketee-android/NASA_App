package com.ketee_jishs.nasa_app.ui.earth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ketee_jishs.nasa_app.util.DAY_BEFORE_YESTERDAY_EARTH_FRAGMENT
import com.ketee_jishs.nasa_app.util.TODAY_EARTH_FRAGMENT
import com.ketee_jishs.nasa_app.util.YESTERDAY_EARTH_FRAGMENT

@Suppress("DEPRECATION")
class MainEarthViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        EarthTodayFragment(),
        EarthYesterdayFragment(),
        EarthDayBeforeYesterdayFragment()
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
            0 -> "            Today            "
            1 -> "    Yesterday    "
            2 -> "Day before YD"
            else -> "      Today      "
        }
    }
}