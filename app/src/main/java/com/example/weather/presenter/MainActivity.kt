package com.example.weather.presenter

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devpraskov.android_ext.statusBarColor
import com.example.weather.R
import com.example.weather.presenter.main.MainFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusBarColor(Color.parseColor("#BC8DB8"))
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MainFragment())
            .commit()
    }
}
