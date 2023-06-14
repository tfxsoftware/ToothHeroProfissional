package com.tfxsoftware.toothhero

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AvaliacoesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val avaliacaoComentario: TextView = itemView.findViewById(R.id.avaliacao_comentario)
    private val avaliacaoNota: TextView = itemView.findViewById(R.id.avaliacao_nota)
    private val botaoDisputa: Button = itemView.findViewById(R.id.btnreportar)
    private val avaliacaoNome: TextView = itemView.findViewById(R.id.avaliacao_nome)

    fun bind (avaliacao: Avaliacao, listener: (Avaliacao) -> Unit){
        itemView.setOnClickListener { listener(avaliacao) }
        avaliacaoNome.text = avaliacao.nomeSocorrista
        avaliacaoComentario.text = avaliacao.comentario
        avaliacaoNota.text = avaliacao.nota.toString()
        botaoDisputa.setOnClickListener {
            val intent = Intent(itemView.context, DisputaActivity::class.java)
            intent.putExtra("aid", avaliacao.aid)
            intent.putExtra("nome", avaliacao.nomeSocorrista)
            intent.putExtra("comentario", avaliacao.comentario)
            itemView.context.startActivity(intent)
        }

    }
}