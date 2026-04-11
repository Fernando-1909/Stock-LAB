package com.example.sistema_de_laboratorio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipamentos")
data class Equipamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var nome: String,
    var quantidade: Int,
    var descricao: String = "",
    var numeroSerie: String = "",
    var localizacao: String = "",
    var dataAquisicao: String = "",
    var fornecedor: String = "",
    var manual: String = "",
    var status: Status = Status.DISPONIVEL
)