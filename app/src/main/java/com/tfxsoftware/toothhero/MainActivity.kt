package com.tfxsoftware.toothhero



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.tfxsoftware.toothhero.databinding.ActivityMainBinding

class MainActivity :AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar!!.hide()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, DentistaActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding.btnCriarConta.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.btnEntrar.setOnClickListener {
            try {
                binding.btnEntrar.isActivated = false
                val email = binding.etEmail.text.toString()
                val senha = binding.etSenha.text.toString()

                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, DentistaActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> "Credenciais inválidos"
                                else -> "Autenticação falhou! Tente novamente mais tarde!"
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }catch(e: Exception){
                binding.btnEntrar.isActivated = true
                Toast.makeText(this, "Preencha ambosos campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
