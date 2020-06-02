package com.example.weather.data.service

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.devpraskov.android_ext.getUtcOffsetSec
import com.devpraskov.android_ext.getUtcOffsetStr
import com.example.weather.data.db.city.CityEntity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class Locator(context: Context) {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val gsd = Geocoder(context, Locale.getDefault())

    private var locationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(60)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null

    var lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    fun blockingGetLocation(): ResultLocation {
        val count = CountDownLatch(1)
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                count.countDown()
                currentLocation = it
            }
        } catch (security: SecurityException) {
            return ResultLocation(securityError = true)
        }
        count.await()

        return if (currentLocation?.latitude == null || currentLocation?.longitude == null) {
            ResultLocation(locationIsNotAvailableNow = true)
        } else {
            val addresses: List<Address> =
                gsd.getFromLocation(
                    currentLocation?.latitude ?: 0.0,
                    currentLocation?.longitude ?: 0.0,
                    1
                )
            if (addresses.isNotEmpty()) println(addresses[0].locality)
            ResultLocation(
                city = CityEntity(
                    lat = currentLocation?.latitude ?: 0.0,
                    lon = currentLocation?.longitude ?: 0.0,
                    country = addresses.getOrNull(0)?.countryName ?: "",
                    cityName = addresses.getOrNull(0)?.locality ?: "",
                    utc = getUtcOffsetStr(),
                    offsetSec = getUtcOffsetSec(),
                    isCurrentSelected = true,
                    countryAndPostCode = addresses.getOrNull(0)?.let { it.countryCode + it.postalCode } ?: ""
                )
            )
        }
    }

    data class ResultLocation(
        val securityError: Boolean = false,
        val locationIsNotAvailableNow: Boolean = false, //привязать к снекбару
        val city: CityEntity? = null
    )
}