package com.ud.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.FirebaseDatabase
import com.ud.myapplication.ui.theme.SnakesAndLaddersTheme
import com.ud.myapplication.navigation.Navigation
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Conexión con Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("mensaje_prueba")

        myRef.setValue("¡Hola desde Firebase!")
            .addOnSuccessListener {
                Toast.makeText(this, "Conexión exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        private fun startGame() {
            Tablero.iniciarTablero()
            Tablero.addPlayer(Player(idPlayer = "1", name = "Jugador 1"))
            Tablero.addPlayer(Player(idPlayer = "2", name = "Jugador 2"))
            while (true) {
                Tablero.startTurn()

                // Verificar si alguien ganó
                val winner = Tablero.getPlayers().find { it.position == 64 }
                if (winner != null) {
                    println("🎉 ${winner.name} ganó el juego!")
                    break
                }
        }

        setContent {
            SnakesAndLaddersTheme {
                Navigation()
            }

        }
    }

}