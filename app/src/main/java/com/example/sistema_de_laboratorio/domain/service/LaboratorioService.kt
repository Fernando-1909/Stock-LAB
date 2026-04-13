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
    // SEED INITIAL DATA
    // =========================

    suspend fun seedInitialData() {
        // Seeding materials (8 items)
        val materiaisIniciais = listOf(
            Material(nome = "Álcool 70%", quantidade = 15, descricao = "Uso geral para higienização", localizacao = "Armário A", dataAquisicao = "10/01/2024", fornecedor = "LabShop"),
            Material(nome = "Luvas de Látex", quantidade = 100, descricao = "Caixa com 100 unidades", localizacao = "Gaveta B1", dataAquisicao = "15/01/2024", fornecedor = "MedSupply"),
            Material(nome = "Máscaras Descartáveis", quantidade = 50, descricao = "Proteção individual", localizacao = "Gaveta B2", dataAquisicao = "15/01/2024", fornecedor = "MedSupply"),
            Material(nome = "Becker de Vidro 250ml", quantidade = 20, descricao = "Vidraria de laboratório", localizacao = "Prateleira C", dataAquisicao = "20/01/2024", fornecedor = "GlassLab"),
            Material(nome = "Tubos de Ensaio", quantidade = 200, descricao = "Tubos de vidro 10ml", localizacao = "Caixa D", dataAquisicao = "05/02/2024", fornecedor = "GlassLab"),
            Material(nome = "Papel Filtro", quantidade = 500, descricao = "Filtração qualitativa", localizacao = "Armário A", dataAquisicao = "12/02/2024", fornecedor = "QuimicaBR"),
            Material(nome = "Pipetas de Pasteur", quantidade = 100, descricao = "Transferência de líquidos", localizacao = "Gaveta B3", dataAquisicao = "18/02/2024", fornecedor = "LabShop"),
            Material(nome = "Ácido Sulfúrico 1L", quantidade = 5, descricao = "Reagente concentrado", localizacao = "Depósito Especial", dataAquisicao = "01/03/2024", fornecedor = "BioChem")
        )
        
        for (m in materiaisIniciais) {
            materialDao.insert(m)
        }

        // Seeding equipment (9 items)
        // 4 Disponível, 3 Indisponível, 2 Manutenção
        val equipamentosIniciais = listOf(
            Equipamento(nome = "Microscópio Óptico", quantidade = 4, descricao = "Observação biológica", localizacao = "Mesa 1", status = Status.DISPONIVEL, dataAquisicao = "01/12/2023", fornecedor = "Zeiss", numeroSerie = "ZE-123"),
            Equipamento(nome = "Centrífuga Digital", quantidade = 2, descricao = "Separação de amostras", localizacao = "Bancada 2", status = Status.DISPONIVEL, dataAquisicao = "15/12/2023", fornecedor = "Thermo", numeroSerie = "TH-456"),
            Equipamento(nome = "Autoclave", quantidade = 1, descricao = "Esterilização por vapor", localizacao = "Área de Limpeza", status = Status.DISPONIVEL, dataAquisicao = "10/01/2024", fornecedor = "Steris", numeroSerie = "ST-789"),
            Equipamento(nome = "Balança de Precisão", quantidade = 3, descricao = "Pesagem analítica", localizacao = "Bancada 1", status = Status.DISPONIVEL, dataAquisicao = "20/01/2024", fornecedor = "Mettler", numeroSerie = "MT-012"),
            Equipamento(nome = "Agitador Magnético", quantidade = 5, descricao = "Mistura de soluções", localizacao = "Bancada 3", status = Status.INDISPONIVEL, dataAquisicao = "05/02/2024", fornecedor = "IKA", numeroSerie = "IK-345"),
            Equipamento(nome = "Estufa de Secagem", quantidade = 1, descricao = "Secagem de vidraria", localizacao = "Área Técnica", status = Status.INDISPONIVEL, dataAquisicao = "12/02/2024", fornecedor = "Memmert", numeroSerie = "MM-678"),
            Equipamento(nome = "pHmetro Digital", quantidade = 2, descricao = "Medição de acidez", localizacao = "Bancada 1", status = Status.INDISPONIVEL, dataAquisicao = "25/02/2024", fornecedor = "Hanna", numeroSerie = "HN-901"),
            Equipamento(nome = "Espectrofotômetro", quantidade = 1, descricao = "Análise quantitativa", localizacao = "Sala Escura", status = Status.MANUTENCAO, dataAquisicao = "05/03/2024", fornecedor = "Shimadzu", numeroSerie = "SH-234"),
            Equipamento(nome = "Banho Maria", quantidade = 2, descricao = "Aquecimento controlado", localizacao = "Bancada 4", status = Status.MANUTENCAO, dataAquisicao = "15/03/2024", fornecedor = "Quimis", numeroSerie = "QM-567")
        )
        
        for (e in equipamentosIniciais) {
            equipamentoDao.insert(e)
        }
    }

    // =========================
    // RELATÓRIOS
    // =========================

    suspend fun relatorioEstoque(): List<Material> = materialDao.getAll()
    suspend fun relatorioEquipamentos(): List<Equipamento> = equipamentoDao.getAll()
    suspend fun relatorioOcorrencias(): List<Ocorrencia> = ocorrenciaDao.getAll().reversed()
}
