package com.example.weather.utils.view

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.devpraskov.android_ext.getColor
import com.example.weather.R
import com.example.weather.models.main.HourUI
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlin.math.absoluteValue

class ForecastChartCreator(val f: Fragment, val chart: LineChart) {

    fun initHourlyForecastChart(hours: List<HourUI>) {
        initChart(hours)
    }

    private fun initChart(hours: List<HourUI>) {
        chart.apply {
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            extraBottomOffset = 8f
            setTouchEnabled(false)
            extraLeftOffset = 25f
            extraRightOffset = 25f
            extraTopOffset = 35f
        }
        val values = hours.mapIndexed { index, h ->
            Entry(index.toFloat(), h.temp, h.iconId)
        }
        initXAxis(hours)
        initYAxis(values)
        val isDataEmpty = (chart.data?.dataSetCount ?: 0 == 0)
        chart.data = getData(values)
        if (isDataEmpty) chart.animateX(700)
        else chart.invalidate()
    }

    private fun initYAxis(values: List<Entry>?) {
        val leftAxis = chart.axisLeft
        val minY = (values?.minBy { it.y }?.y ?: 0f) - 2f
        val maxY = (values?.maxBy { it.y }?.y ?: 0f) + 3f
        leftAxis.axisMaximum = maxY
        leftAxis.axisMinimum = minY
    }

    private fun initXAxis(hours: List<HourUI>) {
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 15f
            textColor = Color.WHITE
            setDrawGridLines(false)
            axisLineColor = f.getColor(R.color.lineColor)
            setDrawAxisLine(false) //граница графика сверху

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val current = hours.getOrNull(value.toInt())?.timeH ?: 0f
                    return formatXAxis(value, current)
                }
            }
            setLabelCount(6, true)
            axisMinimum = 0f
            axisMaximum = 5.001f
        }

    }

    fun formatXAxis(value: Float, current: Float): String {
        if (value == 0f) return "NOW"
        var h = current - 12
        val postfix = if (h < 0) "am" else "pm"
        h = if (h <= 0 && h != -12f) h + 12 else h
        return "%.0f".format(h.absoluteValue) + postfix
    }


    private fun getData(values: List<Entry>?): LineData {

        //график температуры
        val set1 = LineDataSet(values, "DataSet 1").apply {
            setDrawHorizontalHighlightIndicator(false) //отключает горизонтальную линию highlight
            setDrawVerticalHighlightIndicator(false) //отключает вертикальную линию highlight
            mode = LineDataSet.Mode.CUBIC_BEZIER
            enableDashedLine(10f, 10f, 0f)
            cubicIntensity = 0.2f
            circleRadius = 2.5f
            lineWidth = 1.3f
            setDrawFilled(false)
            setDrawCircles(true)
            setCircleColor(Color.WHITE)
            color = f.getColor(R.color.lineColor)
        }

        //Подсветка первой точки
        val firstPoint = mutableListOf(values?.getOrNull(0)?.copy())
        val set2 = LineDataSet(firstPoint, "DataSet 2").apply {
            setDrawCircles(true)
            circleRadius = 7f
            setCircleColor(f.getColor(R.color.white_30))
        }
        // create marker to display box when values are selected
        val mv = ChartDataView(
            f.requireContext(),
            R.layout.chart_data_text_view
        )
        mv.chartView = chart
        chart.marker = mv

        val data = LineData()
        data.addDataSet(set1)
        data.addDataSet(set2)

        //Делаем вертикальные линии
        val highlightList = ArrayList<Highlight>()
        values?.forEachIndexed { index, it ->
            highlightList.add(Highlight(it.x, it.y, 0))
            val set = LineDataSet(mutableListOf(), "set").apply {
                lineWidth = if (index == 0 || index == 5) 0.8f * 2.5f else 0.8f
                mode = LineDataSet.Mode.LINEAR
                setColor(Color.WHITE, 50)
                addColor(Color.WHITE)
                setDrawCircles(false)
                setDrawFilled(false)
                clear()
                addEntry(Entry(it.x, chart.yChartMin)) //нижняя точка вертикальной линии
                addEntry(Entry(it.x, it.y)) //верхняя точка вертикальной линии
            }
            data.addDataSet(set)
        }
        chart.highlightValues(highlightList.toTypedArray())
        data.setDrawValues(false)
        return data
    }
}