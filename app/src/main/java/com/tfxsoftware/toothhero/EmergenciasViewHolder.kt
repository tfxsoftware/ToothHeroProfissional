package com.tfxsoftware.toothhero

import com.google.android.gms.location.FusedLocationProviderClient
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class EmergenciasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val emergenciaNome: TextView = itemView.findViewById(R.id.emergencia_nome)
    private val emergenciaData: TextView = itemView.findViewById(R.id.emergencia_data)
    private val emergenciaDistancia: TextView = itemView.findViewById(R.id.DistanciaKm)
    private lateinit var lastlocation: LatLng

    fun bind (emergencia: Emergencia, listener: (Emergencia) -> Unit) {
        itemView.setOnClickListener { listener(emergencia) }
        emergenciaNome.text = emergencia.nome
        emergenciaData.text = emergencia.datahora

        // Check for location permission
        if (ContextCompat.checkSelfPermission(itemView.context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(itemView.context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener(OnSuccessListener { location ->
                    if (location != null) {
                        lastlocation = LatLng(location.latitude, location.longitude)
                        Log.d("localizacao", lastlocation.toString())
                        val distancia = calculateDistance(
                            emergencia.latitude!!,
                            emergencia.longitude!!,
                            lastlocation.latitude,
                            lastlocation.longitude
                        )
                        emergenciaDistancia.text = distancia.toInt().toString()
                    } else{
                        Log.d("localizacao", "é null")
                        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                            CancellationTokenSource().token).addOnSuccessListener {location ->
                            lastlocation = LatLng(location.latitude, location.longitude)
                            Log.d("localizacao", lastlocation.toString())
                            val distancia = calculateDistance(
                                emergencia.latitude!!,
                                emergencia.longitude!!,
                                lastlocation.latitude,
                                lastlocation.longitude
                            )
                            emergenciaDistancia.text = distancia.toInt().toString()
                        }
                    }
                })
                .addOnFailureListener(OnFailureListener { exception ->
                    // Handle failure
                })

        }
    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000 // Convert meters to kilometers
    }
}
