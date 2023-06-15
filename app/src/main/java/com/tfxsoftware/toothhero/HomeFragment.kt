package com.tfxsoftware.toothhero

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

class HomeFragment : Fragment(), OnMapReadyCallback {
    private val messaging = FirebaseMessaging.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var dentista: Dentista? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatLng: LatLng? = null


    //
    override fun onMapReady(map: GoogleMap) {
        Log.d("localizacao", "mapready")

        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            Log.d("localizacao", "permissao negada")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    Log.d("localizacao", currentLatLng.toString())
                    map.addMarker(
                        MarkerOptions().title("Minha localização").position(currentLatLng!!)
                    )
                    ApiRequests().getAtendimentoEmAndamento(auth.uid){
                        val atendimento = it
                        if (atendimento != null){
                            map.addMarker(
                                MarkerOptions().position(LatLng(atendimento.latitude!!, atendimento.longitude!!)).title("Atendimento")
                            )

                        }
                    }
                    map.animateCamera(CameraUpdateFactory.zoomIn())
                    map.animateCamera(CameraUpdateFactory.zoomIn())
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng!!))


                }
            }

    }
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        botaoEncerra.visibility = View.GONE
        requestLocationPermission()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)





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
                botaoEncerra.visibility = View.VISIBLE

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
                }
                else{
                    Toast.makeText(this.context, "Erro ao finalizar atendimento!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(super.requireActivity(),
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, handle accordingly
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}
