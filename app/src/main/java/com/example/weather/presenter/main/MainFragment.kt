package com.example.weather.presenter.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.devpraskov.android_ext.newText
import com.devpraskov.android_ext.onClick
import com.devpraskov.android_ext.snack
import com.devpraskov.android_ext.statusBarColor
import com.example.weather.R
import com.example.weather.presenter.city.CityFragment
import com.example.weather.presenter.main.AddCityDialog.Companion.ADD_CITY_REQUEST_CODE
import com.example.weather.presenter.main.AddCityDialog.Companion.CITY_EXTRA_KEY
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.second.DetailsFragment
import com.example.weather.utils.view.ForecastChartCreator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.Calendar.SECOND


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 456
    }

    private val viewModel: MainViewModel by viewModel()
    lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val currentSeconds: Long
        get() = Calendar.getInstance().get(SECOND).toLong()
    private val remainingSecondsInMinute: Long
        get() = (60 - currentSeconds) * 1000L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        checkPermission()
    }

    private fun initViews() {
        statusBarColor(Color.parseColor("#BC8DB8"))
        updateTimeEveryMinutes()
        chart.setNoDataText("")
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
        runnable = object : Runnable {
            override fun run() {
                viewModel.setNextAction(MainAction.UpdateTime)
                handler.postDelayed(this, remainingSecondsInMinute)
            }
        }
    }

    private fun initObservers() {
        viewModel.firsAction = MainAction.LoadCurrentCity
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            swipeRefreshLayout?.isRefreshing = state.isLoading
            val data = state.data.getAnyway() ?: return@Observer

            with(data.current) {
                cityName?.newText = city
                date?.newText = state.time
                iconId.let { iconState?.setImageResource(it) }
                currentState?.newText = condition
                currentTemperature?.newText = temp
                maxTemperature?.newText = maxTemp
                minTemperature?.newText = minTemp
                if (!state.data.hasBeenHandled || chart.data == null || chart.data.dataSetCount == 0) //либо новые данные, либо график не заполнен
                    ForecastChartCreator(this@MainFragment, chart)
                        .initHourlyForecastChart(data.hours)
            }
        })
    }

    private fun initListeners() {
        addCity?.onClick {
            //            val dialog = AddCityDialog()
//            dialog.setTargetFragment(this, ADD_CITY_REQUEST_CODE)
//            dialog.show(parentFragmentManager, "Dialog")
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fragment_container, CityFragment())
                .addToBackStack(DetailsFragment.TAG)
                .commit()
        }
        swipeRefreshLayout?.setOnRefreshListener { viewModel.setNextAction(MainAction.LoadCurrentCity) }
        menu?.onClick {
            val weather = viewModel.viewState.value?.data?.getAnyway() ?: return@onClick
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fragment_container, DetailsFragment.create(weather))
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
        when (PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) ->
                initObservers()
            else -> requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSION_REQUEST_CODE) return
        if ((grantResults.getOrNull(0) == PERMISSION_GRANTED)) initObservers()
        else main.snack(R.string.location_permission, Snackbar.LENGTH_INDEFINITE)
        {
            setAction("Request") { requestPermission() }
            setActionTextColor(Color.parseColor("#BC8DB8"))
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf("android.permission.ACCESS_COARSE_LOCATION"),
            PERMISSION_REQUEST_CODE
        )
    }
}
