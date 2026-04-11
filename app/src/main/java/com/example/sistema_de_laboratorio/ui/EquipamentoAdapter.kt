package com.example.sistema_de_laboratorio.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Status

class EquipamentoAdapter(
    private var lista: List<Equipamento>,
    private val onToggleStatus: (Int) -> Unit,
    private val onEditClick: (Equipamento) -> Unit,
    private val onDeleteClick: (Equipamento) -> Unit
) : RecyclerView.Adapter<EquipamentoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.txtNome)
        val status: TextView = view.findViewById(R.id.txtStatus)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val btnToggle: ImageButton = view.findViewById(R.id.btnToggleStatus)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipamento, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.nome.text = item.nome
        holder.status.text = "Status: ${item.status}"
        
        val icon = if (item.status == Status.DISPONIVEL) 
            android.R.drawable.presence_online else android.R.drawable.presence_busy
        holder.btnToggle.setImageResource(icon)

        holder.btnToggle.setOnClickListener { onToggleStatus(item.id) }
        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    fun atualizarLista(novaLista: List<Equipamento>) {
        lista = novaLista
        notifyDataSetChanged()
    }
}
