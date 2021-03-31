package com.ketee_jishs.nasa_app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.ui.earth.MainEarthFragment
import com.ketee_jishs.nasa_app.ui.mars.MainMarsFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayData
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayViewModel
import com.ketee_jishs.nasa_app.ui.settings.SettingsFragment
import com.ketee_jishs.nasa_app.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val sharedPrefs by lazy {
        getSharedPreferences(CHIPS_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private val themePrefs by lazy {
        getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private val screenPrefs by lazy {
        getSharedPreferences(SCREEN_PREFS_NAME, Context.MODE_PRIVATE)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            goToMainScreen()
        }

        initScreen()
        initData()
        initTheme()
        setBottomAppBar()
        
        setBottomSheetBehavior(findViewById(R.id.bottomSheetContainer))
        onMenuItemClickListener()
    }

    private fun onMenuItemClickListener() {
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.appBarEarth -> {
                    goToEarthScreen()
                    PictureOfTheDayFragment.isMain = false
                }
                R.id.appBarMars -> {
                    goToMarsScreen()
                    PictureOfTheDayFragment.isMain = false
                }
            }
            true
        }
    }

    private fun Activity.toast(string: String?) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }

    private fun goToMainScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PictureOfTheDayFragment.newInstance())
            .commitNow()

        setBottomAppBar()
        initData()
        initTheme()
        bottomSheetContainer.visibility = View.VISIBLE
    }

    private fun goToSettingsScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()

        PictureOfTheDayFragment.isMain = false
        setBottomAppBar()
        initTheme()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun goToEarthScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainEarthFragment.newInstance())
            .addToBackStack(null)
            .commit()

        setAnotherBottomAppBar()
        initTheme()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun goToMarsScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainMarsFragment.newInstance())
            .addToBackStack(null)
            .commit()

        setAnotherBottomAppBar()
        setMarsBottomMenu()
        initTheme()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun setBottomAppBar() {
        when (PictureOfTheDayFragment.isMain) {
            true -> setMainBottomAppBar()
            false -> setAnotherBottomAppBar()
        }
    }

    private fun setMainBottomAppBar() {
        PictureOfTheDayFragment.isMain = true

        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_plus_fab))
        fab.setOnClickListener {
            setScreen(
                goToSettingsScreen(),
                SETTINGS_SCREEN
            )
        } // Потом придумаю что-нибудь другое (UPD: пока не придумала, но кнопку хочу оставить)

        bottomAppBar.navigationIcon = ContextCompat.getDrawable(
            applicationContext,
            R.drawable.ic_settings
        )

        when (getThemePrefs()) {
            THEME_DAY -> bottomAppBar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.navigation_icon_day_color
                )
            )
            THEME_NIGHT -> bottomAppBar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.navigation_icon_night_color
                )
            )
        }

        bottomAppBar.setNavigationOnClickListener {
            setScreen(goToSettingsScreen(), SETTINGS_SCREEN)
        }
    }

    private fun setAnotherBottomAppBar() {
        PictureOfTheDayFragment.isMain = false
        bottomAppBar.navigationIcon = null
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_fab))
        fab.setOnClickListener {
            PictureOfTheDayFragment.isMain = true
            setScreen(goToMainScreen(), PICTURE_SCREEN)
        }
    }

    private fun setMarsBottomMenu() {
        bottomAppBar.navigationIcon = ContextCompat.getDrawable(
            applicationContext,
            R.drawable.ic_change_mars_camera
        )

        when (getThemePrefs()) {
            THEME_DAY -> bottomAppBar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.navigation_icon_day_color
                )
            )
            THEME_NIGHT -> bottomAppBar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.navigation_icon_night_color
                )
            )
        }

        bottomAppBar.setNavigationOnClickListener {
            bottomAppBar.visibility = View.GONE
            fab.visibility = View.GONE
            marsCamerasNavigationView.visibility = View.VISIBLE
            viewToHideCamerasChips.visibility = View.VISIBLE
        }

        // Потом возможно придумаю другой вариант реализации выбора камеры ровера
        marsCamerasNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.FHAZ -> {
                    MainMarsFragment.camera = "FHAZ"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.RHAZ -> {
                    MainMarsFragment.camera = "RHAZ"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.MAST -> {
                    MainMarsFragment.camera = "MAST"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.CHEMCAM -> {
                    MainMarsFragment.camera = "CHEMCAM"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.MAHLI -> {
                    MainMarsFragment.camera = "MAHLI"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.MARDI -> {
                    MainMarsFragment.camera = "MARDI"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }

                R.id.NAVCAM -> {
                    MainMarsFragment.camera = "NAVCAM"
                    goToMarsScreen()
                    bottomAppBar.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    PictureOfTheDayFragment.isMain = false
                    marsCamerasNavigationView.visibility = View.GONE
                    viewToHideCamerasChips.visibility = View.GONE
                }
            }
            true
        }

        viewToHideCamerasChips.setOnClickListener {
            bottomAppBar.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            marsCamerasNavigationView.visibility = View.GONE
            viewToHideCamerasChips.visibility = View.GONE
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fab.visibility = View.VISIBLE
                        bottomAppBar.visibility = View.VISIBLE
                    }
                    else -> {
                        initData()
                        fab.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initData() {
        when (getChipsPrefs()) {
            CHECKED_TODAY -> getData(formatter.format(getCurrentDate()))
            CHECKED_YESTERDAY -> getData(formatter.format(getYesterdayDate()))
            CHECKED_DAY_BEFORE_YESTERDAY -> getData(formatter.format(getDayBeforeYesterdayDate()))
        }
    }

    private fun initTheme() {
        when (getThemePrefs()) {
            THEME_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setScreen(screen: Unit, prefsMode: Int) {
        apply { screen }
        saveScreen(prefsMode)
    }

    private fun saveScreen(screen: Int) = screenPrefs.edit().putInt(KEY_SCREEN, screen).apply()

    private fun initScreen() {
        when (getScreenPrefs()) {
            PICTURE_SCREEN -> goToMainScreen()
            SETTINGS_SCREEN -> goToSettingsScreen()
        }
    }

    private fun getChipsPrefs() = sharedPrefs?.getInt(KEY_CHIP, CHECKED_TODAY)
    private fun getThemePrefs() = themePrefs?.getInt(KEY_THEME, THEME_DAY)
    private fun getScreenPrefs() = screenPrefs?.getInt(KEY_SCREEN, PICTURE_SCREEN)

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@MainActivity, Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    bottomSheetDescriptionHeader.text = serverResponseData.title
                    bottomSheetDescription.text = serverResponseData.explanation
                    bottomSheetProgressBar.visibility = View.GONE
                }
            }
            is PictureOfTheDayData.Loading -> {
                bottomSheetProgressBar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }
}