package com.tfxsoftware.toothhero

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage


class HomeFragment : Fragment() {
    private val messaging = FirebaseMessaging.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var dentista: Dentista? = null

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
        botaoEncerra.visibility = View.GONE

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
}

