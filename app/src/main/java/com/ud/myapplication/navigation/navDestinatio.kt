package com.ud.myapplication.navigation

import kotlinx.serialization.Serializable


@Serializable
object Login

@Serializable
data class Game(val gameId: String)

@Serializable
data class Home(val personId: String)
