package com.julio.runningtranckerapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.julio.runningtranckerapp.R
import com.julio.runningtranckerapp.other.Constants.ACTION_PAUSE_SERVICE
import com.julio.runningtranckerapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.julio.runningtranckerapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.julio.runningtranckerapp.other.Constants.ACTION_STOP_SERVICE
import com.julio.runningtranckerapp.other.Constants.FASTEST_LOCATION_INTERNAL
import com.julio.runningtranckerapp.other.Constants.LOCATION_UPDATE_INTERNAL
import com.julio.runningtranckerapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.julio.runningtranckerapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.julio.runningtranckerapp.other.Constants.NOTIFICATION_ID
import com.julio.runningtranckerapp.other.TrackingUtility
import com.julio.runningtranckerapp.ui.MainActivity
import timber.log.Timber


typealias  PolyLine = MutableList<LatLng>
typealias  Polylines = MutableList<PolyLine>
class TrackingService : LifecycleService() {

    var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }
    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE->{
                    if (isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else {
                        Timber.d("Running service!!")
                    }
                    Timber.d("Started or resumed service")
                }
                ACTION_PAUSE_SERVICE->{
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE->{
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!){
                result?.locations?.let {locations ->
                    for (location in locations){
                        addPathPoint(location)
                        Timber.d("NEW LOCATION -- longitud :${location.longitude} -- latitud: ${location.latitude}")
                    }
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private  fun updateLocationTracking(isTracking : Boolean){
        if (isTracking){
            if (TrackingUtility.hasLocationPermissions(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERNAL
                    fastestInterval = FASTEST_LOCATION_INTERNAL
                    priority = PRIORITY_HIGH_ACCURACY
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )

            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
    private fun addPathPoint(location: Location){
        location.let {
            val pos = LatLng(location.latitude,location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService(){
        addEmptyPolyline()

        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_navigation_24)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent()).build()

        startForeground(NOTIFICATION_ID,notificationBuilder)

    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )
    @RequiresApi(Build.VERSION_CODES.O)
    private  fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)

    }
}