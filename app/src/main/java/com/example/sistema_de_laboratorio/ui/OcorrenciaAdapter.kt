package com.example.sistema_de_laboratorio.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Ocorrencia

class OcorrenciaAdapter(private val lista: List<Ocorrencia>) :
    RecyclerView.Adapter<OcorrenciaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descricao: TextView = view.findViewById(R.id.txtDescricao)
        val data: TextView = view.findViewById(R.id.txtData)
        val responsavel: TextView = view.findViewById(R.id.txtResponsavel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ocorrencia, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.descricao.text = item.descricao
        holder.data.text = item.data
        holder.responsavel.text = "Por: ${item.responsavel}"
    }
}