package com.tfxsoftware.toothhero

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView



class EmergenciasAdapter(private val emergenciasList: List<Emergencia>, private val clickListener: (Emergencia) -> Unit) : RecyclerView.Adapter<EmergenciasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergenciasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emergencia_item, parent, false)
        return EmergenciasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emergenciasList.size
    }

    override fun onBindViewHolder(holder: EmergenciasViewHolder, position: Int) {
        val item: Emergencia = emergenciasList[position]
        holder.bind(item, clickListener)
    }

    fun notifyAdapter(list:List<Emergencia>){
        notifyDataSetChanged()
    }
}