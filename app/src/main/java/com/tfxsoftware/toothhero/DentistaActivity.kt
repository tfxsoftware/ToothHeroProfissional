package com.tfxsoftware.toothhero

import androidx.navigation.fragment.NavHostFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavInflater
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.tfxsoftware.toothhero.databinding.ActivityDentistaBinding

class DentistaActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityDentistaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDentistaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)


        supportActionBar!!.hide()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_list -> {
                    // Switch to the Dashboard fragment or activity
                    true
                }
                R.id.navigation_logout -> {
                    auth.signOut()
                    finish()
                    true
                }
                else -> false
            }
        }


    }
}