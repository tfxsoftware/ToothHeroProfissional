package com.tfxsoftware.toothhero

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ListaAvaliacoesFragment : Fragment() {
    private var dentista: Dentista? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_avaliacoes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nota = view.findViewById<TextView>(R.id.tvNota)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        ApiRequests().getDentista(FirebaseAuth.getInstance().currentUser?.uid) { dentista ->
            this.dentista = dentista
            val ratingValue = dentista?.nota?.toFloat() ?: 0.0f
            nota.text = ratingValue.toString()
            ratingBar.rating = ratingValue
        }
        ApiRequests().findAvaliacoes(FirebaseAuth.getInstance().currentUser?.uid) { avaliacoes ->
            if (avaliacoes != null) {
                val recyclerView = view.findViewById<RecyclerView>(R.id.recylerView1)
                recyclerView.adapter = AvalicoesAdapter(avaliacoes) { avaliacao ->

                }
            }
        }
    }
}
