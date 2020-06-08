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

    private val gsd = Geocoder(context, Locale.getDefault())
    private var currentLocation: Location? = null

    fun blockingGetLocation(): ResultLocation {
        val count = CountDownLatch(1) //делаем callback синхронным
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                count.countDown()
                currentLocation = it
            }
        } catch (security: SecurityException) {
            return ResultLocation(securityError = true)
        }
        count.await()

        currentLocation?.apply {
            return run {
                val addresses = gsd.getFromLocation(latitude, longitude, 1)
                ResultLocation(
                    city = CityEntity(
                        lat = latitude,
                        lon = longitude,
                        country = addresses.getOrNull(0)?.countryName ?: "",
                        cityName = addresses.getOrNull(0)?.locality ?: "",
                        utc = getUtcOffsetStr(),
                        offsetSec = getUtcOffsetSec(),
                        isCurrentSelected = true,
                        idString = getId(addresses, currentLocation)
                    )
                )
            }
        }
        return ResultLocation(locationIsNotAvailableNow = true)
    }


    private fun getId(address: List<Address>, currentLocation: Location?): String {
        val code = address.getOrNull(0)?.countryCode?.toLowerCase()
        val lngLt =
            "%.4f".format(currentLocation?.longitude) + "%.4f".format(currentLocation?.latitude)
        return code + lngLt
    }

    data class ResultLocation(
        val securityError: Boolean = false,
        val locationIsNotAvailableNow: Boolean = false,
        val city: CityEntity? = null
    )
}