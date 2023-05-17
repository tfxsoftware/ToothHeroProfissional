package com.tfxsoftware.recyclelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfxsoftware.toothhero.EmergenciaL
import com.tfxsoftware.toothhero.EmergenciasViewHolder
import com.tfxsoftware.toothhero.R


class EmergenciasAdapter(private val emergenciasList: List<EmergenciaL>) : RecyclerView.Adapter<EmergenciasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergenciasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emergencia_item, parent, false)
        return EmergenciasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emergenciasList.size
    }

    override fun onBindViewHolder(holder: EmergenciasViewHolder, position: Int) {
        val item: EmergenciaL = emergenciasList[position]
        holder.bind(item)
    }

    fun notifyAdapter(list:List<EmergenciaL>){
        notifyDataSetChanged()
    }
}