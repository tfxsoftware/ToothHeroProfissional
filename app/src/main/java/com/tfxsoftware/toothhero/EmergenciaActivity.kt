package com.tfxsoftware.toothhero

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.tfxsoftware.toothhero.databinding.ActivityEmergenciaBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.storage.ktx.storage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource

class EmergenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciaBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var fcmtoken: String? = null
    private val messaging = FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
        fcmtoken = token!!
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitudeClient: Double? = null
    private var longitudeClient: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        super.onCreate(savedInstanceState)
        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("ToothHeroFirebaseMsgService", intent.extras.toString())
        val extras = intent.extras

        val eid = extras?.getString("eid")
        val nome = extras?.getString("nome")
        val telefone = extras?.getString("telefone")
        val datahora = extras?.getString("datahora")
        val fotoambos = extras?.getString("fotoambos")
        val fotocrianca = extras?.getString("fotocrianca")
        val fotodoc = extras?.getString("fotodoc")
        val status = extras?.getString("status")
        val latitude = extras?.getDouble("latitude")
        val longitude = extras?.getDouble("longitude")

        val emergencia = Emergencia(eid, nome, telefone, fotoambos, fotocrianca, fotodoc, datahora, status, latitude, longitude)

        val storage = Firebase.storage
        val storageRef = storage.reference

        val refAmbos = storageRef.child(emergencia.fotoambos!!)
        refAmbos.downloadUrl
            .addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.foto2)
            }
            .addOnFailureListener { exception ->
                Log.d("image", "erro ao carregar imagem, $exception")
            }

        val refCrianca = storageRef.child(emergencia.fotocrianca!!)

        refCrianca.downloadUrl
            .addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.foto1)
            }
            .addOnFailureListener { exception ->
                Log.d("image", "erro ao carregar imagem, $exception")
            }

        val refDoc = storageRef.child(emergencia.fotodoc!!)

        refDoc.downloadUrl
            .addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.foto3)
            }
            .addOnFailureListener { exception ->
                Log.d("image", "erro ao carregar imagem, $exception")
            }

        binding.tvPaciente.text = emergencia.nome
        binding.tvData.text = emergencia.datahora
        binding.tvNumero.text = emergencia.telefone


        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener(OnSuccessListener { location ->
                    if (location != null) {
                        val lastlocation = LatLng(location.latitude, location.longitude)
                        latitudeClient = lastlocation.latitude
                        longitudeClient = lastlocation.longitude
                        val distancia = calculateDistance(
                            emergencia.latitude!!,
                            emergencia.longitude!!,
                            lastlocation.latitude,
                            lastlocation.longitude
                        )
                        binding.tvDistanciaKm.text = distancia.toInt().toString()
                    }else {
                        Log.d("localizacao", "é null")
                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            CancellationTokenSource().token
                        ).addOnSuccessListener { location ->
                            val lastlocation = LatLng(location.latitude, location.longitude)
                            latitudeClient = lastlocation.latitude
                            longitudeClient = lastlocation.longitude
                            val distancia = calculateDistance(
                                emergencia.latitude!!,
                                emergencia.longitude!!,
                                lastlocation.latitude,
                                lastlocation.longitude
                            )
                            binding.tvDistanciaKm.text = distancia.toInt().toString()
                        }
                    }
                })
        }

        binding.bAceite.setOnClickListener {

                val atendimento = Atendimento(
                    emergencia.nome,
                    LocalDateTime.now().format(formatter),
                    emergencia.eid, firebaseAuth.currentUser?.uid, "Aceito",
                    fcmtoken, emergencia.telefone, latitudeClient, longitudeClient
                )
                ApiRequests().addNovoAtendimento(atendimento) { success, _ ->
                    if (success) {
                        Toast.makeText(this, "Atendimento Aceito!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Falha ao aceitar atendimento!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                intent.removeExtra("emergencia")
                finish()

        }
        binding.bRejeitar.setOnClickListener {
            intent.removeExtra("emergencia")
            finish()
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
