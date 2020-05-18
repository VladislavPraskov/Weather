package com.example.weather

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var tvX: TextView? = null
    private var tvY: TextView? = null
    //    private lateinit var tfLight: Typeface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")

        title = "CubicLineChartActivity"

//        tvX = findViewById(R.id.tvXMax)
//        tvY = findViewById(R.id.tvYMax)

        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
//        chart.setBackgroundColor(android.graphics.Color.rgb(104, 241, 175))

        chart.setExtraOffsets(16f, 0f, 16f, 16f)
        // no description text
        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)


//        chart.setDrawGridBackground(false)
//        chart.maxHighlightDistance = 300f

        val x: XAxis = chart.xAxis
        x.isEnabled = true

        val y: YAxis = chart.axisLeft
        y.isEnabled = false

//        y.typeface = tfLight
        x.setLabelCount(6, false)
        x.textColor = android.graphics.Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = ContextCompat.getColor(this, R.color.lineColor)
        x.setDrawAxisLine(false)
        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false

//        x.setAxisMinimum(5f);
//        x.setAxisMaximum(10f);
        chart.xAxis.spaceMin = 0.18f
        chart.xAxis.spaceMax = 0.18f
        chart.axisRight.setDrawAxisLine(false);
        chart.axisLeft.setDrawAxisLine(false);
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

        for (set in chart.data.dataSets) set.setDrawValues(!set.isDrawValuesEnabled)

        val sets1 = chart.data.dataSets

        // // Create Limit Lines // //
        val llXAxis = LimitLine(9f)
        llXAxis.lineWidth = 4f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f

//        chart.xAxis.addLimitLine(llXAxis)
//        chart.xAxis.setDrawLimitLinesBehindData(true)
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
            set1.lineWidth = 1.8f
            set1.enableDashedLine(10f, 10f, 0f)
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = ContextCompat.getColor(this, R.color.lineColor)
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            // create a data object with the data sets
            val data = LineData(set1)
//            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            chart.data = data
        }
    }

}
