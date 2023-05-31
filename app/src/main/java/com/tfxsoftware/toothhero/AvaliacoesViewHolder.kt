package com.tfxsoftware.toothhero

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AvaliacoesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val avaliacaoComentario: TextView = itemView.findViewById(R.id.avaliacao_comentario)
    private val avaliacaoNota: TextView = itemView.findViewById(R.id.avaliacao_nota)

    fun bind (avaliacao: Avaliacao, listener: (Avaliacao) -> Unit){
        itemView.setOnClickListener { listener(avaliacao) }
        avaliacaoComentario.text = avaliacao.comentario
        avaliacaoNota.text = avaliacao.nota.toString()
    }
}