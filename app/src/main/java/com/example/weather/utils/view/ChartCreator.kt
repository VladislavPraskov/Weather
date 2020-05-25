package com.example.weather.utils.view

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.devpraskov.android_ext.getColor
import com.example.weather.R
import com.example.weather.models.main.HourlyData
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlin.math.absoluteValue

class ChartCreator(val f: Fragment?) {

    fun initHourlyForecastChart(chart: LineChart?, hourlyForecast: List<HourlyData>) {
        initChart(hourlyForecast, chart)
    }

    private fun initChart(
        hourlyForecast: List<HourlyData>?,
        chart: LineChart?
    ) {
        chart ?: return
        f ?: return
        val x: XAxis = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.textSize = 16f
        x.textColor = Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = f.getColor(R.color.lineColor)
        x.setDrawAxisLine(false) //граница графика сверху
        x.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                //формат оси Ох
                return if (value == 0f) "NOW"
                else {
                    var h = (hourlyForecast?.getOrNull(value.toInt())?.hour ?: 0f) - 12
                    val postfix = if (h < 0) "am" else "pm"
                    h = if (h <= 0 && h != -12f) h + 12 else h
                    "%.0f".format(h.absoluteValue) + postfix
                }
            }
        }

        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.extraBottomOffset = 8f
        chart.setTouchEnabled(false)
//        chart.animateXY(500, 500)

        val values = hourlyForecast?.mapIndexed { index, h ->
            Entry(index.toFloat(), h.temp, h.iconRes)
        }

        chart.xAxis.setLabelCount(6, true)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 5.001f
        val leftAxis = chart.axisLeft
        val minY = (values?.minBy { it.y }?.y ?: 0f) - 2f
        val maxY = (values?.maxBy { it.y }?.y ?: 0f) + 3f
        leftAxis.axisMaximum = maxY
        leftAxis.axisMinimum = minY

        setData(chart, values)
        chart.invalidate()

//        for (set in chart.data.dataSets) set.setDrawValues(false) // включает/отключает рисовку значений
    }


    private fun setData(
        chart: LineChart?,
        values: List<Entry>?
    ) {
        chart ?: return
        f ?: return

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(false)
        set1.setDrawCircles(true)
        set1.lineWidth = 1.3f
        set1.enableDashedLine(10f, 10f, 0f)
        set1.circleRadius = 2.5f
        set1.setCircleColor(Color.WHITE)
        set1.color = f.getColor(R.color.lineColor)

//            data.setValueTypeface(tfLight)
        val set2 = LineDataSet(mutableListOf(values?.getOrNull(0)?.copy()), "DataSet 2")
        set2.setDrawCircles(true)
        set2.circleRadius = 7f
        set2.setCircleColor(f.getColor(R.color.white_30))
        // create marker to display box when values are selected
        val mv = ChartDataView(f.requireContext(), R.layout.chart_data_text_view)
        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv
        val data = LineData()
        data.addDataSet(set1)
        data.addDataSet(set2)
        //Выделяет все точки
        val highlightList = ArrayList<Highlight>()
        values?.forEachIndexed { index, it ->
            highlightList.add(Highlight(it.x, it.y, 0))
            val set = LineDataSet(mutableListOf(), "set")
            set.mode = LineDataSet.Mode.LINEAR
            set.setDrawFilled(false)
            set.lineWidth = if (index == 0 || index == 5) 0.8f * 2.5f else 0.8f
//                set2.color = ContextCompat.getColor(this, R.color.white_30)
            set.setColor(Color.WHITE, 50)
            set.setDrawCircles(false)
            set.addColor(Color.WHITE)
            set.clear()
            set.addEntry(Entry(it.x, chart.yChartMin))
            set.addEntry(Entry(it.x, it.y))
            data.addDataSet(set)
        }
        chart.highlightValues(highlightList.toTypedArray())
        data.setValueTextSize(12f)
        data.setDrawValues(false)
        set1.setDrawHorizontalHighlightIndicator(false) //отключает вертикальную линию highlight
        set1.setDrawVerticalHighlightIndicator(false) //отключает вертикальную линию highlight
        data.setValueTextColor(f.getColor(R.color.white))
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "%.0f".format(value) + f.getString(R.string.celsius)
            }
        })
        data.setDrawValues(false)
        chart.extraLeftOffset = 25f
        chart.extraRightOffset = 25f
        chart.extraTopOffset = 35f
        chart.data = data
//        }
    }
}