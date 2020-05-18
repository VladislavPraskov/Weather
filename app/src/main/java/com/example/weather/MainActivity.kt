package com.example.weather

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var tvX: TextView? = null
    private var tvY: TextView? = null
    //    private lateinit var tfLight: Typeface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(false)

        // enable scaling and dragging
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)


//        chart.setDrawGridBackground(false)
//        chart.maxHighlightDistance = 300f

        val x: XAxis = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 0f) "NOW"
                else "%.0f".format(value) + "pm"
            }
        }
        x.textSize = 12f
        val y: YAxis = chart.axisLeft
        y.isEnabled = false

//        y.typeface = tfLight
        x.setLabelCount(6, false)
        x.textColor = android.graphics.Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = ContextCompat.getColor(this, R.color.lineColor)
        x.setDrawAxisLine(false) //граница графика сверху

        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false

//        x.setAxisMinimum(5f);
//        x.setAxisMaximum(10f);
        chart.xAxis.spaceMin = 0.18f
        chart.xAxis.spaceMax = 0.18f

//        chart.animateXY(2000, 2000)


        val leftAxis = chart.axisLeft
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 30f //todo переделать в зависимости от входных данных
        leftAxis.axisMinimum = 20f //todo переделать в зависимости от входных данных
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        // don't forget to refresh the drawing
        // don't forget to refresh the drawing
        setData()

        val sets = chart.data.dataSets

        for (set in chart.data.dataSets) set.setDrawValues(false)

        val sets1 = chart.data.dataSets

        chart.invalidate()
    }

    private fun setData() {
        val values = ArrayList<Entry>()
        values.add(Entry(0f, 23f))
        values.add(Entry(1f, 24f))
        values.add(Entry(2f, 25f))
        values.add(Entry(3f, 24f))
        values.add(Entry(4f, 22f))
        values.add(Entry(5f, 25f))
        values.add(Entry(6f, 26f))

        val set1: LineDataSet
        chart ?: return
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else { // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(false)
            set1.setDrawCircles(true)
            set1.lineWidth = 1.3f


            val minY = chart.yChartMin



            set1.enableDashedLine(10f, 10f, 0f)
            set1.circleRadius = 2.5f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = ContextCompat.getColor(this, R.color.lineColor)
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)

            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            // create a data object with the data sets


//            data.setValueTypeface(tfLight)

            // create marker to display box when values are selected
            val mv = ChartDataView(this, R.layout.chart_data_text_view)
            // Set the marker to the chart
            mv.chartView = chart
            chart.marker = mv
            val data = LineData()
            data.addDataSet(set1)
            //Выделяет все точки
            val highlightList = ArrayList<Highlight>()
            values.forEach {
                highlightList.add(Highlight(it.x, it.y, 0))
                val set = LineDataSet(mutableListOf(), "set")
                set.mode = LineDataSet.Mode.LINEAR
                set.setDrawFilled(false)
                set.lineWidth = 0.8f
//                set2.color = ContextCompat.getColor(this, R.color.white_30)
                set.setColor(Color.WHITE , 50)
                set.setDrawCircles(false)
                set.addColor(Color.WHITE)
                set.clear()
                set.addEntry(Entry(it.x, minY))
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
