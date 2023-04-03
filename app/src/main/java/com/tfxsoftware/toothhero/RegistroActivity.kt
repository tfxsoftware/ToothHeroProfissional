

package com.tfxsoftware.toothhero


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.google.firebase.FirebaseApp

import com.tfxsoftware.toothhero.databinding.ActivityRegistroBinding
import kotlinx.coroutines.cancel
import okhttp3.internal.wait

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val apiRequests = ApiRequests()



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
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (binding.editTextContainer.childCount > 0) {
                layoutParams.topMargin = 16
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
            binding.btnCriarConta.isActivated = false
            val listaEndereco = mutableListOf<String>()
            for (i in 0 until  binding.editTextContainer.childCount) {
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
                        binding.etCurriculo.text.toString(),
                        listaEndereco)
                    apiRequests.addNovoDentista(dentista)
                    Toast.makeText(this, "Dentista cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                } catch(e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
                finally {

                }
            }

        }
    }






}









