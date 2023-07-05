package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
                    InserirPedido2(listaClientes, listaProdutos)
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
    val pedidoController = PedidoController()

    Column(modifier = Modifier.padding(16.dp)) {
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
            )
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
            )
        )
        //Carregar Os Produtos em CheckBoxs
        listaProdutos.forEach { produto ->
            val quantidadeSelecionada =
                mapaQntdProduto[produto]?.let { remember { mutableStateOf(it) } }
                    ?: remember { mutableStateOf(0) }
            //Mapeamento para "Linkar" um produto a sua devida quantidade
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = produto in produtosSelecionados, onCheckedChange = { isChecked ->
                    if (isChecked) {
                        produtosSelecionados.add(produto)
                    } else {
                        produtosSelecionados.remove(produto)
                        mapaQntdProduto.remove(produto)
                    }
                })

                Text(text = produto.nome, Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { quantidadeSelecionada.value++ }) {
                        Text(text = "+")
                    }

                    Text(
                        text = quantidadeSelecionada.value.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Button(onClick = { if (quantidadeSelecionada.value > 0) quantidadeSelecionada.value-- }) {
                        Text(text = "-")
                    }
                }
            }

            if (quantidadeSelecionada.value > 0) {
                mapaQntdProduto[produto] = quantidadeSelecionada.value
            } else {
                mapaQntdProduto.remove(produto)
            }
        }

        Spacer(modifier = Modifier.height(160.dp))

        Button(onClick = {
            // Lógica para finalizar o pedido com os produtos selecionados
            // Aqui você pode chamar o método para finalizar o pedido no backend
            // Exemplo: finalizarPedido(produtosSelecionados, mapaQntdProduto)
            val currunt = LocalDateTime.now(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val formatted = currunt.format(formatter)

            val idPedido = fieldId.value.text
            val cpfCliente = fieldCpf.value.text
            if (idPedido.isNotEmpty() && cpfCliente.isNotEmpty() && produtosSelecionados.isNotEmpty()) {
                val clienteEncontrado = listaClientes.find {
                    it.cpf == cpfCliente
                }
                if (clienteEncontrado != null) {
                    mapaQntdProduto.forEach { (produto, qntd) ->
                        val prodQntd = QntdProduto(produto = produto, qntd)
                        //listaQntdProduto.clear()
                        listaQntdProduto.add(prodQntd)
                        println("Finalizar pedido com o produto: ${produto.nome} e quantidade: ${qntd}, Data Local : ${formatted}")
                    }

                    val pedido = Pedido(idPedido, formatted, cpfCliente, listaQntdProduto)

                    pedidoController.inserirPedido(pedido, contexto) { sucesso ->
                        if (sucesso) {
                            // Pedido realizado com sucesso
                            Toast.makeText(
                                contexto,
                                "Pedido Realizado Com Sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Erro ao realizar o pedido
                            Toast.makeText(
                                contexto,
                                "Erro ao Realizar o pedido",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } else {
                    // CPF do cliente não encontrado na lista de clientes
                    Toast.makeText(contexto, "CPF do Cliente não encontrado", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                // Campos obrigatórios não preenchidos
                Toast.makeText(
                    contexto,
                    "Preencha todos os campos e selecione pelo menos um produto",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(text = "Finalizar Pedido")
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun InserirPedido2(listaClientes: ArrayList<Cliente>, listaProdutos: ArrayList<Produto>) {
    val produtosSelecionados: MutableList<Produto> = remember { mutableStateListOf() }
    val mapaQntdProduto = remember { mutableStateMapOf<Produto, Int>() }
    val listaQntdProduto = ArrayList<QntdProduto>()
    val fieldId: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldCpf: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val contexto: Context = LocalContext.current
    val pedidoController = PedidoController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(16.dp)) {
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
                    )
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
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(listaProdutos) { index, produto ->
                            val quantidadeSelecionada =
                                mapaQntdProduto[produto]?.let { remember { mutableStateOf(it) } }
                                    ?: remember { mutableStateOf(0) }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = produto in produtosSelecionados,
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            produtosSelecionados.add(produto)
                                        } else {
                                            produtosSelecionados.remove(produto)
                                            mapaQntdProduto.remove(produto)
                                        }
                                    }
                                )

                                Text(text = produto.nome, Modifier.weight(1f))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(onClick = { quantidadeSelecionada.value++ }) {
                                        Text(text = "+")
                                    }

                                    Text(
                                        text = quantidadeSelecionada.value.toString(),
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )

                                    Button(onClick = { if (quantidadeSelecionada.value > 0) quantidadeSelecionada.value-- }) {
                                        Text(text = "-")
                                    }
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
                Spacer(modifier = Modifier.height(160.dp))
                Button(onClick = {
                    // Lógica para finalizar o pedido com os produtos selecionados
                    // Aqui você pode chamar o método para finalizar o pedido no backend
                    // Exemplo: finalizarPedido(produtosSelecionados, mapaQntdProduto)
                    val currunt = LocalDateTime.now(ZoneId.systemDefault())
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
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

                            val pedido = Pedido(idPedido, formatted, cpfCliente, listaQntdProduto)

                            pedidoController.inserirPedido(pedido, contexto) { sucesso ->
                                if (sucesso) {
                                    // Pedido realizado com sucesso
                                    Toast.makeText(
                                        contexto,
                                        "Pedido Realizado Com Sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Erro ao realizar o pedido
                                    Toast.makeText(
                                        contexto,
                                        "Erro ao Realizar o pedido",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        } else {
                            // CPF do cliente não encontrado na lista de clientes
                            Toast.makeText(
                                contexto,
                                "CPF do Cliente não encontrado",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        // Campos obrigatórios não preenchidos
                        Toast.makeText(
                            contexto,
                            "Preencha todos os campos e selecione pelo menos um produto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text(text = "Finalizar Pedido")
                }
            }
        }
    )
}
