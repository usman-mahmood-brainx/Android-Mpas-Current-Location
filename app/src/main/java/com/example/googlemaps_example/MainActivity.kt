package com.example.googlemaps_example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.googlemaps_example.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 201

    private val permissionsFlag = MutableLiveData<Boolean>(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenMap.setOnClickListener {
            if(checkPermissions()){
              val intent = Intent(this,MapsActivity::class.java)
              startActivity(intent)
            }
            else{
                getPermissions()
            }
        }

        permissionsFlag.observe(this, Observer {
            if(it){
                val intent = Intent(this,MapsActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun getPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )

    }
    
    private fun checkPermissions(): Boolean {
        val foregroundPermission =  ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return foregroundPermission
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED){

                    permissionsFlag.postValue(true)
                }
                else {
                    Toast.makeText(this, "Please Give Location Persmission", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}