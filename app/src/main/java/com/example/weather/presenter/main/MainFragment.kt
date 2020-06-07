package com.example.weather.presenter.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.devpraskov.android_ext.*
import com.example.weather.R
import com.example.weather.presenter.city.CityFragment
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.second.DetailsFragment
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.view.ForecastChartCreator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.swipeRefreshLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.Calendar.SECOND


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 456
        val animationRight = listOf(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        val animationLeft = listOf(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )
    }

    private val viewModel: MainViewModel by viewModel()
    lateinit var handler: Handler
    private lateinit var runnable: Runnable
    var isUpdateCityNeeded = true
    var currentColor = Pair(16507811, 8483167)
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
        viewModel.setNextAction(MainAction.UpdateTime)
        if (isUpdateCityNeeded) {
            viewModel.setNextAction(MainAction.LoadCurrentCity)
            isUpdateCityNeeded = false
        }
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
                showError(state.error.getIfNotBeenHandled())
                currentColor = Pair(getColor(colorStartId), getColor(colorEndId))
                setGradient(currentColor.first, currentColor.second)
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

    private fun setGradient(colorStart: Int, colorEnd: Int) {
        val gradient = GradientDrawable(TOP_BOTTOM, intArrayOf(colorStart, colorEnd))
        main?.background = gradient
        statusBarColor(colorStart)
    }


    private fun showError(error: ErrorMVI?) {
        error ?: return
        error.message ?: return
        Snackbar.make(main, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun initListeners() {
        addCity?.onClick {
            val fragment = CityFragment.create(currentColor)
            fragment.setTargetFragment(this, 42)
            startFragment(fragment)
        }
        swipeRefreshLayout?.setOnRefreshListener { viewModel.setNextAction(MainAction.LoadCurrentCity) }
        details?.onClick {
            val weather = viewModel.viewState.value?.data?.getAnyway() ?: return@onClick
            startFragment(DetailsFragment.create(weather))
        }
    }

    private fun startFragment(f: Fragment) {
        val anim = if (f is CityFragment) animationRight else animationLeft
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(anim[0], anim[1], anim[2], anim[3])
            .replace(R.id.fragment_container, f)
            .addToBackStack(null)
            .commit()
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
