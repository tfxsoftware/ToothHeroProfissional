package com.tfxsoftware.toothhero

import android.os.Parcel
import android.os.Parcelable

data class Emergencia(val eid: String?, val nome: String?, val telefone: String?, val fotos: String?,
                        val datahora: String?, val status: String?){

    override fun toString(): String{
        return "$eid, $nome, $telefone, $fotos, $datahora, $status"
    }

}

