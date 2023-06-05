package com.tfxsoftware.toothhero

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityIntro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val button = findViewById<TextView>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this@ActivityIntro, MainActivity::class.java)
            startActivity(intent)
        }
    }
}