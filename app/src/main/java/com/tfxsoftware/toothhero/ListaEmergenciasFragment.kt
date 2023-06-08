package com.tfxsoftware.toothhero

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfxsoftware.toothhero.EmergenciasAdapter


class ListaEmergenciasFragment : Fragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista_emergencias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRequests().findEmergencias {
            if (it != null) {
                val recyclerView = view.findViewById<RecyclerView>(R.id.recylerView)
                recyclerView.adapter = EmergenciasAdapter(it){ emergencia ->
                    val intent = Intent(requireContext(), EmergenciaActivity::class.java)
                    intent.putExtra("eid", emergencia.eid.toString())
                    intent.putExtra("nome", emergencia.nome.toString())
                    intent.putExtra("telefone", emergencia.telefone.toString())
                    intent.putExtra("datahora", emergencia.datahora.toString())
                    intent.putExtra("fotoambos", emergencia.fotoambos.toString())
                    intent.putExtra("fotocrianca", emergencia.fotocrianca.toString())
                    intent.putExtra("fotodoc", emergencia.fotodoc.toString())
                    intent.putExtra("status", emergencia.status.toString())
                    startActivity(intent)
                }

            }
        }



    }
}