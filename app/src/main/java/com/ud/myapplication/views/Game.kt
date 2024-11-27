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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ud.myapplication.R
import com.ud.myapplication.persistence.Casilla
import com.ud.myapplication.persistence.EnumNavigation
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero
import kotlin.random.Random


    @Preview
    @Composable
    fun PreviewMainGame() {
        //MainGame()
    }

    @Preview
    @Composable
    fun PreviewPlaterInfo() {
        val players = remember {mutableListOf<Player>(
            Player(
                idPlayer = "1",
                correo = "Edward",
            ),
            Player(
                idPlayer = "2",
                correo = "Robin",
            ),
        )}
        PlayerInfo(players[0], 1)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGame(idBoard: String?, navController: NavHostController, viewModel: GameViewModel = viewModel()) {
    if (idBoard == null) {
        navController.navigate(EnumNavigation.LOGIN.toString())
    }
    val board by viewModel.board.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    idBoard?.let { viewModel.consultarTablero(it) }

    if (board?.state == true) {

        val tablero = board?.let { Tablero.iniciarTablero(it) }
        val players = board?.jugadores ?: listOf()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {  }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (players.size) {
                    2 -> {
                        Row {
                            PlayerInfo(players[0], 1)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        TableroScreen(tablero)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            PlayerInfo(players[1], 2)
                        }
                    }
                    3 -> {
                        Row {
                            PlayerInfo(players[2], 1)
                            Spacer(modifier = Modifier.width(50.dp))
                            PlayerInfo(players[1], 2)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        TableroScreen(tablero)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            PlayerInfo(players[0], 1)
                        }
                    }
                    4 -> {
                        Row {
                            PlayerInfo(players[3], 1)
                            Spacer(modifier = Modifier.width(50.dp))
                            PlayerInfo(players[2], 2)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        TableroScreen(tablero)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            PlayerInfo(players[0], 1)
                            Spacer(modifier = Modifier.width(50.dp))
                            PlayerInfo(players[1], 2)
                        }
                    }
                }
            }
        }

    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Text("Esperando a que el host inicie el juego...")
        }
    }

    errorMessage?.let {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = it, color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
    fun TableroScreen(tablero: List<List<Casilla>>?) {
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
                                when (tablero!![i][j].valor) {
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
                            text = player.correo,
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