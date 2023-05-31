package com.tfxsoftware.toothhero
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView



class AvalicoesAdapter(private val avaliacoesList: List<Avaliacao>, private val clickListener: (Avaliacao) -> Unit) : RecyclerView.Adapter<AvaliacoesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacoesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.avaliacao_item, parent, false)
        return AvaliacoesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return avaliacoesList.size
    }

    override fun onBindViewHolder(holder: AvaliacoesViewHolder, position: Int) {
        val item: Avaliacao = avaliacoesList[position]
        holder.bind(item, clickListener)
    }

    fun notifyAdapter(list:List<Emergencia>){
        notifyDataSetChanged()
    }
}