package com.tfxsoftware.toothhero



data class Emergencia(val eid: String?, val nome: String?, val telefone: String?, val fotoambos: String?,
                      val fotocrianca: String?, val fotodoc: String?, val datahora: String?, val status: String?,
                      val latitude: Double?, val longitude: Double?){

    override fun toString(): String{
        return "$eid, $nome, $telefone, $datahora, $status"
    }

}

