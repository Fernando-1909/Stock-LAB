package com.example.sistema_de_laboratorio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Status
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.cadastro.CadastroEquipamentoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class EquipamentosActivity : AppCompatActivity() {

    private lateinit var adapter: EquipamentoAdapter
    private lateinit var busca: EditText
    private lateinit var service: LaboratorioService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamentos)

        service = LaboratorioService(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEquip)
        busca = findViewById<EditText>(R.id.edtBusca)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddEquip)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = EquipamentoAdapter(
            lista = emptyList(),
            onItemClick = { equip ->
                showDetalhesDialog(equip)
            },
            onStatusClick = { equip, view ->
                showStatusPopup(equip, view)
            },
            onEditClick = { equip ->
                val intent = Intent(this, CadastroEquipamentoActivity::class.java)
                intent.putExtra("EQUIP_ID", equip.id)
                startActivity(intent)
            },
            onDeleteClick = { equip ->
                AlertDialog.Builder(this)
                    .setTitle("Excluir Equipamento")
                    .setMessage("Deseja realmente excluir ${equip.nome}?")
                    .setPositiveButton("Sim") { _, _ ->
                        lifecycleScope.launch {
                            service.excluirEquipamento(equip)
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
            startActivity(Intent(this, CadastroEquipamentoActivity::class.java))
        }
    }

    private fun showDetalhesDialog(equip: Equipamento) {
        val mensagem = """
            Nome: ${equip.nome}
            Quantidade: ${equip.quantidade}
            Status: ${equip.status}
            Localização: ${equip.localizacao}
            Nº Série: ${equip.numeroSerie}
            Data Aquisição: ${equip.dataAquisicao}
            Fornecedor: ${equip.fornecedor}
            Descrição: ${equip.descricao}
            Manual: ${equip.manual}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Detalhes do Equipamento")
            .setMessage(mensagem)
            .setPositiveButton("Fechar", null)
            .setNeutralButton("Editar") { _, _ ->
                val intent = Intent(this, CadastroEquipamentoActivity::class.java)
                intent.putExtra("EQUIP_ID", equip.id)
                startActivity(intent)
            }
            .show()
    }

    private fun showStatusPopup(equip: Equipamento, view: View) {
        val popup = PopupMenu(this, view)
        Status.values().forEach { status ->
            popup.menu.add(status.name)
        }
        
        popup.setOnMenuItemClickListener { menuItem ->
            val novoStatus = Status.valueOf(menuItem.title.toString())
            lifecycleScope.launch {
                service.updateStatusEquipamento(equip.id, novoStatus)
                atualizarLista()
            }
            true
        }
        popup.show()
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun atualizarLista() {
        val textoBusca = busca.text.toString()
        lifecycleScope.launch {
            val resultado = service.buscarEquipamentos(textoBusca)
            adapter.atualizarLista(resultado)
        }
    }
}
