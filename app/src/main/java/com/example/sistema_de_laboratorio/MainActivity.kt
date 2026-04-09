package com.example.sistema_de_laboratorio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.data.repository.LaboratorioRepository
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.EquipamentosActivity
import com.example.sistema_de_laboratorio.ui.OcorrenciasActivity
import com.example.sistema_de_laboratorio.ui.RelatorioActivity
import com.example.sistema_de_laboratorio.ui.materiais.MateriaisActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dados de teste
        if (LaboratorioService.relatorioEstoque().isEmpty()) {
            LaboratorioService.cadastrarMaterial(
                Material(
                    LaboratorioRepository.gerarMaterialId(),
                    "Álcool 70%",
                    "Uso geral",
                    15,
                    "Armário A",
                    "2024",
                    "Fornecedor X"
                )
            )
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
