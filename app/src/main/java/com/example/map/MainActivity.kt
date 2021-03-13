package com.example.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    var TAG: String ="JUMA"
    private var mapView: MapView? = null
    private lateinit var permissionManager: PermissionsManager
    private lateinit var mapboxMap: MapboxMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)}
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.Builder().fromUri(
                "mapbox://styles/mapbox/satellite-v9")) {

            enableLocationComponent(it)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this,"permission granted", Toast.LENGTH_LONG).show()    }

    override fun onPermissionResult(granted: Boolean) {
         if(granted){
            enableLocationComponent(mapboxMap.style!!)
        }
        else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show()
            finish()
        }
    }
    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            /* initializeLocationEngine()
            initializeLocationLayer()*/
            val customerLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.mapbox_blue))
                .build()

            val locationComponentsActivationOptions = LocationComponentActivationOptions.Builder(this, loadedMapStyle)
                .locationComponentOptions(customerLocationComponentOptions)
                .build()
             mapboxMap.locationComponent.apply {
                activateLocationComponent(locationComponentsActivationOptions)

                 isLocationComponentEnabled = true

                cameraMode = CameraMode.TRACKING

                renderMode = RenderMode.COMPASS

            }
        }else {

                permissionManager = PermissionsManager(this)
                permissionManager.requestLocationPermissions(this)
            }
        }

override fun onRequestPermissionsResult(requestCode: Int,
                                        permissions: Array<String>,grantResults: IntArray){
    permissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults)

}

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }


}
