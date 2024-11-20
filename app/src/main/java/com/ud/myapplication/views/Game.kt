package com.ud.myapplication.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ud.myapplication.R
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero
import kotlin.random.Random

class Game {


    @Preview
    @Composable
    fun PreviewTableroScreen() {
        TableroScreen()
    }

    @Preview
    @Composable
    fun PreviewMainGame() {
        MainGame()
    }

    @Preview
    @Composable
    fun PreviewPlaterInfo() {
        val players = remember {mutableListOf<Player>(
            Player(
                idPlayer = "1",
                name = "Edward",
            ),
            Player(
                idPlayer = "2",
                name = "Robin",
            ),
        )}
        PlayerInfo(players[0], 1)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainGame(){
        val players = remember {mutableListOf<Player>(
            Player(
                idPlayer = "1",
                name = "Edward",
            ),
            Player(
                idPlayer = "2",
                name = "Robin",
            ),
        )}
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = {  }
                )
            },

            ){innerPadding->
            Column (
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(){
                    PlayerInfo(players[0], 1)
                    Spacer(modifier = Modifier.width(50.dp))
                    PlayerInfo(players[1], 2)
                }
                Spacer(modifier = Modifier.height(20.dp))
                TableroScreen()
                Spacer(modifier = Modifier.height(20.dp))
                Row(){
                    PlayerInfo(players[0], 1)
                    Spacer(modifier = Modifier.width(50.dp))
                    PlayerInfo(players[1], 2)
                }
            }

        }
    }

    @Composable
    fun TableroScreen() {
        val tablero = Tablero.iniciarTablero();
        Box(
            modifier = Modifier.width(350.dp)
        ) {
            Column() {
                for (i in 0 until 8) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (j in 0 until 8) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(2.dp, Color.DarkGray),
                                contentAlignment = Alignment.Center
                            ) {
                                when (tablero[i][j].valor) {
                                    64 -> Text("Meta")
                                    1 -> Text("Inicio")
                                    else -> Text(tablero[i][j].valor.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PlayerInfo(player: Player, slots: Int){
        val dieValue = remember { mutableIntStateOf(1) }
        Card(
            shape = RoundedCornerShape(3.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier.padding(4.dp)
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = "Player image",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = slots.toString(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = player.name,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                    DieCompose(dieValue)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }

    @Composable
    fun DieCompose(dieValue: MutableState<Int>) {

        IconButton(
            onClick = {
                dieValue.value = Random.nextInt(1,7)
            },
            modifier = Modifier.padding(0.dp)
        ) {
            val icon = when(dieValue.value){
                1 -> R.drawable.die_icon_1
                2 -> R.drawable.die_icon_2
                3 -> R.drawable.die_icon_3
                4 -> R.drawable.die_icon_4
                5 -> R.drawable.die_icon_5
                6 -> R.drawable.die_icon_6
                else -> R.drawable.ic_launcher_background
            }
            Icon(
                painter = painterResource(id=icon),
                contentDescription = "Die Icon",
            )
        }
    }
}