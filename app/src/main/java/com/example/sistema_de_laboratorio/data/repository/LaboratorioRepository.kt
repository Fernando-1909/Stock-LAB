package com.example.sistema_de_laboratorio.data.repository

import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.data.model.Ocorrencia

object LaboratorioRepository {

    val materiais = mutableListOf<Material>()
    val equipamentos = mutableListOf<Equipamento>()
    val ocorrencias = mutableListOf<Ocorrencia>()

    private var materialId = 0
    private var equipamentoId = 0
    private var ocorrenciaId = 0

    fun gerarMaterialId() = ++materialId
    fun gerarEquipamentoId() = ++equipamentoId
    fun gerarOcorrenciaId() = ++ocorrenciaId

    fun excluirMaterial(id: Int) {
        materiais.removeIf { it.id == id }
    }

    fun excluirEquipamento(id: Int) {
        equipamentos.removeIf { it.id == id }
    }
}