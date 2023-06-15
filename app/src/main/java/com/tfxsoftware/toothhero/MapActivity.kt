package com.tfxsoftware.toothhero

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar!!.hide()
        val zoomLevel = 17
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            val extras = intent.extras
            val latitude: Double = extras!!.getDouble("latitude")
            val longitude: Double = extras!!.getDouble("longitude")
            val eid: String? = extras!!.getString("eid")
            val marker = googleMap.addMarker(MarkerOptions().title("Minha localização")
                .position(LatLng(latitude, longitude)))

            ApiRequests().getEmergencia(eid){emergencia ->
                if (emergencia!=null){
                    val eLat = emergencia.latitude
                    val eLong = emergencia.longitude
                    val eMarker = googleMap.addMarker(MarkerOptions().title("Localização Emergencia")
                        .position(LatLng(eLat!!, eLong!!)))
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(eMarker!!.position, zoomLevel.toFloat())
                    googleMap.moveCamera(cameraUpdate)

                }
            }



            }

        }
    }

