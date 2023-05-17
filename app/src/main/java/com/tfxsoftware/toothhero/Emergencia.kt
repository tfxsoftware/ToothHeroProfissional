package com.tfxsoftware.toothhero



data class Emergencia(val eid: String?, val nome: String?, val telefone: String?, val fotos: String?,
                        val datahora: String?, val status: String?){

    override fun toString(): String{
        return "$eid, $nome, $telefone, $fotos, $datahora, $status"
    }

}


data class EmergenciaL(val nome: String?, val telefone: String?, val fotos: String?,
                      val datahora: String?, val status: String?)

