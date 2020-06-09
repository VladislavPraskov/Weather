package com.example.weather.presenter.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.devpraskov.android_ext.hide
import com.example.weather.R
import kotlinx.android.synthetic.main.day_forecast_view.view.*
import kotlinx.android.synthetic.main.day_forecast_view.view.icon

class DayForecastView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var dayText: String? = ""
        set(value) {
            field = value; day?.text = field
        }

    var min: String? = ""
        set(value) {
            field = value; minTemp?.text = field
        }

    var max: String? = ""
        set(value) {
            field = value; maxTemp?.text = field
        }

    var iconRes: Int? = null
        set(value) {
            field = value; iconRes?.let { icon.setImageResource(it) }
        }


    init {
        LayoutInflater.from(context).inflate(R.layout.day_forecast_view, this, true)
        val a = context.obtainStyledAttributes(attrs, R.styleable.DayForecastView, defStyle, 0)
        dayText = a.getString(R.styleable.DayForecastView_dayText)
        min = a.getString(R.styleable.DayForecastView_minText)
        max = a.getString(R.styleable.DayForecastView_maxText)
        if (a.hasValue(R.styleable.DayForecastView_icon)) {
            val icon = a.getResourceId(R.styleable.DetailsView_icon, 0)
            iconRes = if (icon != 0) icon else null
        }
        hide()
        a.recycle()
    }
}
