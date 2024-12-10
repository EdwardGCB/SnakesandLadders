package com.ud.myapplication.persistence

data class Player (
    val idPlayer: String="",
    val correo: String="",
    var position: Int=1,
    val color: String="",
    var dado: Int=0,
    var turno: Int=0
)