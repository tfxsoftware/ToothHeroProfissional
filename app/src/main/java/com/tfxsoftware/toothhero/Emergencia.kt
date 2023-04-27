package com.tfxsoftware.toothhero

import android.os.Parcel
import android.os.Parcelable

data class Emergencia(val nome: String, val telefone: String, val fotos: String,
                        val datahora: String, val status: String): Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(telefone)
        parcel.writeString(fotos)
        parcel.writeString(datahora)
        parcel.writeString(status)

    }

    override fun describeContents(): Int {
        return 0
    }

    // CREATOR companion object
    companion object CREATOR : Parcelable.Creator<Emergencia> {
        override fun createFromParcel(parcel: Parcel): Emergencia {
            return Emergencia(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                )
        }

        override fun newArray(size: Int): Array<Emergencia?> {
            return arrayOfNulls(size)
        }

    }
}

