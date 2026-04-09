package com.example.sistema_de_laboratorio.ui.cadastro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.data.repository.LaboratorioRepository
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService

class CadastroMaterialActivity : AppCompatActivity() {

    private var materialId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_material)

        val txtTitulo = findViewById<TextView>(R.id.txtTituloCadastro)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtQuantidade = findViewById<EditText>(R.id.edtQuantidade)
        val edtLocalizacao = findViewById<EditText>(R.id.edtLocalizacao)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        materialId = intent.getIntExtra("MATERIAL_ID", -1)

        if (materialId != -1) {
            txtTitulo.text = "Editar Material"
            btnSalvar.text = "Atualizar Material"
            val material = LaboratorioRepository.materiais.find { it.id == materialId }
            material?.let {
                edtNome.setText(it.nome)
                edtQuantidade.setText(it.quantidade.toString())
                edtLocalizacao.setText(it.localizacao)
            }
        }

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val qtdStr = edtQuantidade.text.toString()
            val localizacao = edtLocalizacao.text.toString()

            if (nome.isNotBlank() && qtdStr.isNotBlank()) {
                try {
                    val material = Material(
                        id = if (materialId != -1) materialId else LaboratorioRepository.gerarMaterialId(),
                        nome = nome,
                        descricao = "",
                        quantidade = qtdStr.toInt(),
                        localizacao = localizacao,
                        dataAquisicao = "",
                        fornecedor = ""
                    )
                    
                    if (materialId != -1) {
                        LaboratorioService.editarMaterial(material)
                    } else {
                        LaboratorioService.cadastrarMaterial(material)
                    }
                    
                    Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}