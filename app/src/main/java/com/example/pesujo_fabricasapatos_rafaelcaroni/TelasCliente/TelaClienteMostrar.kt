package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ClienteController
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.PedidoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido.TelaPedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido.TelaPedidoAlterar
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto.TelaProduto
import kotlinx.coroutines.launch


class TelaClienteMostrar : ComponentActivity() {
    lateinit var listaCliente: ArrayList<Cliente>
    lateinit var clienteController: ClienteController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clienteController = ClienteController()
        lifecycleScope.launch {
            try {
                listaCliente = clienteController.carregarListaClientes()
                setContent {
                    ListagemDeClientes2(listaClientes = listaCliente)
                }
            } catch (e: Exception) {
                setContent {
                    clienteController.LoadingScreen()
                    Log.i("errorCarregarListaClientes", "${e}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListagemDeClientes2(listaClientes: ArrayList<Cliente>) {
    val activity: Activity? = (LocalContext.current as? Activity)
    val contexto: Context = LocalContext.current
    val clienteController = ClienteController()

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
                    itemsIndexed(listaClientes) { index, cliente ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .align(Alignment.CenterHorizontally),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Text(
                                    text = "Id#${cliente.nome}  - Cpf: ${cliente.cpf}",
                                    style = typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(2.dp)
                                )
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                        .align(Alignment.CenterHorizontally),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Endereço: ${cliente.endereco}",
                                            style = typography.bodyMedium
                                        )
                                        Text(text = "Telefone: ${cliente.telefone}",
                                            style = typography.bodyMedium
                                        )
                                        Text(
                                            text = "Instgram: @${cliente.instagram}",
                                            style = typography.bodyMedium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    IconButton(onClick = {
                                        showConfirmationDialog(contexto) {
                                            clienteController.deletarCliente(
                                                cliente,
                                                contexto
                                            ) { sucesso ->
                                                if (sucesso) {
                                                    // Pedido excluído com sucesso
                                                    Toast.makeText(
                                                        contexto,
                                                        "Cliente deletado com sucesso!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    activity?.recreate()
                                                } else {
                                                    // Falha ao excluir o cliente
                                                    Toast.makeText(
                                                        contexto,
                                                        "Erro ao deletar o cliente",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Deletar Cliente"
                                        )
                                    }
                                    IconButton(onClick = {
                                        // Inicie a atividade usando requestActivityResult
                                        requestActivityResult.launch(
                                            Intent(contexto, TelaClienteAlterar::class.java).apply {
                                                putExtra("cpfCliente", cliente.cpf.toString())
                                            }
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Alterar Cliente"
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
        .setMessage("Tem certeza de que deseja excluir este Cliente?")
        .setPositiveButton("Sim") { _, _ ->
            onConfirmation.invoke()
        }
        .setNegativeButton("Não", null)
        .show()
}

