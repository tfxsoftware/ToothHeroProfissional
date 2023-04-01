package com.tfxsoftware.toothhero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.google.firebase.auth.FirebaseAuth
import com.tfxsoftware.toothhero.databinding.ActivityLoginConcluidoBinding

class LoginConcluido : AppCompatActivity() {
    lateinit var tvMsgBoasVindas: AppCompatTextView
    lateinit var binding:ActivityLoginConcluidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginConcluidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()


        /*var email = intent.getStringExtra("Email")*/
        /* if(intent.hasExtra("Email")){
             Toast.makeText(this,"recebeu string",Toast.LENGTH_SHORT).show()

         }*/
     /*   var intent=getIntent()
        var resultadoLogin= intent.getStringExtra("email")
        var resultadoContaCriada=intent.getStringExtra("nome")
        if(resultadoLogin!=null){
            tvMsgBoasVindas.text="Bem vindo(a)\n(nome cadastrado no email:$resultadoLogin)"
        }
        if(resultadoContaCriada!=null){
            tvMsgBoasVindas.text="Bem vindo(a),\n$resultadoContaCriada"

        }*/


        binding.btnDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut() //Este método torna o currentUser nulo,impossibilitando o usuário de ser direcionado para a tela principal
            var voltarTelaLogin= Intent(this,MainActivity::class.java)
            startActivity(voltarTelaLogin)
            finish()

        }





    }
}