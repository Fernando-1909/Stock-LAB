package com.example.sistema_de_laboratorio.ui.cadastro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Status
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import kotlinx.coroutines.launch

class CadastroEquipamentoActivity : AppCompatActivity() {

    private var equipId: Int = -1
    private lateinit var service: LaboratorioService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_equipamento)

        service = LaboratorioService(this)

        val txtTitulo = findViewById<TextView>(R.id.txtTituloCadastro)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtQuantidade = findViewById<EditText>(R.id.edtQuantidade)
        val edtDescricao = findViewById<EditText>(R.id.edtDescricao)
        val edtSerie = findViewById<EditText>(R.id.edtSerie)
        val edtLocalizacao = findViewById<EditText>(R.id.edtLocalizacao)
        val edtDataAquisicao = findViewById<EditText>(R.id.edtDataAquisicao)
        val edtFornecedor = findViewById<EditText>(R.id.edtFornecedor)
        val edtManual = findViewById<EditText>(R.id.edtManual)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        equipId = intent.getIntExtra("EQUIP_ID", -1)

        if (equipId != -1) {
            txtTitulo.text = "Editar Equipamento"
            btnSalvar.text = "Atualizar Equipamento"
            lifecycleScope.launch {
                val equipamentos = service.buscarEquipamentos()
                val equip = equipamentos.find { it.id == equipId }
                equip?.let {
                    edtNome.setText(it.nome)
                    edtQuantidade.setText(it.quantidade.toString())
                    edtDescricao.setText(it.descricao)
                    edtSerie.setText(it.numeroSerie)
                    edtLocalizacao.setText(it.localizacao)
                    edtDataAquisicao.setText(it.dataAquisicao)
                    edtFornecedor.setText(it.fornecedor)
                    edtManual.setText(it.manual)
                }
            }
        }

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val qtdStr = edtQuantidade.text.toString()
            val descricao = edtDescricao.text.toString()
            val serie = edtSerie.text.toString()
            val localizacao = edtLocalizacao.text.toString()
            val dataAquisicao = edtDataAquisicao.text.toString()
            val fornecedor = edtFornecedor.text.toString()
            val manual = edtManual.text.toString()

            if (nome.isNotBlank() && qtdStr.isNotBlank() && localizacao.isNotBlank()) {
                try {
                    lifecycleScope.launch {
                        val equipExistente = if (equipId != -1) {
                            service.buscarEquipamentos().find { it.id == equipId }
                        } else null

                        val equipamento = Equipamento(
                            id = if (equipId != -1) equipId else 0,
                            nome = nome,
                            quantidade = qtdStr.toInt(),
                            descricao = descricao,
                            numeroSerie = serie,
                            localizacao = localizacao,
                            dataAquisicao = dataAquisicao,
                            fornecedor = fornecedor,
                            manual = manual,
                            status = equipExistente?.status ?: Status.DISPONIVEL
                        )
                        
                        if (equipId != -1) {
                            service.editarEquipamento(equipamento)
                        } else {
                            service.cadastrarEquipamento(equipamento)
                        }
                        
                        Toast.makeText(this@CadastroEquipamentoActivity, "Sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nome, Quantidade e Localização são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
