package com.ud.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.FirebaseDatabase
import com.ud.myapplication.ui.theme.SnakesAndLaddersTheme
import com.ud.myapplication.navigation.Navigation

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

        setContent {
            SnakesAndLaddersTheme {
                Navigation()
            }
        }
    }
}