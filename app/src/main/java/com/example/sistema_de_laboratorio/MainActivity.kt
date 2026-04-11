package com.example.sistema_de_laboratorio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.EquipamentosActivity
import com.example.sistema_de_laboratorio.ui.OcorrenciasActivity
import com.example.sistema_de_laboratorio.ui.RelatorioActivity
import com.example.sistema_de_laboratorio.ui.materiais.MateriaisActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = LaboratorioService(this)

        // Dados de teste (opcional, apenas se o banco estiver vazio)
        lifecycleScope.launch {
            if (service.relatorioEstoque().isEmpty()) {
                service.cadastrarMaterial(
                    Material(
                        nome = "Álcool 70%",
                        quantidade = 15,
                        descricao = "Uso geral",
                        localizacao = "Armário A",
                        dataAquisicao = "2024",
                        fornecedor = "Fornecedor X"
                    )
                )
            }
        }

        findViewById<Button>(R.id.btnMateriais).setOnClickListener {
            startActivity(Intent(this, MateriaisActivity::class.java))
        }

        findViewById<Button>(R.id.btnEquipamentos).setOnClickListener {
            startActivity(Intent(this, EquipamentosActivity::class.java))
        }

        findViewById<Button>(R.id.btnRelatorios).setOnClickListener {
            startActivity(Intent(this, RelatorioActivity::class.java))
        }

        findViewById<Button>(R.id.btnOcorrencias).setOnClickListener {
            startActivity(Intent(this, OcorrenciasActivity::class.java))
        }
    }
}
