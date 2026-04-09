package com.example.sistema_de_laboratorio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService

class OcorrenciasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencias)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOcorrencias)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OcorrenciaAdapter(LaboratorioService.relatorioOcorrencias())
    }
}
