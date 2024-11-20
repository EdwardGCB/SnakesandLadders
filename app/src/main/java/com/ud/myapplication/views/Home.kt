package com.ud.myapplication.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Text("Snakes and Ladders")
    }
    Spacer(Modifier.height(40.dp))
    Row {
        CardButton()
        Spacer(Modifier.height(40.dp))
        CardButton()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CardButton(title: String, imageUrl: String) {
    Card() {
        Text(title)
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )

    }
}