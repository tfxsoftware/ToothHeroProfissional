package com.tfxsoftware.toothhero

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.messaging.FirebaseMessaging


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

        if (dentista == null) {
            ApiRequests().getDentista(auth.uid) {
                dentista = it
                nomeDentista.text = "Dr. ${dentista?.nome}"
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
        }
}

