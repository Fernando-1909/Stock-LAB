package com.example.sistema_de_laboratorio.domain.service

import com.example.sistema_de_laboratorio.data.model.*
import com.example.sistema_de_laboratorio.data.repository.LaboratorioRepository
import java.text.SimpleDateFormat
import java.util.*

object LaboratorioService {

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun logOcorrencia(itemId: Int, acao: String, nomeItem: String) {
        val ocorrencia = Ocorrencia(
            id = LaboratorioRepository.gerarOcorrenciaId(),
            itemId = itemId,
            descricao = "$acao: $nomeItem",
            data = getCurrentDate(),
            responsavel = "Sistema"
        )
        LaboratorioRepository.ocorrencias.add(0, ocorrencia) // Add to top
    }

    // =========================
    // MATERIAIS
    // =========================

    fun cadastrarMaterial(material: Material) {
        LaboratorioRepository.materiais.add(material)
        logOcorrencia(material.id, "CADASTRO MATERIAL", material.nome)
    }

    fun editarMaterial(material: Material) {
        val index = LaboratorioRepository.materiais.indexOfFirst { it.id == material.id }
        if (index != -1) {
            LaboratorioRepository.materiais[index] = material
            logOcorrencia(material.id, "EDIÇÃO MATERIAL", material.nome)
        }
    }

    fun excluirMaterial(id: Int) {
        val item = LaboratorioRepository.materiais.find { it.id == id }
        item?.let {
            logOcorrencia(id, "EXCLUSÃO MATERIAL", it.nome)
            LaboratorioRepository.excluirMaterial(id)
        }
    }

    // =========================
    // EQUIPAMENTOS
    // =========================

    fun cadastrarEquipamento(equipamento: Equipamento) {
        LaboratorioRepository.equipamentos.add(equipamento)
        logOcorrencia(equipamento.id, "CADASTRO EQUIPAMENTO", equipamento.nome)
    }

    fun editarEquipamento(equipamento: Equipamento) {
        val index = LaboratorioRepository.equipamentos.indexOfFirst { it.id == equipamento.id }
        if (index != -1) {
            LaboratorioRepository.equipamentos[index] = equipamento
            logOcorrencia(equipamento.id, "EDIÇÃO EQUIPAMENTO", equipamento.nome)
        }
    }

    fun toggleStatusEquipamento(id: Int) {
        val equipamento = LaboratorioRepository.equipamentos.find { it.id == id }
        equipamento?.let {
            it.status = if (it.status == Status.DISPONIVEL) Status.MANUTENCAO else Status.DISPONIVEL
            logOcorrencia(id, "STATUS ALTERADO (${it.status})", it.nome)
        }
    }

    fun excluirEquipamento(id: Int) {
        val item = LaboratorioRepository.equipamentos.find { it.id == id }
        item?.let {
            logOcorrencia(id, "EXCLUSÃO EQUIPAMENTO", it.nome)
            LaboratorioRepository.excluirEquipamento(id)
        }
    }

    // =========================
    // BUSCA E RELATÓRIOS
    // =========================

    fun buscarMateriais(nome: String? = null): List<Material> {
        return LaboratorioRepository.materiais.filter {
            nome.isNullOrBlank() || it.nome.contains(nome, true)
        }
    }

    fun buscarEquipamentos(nome: String? = null): List<Equipamento> {
        return LaboratorioRepository.equipamentos.filter {
            nome.isNullOrBlank() || it.nome.contains(nome, true)
        }
    }

    fun relatorioEstoque(): List<Material> = LaboratorioRepository.materiais
    fun relatorioEquipamentos(): List<Equipamento> = LaboratorioRepository.equipamentos
    fun relatorioOcorrencias(): List<Ocorrencia> = LaboratorioRepository.ocorrencias
}
