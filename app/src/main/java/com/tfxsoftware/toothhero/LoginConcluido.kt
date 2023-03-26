package com.tfxsoftware.toothhero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat

class LoginConcluido : AppCompatActivity() {
    lateinit var tvMsgBoasVindas: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_concluido)
        supportActionBar!!.hide()

        tvMsgBoasVindas =findViewById(R.id.tvMsgBoasVindas)
        /*var email = intent.getStringExtra("Email")*/
        /* if(intent.hasExtra("Email")){
             Toast.makeText(this,"recebeu string",Toast.LENGTH_SHORT).show()

         }*/
        var intent=getIntent()
        var resultadoLogin= intent.getStringExtra("email")
        var resultadoContaCriada=intent.getStringExtra("nome")
        if(resultadoLogin!=null){
            tvMsgBoasVindas.text="Bem vindo(a)\n(nome cadastrado no email:$resultadoLogin)"
        }
        if(resultadoContaCriada!=null){
            tvMsgBoasVindas.text="Bem vindo(a),\n$resultadoContaCriada"

        }




    }
}