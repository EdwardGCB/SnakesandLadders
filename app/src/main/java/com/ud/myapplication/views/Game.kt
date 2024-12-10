package com.ud.myapplication.views

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ud.myapplication.R
import com.ud.myapplication.ViewModel.GameViewModel
import com.ud.myapplication.persistence.Casilla
import com.ud.myapplication.persistence.EnumNavigation
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGame(idBoard: String?, navController: NavHostController, viewModel: GameViewModel = viewModel()) {
    if (idBoard == null) {
        navController.navigate(EnumNavigation.LOGIN.toString())
        return
    }
    val id = remember { mutableStateOf(idBoard) }
    val board by viewModel.board.collectAsState()
    val players by viewModel.players.collectAsState() // Escuchar la lista de jugadores en tiempo real
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val currentUserEmail = viewModel.currentUserEmail // Obtener el correo del usuario logueado
    // Iniciar el escuchador de jugadores
    LaunchedEffect(id.value) {
        id.value.let { viewModel.listenToPlayers(it) }
        id.value.let { viewModel.consultarTablero(it) }
    }

    when {
        isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text("Cargando datos del tablero...")
            }
        }
        board?.state == true -> {
            val tablero = board?.let { Tablero.iniciarTablero(it) }

            // Ordenar jugadores para que el usuario logueado esté abajo a la izquierda
            val sortedPlayers = players.sortedBy { it.correo != currentUserEmail }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Juego en progreso") }
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (sortedPlayers.size) {
                        2 -> {
                            Row {
                                PlayerInfo(sortedPlayers[1], viewModel, id.value, board!!.turn) // Jugador 1
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            TableroScreen(tablero, players)
                            Spacer(modifier = Modifier.height(20.dp))
                            Row {
                                PlayerInfo(sortedPlayers[0], viewModel, id.value, board!!.turn) // Jugador logueado
                            }
                        }
                        3 -> {
                            Row {
                                PlayerInfo(sortedPlayers[1], viewModel, id.value, board!!.turn)
                                Spacer(modifier = Modifier.width(50.dp))
                                PlayerInfo(sortedPlayers[2], viewModel, id.value, board!!.turn)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            TableroScreen(tablero, players)
                            Spacer(modifier = Modifier.height(20.dp))
                            Row {
                                PlayerInfo(sortedPlayers[0], viewModel, id.value, board!!.turn) // Jugador logueado
                            }
                        }
                        4 -> {
                            Row {
                                PlayerInfo(sortedPlayers[2], viewModel, id.value, board!!.turn)
                                Spacer(modifier = Modifier.width(50.dp))
                                PlayerInfo(sortedPlayers[3], viewModel, id.value, board!!.turn)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            TableroScreen(tablero, players)
                            Spacer(modifier = Modifier.height(20.dp))
                            Row {
                                PlayerInfo(sortedPlayers[0], viewModel, id.value, board!!.turn)
                                Spacer(modifier = Modifier.width(50.dp))
                                PlayerInfo(sortedPlayers[1], viewModel, id.value, board!!.turn) // Jugador logueado
                            }
                        }
                        else -> {
                            Text("Esperando más jugadores...")
                        }
                    }
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text("Esperando a que el host inicie el juego...")
            }
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




@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TableroScreen(tablero: List<List<Casilla>>?, players: List<Player>) {
    Box(
        modifier = Modifier.width(350.dp)
    ) {
        Column {
            for (i in 0 until 8) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (j in 0 until 8) {
                        val casillaJugadores = players.filter { it.position == tablero!![i][j].valor }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(2.dp, Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (casillaJugadores.isNotEmpty()) {
                                // Muestra a los jugadores en la casilla
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // Renderizar los jugadores en un cuadrado
                                        FlowRow(
                                        ) {
                                            casillaJugadores.forEachIndexed { index, player ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .background(player.color.colorFromHex())
                                                        .border(1.dp, Color.Black)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Renderizar casillas normales
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
}

// Función auxiliar para convertir el color desde Hexadecimal a Color
fun String.colorFromHex(): Color {
    return Color(android.graphics.Color.parseColor(this))
}


@Composable
fun PlayerInfo(player: Player, viewModel: GameViewModel, idBoard: String, turn: Int){
    val dieValue = remember { mutableIntStateOf(player.dado) }
    Card(
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center)
        {
            Row(Modifier.padding(4.dp))
            {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Player image",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = player.correo,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
                DieCompose(
                    {
                        if(player.turno == turn){
                            dieValue.intValue = Random.nextInt(1, 7)
                            if (player.position + dieValue.intValue == 64) {
                                player.position = 64
                            } else {
                                player.position += dieValue.intValue
                            }
                            player.dado = dieValue.intValue
                            viewModel.updatePlayer(idBoard, player)
                            viewModel.switchTurn(idBoard)
                        }
                    },
                    player.dado,
                    player.color,
                    player.turno == turn
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun DieCompose(onClick: () -> Unit, dieValue: Int, color: String, flag: Boolean) {
    val icon = when (dieValue) {
        1 -> R.drawable.die_icon_1
        2 -> R.drawable.die_icon_2
        3 -> R.drawable.die_icon_3
        4 -> R.drawable.die_icon_4
        5 -> R.drawable.die_icon_5
        6 -> R.drawable.die_icon_6
        else -> R.drawable.ic_launcher_background
    }

    Box(
        modifier = Modifier
            .size(60.dp) // Tamaño del dado
            .clickable { if(flag)onClick() },
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Die Icon",
            modifier = Modifier.size(50.dp),
            tint = Color(parseColor(color))
        )
    }
}

