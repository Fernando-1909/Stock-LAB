package com.example.sistema_de_laboratorio.ui.materiais

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.cadastro.CadastroMaterialActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MateriaisActivity : AppCompatActivity() {

    private lateinit var adapter: MaterialAdapter
    private lateinit var busca: EditText
    private lateinit var service: LaboratorioService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materiais)

        service = LaboratorioService(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        busca = findViewById<EditText>(R.id.edtBusca)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = MaterialAdapter(
            lista = emptyList(),
            onItemClick = { material ->
                showDetalhesDialog(material)
            },
            onEditClick = { material ->
                val intent = Intent(this, CadastroMaterialActivity::class.java)
                intent.putExtra("MATERIAL_ID", material.id)
                startActivity(intent)
            },
            onDeleteClick = { material ->
                AlertDialog.Builder(this)
                    .setTitle("Excluir Material")
                    .setMessage("Deseja realmente excluir ${material.nome}?")
                    .setPositiveButton("Sim") { _, _ ->
                        lifecycleScope.launch {
                            service.excluirMaterial(material)
                            atualizarLista()
                        }
                    }
                    .setNegativeButton("Não", null)
                    .show()
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

    private fun showDetalhesDialog(material: Material) {
        val mensagem = """
            Nome: ${material.nome}
            Quantidade: ${material.quantidade}
            Localização: ${material.localizacao}
            Data Aquisição: ${material.dataAquisicao}
            Fornecedor: ${material.fornecedor}
            Descrição: ${material.descricao}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Detalhes do Material")
            .setMessage(mensagem)
            .setPositiveButton("Fechar", null)
            .setNeutralButton("Editar") { _, _ ->
                val intent = Intent(this, CadastroMaterialActivity::class.java)
                intent.putExtra("MATERIAL_ID", material.id)
                startActivity(intent)
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun atualizarLista() {
        val textoBusca = busca.text.toString()
        lifecycleScope.launch {
            val resultado = service.buscarMateriais(textoBusca)
            adapter.atualizarLista(resultado)
        }
    }
}
