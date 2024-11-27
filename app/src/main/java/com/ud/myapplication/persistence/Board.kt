package com.ud.myapplication.persistence

data class Board(
    val id: String = "",
    val filas: Int = 0,
    val columnas: Int = 0,
    val jugadores: List<Player> = emptyList(),
    val serpientes: Int = 0,
    val escaleras: Int = 0,
    val state: Boolean = false
)
