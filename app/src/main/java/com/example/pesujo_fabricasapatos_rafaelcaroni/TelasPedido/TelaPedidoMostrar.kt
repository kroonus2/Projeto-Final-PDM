package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Pedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.PedidoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente.TelaCliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto.TelaProduto
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
    val contexto: Context = LocalContext.current
    val pedidoController = PedidoController()

    var expanded by remember { mutableStateOf(false) }

    val requestActivityResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // O resultado está OK, recarregue a página aqui
            activity?.recreate()
        } else {
            // O resultado não está OK, tratamento de erro ou outra lógica aqui
        }
    }

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
            containerColor = Color(234, 223, 235, 255),
            bottomBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Mostrar Pedidos",
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
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                    modifier = Modifier.background(Color(234, 223, 235, 255))
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
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .align(Alignment.CenterHorizontally),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Text(
                                    text = "Id#${pedido.idPedido}  - Cpf: ${pedido.cpf}",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(2.dp)
                                )
                                pedido.produtos.forEach { produto ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                            .align(Alignment.CenterHorizontally),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "NomeProduto: ${produto.produto.nome}, Qntd: ${produto.qntd}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "SubTotal: R$${produto.produto.valor * produto.qntd}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Data Compra: ${pedido.data}",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(2.dp)
                                )
                                Row(
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    IconButton(onClick = {
                                        showConfirmationDialog(contexto) {
                                            pedidoController.deletarPedido(
                                                pedido,
                                                contexto
                                            ) { sucesso ->
                                                if (sucesso) {
                                                    // Pedido excluído com sucesso
                                                    Toast.makeText(
                                                        contexto,
                                                        "Pedido deletado com sucesso!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    activity?.recreate()
                                                } else {
                                                    // Falha ao excluir o pedido
                                                    Toast.makeText(
                                                        contexto,
                                                        "Erro ao deletar o pedido",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Deletar Pedido"
                                        )
                                    }
                                    IconButton(onClick = {
                                        requestActivityResult.launch(
                                            Intent(contexto, TelaPedidoAlterar::class.java).apply {
                                                putExtra("idPedido", pedido.idPedido.toString())
                                            }
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Alterar Pedido"
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

private fun showConfirmationDialog(context: Context, onConfirmation: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle("Confirmação")
        .setMessage("Tem certeza de que deseja excluir este pedido?")
        .setPositiveButton("Sim") { _, _ ->
            onConfirmation.invoke()
        }
        .setNegativeButton("Não", null)
        .show()
}

