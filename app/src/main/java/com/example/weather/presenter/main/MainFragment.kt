package com.example.weather.presenter.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.devpraskov.android_ext.getColor
import com.devpraskov.android_ext.onClick
import com.devpraskov.android_ext.statusBarColor
import com.example.weather.R
import com.example.weather.models.main.HourlyData
import com.example.weather.presenter.main.AddCityDialog.Companion.ADD_CITY_REQUEST_CODE
import com.example.weather.presenter.main.AddCityDialog.Companion.CITY_EXTRA_KEY
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.second.DetailsFragment
import com.example.weather.utils.ChartDataView
import com.example.weather.utils.view.ChartCreator
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.Calendar.SECOND
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 456
        val TAG = MainFragment::class.java.name
    }

    private val viewModel: MainViewModel by viewModel()
    lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val currentSeconds: Long
        get() = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(SECOND).toLong()
    private val remainingSecondsInMinute: Long
        get() = (60 - currentSeconds) * 1000L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        statusBarColor(Color.parseColor("#BC8DB8"))
        updateTimeEveryMinutes()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, remainingSecondsInMinute)
    }

    private fun updateTimeEveryMinutes() {
        handler = Handler()
        runnable = object : Runnable { //todo обновление каждый час
            override fun run() {
                viewModel.setNextAction(MainAction.UpdateTime)
                handler.postDelayed(this, remainingSecondsInMinute)
            }
        }
    }

    private fun initObservers() {
        checkPermission()
        viewModel.firsAction = MainAction.LoadCurrentCity
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            swipeRefreshLayout?.isRefreshing = state.isLoading
            date?.text = state.time
            val data = state.data.getIfNotBeenHandled()
            data ?: return@Observer
            city?.text = data.city
            data.mainIcon?.let { iconState?.setImageResource(it) }
            currentState?.text = data.condition
            currentTemperature?.text = data.temp
            maxTemperature?.text = (data.maxTemp + getString(R.string.celsius) + "C")
            minTemperature?.text = (data.minTemp + getString(R.string.celsius) + "C")
            ChartCreator(this).initHourlyForecastChart(chart, data.hourlyForecast)
        })
    }

    private fun initListeners() {
        addCity?.onClick {
            val dialog = AddCityDialog()
            dialog.setTargetFragment(this, ADD_CITY_REQUEST_CODE)
            dialog.show(parentFragmentManager, "Dialog")
        }
        swipeRefreshLayout?.setOnRefreshListener { viewModel.setNextAction(MainAction.LoadCurrentCity) }
        menu?.onClick {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fragment_container, DetailsFragment())
                .addToBackStack(DetailsFragment.TAG)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_CITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val city = data?.getStringExtra(CITY_EXTRA_KEY) ?: ""
            if (city.isNotEmpty()) {
                viewModel.setNextAction(MainAction.LoadCurrentCity)
            }
        }
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
            }
            else -> {//todo
                requestPermissions(
                    arrayOf("android.permission.ACCESS_COARSE_LOCATION"),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    //todo
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
