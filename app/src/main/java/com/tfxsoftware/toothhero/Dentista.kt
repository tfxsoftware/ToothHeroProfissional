package com.tfxsoftware.toothhero

data class Dentista(
    var nome: String,
    var email: String,
    var senha: String,
    var cro: String,
    var foto: String? = null
) {
}