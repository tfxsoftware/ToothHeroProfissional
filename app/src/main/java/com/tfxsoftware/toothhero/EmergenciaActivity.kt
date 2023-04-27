package com.tfxsoftware.toothhero

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.tfxsoftware.toothhero.databinding.ActivityEmergenciaBinding

class EmergenciaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val emergencia = intent.getParcelableExtra<Emergencia>("emergencia")
        binding.tvNome.text = emergencia?.nome

        }
}


