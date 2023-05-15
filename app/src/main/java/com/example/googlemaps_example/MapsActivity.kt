package com.example.googlemaps_example

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.googlemaps_example.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.concurrent.TimeUnit


class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mMap = map
            // Call the function to get the current location
            getCurrentLocation()
        }
        title = "Current Location"

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



    }



    private fun getCurrentLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(5000)
            .build()

            // Get the last known location
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            fusedLocationProviderClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    location?.let {
//                        // Set the map camera position to the current location
//                        val latLng = LatLng(location.latitude, location.longitude)
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//
//                        // Add a marker at the current location
//                        mMap.addMarker(MarkerOptions().position(latLng))
//                    }
//                }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                        locationResult.lastLocation?.let { location ->
                            val latLng = LatLng(location.latitude, location.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            mMap.addMarker(MarkerOptions().position(latLng))
                        }
                    }
                },
                null
            )
        }
        else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show()
        }

    }



}