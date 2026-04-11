package com.example.sistema_de_laboratorio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import kotlinx.coroutines.launch

class OcorrenciasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocorrencias)

        val service = LaboratorioService(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOcorrencias)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        lifecycleScope.launch {
            val ocorrencias = service.relatorioOcorrencias()
            recyclerView.adapter = OcorrenciaAdapter(ocorrencias)
        }
    }
}
