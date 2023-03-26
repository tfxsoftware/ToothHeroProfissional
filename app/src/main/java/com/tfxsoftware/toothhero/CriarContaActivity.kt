package com.tfxsoftware.toothhero


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children

class CriarContaActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnCriarConta:AppCompatButton
    lateinit var btnAdcEndereco:AppCompatButton
    lateinit var etNome: AppCompatEditText
    lateinit var btnApagarCampo:AppCompatButton
    lateinit var editTextContainer: LinearLayout
    lateinit var etTelefone: AppCompatEditText
    lateinit var etCro: AppCompatEditText
    lateinit var etEmail: AppCompatEditText
    lateinit var etSenha: AppCompatEditText
    lateinit var etConfirmarSenha: AppCompatEditText
    lateinit var etEndereco2: AppCompatEditText
    lateinit var etEndereco3: AppCompatEditText
    lateinit var etCurriculo: AppCompatEditText




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)
        supportActionBar!!.hide()

        editTextContainer = findViewById<LinearLayout>(R.id.editTextContainer)
        btnCriarConta=findViewById(R.id.btnCriarConta)
        btnAdcEndereco=findViewById(R.id.btnAdcEndereco)
        btnApagarCampo=findViewById(R.id.btnApagarCampo)
        etNome=findViewById(R.id.etNome)
        etTelefone=findViewById(R.id.etTelefone)
        etCro=findViewById(R.id.etCRO)
        etEmail=findViewById(R.id.etEmail)
        etSenha=findViewById(R.id.etSenha)
        etConfirmarSenha=findViewById(R.id.etSenha)
        etCurriculo=findViewById(R.id.etCurriculo)



        btnAdcEndereco.setOnClickListener(this)
        btnCriarConta.setOnClickListener(this)
        btnApagarCampo.setOnClickListener(this)



    }

    override fun onClick(v:View?){

        if(v!!.id==R.id.btnCriarConta){
            if(etSenha.text.toString() == etConfirmarSenha.text.toString()) {
                var enderecos:MutableList<String> = mutableListOf<String>()
                for (i in 0 until editTextContainer.childCount){
                    var id = editTextContainer.getChildAt(i).id
                    var endereco = findViewById<EditText>(id).text.toString()
                    enderecos.add(i, endereco)
                }
                try{
                    val novoDentista = Dentista(
                        etNome.text.toString(),
                        etTelefone.text.toString(),
                        etEmail.text.toString(),
                        etSenha.text.toString(),
                        etCro.text.toString(),
                        etCurriculo.text.toString(),
                        enderecos)

                    var intent = Intent(this, LoginConcluido::class.java)
                    intent.putExtra("nome", etNome.text.toString())
                    startActivity(intent)
                    Toast.makeText(this, novoDentista.enderecos[1], Toast.LENGTH_SHORT).show()
                }
                catch(e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            }
            else Toast.makeText(this, "Confirmação de senha errada", Toast.LENGTH_SHORT).show()
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