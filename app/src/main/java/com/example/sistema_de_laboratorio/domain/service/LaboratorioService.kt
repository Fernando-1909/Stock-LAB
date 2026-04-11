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

    private suspend fun logOcorrencia(itemId: Int, acao: String, nomeItem: String) {
        val ocorrencia = Ocorrencia(
            itemId = itemId,
            descricao = "$acao: $nomeItem",
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
        // Note: For auto-generated IDs, if we need the ID for the log, 
        // we might need to change the DAO to return the inserted ID.
        // For now, logging with 0 or a placeholder is a compromise 
        // unless we fetch the last inserted ID.
        logOcorrencia(0, "CADASTRO MATERIAL", material.nome)
    }

    suspend fun editarMaterial(material: Material) {
        materialDao.update(material)
        logOcorrencia(material.id, "EDIÇÃO MATERIAL", material.nome)
    }

    suspend fun excluirMaterial(material: Material) {
        logOcorrencia(material.id, "EXCLUSÃO MATERIAL", material.nome)
        materialDao.delete(material)
    }

    suspend fun buscarMateriais(nome: String? = null): List<Material> {
        val all = materialDao.getAll()
        return if (nome.isNullOrBlank()) {
            all
        } else {
            all.filter { it.nome.contains(nome, true) }
        }
    }

    // =========================
    // EQUIPAMENTOS
    // =========================

    suspend fun cadastrarEquipamento(equipamento: Equipamento) {
        equipamentoDao.insert(equipamento)
        logOcorrencia(0, "CADASTRO EQUIPAMENTO", equipamento.nome)
    }

    suspend fun editarEquipamento(equipamento: Equipamento) {
        equipamentoDao.update(equipamento)
        logOcorrencia(equipamento.id, "EDIÇÃO EQUIPAMENTO", equipamento.nome)
    }

    suspend fun toggleStatusEquipamento(id: Int) {
        val equipamento = equipamentoDao.getById(id)
        equipamento?.let {
            it.status = if (it.status == Status.DISPONIVEL) Status.MANUTENCAO else Status.DISPONIVEL
            equipamentoDao.update(it)
            logOcorrencia(id, "STATUS ALTERADO (${it.status})", it.nome)
        }
    }

    suspend fun excluirEquipamento(equipamento: Equipamento) {
        logOcorrencia(equipamento.id, "EXCLUSÃO EQUIPAMENTO", equipamento.nome)
        equipamentoDao.delete(equipamento)
    }

    suspend fun buscarEquipamentos(nome: String? = null): List<Equipamento> {
        val all = equipamentoDao.getAll()
        return if (nome.isNullOrBlank()) {
            all
        } else {
            all.filter { it.nome.contains(nome, true) }
        }
    }

    // =========================
    // RELATÓRIOS
    // =========================

    suspend fun relatorioEstoque(): List<Material> = materialDao.getAll()
    suspend fun relatorioEquipamentos(): List<Equipamento> = equipamentoDao.getAll()
    suspend fun relatorioOcorrencias(): List<Ocorrencia> = ocorrenciaDao.getAll().reversed()
}
