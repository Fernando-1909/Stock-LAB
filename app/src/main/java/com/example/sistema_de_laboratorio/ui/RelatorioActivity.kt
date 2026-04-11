package com.example.sistema_de_laboratorio.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import kotlinx.coroutines.launch

class RelatorioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio)

        val service = LaboratorioService(this)
        val txtRelatorio = findViewById<TextView>(R.id.txtRelatorio)
        
        lifecycleScope.launch {
            val materiais = service.relatorioEstoque()
            val equipamentos = service.relatorioEquipamentos()
            
            val relatorio = StringBuilder()
            relatorio.append("Resumo do Estoque:\n")
            materiais.forEach {
                relatorio.append("${it.nome}: ${it.quantidade} unidades\n")
            }
            
            relatorio.append("\nEquipamentos:\n")
            equipamentos.forEach {
                relatorio.append("${it.nome}: ${it.status}\n")
            }

            txtRelatorio.text = relatorio.toString()
        }
    }
}
