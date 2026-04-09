package com.example.sistema_de_laboratorio.data.model

data class Ocorrencia(
    val id: Int,
    val itemId: Int,
    val descricao: String,
    val data: String,
    val responsavel: String
)