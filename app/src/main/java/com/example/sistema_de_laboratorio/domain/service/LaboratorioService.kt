package com.example.sistema_de_laboratorio.domain.service

import android.content.Context
import com.example.sistema_de_laboratorio.data.local.AppDatabase
import com.example.sistema_de_laboratorio.data.model.*
import java.text.SimpleDateFormat
import java.util.*

class LaboratorioService(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val materialDao = db.materialDao()
    private val equipamentoDao = db.equipamentoDao()
    private val ocorrenciaDao = db.ocorrenciaDao()

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private suspend fun logOcorrencia(itemId: Int, acao: String, descricao: String) {
        val ocorrencia = Ocorrencia(
            itemId = itemId,
            descricao = "[$acao] $descricao",
            data = getCurrentDate(),
            responsavel = "Sistema"
        )
        ocorrenciaDao.insert(ocorrencia)
    }

    // =========================
    // MATERIAIS
    // =========================

    suspend fun cadastrarMaterial(material: Material) {
        materialDao.insert(material)
        logOcorrencia(0, "CADASTRO", "Material criado: ${material.nome} (Qtd: ${material.quantidade})")
    }

    suspend fun editarMaterial(novoMaterial: Material) {
        val antigo = materialDao.getById(novoMaterial.id)
        if (antigo != null) {
            val mudancas = mutableListOf<String>()
            if (antigo.nome != novoMaterial.nome) mudancas.add("Nome: '${antigo.nome}' -> '${novoMaterial.nome}'")
            if (antigo.quantidade != novoMaterial.quantidade) mudancas.add("Qtd: ${antigo.quantidade} -> ${novoMaterial.quantidade}")
            if (antigo.descricao != novoMaterial.descricao) mudancas.add("Desc: '${antigo.descricao}' -> '${novoMaterial.descricao}'")
            if (antigo.localizacao != novoMaterial.localizacao) mudancas.add("Local: '${antigo.localizacao}' -> '${novoMaterial.localizacao}'")
            if (antigo.fornecedor != novoMaterial.fornecedor) mudancas.add("Forn: '${antigo.fornecedor}' -> '${novoMaterial.fornecedor}'")

            val desc = if (mudancas.isEmpty()) "Nenhuma alteração detectada" else mudancas.joinToString(", ")
            logOcorrencia(novoMaterial.id, "EDIÇÃO", "Material '${antigo.nome}': $desc")
        }
        materialDao.update(novoMaterial)
    }

    suspend fun excluirMaterial(material: Material) {
        logOcorrencia(material.id, "EXCLUSÃO", "Material removido: ${material.nome}")
        materialDao.delete(material)
    }

    suspend fun buscarMateriais(nome: String? = null, localizacao: String? = null): List<Material> {
        var result = materialDao.getAll()
        
        if (!nome.isNullOrBlank()) {
            result = result.filter { it.nome.contains(nome, true) }
        }
        
        if (!localizacao.isNullOrBlank()) {
            result = result.filter { it.localizacao.equals(localizacao, true) }
        }
        
        return result
    }

    suspend fun buscarTodasLocalizacoesMateriais(): List<String> {
        return materialDao.getAll()
            .map { it.localizacao.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .sortedBy { it.lowercase() }
    }

    // =========================
    // EQUIPAMENTOS
    // =========================

    suspend fun cadastrarEquipamento(equipamento: Equipamento) {
        equipamentoDao.insert(equipamento)
        logOcorrencia(0, "CADASTRO", "Equipamento criado: ${equipamento.nome}")
    }

    suspend fun editarEquipamento(novoEquip: Equipamento) {
        val antigo = equipamentoDao.getById(novoEquip.id)
        if (antigo != null) {
            val mudancas = mutableListOf<String>()
            if (antigo.nome != novoEquip.nome) mudancas.add("Nome: '${antigo.nome}' -> '${novoEquip.nome}'")
            if (antigo.quantidade != novoEquip.quantidade) mudancas.add("Qtd: ${antigo.quantidade} -> ${novoEquip.quantidade}")
            if (antigo.status != novoEquip.status) mudancas.add("Status: ${antigo.status} -> ${novoEquip.status}")
            if (antigo.numeroSerie != novoEquip.numeroSerie) mudancas.add("Série: '${antigo.numeroSerie}' -> '${novoEquip.numeroSerie}'")
            if (antigo.localizacao != novoEquip.localizacao) mudancas.add("Local: '${antigo.localizacao}' -> '${novoEquip.localizacao}'")

            val desc = if (mudancas.isEmpty()) "Nenhuma alteração detectada" else mudancas.joinToString(", ")
            logOcorrencia(novoEquip.id, "EDIÇÃO", "Equipamento '${antigo.nome}': $desc")
        }
        equipamentoDao.update(novoEquip)
    }

    suspend fun updateStatusEquipamento(id: Int, novoStatus: Status) {
        val equipamento = equipamentoDao.getById(id)
        equipamento?.let {
            val statusAntigo = it.status
            if (statusAntigo != novoStatus) {
                it.status = novoStatus
                equipamentoDao.update(it)
                logOcorrencia(id, "STATUS", "Equipamento '${it.nome}': Status alterado de $statusAntigo para $novoStatus")
            }
        }
    }

    suspend fun toggleStatusEquipamento(id: Int) {
        val equipamento = equipamentoDao.getById(id)
        equipamento?.let {
            val novoStatus = when (it.status) {
                Status.DISPONIVEL -> Status.INDISPONIVEL
                Status.INDISPONIVEL -> Status.MANUTENCAO
                Status.MANUTENCAO -> Status.DISPONIVEL
            }
            updateStatusEquipamento(id, novoStatus)
        }
    }

    suspend fun excluirEquipamento(equipamento: Equipamento) {
        logOcorrencia(equipamento.id, "EXCLUSÃO", "Equipamento removido: ${equipamento.nome}")
        equipamentoDao.delete(equipamento)
    }

    suspend fun buscarEquipamentos(nome: String? = null, localizacao: String? = null): List<Equipamento> {
        var result = equipamentoDao.getAll()
        
        if (!nome.isNullOrBlank()) {
            result = result.filter { it.nome.contains(nome, true) }
        }
        
        if (!localizacao.isNullOrBlank()) {
            result = result.filter { it.localizacao.equals(localizacao, true) }
        }
        
        return result
    }

    suspend fun buscarTodasLocalizacoesEquipamentos(): List<String> {
        return equipamentoDao.getAll()
            .map { it.localizacao.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .sortedBy { it.lowercase() }
    }

    // =========================
    // RELATÓRIOS
    // =========================

    suspend fun relatorioEstoque(): List<Material> = materialDao.getAll()
    suspend fun relatorioEquipamentos(): List<Equipamento> = equipamentoDao.getAll()
    suspend fun relatorioOcorrencias(): List<Ocorrencia> = ocorrenciaDao.getAll().reversed()
}
