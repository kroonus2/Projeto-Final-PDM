package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Pedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.PedidoController
import kotlinx.coroutines.launch

class TelaPedidoMostrar : ComponentActivity() {
    lateinit var listaPedidos: ArrayList<Pedido>
    lateinit var pedidoController: PedidoController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pedidoController = PedidoController()
        lifecycleScope.launch {
            try {
                listaPedidos = pedidoController.carregarListaPedidos()
                setContent {
                    ListagemDePedidos(listaPedidos = listaPedidos)
                }
            } catch (e: Exception) {
                setContent {
                    pedidoController.LoadingScreen()
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListagemDePedidos(listaPedidos: ArrayList<Pedido>) {
    val activity: Activity? = (LocalContext.current as? Activity)

    Scaffold(
        bottomBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Centered TopAppBar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Voltar"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(listaPedidos) { index, pedido ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Text(text = "#${pedido.idPedido}, Cpf: ${pedido.cpf}, Data: ${pedido.data}", style = MaterialTheme.typography.titleMedium)
                            pedido.produtos.forEach { produto ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "NomeProduto: ${produto.produto.nome}, Qntd: ${produto.qntd}", style = MaterialTheme.typography.bodyMedium)
                                        Text(text = "SubTotal: R$${produto.produto.valor * produto.qntd}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}




