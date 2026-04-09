package com.example.sistema_de_laboratorio.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService

class RelatorioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio)

        val txtRelatorio = findViewById<TextView>(R.id.txtRelatorio)
        
        val relatorio = StringBuilder()
        relatorio.append("Resumo do Estoque:\n")
        LaboratorioService.relatorioEstoque().forEach {
            relatorio.append("${it.nome}: ${it.quantidade} unidades\n")
        }
        
        relatorio.append("\nEquipamentos:\n")
        LaboratorioService.relatorioEquipamentos().forEach {
            relatorio.append("${it.nome}: ${it.status}\n")
        }

        txtRelatorio.text = relatorio.toString()
    }
}
