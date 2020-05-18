package com.example.weather

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initFlipper()
    }

    private fun initFlipper() {
        SoLoader.init(this, false)
        if (BuildConfig.DEBUG) {
            val client = AndroidFlipperClient.getInstance(this)
            val descriptorMapping = DescriptorMapping.withDefaults()
            client.addPlugin(InspectorFlipperPlugin(this, descriptorMapping))
            client.addPlugin(InspectorFlipperPlugin(this, descriptorMapping))
            client.addPlugin(DatabasesFlipperPlugin(this))
            client.start()
        }
    }
}