package com.example.sistema_de_laboratorio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materiais")
data class Material(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var nome: String,
    var quantidade: Int,
    var descricao: String = "",
    var localizacao: String = "",
    var dataAquisicao: String = "",
    var fornecedor: String = ""
)