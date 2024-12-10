package com.ud.myapplication.views


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.ud.myapplication.R
import com.ud.myapplication.ViewModel.GameBoardViewModel
import com.ud.myapplication.persistence.EnumNavigation
import com.ud.myapplication.persistence.Operaciones
import com.ud.myapplication.persistence.Player

@Preview
@Composable
fun PreviewHomeScreen() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,viewModel: GameBoardViewModel = viewModel()) {
    val host = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("") }
    val flag = remember { mutableStateOf(false) }

    val players by viewModel.players.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState() // Observamos los errores

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Row {
                    Text(
                        "Snakes",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "&",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Ladders",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            Row {
                CardButton("Local", R.drawable.local_icon, onClick = {})
                Spacer(Modifier.width(40.dp))
                CardButton(
                    "Multijugador",
                    R.drawable.multiplayer_icon,
                    onClick = {
                        flag.value = !flag.value
                        if (flag.value) {
                            // Crear un nuevo tablero y establecer el host
                            val boardId = viewModel.createGameBoard()
                            host.value = boardId
                            viewModel.listenToPlayers(boardId) // Escuchar jugadores en tiempo real
                        } else {
                            host.value = ""
                        }
                    }
                )
            }

            Spacer(Modifier.height(40.dp))

            if (flag.value) {
                // Mostramos el evento con la lista de jugadores
                CardEvent(
                    players,
                    host,
                    onClick = {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val player = Player(
                                idPlayer = user.uid,
                                correo = user.email ?: "",
                                1,
                                Operaciones().generateRandomColor(),
                                1)
                            viewModel.joinBoard(host.value, player) // Unirse a la sala en firebase
                            viewModel.updateBoardState(host.value, 8, 8)
                            navController.navigate("${EnumNavigation.PLAY}/${host.value}")
                        } else {
                            navController.navigate(EnumNavigation.LOGIN.toString())
                        }
                    }
                )
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                Text(
                    "-------------- or --------------",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(40.dp))
                Text(
                    "Unirse a una sala",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(20.dp))

                // Campo para ingresar el código del tablero (host)
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = { Text("Host") },
                )
                Spacer(Modifier.height(20.dp))

                // Botón para unirse a la sala
                Button(
                    onClick = {
                        if (text.value.isNotEmpty()) {
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                val player = Player(
                                    idPlayer = user.uid,
                                    correo = user.email ?: "",
                                    1,
                                    Operaciones().generateRandomColor(),
                                    1)
                                viewModel.joinBoard(text.value, player) // Unirse a la sala
                                navController.navigate("${EnumNavigation.PLAY}/${text.value}")
                             } else {
                                navController.navigate(EnumNavigation.LOGIN.toString())
                             }
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .width(200.dp)
                ) {
                    Text(text = "Unirse", fontSize = 18.sp)
                }
            }
        }
    }
}


@Composable
fun CardButton(title: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(5.dp))
            Text(
                title,
                fontWeight = FontWeight.ExtraBold
            )
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
            )
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun CardEvent(players: List<Player>, host: MutableState<String>, onClick: () -> Unit){
    val context = LocalContext.current
    Card(
        modifier = Modifier
           .padding(10.dp)
            .width(360.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Row (
                    modifier = Modifier
                        .padding(10.dp)
                ){
                    Text(
                        "Event",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(50.dp))
                    Card(
                        modifier = Modifier
                            .clickable(onClick = { copyToClipBoard(context, host) })
                            .width(250.dp),
                        shape = RoundedCornerShape(9.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row (
                            Modifier.padding(5.dp)
                        ){
                            Text(
                                host.value,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Icon(
                                painter = painterResource(R.drawable.copy_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                    }
                }
                Spacer(Modifier.height(5.dp))
            }
            item {
                Spacer(
                    Modifier.height(2.dp).background(Color.Gray).width(330.dp)
                )
                Spacer(Modifier.height(10.dp))
            }
            item {
                Text(
                    "Players: ${players.size} of 4"
                )
                Spacer(Modifier.height(10.dp))
            }
            items(players) {player->
                PlayerCard(player)
            }
            item{
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(200.dp)
                ) {
                    Text(text = "Empezar partida", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player){
    Card(
        modifier = Modifier
           .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
               .padding(5.dp)
               .fillMaxWidth()
        ) {
            Icon(
                painter = rememberImagePainter("https://cdn-icons-png.flaticon.com/512/3135/3135768.png"),
                contentDescription = null,
                modifier = Modifier
                   .size(50.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                player.correo,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}

fun copyToClipBoard(context: Context, host: MutableState<String>) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", host.value)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
}