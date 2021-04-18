package com.eneskayiklik.yellowtaxi

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eneskayiklik.yellowtaxi.databinding.ActivityMainBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseActivity
import com.eneskayiklik.yellowtaxi.ui.main.MainViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel by viewModels<MainViewModel>()
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentNavHost) as NavHostFragment).navController
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initLayout() {
        setupNavChangeListener()
        lifecycleScope.launchWhenCreated {
            viewModel.showDatePicker.collectLatest {
                if (it) showDatePicker()
            }
        }
    }

    private fun setupNavChangeListener() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            when (navController.currentDestination?.id) {
                R.id.splashFragment -> {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.decorView.systemUiVisibility = 0
                    window.statusBarColor = ContextCompat.getColor(this, R.color.taxiYellow)
                }
                R.id.mapFragment -> {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                }
                else -> {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                }
            }
        }
    }

    private fun showDatePicker() {
        val rangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(getCalendarConstraints())
            .setTitleText("Select Dates")
            .build()
        rangePicker.apply {
            show(supportFragmentManager, "date_picker")
            addOnPositiveButtonClickListener {
                val start = selection?.first
                val end = selection?.second
                if (start != null && end != null) {
                    viewModel.selectMinDistance(SimpleDateFormat("dd", Locale.ENGLISH).format(start).toInt(), SimpleDateFormat("dd", Locale.ENGLISH).format(end).toInt())
                }
            }
            addOnDismissListener { viewModel.showDatePicker(false) }
        }
    }

    private fun getCalendarConstraints(): CalendarConstraints {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val december = calendar.timeInMillis
        return CalendarConstraints.Builder()
            .setStart(december)
            .setEnd(december)
            .build()
    }
}