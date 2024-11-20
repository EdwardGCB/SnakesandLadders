package com.ud.myapplication.persistence

import kotlin.random.Random

open class Operaciones {
    open fun generateRandomString(length: Int = 6): String {
        val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9') // Letras y números
        return (1..length)
            .map { Random.nextInt(0, charset.size) }
            .map(charset::get)
            .joinToString("")
    }
}