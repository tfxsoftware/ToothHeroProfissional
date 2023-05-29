package com.tfxsoftware.toothhero

import androidx.core.text.isDigitsOnly

data class Dentista(
    var nome: String,
    var telefone: String,
    var email: String,
    var senha: String,
    var cro: String,
    var curriculo:String,
    var enderecos: MutableList<String>,
    var foto: String?

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
        if (this.foto == null) throw Exception ("Favor adicionar foto!")


    }

    override fun toString(): String {
        return "$nome, $telefone, $email, $senha, $cro, $curriculo"
    }






}