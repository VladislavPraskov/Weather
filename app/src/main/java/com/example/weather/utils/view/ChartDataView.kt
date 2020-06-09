package com.example.weather.utils.view

import android.annotation.SuppressLint
import android.content.Context
import com.example.weather.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.chart_data_text_view.view.*


@SuppressLint("ViewConstructor")
class ChartDataView(context: Context, res: Int) : MarkerView(context, res) {
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val value = e?.y
        (e?.data as? Int)?.let { icon.setImageResource(it) }
        marker?.text =
            if (value != null) ("%.0f".format(value) + context.getString(R.string.celsius)) else ""
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF? {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}