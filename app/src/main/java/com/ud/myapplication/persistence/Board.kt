package com.ud.myapplication.persistence

data class Board(
    val id: String = "",
    val rows: Int = 0,
    val columns: Int = 0,
    val players: List<Player> = emptyList(),
    val snakes: List<Snake> = emptyList(),
    val ladders: List<Ladder> = emptyList(),
    val state: Boolean = false,
    val turn: Int =1
)
