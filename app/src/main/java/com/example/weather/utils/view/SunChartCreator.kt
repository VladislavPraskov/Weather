package com.example.weather.utils.view

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.devpraskov.android_ext.getColor
import com.example.weather.R
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import org.koin.android.ext.android.get
import kotlin.math.absoluteValue
import kotlin.math.sqrt


class SunChartCreator(val f: Fragment, private val chart: LineChart) {

    fun initChart(sunrise: Float, sunset: Float, time: Float) {
        chart.apply {
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            extraBottomOffset = 8f
            setTouchEnabled(false)
            extraLeftOffset = 5f
            extraRightOffset = 5f
            extraTopOffset = 5f
        }
//        chart.animateXY(500, 500)
        val sunXPosition = (time - sunrise) / (sunset - sunrise) * 2
        val sunYPosition = sqrt(2f * sunXPosition - sunXPosition * sunXPosition) * 2
        val sun =
            Entry(sunXPosition, sunYPosition, f.requireContext().getDrawable(R.drawable.sun_icon))
        var sum = 0f
        val xPoints = FloatArray(61) { i -> sum += 2 / 61f; sum }.toMutableList()
        xPoints.removeAt(xPoints.lastIndex)
        val yPoints = xPoints.map { x -> sqrt(2f * x - x * x) * 2 }
        val values = xPoints.mapIndexed { index, x ->
            Entry(x, yPoints[index])
        }.toMutableList()
        values.add(sun)
        values.sortBy { it.x }
        if (sun != values[values.lastIndex] && sun != values[values.lastIndex - 1]) {
            values.removeAt(values.lastIndex)
            values.removeAt(values.lastIndex)
        }
        initXAxis()
        initYAxis()
        chart.data = getData(values, values.indexOf(sun))
        chart.invalidate()
    }

    private fun initYAxis() {
        val leftAxis = chart.axisLeft
        leftAxis.axisMaximum = 2f
        leftAxis.axisMinimum = 0f
    }

    private fun initXAxis() {
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 15f
            textColor = Color.WHITE
            setDrawGridLines(false)
            setDrawLabels(false)
            axisLineColor = f.getColor(R.color.lineColor)
            setDrawAxisLine(false) //граница графика сверху
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val current = 0f
                    return formatXAxis(value, current)
                }
            }
            axisMinimum = 0f
            axisMaximum = 2.001f
        }

    }

    fun formatXAxis(value: Float, current: Float): String {
        if (value == 0f) return "NOW"
        var h = current - 12
        val postfix = if (h < 0) "am" else "pm"
        h = if (h <= 0 && h != -12f) h + 12 else h
        return "%.0f".format(h.absoluteValue) + postfix
    }


    private fun getData(
        values: List<Entry>,
        indexSun: Int
    ): LineData {

        val sun = values.getOrNull(indexSun)

        val listBefore = if (indexSun - 2 > 0) values.subList(
            0,
            indexSun - 2
        ).filter { it.y > 0.8f }.toMutableList() else mutableListOf()
        val listAfter: MutableList<Entry> = when {
            indexSun + 3 < values.lastIndex -> values.subList(
                indexSun + 3,
                values.lastIndex + 1
            ).filter { it.y > 0.8f }.toMutableList()
            indexSun + 3 == values.lastIndex -> mutableListOf(values[values.lastIndex])
            else -> mutableListOf()
        }
        //график до иконки
        val set1 = getSet(listBefore, false)
        val set2 = getSet(listAfter)

        //Иконка солнца
        chart.highlightValues(arrayOf(Highlight(sun?.x ?: -100f, sun?.y ?: -100f, 1)))
        val sunList = if (sun == null || sun.y < 0.78f) listOf() else listOf(sun)
        val set3 = LineDataSet(sunList, "DataSet 2").apply {
            setDrawHorizontalHighlightIndicator(false) //отключает горизонтальную линию highlight
            setDrawVerticalHighlightIndicator(false) //отключает вертикальную линию highlight
            setDrawCircles(false)
            setDrawCircleHole(false)
        }
        //Иконка солнца
        chart.highlightValues(
            arrayOf(
                Highlight(
                    values[0].x,
                    values[0].y,
                    5
                )
            )
        )
        val yLine = 0.55f
        val sunrise =
            if (sun != null && (sun.y < 0.94f && sun.y > 0.78f && sun.x < 1))
                Entry(0.1f, yLine + 0.072f)
            else
                Entry(0.1f, yLine + 0.072f, f.requireContext().getDrawable(R.drawable.sunrise_icon))

        val sunset =
            if (sun != null && (sun.y < 0.94f && sun.y > 0.78f && sun.x > 1))
                Entry(1.9f, yLine + 0.072f)
            else
                Entry(1.9f, yLine + 0.072f, f.requireContext().getDrawable(R.drawable.sunset_icon))

        val set5 = LineDataSet(listOf(sunrise, sunset), "DataSet 2").apply {
            setDrawHorizontalHighlightIndicator(false) //отключает горизонтальную линию highlight
            setDrawVerticalHighlightIndicator(false) //отключает вертикальную линию highlight
            setDrawCircles(false)
            setDrawCircleHole(false)
            enableDashedLine(0f, 1f, 0f)
        }

        val set4 = LineDataSet(mutableListOf(), "horizont").apply {
            lineWidth = 1.3f
            mode = LineDataSet.Mode.LINEAR
            color = Color.WHITE
            setDrawCircles(false)
            setDrawFilled(false)
            clear()
            addEntry(Entry(-0.5f, yLine))
            addEntry(Entry(2.5f, yLine))
        }
//         create marker to display box when values are selected

//        val mv = ChartDataView(f.requireContext(), R.layout.chart_data_text_view)
//        mv.chartView = chart
//        chart.marker = mv

        val data = LineData()
        data.addDataSet(set1)
        data.addDataSet(set2)
        if (set3.values.size == 1) data.addDataSet(set3)
        data.addDataSet(set4)
        data.addDataSet(set5)
        data.setDrawValues(false)

        return data
    }

    private fun getSet(values: List<Entry>, isAfter: Boolean = true): LineDataSet {
        return LineDataSet(values, "").apply {
            setDrawHorizontalHighlightIndicator(false) //отключает горизонтальную линию highlight
            setDrawVerticalHighlightIndicator(false) //отключает вертикальную линию highlight
            mode = LineDataSet.Mode.CUBIC_BEZIER
            if (isAfter)
                enableDashedLine(10f, 10f, 0f)
            cubicIntensity = 0.2f
            circleRadius = 2.5f
            lineWidth = 1.3f
            setDrawFilled(false)
            setDrawCircles(false)
            setCircleColor(Color.WHITE)
            color = f.getColor(R.color.lineColor)
        }
    }
}