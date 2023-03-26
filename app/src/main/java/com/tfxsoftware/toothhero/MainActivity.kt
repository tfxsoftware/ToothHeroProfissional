package com.tfxsoftware.toothhero



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class MainActivity : View.OnClickListener, AppCompatActivity(){
    lateinit var btnEntrar:AppCompatButton
    lateinit var btnCriarConta:AppCompatButton
    lateinit var etEmail:AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        btnEntrar=findViewById(R.id.btnEntrar)
        btnCriarConta=findViewById(R.id.btnCriarConta)
        etEmail=findViewById(R.id.etEmail)
        btnEntrar.setOnClickListener(this)
        btnCriarConta.setOnClickListener(this)

    }
    override fun onClick(v: View?){
        if(v!!.id == R.id.btnEntrar){
            var intent= Intent(this,LoginConcluido::class.java)
            intent.putExtra("email",etEmail.text.toString())
            startActivity(intent)
            finish()
        }
        if(v!!.id==R.id.btnCriarConta){
            var intent =Intent(this,RegistroAcitivty::class.java)
            startActivity(intent)
            finish()
        }
    }


}