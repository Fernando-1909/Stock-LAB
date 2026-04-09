package com.example.sistema_de_laboratorio.ui.materiais

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.cadastro.CadastroMaterialActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MateriaisActivity : AppCompatActivity() {

    private lateinit var adapter: MaterialAdapter
    private lateinit var busca: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materiais)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        busca = findViewById<EditText>(R.id.edtBusca)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = MaterialAdapter(
            lista = LaboratorioService.relatorioEstoque(),
            onEditClick = { material ->
                val intent = Intent(this, CadastroMaterialActivity::class.java)
                intent.putExtra("MATERIAL_ID", material.id)
                startActivity(intent)
            },
            onDeleteClick = { id ->
                LaboratorioService.excluirMaterial(id)
                atualizarLista()
            }
        )
        recyclerView.adapter = adapter

        busca.addTextChangedListener {
            atualizarLista()
        }

        fabAdd.setOnClickListener {
            startActivity(Intent(this, CadastroMaterialActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun atualizarLista() {
        val textoBusca = busca.text.toString()
        val resultado = LaboratorioService.buscarMateriais(textoBusca)
        adapter.atualizarLista(resultado)
    }
}
