package com.example.weather.presenter.second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather.R
import com.example.weather.data.db.weather.WeatherEntity
import com.example.weather.models.main.WeatherForecast
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(R.layout.fragment_details) {

    companion object {
        val TAG = DetailsFragment::class.java.name
        const val WEATHER_LIST_TAG = "weather_list_tag"
        fun create(weather: List<WeatherEntity>): DetailsFragment {
            return DetailsFragment().apply {
                arguments =
                    Bundle().apply { putParcelableArrayList(WEATHER_LIST_TAG, ArrayList(weather)) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val weather = arguments?.getParcelableArrayList<WeatherEntity>(WEATHER_LIST_TAG)
//        detailsTemp.dataText =
//        detailsWind.dataText =
//        detailsHumidity.dataText =
//        detailsPressure.dataText =
//        detailsVisibility.dataText =
//        detailsDewPoint.dataText =
    }


}
