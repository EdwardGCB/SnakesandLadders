package com.ud.myapplication.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewTableroScreen() {
    TableroScreen()
}

@Composable
fun TableroScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            var count = 8*8
            for (i in 7 downTo 0) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (j in 7 downTo 0) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            when (count) {
                                8*8 -> Text("Meta")
                                1 -> Text("Inicio")
                                else -> Text(count.toString())
                            }
                        }
                        if (i % 2 != 0) {
                            count-- // Disminuir el conteo en filas pares
                        } else {
                            count++ // Aumentar el conteo en filas impares
                        }
                    }
                }
                if (i % 2 != 0) {
                    count -= 7 // Ajustar el conteo al final de las filas pares
                } else {
                    count -= 9 // Ajustar el conteo al final de las filas impares
                }
            }
        }
    }
}