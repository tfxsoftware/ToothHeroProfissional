package com.tfxsoftware.toothhero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast

import com.google.firebase.messaging.FirebaseMessaging


class HomeFragment : Fragment() {
    private val messaging = FirebaseMessaging.getInstance()

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
        subscribeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Subscribe the user to the topic
                messaging.subscribeToTopic("Emergencia")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Subscribed to topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, "Failed to subscribe to topic", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Unsubscribe the user from the topic
                messaging.unsubscribeFromTopic("Emergencia")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Unsubscribed from topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, "Failed to unsubscribe from topic", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        }
}

