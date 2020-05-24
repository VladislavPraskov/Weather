package com.example.weather.presenter.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.devpraskov.android_ext.getColor
import com.devpraskov.android_ext.onClick
import com.devpraskov.android_ext.statusBarColor
import com.example.weather.R
import com.example.weather.models.main.HourlyData
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
import java.util.*
import java.util.Calendar.SECOND
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 456
    }

    private val viewModel: MainViewModel by viewModel()
    lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val currentSeconds: Long
        get() = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(SECOND).toLong()
    private val remainingSecondsInMinute: Long
        get() = (60 - currentSeconds) * 1000L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() {
        statusBarColor(Color.parseColor("#BC8DB8"))
        updateTimeEveryMinutes()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, remainingSecondsInMinute)
    }

    private fun updateTimeEveryMinutes() {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                viewModel.setNextAction(MainAction.UpdateTime)
                handler.postDelayed(this, remainingSecondsInMinute)
            }
        }
    }

    private fun initObservers() {
        checkPermission()
        viewModel.firsAction = MainAction.LoadCurrentCity
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            swipeRefreshLayout?.isRefreshing = state.isLoading
//            date?.text = "Mon, 6:44 AM"
            date?.text = state.time
            val data = state.data.getIfNotBeenHandled()
            data ?: return@Observer
//            city?.text = "Marseille"
            city?.text = data.city
            data.mainIcon?.let { iconState?.setImageResource(it) }
//            currentState?.text = "Fog"
            currentState?.text = data.condition
//            currentTemperature?.text = "18"
            currentTemperature?.text = data.temp
//            maxTemperature?.text = ("24" + getString(R.string.celsius) + "C")
//            minTemperature?.text = ("12" + getString(R.string.celsius) + "C")
            maxTemperature?.text = (data.maxTemp + getString(R.string.celsius) + "C")
            minTemperature?.text = (data.minTemp + getString(R.string.celsius) + "C")
//            initChart(
//                listOf(
//                    Pair(18f, R.drawable.cloud_icon),
//                    Pair(18f, R.drawable.cloud_icon),
//                    Pair(19f, R.drawable.cloud_icon),
//                    Pair(21f, R.drawable.cloud_icon),
//                    Pair(19f, R.drawable.cloud_icon),
//                    Pair(18f, R.drawable.cloud_icon)
//                )
//            )
            initChart(data.hourlyForecast)
        })
    }

    private fun initListeners() {
        addCity?.onClick {
            val dialog = AddCityDialog()
            dialog.setTargetFragment(this, ADD_CITY_REQUEST_CODE)
            dialog.show(parentFragmentManager, "Dialog")
        }
        swipeRefreshLayout?.setOnRefreshListener { viewModel.setNextAction(MainAction.LoadCurrentCity) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_CITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val city = data?.getStringExtra(CITY_EXTRA_KEY) ?: ""
            if (city.isNotEmpty()) {
                viewModel.setNextAction(MainAction.LoadCurrentCity)
            }
        }
    }

    private fun checkPermission() {
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
                    //todo
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

    private fun initChart(hourlyForecast: List<HourlyData>?) {
        val x: XAxis = chart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.textSize = 16f
        x.textColor = Color.WHITE
        x.setDrawGridLines(false)
        x.axisLineColor = getColor(R.color.lineColor)
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

        setData(values)
        chart.invalidate()

//        for (set in chart.data.dataSets) set.setDrawValues(false) // включает/отключает рисовку значений
    }

    private fun checkEndOfDay(hourlyForecast: List<HourlyData>?) {
        // Если часы идут 22, 23, 0, 1, 2, 3 на графике будет фигня, т.к. это шкала x
        hourlyForecast?.find { it.hour == 0f } ?: return
        var h = hourlyForecast.getOrNull(0)?.hour ?: return
        if (h == 0f) return // если 0 на первом месте всё ок
        hourlyForecast.forEach {
            it.hour = h
            h++
        }
    }

    private fun setData(
        values: List<Entry>?
    ) {
        val set1: LineDataSet
        chart ?: return
//        if (chart.data != null && chart.data.dataSetCount > 0) {
//            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
//            set1.values = values
//            chart.data.notifyDataChanged()
//            chart.notifyDataSetChanged()
//        } else {

            val set2 = LineDataSet(mutableListOf(values?.getOrNull(0)?.copy()), "DataSet 2")
            set2.setDrawCircles(true)
            set2.circleRadius = 7f
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
            data.setValueTextColor(getColor(R.color.white))
            data.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.0f".format(value) + getString(R.string.celsius)
                }
            })
            data.setDrawValues(false)
            chart.extraLeftOffset = 25f
            chart.extraRightOffset = 25f
            chart.data = data
//        }
    }
}
