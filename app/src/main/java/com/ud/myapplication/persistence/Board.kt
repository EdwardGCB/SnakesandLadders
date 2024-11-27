package com.ud.myapplication.persistence

data class Board(
    val id: String = "",
    val rows: Int = 0,
    val columns: Int = 0,
    val players: List<Player> = emptyList(),
    val snakes: Int = 0,
    val ladders: Int = 0,
    val state: Boolean = false
)
