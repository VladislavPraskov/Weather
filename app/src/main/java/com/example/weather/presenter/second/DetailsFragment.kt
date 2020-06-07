package com.example.weather.presenter.second

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.postDelayed
import com.devpraskov.android_ext.*
import com.example.weather.R
import com.example.weather.models.WeatherUI
import com.example.weather.utils.view.SunChartCreator
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.chart


class DetailsFragment : Fragment(R.layout.fragment_details) {

    companion object {
        val TAG = DetailsFragment::class.java.name
        const val WEATHER_TAG = "weather_tag"

        fun create(weather: WeatherUI): DetailsFragment {
            val bundle = Bundle().apply { putParcelable(WEATHER_TAG, weather) }
            return DetailsFragment().apply { arguments = bundle }
        }
    }

    lateinit var runnable: Runnable//todo убрать
    lateinit var handler: Handler//todo убрать
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 100)
    }

    private fun initViews() {
        chart.setNoDataText("")
        val weather = arguments?.getParcelable<WeatherUI>(WEATHER_TAG)
        weather?.current?.apply {
            detailsTemp.dataText = feelsLike
            detailsWind.dataText = windSpeed
            detailsHumidity.dataText = humidity
            detailsPressure.dataText = pressure
            detailsVisibility.dataText = visibility
            detailsDewPoint.dataText = dewPoint
            sunDayTime.text = sunDayH
            sunrise_time.text = sunriseH
            sunsetTime.text = sunsetH
            setGradient(getColor(colorStartId), getColor(colorEndId))
        }
        val sunrise = weather?.current?.sunrise ?: 0f
        val sunset = weather?.current?.sunset ?: 0f
        val time = weather?.current?.time ?: 0f
        launchSunAnimation(sunrise, sunset, time)

        //инициализации нижнего блока
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

    private fun setGradient(colorStart: Int, colorEnd: Int) {
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(colorStart, colorEnd))
        main?.background = gradient
        statusBarColor(colorStart)
    }

    private fun launchSunAnimation(sunrise: Float, sunset: Float, time: Float) {
        handler = Handler()
        runnable = object : Runnable {
            var i = sunrise
            override fun run() {
                chart?.let {
                    SunChartCreator(this@DetailsFragment, it).initChart(sunrise, sunset, i)
                }
                i += 400f
                if (i > time) { //остановка анимации
                    handler.removeCallbacks(this)
                    return
                } else handler.postDelayed(this, 20) //продолжаем
            }
        }
        handler.postDelayed(runnable, 100)
    }


}
