package com.tfxsoftware.toothhero



import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tfxsoftware.toothhero.databinding.ActivityMainBinding

class MainActivity :AppCompatActivity(){
    private val auth=FirebaseAuth.getInstance()

    private lateinit var binding: ActivityMainBinding

    //teste1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)
        supportActionBar!!.hide()

            binding.btnEntrar.setOnClickListener {
                hideSoftKeyboard()
                if(binding.etEmail.text.toString().isNotEmpty() && binding.etSenha.text.toString().isNotEmpty()) {
                    auth.signInWithEmailAndPassword(
                        binding.etEmail.text.toString(),
                        binding.etSenha.text.toString()
                    ).addOnCompleteListener { login ->
                        if (login.isSuccessful) {
                            val intent = Intent(this, LoginConcluido::class.java)
                            startActivity(intent)
                        }
                    }.addOnFailureListener { exeption ->
                        val mensagemErro = when (exeption) {
                            is FirebaseAuthInvalidCredentialsException -> "senha incorreta"
                            is FirebaseAuthInvalidUserException -> "usuário não cadastrado"
                            is FirebaseNetworkException -> "Sem conexão a internet"
                            else -> "Erro ao entrar"
                        }
                        val snackbar =
                            Snackbar.make(binding.btnEntrar, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.RED).show()
                    }
                }  else{
                    val snackbar = Snackbar.make(binding.btnEntrar,"Preencha os campos", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED).show()
            }


        }
        binding.btnCriarConta.setOnClickListener {
            val intent = Intent(this, RegistroAcitivty::class.java)
            startActivity(intent)
        }

    }
    private fun navegarTelaPrincipal(){
        val intent=Intent(this, LoginConcluido::class.java)
        startActivity(intent)
        finish() //Se o finish não for adicionado, o usuário (que já está logado) passará rapidamente pela tela de login e já será direcionado para a principal
    }
    override fun onStart() {// toda vez que a aplicação for iniciada, esta função será executada imediatamente
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser // É a instância do usuário.
        if(usuarioAtual!=null){ //Se não for nulo, significa que o usuário já fez o login, portanto ele deve ficar na tela inicial
            navegarTelaPrincipal()

        }
    }
    fun hideSoftKeyboard(){
        val softKeyManager=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        softKeyManager.hideSoftInputFromWindow(binding.etEmail.windowToken,0)
    }


}

