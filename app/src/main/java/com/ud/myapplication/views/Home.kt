package com.ud.myapplication.views


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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.ud.myapplication.R
import com.ud.myapplication.persistence.Operaciones
import com.ud.myapplication.persistence.Player

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val host =  remember { mutableStateOf("") }
    val flag = remember { mutableStateOf(false) }
    Scaffold (
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
    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(Modifier.height(20.dp))

            Row (){
                CardButton("Local", R.drawable.local_icon, onClick = {})
                Spacer(Modifier.width(40.dp))
                CardButton("Multijugador", R.drawable.multiplayer_icon, onClick = {flag.value = !flag.value})
            }
            Spacer(Modifier.height(40.dp))
            if(flag.value){
                val players = remember {mutableListOf<Player>(
                    Player(
                        idPlayer = "1",
                        name = "Edward",
                    ),
                    Player(
                        idPlayer = "2",
                        name = "Robin",
                    ),
                    Player(
                        idPlayer = "1",
                        name = "Edward",
                    ),
                    Player(
                        idPlayer = "2",
                        name = "Robin",
                    ),
                )}
                CardEvent(players)
            }else{
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
                //Login Field
                OutlinedTextField(
                    value = host.value,
                    onValueChange = { host.value = it },
                    label = { Text("Host") },
                )
                Spacer(Modifier.height(20.dp))
                //Button login
                Button(
                    onClick = {
                        //TODO: Unirse a la sala
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
fun CardEvent(players: List<Player>){
    val link = Operaciones().generateRandomString()
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
                    Spacer(Modifier.width(190.dp))
                    Card(
                        modifier = Modifier
                            .clickable(onClick = {  }),
                        shape = RoundedCornerShape(9.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row (Modifier.padding(5.dp)){
                            Text(
                                link,
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
                player.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}
