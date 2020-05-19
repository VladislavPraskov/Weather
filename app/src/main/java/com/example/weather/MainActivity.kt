package com.example.weather

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val x: XAxis = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.textSize = 14f
        x.textColor = Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = ContextCompat.getColor(this, R.color.lineColor)
        x.setDrawAxisLine(false) //граница графика сверху
        x.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 0f) "NOW" else "%.0f".format(value) + " pm"
            }
        }

        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.extraBottomOffset = 8f
        chart.setTouchEnabled(false)
//        chart.animateXY(2000, 2000)

        val values = ArrayList<Entry>()
        values.add(Entry(0f, 23f))
        values.add(Entry(1f, 24f))
        values.add(Entry(2f, 25f))
        values.add(Entry(3f, 24f))
        values.add(Entry(4f, 22f))
        values.add(Entry(5f, 23f))

        chart.xAxis.setLabelCount(6, true)
        chart.xAxis.axisMinimum = 0f
        chart.xAxis.axisMaximum = 5.001f
        val leftAxis = chart.axisLeft
        val minY = (values.minBy { it.y }?.y ?: 0f) - 1f
        val maxY = (values.maxBy { it.y }?.y ?: 0f) + 3f
        leftAxis.axisMaximum = maxY
        leftAxis.axisMinimum = minY
        chart.xAxis.spaceMin = 0.18f
        chart.xAxis.spaceMax = 0.18f

        setData(values)
        chart.invalidate()

//        for (set in chart.data.dataSets) set.setDrawValues(false) // включает/отключает рисовку значений
    }

    private fun setData(values: ArrayList<Entry>) {


        val set1: LineDataSet
        chart ?: return
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {

            val set2 = LineDataSet(mutableListOf(values[0].copy()), "DataSet 2")
            set2.setDrawCircles(true)
            set2.circleRadius = 6f
            set2.setCircleColor(ContextCompat.getColor(this, R.color.white_30))

            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(false)
            set1.setDrawCircles(true)
            set1.lineWidth = 1.3f
            set1.enableDashedLine(10f, 10f, 0f)
            set1.circleRadius = 2.5f
            set1.setCircleColor(Color.WHITE)
            set1.color = ContextCompat.getColor(this, R.color.lineColor)


//            data.setValueTypeface(tfLight)

            // create marker to display box when values are selected
            val mv = ChartDataView(this, R.layout.chart_data_text_view)
            // Set the marker to the chart
            mv.chartView = chart
            chart.marker = mv
            val data = LineData()
            data.addDataSet(set1)
            data.addDataSet(set2)
            //Выделяет все точки
            val highlightList = ArrayList<Highlight>()
            values.forEach {
                highlightList.add(Highlight(it.x, it.y, 0))
                val set = LineDataSet(mutableListOf(), "set")
                set.mode = LineDataSet.Mode.LINEAR
                set.setDrawFilled(false)
                set.lineWidth = 0.8f
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
            data.setValueTextColor(ContextCompat.getColor(this, R.color.white))
            data.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.0f".format(value) + getString(R.string.celsius)
                }
            })
            data.setDrawValues(false)
            // set data
            chart.data = data
        }
    }

}
