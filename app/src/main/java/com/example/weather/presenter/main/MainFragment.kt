package com.example.weather.presenter.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.devpraskov.android_ext.getColor
import com.devpraskov.android_ext.onClick
import com.example.weather.R
import com.example.weather.presenter.main.AddCityDialog.Companion.ADD_CITY_REQUEST_CODE
import com.example.weather.presenter.main.AddCityDialog.Companion.CITY_EXTRA_KEY
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        val PERMISSION_REQUEST_CODE = 456
    }

    private val viewModel: MainViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
        initChart()
    }

    private fun initViews() {

    }

    private fun initObservers() {
        checkPermishion()
        viewModel.firsAction = MainAction.LoadCurrentCity
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->

        })
    }

    private fun initListeners() {
        addCity?.onClick {
            val dialog = AddCityDialog()
            dialog.setTargetFragment(this, ADD_CITY_REQUEST_CODE)
            dialog.show(parentFragmentManager, "Dialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_CITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val city = data?.getStringExtra(CITY_EXTRA_KEY) ?: ""
            if (city.isNotEmpty()) {
                viewModel.setNextAction(MainAction.LoadCurrentCity)
            }
        }
    }

    fun checkPermishion() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {

            }
            else -> {
                requestPermissions(
                    arrayOf("android.permission.ACCESS_COARSE_LOCATION"),
                    PERMISSION_REQUEST_CODE
                )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun initChart() {
        val x: XAxis = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.textSize = 14f
        x.textColor = Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = getColor(R.color.lineColor)
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

    private fun setData(
        values: ArrayList<Entry>
    ) {


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
            set2.setCircleColor(getColor(R.color.white_30))

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
            set1.color = getColor(R.color.lineColor)

//            data.setValueTypeface(tfLight)

            // create marker to display box when values are selected
            val mv = ChartDataView(requireContext(), R.layout.chart_data_text_view)
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
            data.setValueTextColor(getColor(R.color.white))
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
