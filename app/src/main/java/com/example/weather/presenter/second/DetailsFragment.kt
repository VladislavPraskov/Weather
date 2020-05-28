package com.example.weather.presenter.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.devpraskov.android_ext.children
import com.devpraskov.android_ext.gone
import com.devpraskov.android_ext.show
import com.example.weather.R
import com.example.weather.data.db.weather.WeatherEntity
import com.example.weather.models.WeatherUI
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
