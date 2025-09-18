package com.example.guia5

data class Venta(
    val id: String = "",
    val cliente: String = "",
    val fecha: String = "",
    val productos: Map<String, Int> = emptyMap(),
    val total: Double = 0.0
)
