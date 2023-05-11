package com.tfxsoftware.toothhero

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.navigation.fragment.NavHostFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.tfxsoftware.toothhero.databinding.ActivityDentistaBinding

class DentistaActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityDentistaBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("access", "granted")
        } else {
            Log.d("access","not granted")
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = ActivityDentistaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        supportActionBar!!.hide()

        askNotificationPermission()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                    true
                }
                R.id.listaEmergenciasFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.listaEmergenciasFragment)
                    true
                }

                R.id.navigation_logout -> {
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Tem certeza que deseja sair?")
                        .setPositiveButton("Sim") { _, _ ->
                            auth.signOut()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                        .setNegativeButton("Não") { _, _ ->

                        }
                        .create()

                    dialog.show()

                    false
                }
                else -> false
            }
        }


    }
}