package com.example.weather.data.service

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.example.weather.data.db.city.City
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class Locator(context: Context) {
    // FusedLocationProviderClient - Main class for receiving location updates.
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val gsd = Geocoder(context, Locale.getDefault())

    // LocationRequest - Requirements for the location updates, i.e., how often you
// should receive updates, the priority, etc.
    private var locationRequest = LocationRequest().apply {
        // Sets the desired interval for active location updates. This interval is inexact. You
        // may not receive updates at all if no location sources are available, or you may
        // receive them less frequently than requested. You may also receive updates more
        // frequently than requested if other applications are requesting location at a more
        // frequent interval.
        //
        // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
        // targetSdkVersion) may receive updates less frequently than this interval when the app
        // is no longer in the foreground.
        interval = TimeUnit.SECONDS.toMillis(60)

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates more frequently than this value.
        fastestInterval = TimeUnit.SECONDS.toMillis(60)

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
// database, but because this is a simplified sample without a full database, we only need the
// last location to create a Notification if the user navigates away from the app.
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
                gsd.getFromLocation(currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0, 1)
            if (addresses.isNotEmpty()) println(addresses[0].locality)
            ResultLocation(
                city = City(
                    lat = currentLocation?.latitude ?: 0.0,
                    lon = currentLocation?.longitude ?: 0.0,
                    name = addresses.getOrNull(0)?.locality
                )
            )
        }
    }

    data class ResultLocation(
        val securityError: Boolean = false,
        val locationIsNotAvailableNow: Boolean = false,
        val city: City?= null
    )
}