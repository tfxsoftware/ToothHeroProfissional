package com.tfxsoftware.toothhero

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.tfxsoftware.toothhero.databinding.ActivityDisputaBinding

class DisputaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDisputaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisputaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        binding.nomeReporte.text = intent.getStringExtra("nome")
        binding.TvReportar.text = intent.getStringExtra("comentario")
        binding.btnSolicitarRevisao.setOnClickListener {
            ApiRequests().addNovaDisputa(Disputa(intent.getStringExtra("aid")!!,
                                                binding.TvDetalhar.text.toString()))
            {success ->
                if (success){
                    Toast.makeText(this, "Avaliação reportada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falhar ao reportar avaliação", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}