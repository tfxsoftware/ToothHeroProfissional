

package com.tfxsoftware.toothhero


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.google.firebase.FirebaseApp
import com.google.gson.Gson

import com.tfxsoftware.toothhero.databinding.ActivityRegistroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class RegistroAcitivty : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val apiRegisterUrl = "https://us-central1-toothhero-4102d.cloudfunctions.net/addNewData"
    private var apiResponse: String? = "teste"
    private val scope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        supportActionBar!!.hide()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnAdcEndereco.setOnClickListener {
            binding.btnApagarCampo.visibility = View.VISIBLE

            val newEditText = EditText(this)
            newEditText.id = View.generateViewId()
            newEditText.hint = "Endereço"
            newEditText.inputType = InputType.TYPE_CLASS_TEXT

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,//width
                LinearLayout.LayoutParams.WRAP_CONTENT //height
            )
            if (binding.editTextContainer.childCount > 0) {
                layoutParams.topMargin = 16 // adiciona um espaço entre os EditTexts
            }
            binding.editTextContainer.addView(newEditText, layoutParams)
            if (binding.editTextContainer.childCount == 3) {
                binding.btnAdcEndereco.setOnClickListener(null)
                Toast.makeText(this, "Permitido até 3 endereços", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnApagarCampo.setOnClickListener {
            if(binding.editTextContainer.childCount!=0) {
                binding.editTextContainer.removeViewAt(binding.editTextContainer.childCount - 1)
            }
        }
        // BOTAO CRIAR CONTA
        binding.btnCriarConta.setOnClickListener {
            val listaEndereco = mutableListOf<String>()
            for (i in 0 until binding.editTextContainer.childCount) {
                val entry = binding.editTextContainer[i] as EditText
                listaEndereco.add(entry.text.toString())
            }
            if (binding.etSenha.text.toString() == binding.etSenhaConfirm.text.toString()){
                try{
                    val dentista = Dentista(
                        binding.etNome.text.toString(),
                        binding.etTelefone.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etSenha.text.toString(),
                        binding.etCRO.text.toString(),
                        binding.etCurriculo.textAlignment.toString(),
                        listaEndereco)
                    scope.launch {
                        try {

                            val response = ApiRequests().addNovoDentista(dentista)
                            Toast.makeText(this@RegistroAcitivty, "Conta Criada com sucesso!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@RegistroAcitivty, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    limparCampos()
                } catch(e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun limparCampos(){
        binding.etNome.text = null
        binding.etTelefone.text = null
        binding.etEmail.text = null
        binding.etSenha.text = null
        binding.etSenhaConfirm.text = null
        binding.etCRO.text = null
        binding.etCurriculo.text = null
        for (i in 0 until binding.editTextContainer.childCount) binding.editTextContainer.removeViewAt(i)
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}









