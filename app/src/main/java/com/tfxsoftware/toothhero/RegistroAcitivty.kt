
package com.tfxsoftware.toothhero


import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.tfxsoftware.toothhero.databinding.ActivityRegistroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RegistroAcitivty : AppCompatActivity() ,View.OnClickListener {

    private lateinit var binding: ActivityRegistroBinding
    private val auth=FirebaseAuth.getInstance()
    private val apiRegisterUrl = "https://us-central1-toothhero-4102d.cloudfunctions.net/addNewData"
    private var apiResponse: String? = "teste"
    private val scope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnAdcEndereco.setOnClickListener(this)
        binding.btnApagarCampo.setOnClickListener(this)


        // BOTAO CRIAR CONTA
        binding.btnCriarConta.setOnClickListener {
            hideSoftKeyboard()

            val listaEndereco = mutableListOf<String>()
            for (i in 0 until binding.editTextContainer.childCount) {
                val entry = binding.editTextContainer[i] as EditText
                listaEndereco.add(entry.text.toString())
            }
            if (binding.etSenha.text.toString() == binding.etSenhaConfirm.text.toString()){
                try {
                    val email=binding.etEmail.text.toString()
                    val senha=binding.etSenha.text.toString()
                    //Cadastrando usuário no Authentication
                    auth.createUserWithEmailAndPassword(email,senha)
                        .addOnCompleteListener { cadastro ->
                            if (cadastro.isSuccessful) {
                                val snackbar = Snackbar.make(binding.etNome, "Sucesso ao cadastrar usuário", Snackbar.LENGTH_SHORT)
                                snackbar.setBackgroundTint(Color.BLUE)
                                snackbar.show()
                            }
                        }.addOnFailureListener { exception ->
                            val mensagemErro = when (exception) {
                                is FirebaseAuthWeakPasswordException -> "A senha deve possuir no mínimo 6 caracteres"
                                is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada"
                                is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
                                is FirebaseNetworkException -> "Sem conexão a internet"
                                else -> "Erro ao criar conta"

                            }
                            val snackbar = Snackbar.make(binding.etNome, mensagemErro, Snackbar.LENGTH_LONG)
                            snackbar.setBackgroundTint(Color.RED)
                            snackbar.show()


                        }
                }catch (e:java.lang.Exception){
                    android.widget.Toast.makeText(this, "ocorreu algum erro", android.widget.Toast.LENGTH_SHORT).show()
                }
                try{
                    val dentista = Dentista(
                        binding.etNome.text.toString(),
                        binding.etTelefone.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etSenha.text.toString(),
                        binding.etCRO.text.toString(),
                        binding.etCurriculo.text.toString(),
                        listaEndereco)
                    scope.launch {
                        try {
                            //Adicionando instância ao firestore
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


            }else{
                Toast.makeText(this,"As senhas devem ser iguais",Toast.LENGTH_SHORT).show()
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
    fun hideSoftKeyboard(){
        val softKeyManager=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        softKeyManager.hideSoftInputFromWindow(binding.etNome.windowToken,0)
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onClick(v: View?) {
        if(v!!.id==binding.btnAdcEndereco.id){
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
        if(v!!.id==binding.btnApagarCampo.id){
            if(binding.editTextContainer.childCount!=0) {
                binding.editTextContainer.removeViewAt(binding.editTextContainer.childCount - 1)
                binding.btnAdcEndereco.setOnClickListener(this)

        }
    }



}
}









