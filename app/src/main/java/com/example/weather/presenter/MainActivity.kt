package com.example.weather.presenter

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.devpraskov.android_ext.statusBarColor
import com.example.weather.R
import com.example.weather.presenter.main.MainFragment
import com.example.weather.presenter.second.DetailsFragment
import com.example.weather.utils.ChartDataView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusBarColor(Color.parseColor("#BC8DB8"))
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MainFragment())
            .commit()
    }
}
