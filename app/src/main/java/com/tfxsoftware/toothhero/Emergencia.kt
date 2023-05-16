package com.tfxsoftware.toothhero



data class Emergencia(val eid: String?, val nome: String?, val telefone: String?, val fotos: String?,
                        val datahora: String?, val status: String?){
    constructor(
        nome: String?,
        telefone: String?,
        fotos: String?,
        datahora: String?,
        status: String?
    ) : this(null, nome, telefone, fotos, datahora, status)
    override fun toString(): String{
        return "$eid, $nome, $telefone, $fotos, $datahora, $status"
    }

}

