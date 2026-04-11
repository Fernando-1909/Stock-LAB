package com.example.sistema_de_laboratorio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ocorrencias")
data class Ocorrencia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemId: Int,
    val descricao: String,
    val data: String,
    val responsavel: String
)