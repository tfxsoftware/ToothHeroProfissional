package com.tfxsoftware.toothhero

import android.text.TextUtils
import android.util.Patterns
import androidx.core.text.isDigitsOnly


data class Dentista(
    var nome: String,
    var telefone: String,
    var email: String,
    var senha: String,
    var cro: String,
    var curriculo:String,
    var enderecos: MutableList<String>,
    var foto: String? = null,
    var id: String? = null

) {
    init{
        if (nome.isEmpty() ||
            telefone.isEmpty() ||
            email.isEmpty() ||
            cro.isEmpty() ||
            curriculo.isEmpty() ||
            enderecos.isEmpty()
        ) throw Exception ("Preencha todos campos")
        if (!this.cro.isDigitsOnly()) throw Exception("Cro Inválido")
        if (this.senha.length < 6) throw Exception ("Senha muito curta!")
        if(isValidEmail(email)==false) throw Exception("Email inválido")
        id = email



    }

    override fun toString(): String {
        return enderecos[1]
    }




}

private fun isValidEmail(email: CharSequence): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}