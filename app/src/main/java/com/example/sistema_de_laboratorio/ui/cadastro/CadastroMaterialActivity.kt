package com.example.sistema_de_laboratorio.ui.cadastro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import kotlinx.coroutines.launch

class CadastroMaterialActivity : AppCompatActivity() {

    private var materialId: Int = -1
    private lateinit var service: LaboratorioService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_material)

        service = LaboratorioService(this)

        val txtTitulo = findViewById<TextView>(R.id.txtTituloCadastro)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtQuantidade = findViewById<EditText>(R.id.edtQuantidade)
        val edtDescricao = findViewById<EditText>(R.id.edtDescricao)
        val edtLocalizacao = findViewById<EditText>(R.id.edtLocalizacao)
        val edtDataAquisicao = findViewById<EditText>(R.id.edtDataAquisicao)
        val edtFornecedor = findViewById<EditText>(R.id.edtFornecedor)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        materialId = intent.getIntExtra("MATERIAL_ID", -1)

        if (materialId != -1) {
            txtTitulo.text = "Editar Material"
            btnSalvar.text = "Atualizar Material"
            lifecycleScope.launch {
                val materiais = service.buscarMateriais()
                val material = materiais.find { it.id == materialId }
                material?.let {
                    edtNome.setText(it.nome)
                    edtQuantidade.setText(it.quantidade.toString())
                    edtDescricao.setText(it.descricao)
                    edtLocalizacao.setText(it.localizacao)
                    edtDataAquisicao.setText(it.dataAquisicao)
                    edtFornecedor.setText(it.fornecedor)
                }
            }
        }

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val qtdStr = edtQuantidade.text.toString()
            val descricao = edtDescricao.text.toString()
            val localizacao = edtLocalizacao.text.toString()
            val dataAquisicao = edtDataAquisicao.text.toString()
            val fornecedor = edtFornecedor.text.toString()

            if (nome.isNotBlank() && qtdStr.isNotBlank()) {
                try {
                    val material = Material(
                        id = if (materialId != -1) materialId else 0,
                        nome = nome,
                        quantidade = qtdStr.toInt(),
                        descricao = descricao,
                        localizacao = localizacao,
                        dataAquisicao = dataAquisicao,
                        fornecedor = fornecedor
                    )
                    
                    lifecycleScope.launch {
                        if (materialId != -1) {
                            service.editarMaterial(material)
                        } else {
                            service.cadastrarMaterial(material)
                        }
                        Toast.makeText(this@CadastroMaterialActivity, "Sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nome e Quantidade são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
