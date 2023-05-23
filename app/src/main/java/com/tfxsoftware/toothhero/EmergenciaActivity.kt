package com.tfxsoftware.toothhero


import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.tfxsoftware.toothhero.databinding.ActivityEmergenciaBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.storage.ktx.storage


class EmergenciaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergenciaBinding
    private var firebaseAuth = FirebaseAuth.getInstance()

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
            val fotos = extras?.getString("fotos")
            val status = extras?.getString("status")

        Log.d("ToothHeroFirebaseMsgService", fotos.toString())
        val emergencia = Emergencia(eid, nome, telefone, fotos, datahora, status)

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child(emergencia.fotos!!)

        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.foto2)
            }
            .addOnFailureListener { exception ->
                Log.d("image", "erro ao carregar imagem, $exception")
            }
        binding.tvPaciente.text = emergencia.nome
        binding.tvData.text = emergencia.datahora
        binding.tvNumero.text = emergencia.telefone




        binding.bAceite.setOnClickListener {
            ApiRequests().getDentista(firebaseAuth.currentUser?.uid) {
                val atendimento = Atendimento(it?.nome,
                    LocalDateTime.now().format(formatter),
                    emergencia.eid, firebaseAuth.currentUser?.uid, "Aceito"
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
        }
        binding.bRejeitar.setOnClickListener {
            intent.removeExtra("emergencia")
            finish()
        }
        }
}


