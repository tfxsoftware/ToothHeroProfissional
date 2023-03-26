

package com.tfxsoftware.toothhero


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RegistroAcitivty : AppCompatActivity(), View.OnClickListener {
    val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var btnCriarConta:AppCompatButton
    lateinit var btnAdcEndereco:AppCompatButton
    lateinit var etNome:AppCompatEditText
    lateinit var etEmail: AppCompatEditText
    lateinit var etSenha: AppCompatEditText
    lateinit var btnApagarCampo:AppCompatButton
    lateinit var editTextContainer:LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_criar_conta)
        supportActionBar!!.hide()

        editTextContainer = findViewById<LinearLayout>(R.id.editTextContainer)


        btnCriarConta=findViewById(R.id.btnCriarConta)
        btnAdcEndereco=findViewById(R.id.btnAdcEndereco)
        btnApagarCampo=findViewById(R.id.btnApagarCampo)
        etNome=findViewById(R.id.etNome)
        etEmail= findViewById(R.id.etEmail)
        etSenha=findViewById(R.id.etSenha)


        btnAdcEndereco.setOnClickListener(this)
        btnCriarConta.setOnClickListener(this)
        btnApagarCampo.setOnClickListener(this)


    }

    override fun onClick(v:View?){

        if(v!!.id==R.id.btnCriarConta){
            firebaseAuth.sendPasswordResetEmail("aswespeak.tfx@gmail.com")
            try{firebaseAuth.createUserWithEmailAndPassword(etEmail.text.toString(), etSenha.text.toString()).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "conta criada!", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(this, "erro ao criar conta", Toast.LENGTH_SHORT).show()
            }}
            catch (e: java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            var intent= Intent(this,LoginConcluido::class.java)
            intent.putExtra("nome",etNome.text.toString())
            startActivity(intent)
        }
        if(v!!.id==R.id.btnAdcEndereco){
            btnApagarCampo.visibility= View.VISIBLE

            val newEditText = EditText(this)
            newEditText.id=View.generateViewId()
            newEditText.hint = "Endereço"
            newEditText.inputType = InputType.TYPE_CLASS_TEXT

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,//width
                LinearLayout.LayoutParams.WRAP_CONTENT //height
            )
            if (editTextContainer.childCount > 0) {
                layoutParams.topMargin = 16 // adiciona um espaço entre os EditTexts
            }
            editTextContainer.addView(newEditText, layoutParams)
            if(editTextContainer.childCount==3){
                btnAdcEndereco.setOnClickListener(null)
                Toast.makeText(this,"Permitido até 3 endereços",Toast.LENGTH_SHORT).show()
            }

        }
        if(v!!.id==R.id.btnApagarCampo){
            if(editTextContainer.childCount!=0) {
                editTextContainer.removeViewAt(editTextContainer.childCount - 1)
            }
            btnAdcEndereco.setOnClickListener(this)
        }
    }
}
