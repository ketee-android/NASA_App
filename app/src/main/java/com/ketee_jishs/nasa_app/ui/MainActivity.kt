package com.ketee_jishs.nasa_app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
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
import com.ketee_jishs.nasa_app.ui.notes.NotesFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayData
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayViewModel
import com.ketee_jishs.nasa_app.ui.settings.SettingsFragment
import com.ketee_jishs.nasa_app.ui.sun.SunFragment
import com.ketee_jishs.nasa_app.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
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

        if (savedInstanceState == null) {
            goToMainScreen()
        }

        initScreen()
        initData()
        initTheme()
        setBottomAppBar()

        setBottomSheetBehavior(findViewById(R.id.bottomSheetContainer))
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

        setSettingsBottomAppBar()
        initTheme()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun goToMarsScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainMarsFragment.newInstance())
            .addToBackStack(null)
            .commit()

        setSettingsBottomAppBar()
        setMarsBottomMenu()
        initTheme()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun goToSunScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SunFragment.newInstance())
            .addToBackStack(null)
            .commit()

        setSettingsBottomAppBar()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun goToNotesScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, NotesFragment.newInstance())
            .addToBackStack(null)
            .commit()

        setSettingsBottomAppBar()
        bottomSheetContainer.visibility = View.GONE
    }

    private fun setBottomAppBar() {
        when (PictureOfTheDayFragment.isMain) {
            true -> setMainBottomAppBar()
            false -> setSettingsBottomAppBar()
        }
    }

    private fun setMainBottomAppBar() {
        PictureOfTheDayFragment.isMain = true
        bottomAppBar.navigationIcon = null

        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_plus_fab))
        fab.setOnClickListener { setScreen(goToNotesScreen(), NOTES_SCREEN) }

        marsNavigationView.visibility = View.GONE
        mainNavigationView.visibility = View.VISIBLE
        mainNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.appBarEarth -> {
                    goToEarthScreen()
                    PictureOfTheDayFragment.isMain = false
                }
                R.id.appBarMars -> {
                    goToMarsScreen()
                    PictureOfTheDayFragment.isMain = false
                }
                R.id.appBarSun -> {
                    goToSunScreen()
                    PictureOfTheDayFragment.isMain = false
                }
                R.id.appBarSettings -> {
                    setScreen(goToSettingsScreen(), SETTINGS_SCREEN)
                    PictureOfTheDayFragment.isMain = false
                }
            }
            true
        }
    }

    private fun setSettingsBottomAppBar() {
        PictureOfTheDayFragment.isMain = false
        bottomAppBar.navigationIcon = null
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        mainNavigationView.visibility = View.GONE
        marsNavigationView.visibility = View.GONE

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_fab))
        fab.setOnClickListener {
            PictureOfTheDayFragment.isMain = true
            setScreen(goToMainScreen(), PICTURE_SCREEN)
        }
    }

    private fun setMarsBottomMenu() {
        var marsNavigationViewIsVisible = false

        mainNavigationView.visibility = View.GONE
        marsNavigationView.visibility = View.VISIBLE

        marsNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.appBarCameras -> {
                    bottomAppBar.visibility = View.GONE
                    fab.visibility = View.GONE
                    marsNavigationView.visibility = View.GONE

                    viewToHideCamerasMenu.alpha = 0f
                    viewToHideCamerasMenu.visibility = View.VISIBLE
                    viewToHideCamerasMenu.animate().alpha(1f).duration = 300

                    marsNavigationViewIsVisible = !marsNavigationViewIsVisible
                    TransitionManager.beginDelayedTransition(activity_main, Slide(Gravity.BOTTOM))
                    marsCamerasNavigationView.visibility =
                        if (marsNavigationViewIsVisible) View.VISIBLE else View.GONE
                }
            }
            true
        }

        marsCamerasNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.FHAZ -> changeRoverCamera(resources.getString(R.string.fhaz_menu))
                R.id.RHAZ -> changeRoverCamera(resources.getString(R.string.rhaz_menu))
                R.id.MAST -> changeRoverCamera(resources.getString(R.string.mast_menu))
                R.id.CHEMCAM -> changeRoverCamera(resources.getString(R.string.chemcam_menu))
                R.id.MAHLI -> changeRoverCamera(resources.getString(R.string.mahli_menu))
                R.id.MARDI -> changeRoverCamera(resources.getString(R.string.mardi_menu))
                R.id.NAVCAM -> changeRoverCamera(resources.getString(R.string.navcam_menu))
            }
            true
        }

        viewToHideCamerasMenu.setOnClickListener {
            marsNavigationView.visibility = View.VISIBLE
            bottomAppBar.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            viewToHideCamerasMenu.visibility = View.GONE

            marsNavigationViewIsVisible = !marsNavigationViewIsVisible
            marsCamerasNavigationView.visibility =
                if (marsNavigationViewIsVisible) View.VISIBLE else View.GONE
        }

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_fab))
        fab.setOnClickListener {
            PictureOfTheDayFragment.isMain = true
            setScreen(goToMainScreen(), PICTURE_SCREEN)
        }
    }

    private fun changeRoverCamera(cameraName: String) {
        MainMarsFragment.camera = cameraName
        goToMarsScreen()
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
        marsNavigationView.visibility = View.VISIBLE
        PictureOfTheDayFragment.isMain = false
        marsCamerasNavigationView.visibility = View.GONE
        viewToHideCamerasMenu.visibility = View.GONE
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
                        mainNavigationView.visibility = View.VISIBLE
                    }
                    else -> {
                        initData()
                        fab.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE
                        mainNavigationView.visibility = View.GONE
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
            NOTES_SCREEN -> goToNotesScreen()
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