package com.tfxsoftware.toothhero

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmergenciasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val contactName: TextView = itemView.findViewById(R.id.emergencia_nome)
    private val contactNumber: TextView = itemView.findViewById(R.id.emergencia_data)

    fun bind (emergencia: Emergencia, listener: (Emergencia) -> Unit){
        itemView.setOnClickListener { listener(emergencia) }
        contactName.text = emergencia.nome
        contactNumber.text = emergencia.datahora
    }
}