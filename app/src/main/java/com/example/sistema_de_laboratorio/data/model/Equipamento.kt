package com.example.sistema_de_laboratorio.data.model

data class Equipamento(
    val id: Int,
    var nome: String,
    var descricao: String,
    var numeroSerie: String,
    var localizacao: String,
    var dataAquisicao: String,
    var fornecedor: String,
    var manual: String,
    var status: Status,
    var quantidade: Int = 1
)