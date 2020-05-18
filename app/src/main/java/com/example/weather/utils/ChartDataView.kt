package com.example.weather.utils

import android.content.Context
import com.example.weather.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.chart_data_text_view.view.*


class ChartDataView(context: Context, res: Int) : MarkerView(context, res) {
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        marker?.text = if (e?.y != null) ("%.0f".format(e.y) + context.getString(R.string.celsius))
        else ""
        super.refreshContent(e, highlight)
    }
    override fun getOffset(): MPPointF? {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}