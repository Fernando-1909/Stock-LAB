package com.example.sistema_de_laboratorio.data.model

data class Material(
    val id: Int,
    var nome: String,
    var descricao: String,
    var quantidade: Int,
    var localizacao: String,
    var dataAquisicao: String,
    var fornecedor: String
)