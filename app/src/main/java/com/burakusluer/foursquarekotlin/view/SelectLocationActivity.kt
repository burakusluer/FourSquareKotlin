package com.burakusluer.foursquarekotlin.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.burakusluer.foursquarekotlin.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SelectLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    var addLocation:Intent?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                mMap.clear()
                if (location != null) {
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 15F
                        )
                    )
                    locationManager.removeUpdates(locationListener)
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }
            override fun onProviderEnabled(provider: String?) {

            }
            override fun onProviderDisabled(provider: String?) {

            }
        }
        if (ContextCompat.checkSelfPermission(
                this@SelectLocationActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0F,
                locationListener
            )
        } else
            ActivityCompat.requestPermissions(
                this@SelectLocationActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        mMap.setOnMapLongClickListener {
             addLocation=Intent()
            val doubleArray:DoubleArray=DoubleArray(2)
            doubleArray[0]=it.latitude
            doubleArray[1]=it.longitude
            addLocation!!.putExtra("locationData", doubleArray)
            addLocation!!.data=Uri.parse("deneme")
            this.finish()
        }
    }
    override fun finish(){
        this@SelectLocationActivity.setResult(Activity.RESULT_OK,addLocation)

        super.finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0 && grantResults.isNotEmpty() && ContextCompat.checkSelfPermission(
                this@SelectLocationActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0F,
                locationListener
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}