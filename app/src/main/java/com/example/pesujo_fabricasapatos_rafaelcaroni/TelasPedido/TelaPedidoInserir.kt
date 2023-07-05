package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.Checkbox
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.QntdProduto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ClienteController
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.PedidoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ProdutoController
import kotlinx.coroutines.launch

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

    Column(modifier = Modifier.padding(16.dp)) {
        listaProdutos.forEach { produto ->
            val quantidadeSelecionada =
                mapaQntdProduto[produto]?.let { remember { mutableStateOf(it) } }
                    ?: remember { mutableStateOf(0) }

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
            mapaQntdProduto.forEach { (produto, qntd) ->
                val prodQntd = QntdProduto(produto = produto, qntd)
                //listaQntdProduto.clear()
                listaQntdProduto.add(prodQntd)
                println("Finalizar pedido com o produto: ${produto.nome} e quantidade: $qntd")
                println("Teste Lista -->"+listaQntdProduto)
            }
        }) {
            Text(text = "Finalizar Pedido")
        }
    }
}

