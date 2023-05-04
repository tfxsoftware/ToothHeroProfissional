package com.tfxsoftware.toothhero

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.tfxsoftware.toothhero.databinding.ActivityEmergenciaBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.storage.FirebaseStorage

import com.google.firebase.storage.StorageReference




class EmergenciaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergenciaBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private val storageReference:StorageReference = FirebaseStorage.getInstance().reference
    private val megabyte: Long = 1024 * 1024
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

        val imgReference = storageReference.child(emergencia.fotos!!)
        imgReference.getBytes(megabyte).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.foto1.setImageBitmap(bmp)
        }.addOnFailureListener{
            Toast.makeText(this, "erro ao carregar imagem", Toast.LENGTH_SHORT).show()
        }

        binding.tvPaciente.text = emergencia.nome
        binding.tvData.text = emergencia.datahora
        binding.tvNumero.text = emergencia.telefone




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
        binding.bRejeitar.setOnClickListener {
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


