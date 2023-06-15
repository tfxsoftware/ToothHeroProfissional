package com.tfxsoftware.toothhero

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment() {
    private val messaging = FirebaseMessaging.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var dentista: Dentista? = null
    private var userLocation: LatLng? = null
    private var eid :String? = null



    //

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subscribeSwitch: Switch = view.findViewById(R.id.switch1)
        val nomeDentista = view.findViewById<TextView>(R.id.tvNomeHome)
        val fotoDentista = view.findViewById<ImageView>(R.id.Perfil)
        val telefoneCliente = view.findViewById<TextView>(R.id.eTelefoneCliente)
        val nomeCliente = view.findViewById<TextView>(R.id.etNomeCliente)
        val textAtendimento = view.findViewById<TextView>(R.id.textAtendimento)
        val botaoEncerra = view.findViewById<AppCompatButton>(R.id.btnFinalizarAtendimento)
        val botaoMap = view.findViewById<Button>(R.id.buttonMap)
        botaoEncerra.visibility = View.GONE
        botaoMap.visibility = View.GONE
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))






        if (dentista == null) {
            ApiRequests().getDentista(auth.uid) {
                dentista = it
                nomeDentista.text = "Dr. ${dentista?.nome}"
                val storage = Firebase.storage
                val storageRef = storage.reference
                val imageRef = storageRef.child(dentista?.foto!!)

                imageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        Glide.with(this)
                            .load(imageUrl)
                            .into(fotoDentista)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("image", "erro ao carregar imagem, $exception")
                    }
            }
        }

        ApiRequests().getAtendimentoEmAndamento(auth.uid){
            val atendimento = it
            if (atendimento != null){
                textAtendimento.text = "Em atendimento"
                telefoneCliente.text = atendimento.telefone
                nomeCliente.text = atendimento.nome
                userLocation = LatLng(atendimento.latitude!!, atendimento.longitude!!)
                eid = atendimento.emergenciaId
                Log.d("localizacao", userLocation.toString())
                botaoEncerra.visibility = View.VISIBLE
                botaoMap.visibility = View.VISIBLE


            }
        }
        //
        subscribeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Subscribe the user to the topic
                messaging.subscribeToTopic("Emergencia")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Voce está disponível!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, "Falha ao mudar disponibilidade!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Unsubscribe the user from the topic
                messaging.unsubscribeFromTopic("Emergencia")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Você está indisponível!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, "Falha ao mudar disponibilidade!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        botaoEncerra.setOnClickListener {
            ApiRequests().fecharAtendimento(auth.uid){success ->
                if (success){
                    Toast.makeText(this.context, "Atendimento Encerrado!", Toast.LENGTH_SHORT).show()
                    textAtendimento.text = "Sem atendimento ativo"
                    telefoneCliente.text = ""
                    nomeCliente.text = ""
                    botaoEncerra.visibility = View.GONE
                    botaoMap.visibility = View.GONE
                }
                else{
                    Toast.makeText(this.context, "Erro ao finalizar atendimento!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        botaoMap.setOnClickListener {
            val intent = Intent(this.requireContext(), MapActivity::class.java).apply{
                action = "MapActivity"
                putExtra("latitude", userLocation!!.latitude)
                putExtra("longitude", userLocation!!.longitude)
                putExtra("eid", eid)
            }

            startActivity(intent)

        }
    }




    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}
