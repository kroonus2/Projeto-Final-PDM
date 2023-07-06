package com.example.pesujo_fabricasapatos_rafaelcaroni

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente.TelaCliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido.TelaPedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto.TelaProduto

class TelaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElementosDaTela()

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ElementosDaTela() {

    var expanded by remember { mutableStateOf(false) }
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)

    Card(
        border = BorderStroke(2.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(234, 223, 235, 255)),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Card(
            border = BorderStroke(1.dp, Color.Magenta),
            modifier = Modifier.padding(30.dp, 15.dp)
        ) {
            Text(
                text = "Pé Sujo",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(234, 223, 235, 255),
            bottomBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Inserir Pedidos",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.TopStart)
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Clientes") },
                                        onClick = {
                                            contexto.startActivity(
                                                Intent(
                                                    contexto,
                                                    TelaCliente::class.java
                                                )
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.Person,
                                                contentDescription = null
                                            )
                                        })
                                    DropdownMenuItem(
                                        text = { Text("Produtos") },
                                        onClick = {
                                            contexto.startActivity(
                                                Intent(
                                                    contexto,
                                                    TelaProduto::class.java
                                                )
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.List,
                                                contentDescription = null
                                            )
                                        })
                                    DropdownMenuItem(
                                        text = { Text("Pedidos") },
                                        onClick = {
                                            contexto.startActivity(
                                                Intent(
                                                    contexto,
                                                    TelaPedido::class.java
                                                )
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.AddCircle,
                                                contentDescription = null
                                            )
                                        })
                                    Divider()
                                    DropdownMenuItem(
                                        text = { Text("Enviar Feedback") },
                                        onClick = {
                                            Toast.makeText(
                                                contexto,
                                                "Sua mensagem será encaminhada para a caixa de mensagens",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.Email,
                                                contentDescription = null
                                            )
                                        },
                                        trailingIcon = {
                                            Text(
                                                "F11",
                                                textAlign = TextAlign.Center
                                            )
                                        })
                                }
                            }
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                    modifier = Modifier.background(Color(234, 223, 235, 255))
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo Provisoria",
                        modifier = Modifier
                            .size(300.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        )
    }
}