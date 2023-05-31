package com.tfxsoftware.toothhero

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmergenciasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val emergenciaNome: TextView = itemView.findViewById(R.id.emergencia_nome)
    private val emergenciaData: TextView = itemView.findViewById(R.id.emergencia_data)

    fun bind (emergencia: Emergencia, listener: (Emergencia) -> Unit){
        itemView.setOnClickListener { listener(emergencia) }
        emergenciaNome.text = emergencia.nome
        emergenciaData.text = emergencia.datahora
    }
}