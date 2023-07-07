package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.Checkbox
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Pedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.QntdProduto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ClienteController
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.PedidoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ProdutoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente.TelaCliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto.TelaProduto
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class TelaPedidoInserir : ComponentActivity() {
    lateinit var listaClientes: ArrayList<Cliente>
    lateinit var listaProdutos: ArrayList<Produto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clienteController = ClienteController()
        val produtoController = ProdutoController()
        val pedidoController = PedidoController()
        lifecycleScope.launch {
            try {
                listaClientes = clienteController.carregarListaClientes()
                listaProdutos = produtoController.carregarListaProdutos()
                setContent {
                    InserirPedido(listaClientes, listaProdutos)
                }
            } catch (e: Exception) {
                setContent {
                    pedidoController.LoadingScreen()
                }
            }
        }

    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun InserirPedido(listaClientes: ArrayList<Cliente>, listaProdutos: ArrayList<Produto>) {
    val produtosSelecionados: MutableList<Produto> = remember { mutableStateListOf() }
    val mapaQntdProduto = remember { mutableStateMapOf<Produto, Int>() }
    val listaQntdProduto = ArrayList<QntdProduto>()
    val fieldId: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldCpf: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)
    val pedidoController = PedidoController()

    var expanded by remember { mutableStateOf(false) }


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
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    OutlinedTextField(
                        value = fieldId.value, onValueChange = {
                            fieldId.value = it
                        },
                        label = { Text(text = "Id do Pedido") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Magenta,
                            unfocusedBorderColor = Color(145, 89, 150, 255)
                        ),
                        modifier = Modifier
                            .width(270.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = fieldCpf.value, onValueChange = {
                            fieldCpf.value = it
                        },
                        label = { Text(text = "CPF do Cliente") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Magenta,
                            unfocusedBorderColor = Color(145, 89, 150, 255)
                        ),
                        modifier = Modifier
                            .width(270.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 60.dp, vertical = 36.dp)
                            .weight(1f)
                    ) {
                        val boxWidth = if (maxWidth > 300.dp) 300.dp else maxWidth

                        Box(
                            modifier = Modifier
                                //.align(Alignment.Center)
                                .width(boxWidth)
                        ) {
                            LazyColumn(
                                contentPadding = innerPadding,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                itemsIndexed(listaProdutos) { index, produto ->
                                    val quantidadeSelecionada =
                                        mapaQntdProduto[produto]?.let { remember { mutableStateOf(it) } }
                                            ?: remember { mutableStateOf(0) }

                                    // Spacer(modifier = Modifier.height(16.dp))
                                    Row {
                                        Checkbox(
                                            checked = produto in produtosSelecionados,
                                            onCheckedChange = { isChecked ->
                                                if (isChecked) {
                                                    produtosSelecionados.add(produto)
                                                } else {
                                                    produtosSelecionados.remove(produto)
                                                    mapaQntdProduto.remove(produto)
                                                }
                                            },
                                            modifier = Modifier.padding(vertical = 11.dp)
                                        )

                                        Text(
                                            text = produto.nome,
                                            Modifier
                                                .weight(2f)
                                                .padding(horizontal = 2.dp, vertical = 11.dp)
                                        )

                                        Row {
                                            Button(
                                                onClick = { quantidadeSelecionada.value++ },
                                                //modifier = Modifier.padding(15.dp, 15.dp),
                                                border = BorderStroke(1.dp, Color.Magenta),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = Color.Magenta
                                                )
                                            ) {
                                                Text(
                                                    text = "+",
                                                    color = Color.DarkGray,
                                                    fontSize = 16.sp
                                                )
                                            }
                                            Text(
                                                text = quantidadeSelecionada.value.toString(),
                                                color = Color.Magenta, fontSize = 16.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 11.dp
                                                )
                                            )

                                            Button(
                                                onClick = { if (quantidadeSelecionada.value > 0) quantidadeSelecionada.value-- },
                                                //modifier = Modifier.padding(10.dp, 15.dp),
                                                border = BorderStroke(1.dp, Color.Magenta),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = Color.Magenta
                                                )
                                            ) {
                                                Text(
                                                    text = "-",
                                                    color = Color.DarkGray,
                                                    fontSize = 16.sp
                                                )
                                            }
                                        }
                                        if (quantidadeSelecionada.value > 0) {
                                            mapaQntdProduto[produto] = quantidadeSelecionada.value
                                        } else {
                                            mapaQntdProduto.remove(produto)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(150.dp))
                            Button(
                                onClick = {
                                    // Lógica para finalizar o pedido com os produtos selecionados
                                    // Aqui você pode chamar o método para finalizar o pedido no backend
                                    // Exemplo: finalizarPedido(produtosSelecionados, mapaQntdProduto)
                                    val currunt = LocalDateTime.now(ZoneId.systemDefault())
                                    val formatter =
                                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                                    val formatted = currunt.format(formatter)

                                    val idPedido = fieldId.value.text
                                    val cpfCliente = fieldCpf.value.text
                                    if (idPedido.isNotEmpty() && cpfCliente.isNotEmpty() && produtosSelecionados.isNotEmpty()) {
                                        val clienteEncontrado = listaClientes.find {
                                            it.cpf == cpfCliente
                                        }
                                        if (clienteEncontrado != null) {
                                            listaQntdProduto.clear() // Limpar a lista antes de adicionar os produtos

                                            mapaQntdProduto.forEach { (produto, qntd) ->
                                                val prodQntd = QntdProduto(produto = produto, qntd)
                                                //listaQntdProduto.clear()
                                                listaQntdProduto.add(prodQntd)
                                                println("Finalizar pedido com o produto: ${produto.nome} e quantidade: ${qntd}, Data Local : ${formatted}")
                                            }

                                            val pedido =
                                                Pedido(
                                                    idPedido,
                                                    formatted,
                                                    cpfCliente,
                                                    listaQntdProduto
                                                )

                                            pedidoController.inserirPedido(
                                                pedido,
                                                contexto
                                            ) { sucesso ->
                                                if (sucesso) {
                                                    // Pedido realizado com sucesso
                                                    Toast.makeText(
                                                        contexto,
                                                        "Pedido Realizado Com Sucesso!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fieldId.value = TextFieldValue("")
                                                    fieldCpf.value = TextFieldValue("")
                                                } else {
                                                    // Erro ao realizar o pedido
                                                    Toast.makeText(
                                                        contexto,
                                                        "Erro ao Realizar o pedido",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fieldId.value = TextFieldValue("")
                                                }
                                            }
                                        } else {
                                            // CPF do cliente não encontrado na lista de clientes
                                            Toast.makeText(
                                                contexto,
                                                "CPF do Cliente não encontrado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            fieldCpf.value = TextFieldValue("")
                                        }
                                    } else {
                                        // Campos obrigatórios não preenchidos
                                        Toast.makeText(
                                            contexto,
                                            "Preencha todos os campos e selecione pelo menos um produto",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 120.dp),
                                border = BorderStroke(1.dp, Color.Magenta),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Magenta
                                )
                            ) {
                                Text(
                                    text = "Finalizar Pedido", color = Color.DarkGray,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

