package com.example.sistema_de_laboratorio.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.cadastro.CadastroEquipamentoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquipamentosActivity : AppCompatActivity() {

    private lateinit var adapter: EquipamentoAdapter
    private lateinit var busca: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamentos)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEquip)
        busca = findViewById<EditText>(R.id.edtBusca)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddEquip)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = EquipamentoAdapter(
            lista = LaboratorioService.relatorioEquipamentos(),
            onToggleStatus = { id ->
                LaboratorioService.toggleStatusEquipamento(id)
                atualizarLista()
            },
            onEditClick = { equip ->
                val intent = Intent(this, CadastroEquipamentoActivity::class.java)
                intent.putExtra("EQUIP_ID", equip.id)
                startActivity(intent)
            },
            onDeleteClick = { id ->
                LaboratorioService.excluirEquipamento(id)
                atualizarLista()
            }
        )
        recyclerView.adapter = adapter

        busca.addTextChangedListener {
            atualizarLista()
        }

        fabAdd.setOnClickListener {
            startActivity(Intent(this, CadastroEquipamentoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun atualizarLista() {
        val textoBusca = busca.text.toString()
        val resultado = LaboratorioService.buscarEquipamentos(textoBusca)
        adapter.atualizarLista(resultado)
    }
}
