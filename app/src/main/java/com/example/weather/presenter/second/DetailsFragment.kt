package com.example.weather.presenter.second

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.postDelayed
import com.devpraskov.android_ext.children
import com.devpraskov.android_ext.gone
import com.devpraskov.android_ext.show
import com.example.weather.R
import com.example.weather.models.WeatherUI
import com.example.weather.utils.view.SunChartCreator
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(R.layout.fragment_details) {

    companion object {
        val TAG = DetailsFragment::class.java.name
        const val WEATHER_TAG = "weather_tag"

        fun create(weather: WeatherUI): DetailsFragment {
            val bundle = Bundle().apply { putParcelable(WEATHER_TAG, weather) }
            return DetailsFragment().apply { arguments = bundle }
        }
    }

    lateinit var runnable: Runnable
    lateinit var handler: Handler
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handler = Handler()
        runnable = object : Runnable {
            var i: Int = 0
            override fun run() {
                SunChartCreator(this@DetailsFragment, chart).initChart(
                    0f,
                    100f,
                    i.toFloat()
                )
                i += 1
                if (i == 98) { //todo
                    handler.removeCallbacks(this)
                    return
                }
                handler.postDelayed(this, 50)
            }
        }
        handler.postDelayed(runnable, 300)
    }

    private fun initViews() {
        val weather = arguments?.getParcelable<WeatherUI>(WEATHER_TAG)
        weather?.current?.apply {
            detailsTemp.dataText = feelsLike
            detailsWind.dataText = windSpeed
            detailsHumidity.dataText = humidity
            detailsPressure.dataText = pressure
            detailsVisibility.dataText = visibility
            detailsDewPoint.dataText = dewPoint
        }
//        SunChartCreator(this@DetailsFragment, chart).initChart(
//            weather?.current?.sunrise ?: 0f,
//            weather?.current?.sunset ?: 0f,
//            weather?.current?.time ?: 0f
//        )

        weather?.days?.let { dayUI ->
            daysContainer?.children?.forEachIndexed { index, dayView ->
                dayView as DayForecastView
                dayUI.getOrNull(index)?.apply {
                    dayView.dayText = dayOfWeek
                    dayView.max = maxTemp
                    dayView.min = minTemp
                    dayView.iconRes = iconId
                    dayView.show()
                } ?: dayView.gone()
            }
        }

    }


}
