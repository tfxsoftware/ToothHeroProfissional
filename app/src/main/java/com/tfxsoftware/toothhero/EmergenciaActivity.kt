package com.tfxsoftware.toothhero

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.tfxsoftware.toothhero.databinding.ActivityEmergenciaBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        val emergencia = Emergencia(eid, nome, telefone, datahora, fotos, status)

        Log.d("ToothHeroFirebaseMsgService", emergencia.toString())

        binding.tvNome.text = emergencia.nome



        binding.bAceite.setOnClickListener {
            val atendimento = Atendimento(LocalDateTime.now().format(formatter),
                              emergencia.eid, firebaseAuth.currentUser?.uid, "Aceito")
            ApiRequests().addNovoAtendimento(atendimento){success,_ ->
                if(success){
                    Toast.makeText(this, "Atendimento Aceito!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Falha ao aceitar atendimento!", Toast.LENGTH_SHORT).show()
                }
            }
            intent.removeExtra("emergencia")
            finish()
        }
        binding.bRejeite.setOnClickListener {
            val atendimento = Atendimento(LocalDateTime.now().format(formatter),
                emergencia.eid, firebaseAuth.currentUser?.uid, "Rejeitado")
            ApiRequests().addNovoAtendimento(atendimento){success,_ ->
                if(success){
                    Toast.makeText(this, "Atendimento Rejeitado!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Falha ao rejeitar atendimento", Toast.LENGTH_SHORT).show()
                }
            }
            intent.removeExtra("emergencia")
            finish()
        }
        }
}


