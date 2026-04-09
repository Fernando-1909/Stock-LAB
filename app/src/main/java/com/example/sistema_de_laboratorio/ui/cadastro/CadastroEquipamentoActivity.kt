package com.example.sistema_de_laboratorio.ui.cadastro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistema_de_laboratorio.R
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Status
import com.example.sistema_de_laboratorio.data.repository.LaboratorioRepository
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService

class CadastroEquipamentoActivity : AppCompatActivity() {

    private var equipId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_equipamento)

        val txtTitulo = findViewById<TextView>(R.id.txtTituloCadastro)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtSerie = findViewById<EditText>(R.id.edtSerie)
        val edtLocalizacao = findViewById<EditText>(R.id.edtLocalizacao)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        equipId = intent.getIntExtra("EQUIP_ID", -1)

        if (equipId != -1) {
            txtTitulo.text = "Editar Equipamento"
            btnSalvar.text = "Atualizar Equipamento"
            val equip = LaboratorioRepository.equipamentos.find { it.id == equipId }
            equip?.let {
                edtNome.setText(it.nome)
                edtSerie.setText(it.numeroSerie)
                edtLocalizacao.setText(it.localizacao)
            }
        }

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val serie = edtSerie.text.toString()
            val localizacao = edtLocalizacao.text.toString()

            if (nome.isNotBlank()) {
                val equipExistente = LaboratorioRepository.equipamentos.find { it.id == equipId }
                
                val equipamento = Equipamento(
                    id = if (equipId != -1) equipId else LaboratorioRepository.gerarEquipamentoId(),
                    nome = nome,
                    descricao = equipExistente?.descricao ?: "",
                    numeroSerie = serie,
                    localizacao = localizacao,
                    dataAquisicao = equipExistente?.dataAquisicao ?: "",
                    fornecedor = equipExistente?.fornecedor ?: "",
                    manual = equipExistente?.manual ?: "",
                    status = equipExistente?.status ?: Status.DISPONIVEL
                )
                
                if (equipId != -1) {
                    LaboratorioService.editarEquipamento(equipamento)
                } else {
                    LaboratorioService.cadastrarEquipamento(equipamento)
                }
                
                Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "O nome é obrigatório", Toast.LENGTH_SHORT).show()
            }
        }
    }
}