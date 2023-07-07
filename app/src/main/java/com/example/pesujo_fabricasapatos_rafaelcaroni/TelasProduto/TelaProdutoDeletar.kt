package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ProdutoController
import kotlinx.coroutines.launch

class TelaProdutoDeletar : ComponentActivity() {
    lateinit var listaprodutos: ArrayList<Produto>
    lateinit var produtoController: ProdutoController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        produtoController = ProdutoController()
        lifecycleScope.launch {
            try {
                listaprodutos = produtoController.carregarListaProdutos()
                setContent {
                    DeletarProduto(listaProdutos = listaprodutos)
                }
            } catch (e: Exception) {
                setContent {
                    produtoController.LoadingScreen()
                    Log.i("errorPagDeletarProduto", "${e}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeletarProduto(listaProdutos: ArrayList<Produto>) {
    val activity: Activity? = (LocalContext.current as? Activity)
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(listaProdutos[0]) }
    val contexto: Context = LocalContext.current
    val produtoController = ProdutoController()

    val icon = if (expanded)
        Icons.Default.KeyboardArrowUp
    else
        Icons.Default.KeyboardArrowDown

    Card(
        border = BorderStroke(2.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(234, 223, 235, 255)),
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Card(
            border = BorderStroke(1.dp, Color.Magenta),
            modifier = Modifier.padding(30.dp, 15.dp)
        ) {
            Text(
                text = "Produto Selecionado",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(50.dp, 0.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { activity?.finish() },
                    modifier = Modifier
                        .padding(15.dp, 15.dp),
                    border = BorderStroke(1.dp, Color.Magenta),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
                ) {
                    Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        produtoController.deletarProduto(selectedItem, contexto) { sucesso ->
                            if (sucesso) {
                                activity?.recreate()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(15.dp, 15.dp),
                    border = BorderStroke(1.dp, Color.Magenta),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
                ) {
                    Text(text = "Deletar", color = Color.DarkGray, fontSize = 16.sp)
                }
            }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(25.dp, 50.dp)
            ) {

                OutlinedTextField(
                    value = "Id: ${selectedItem.id}, Nome ${selectedItem.nome}",
                    onValueChange = {},
                    label = {
                        Text(
                            text = "Id's"
                        )
                    },
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                icon,
                                "contentDescription",
                                //   Modifier.clickable { expanded = !expanded })
                            )
                        }
                    })
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listaProdutos.forEach { item ->
                        DropdownMenuItem(text = { Text(text = "Id: ${item.id}, Nome: ${item.nome}") },
                            onClick = {
                                selectedItem = item
                                expanded = false
                                Toast.makeText(contexto, "Produto selecionado", Toast.LENGTH_LONG)
                                    .show()
                            })
                        //Text(text = selectedItem.toString())
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 25.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, color = Color.Magenta),
                        colors = CardDefaults.elevatedCardColors(
                            Color(
                                236,
                                191,
                                235,
                                255
                            )
                        ),//cardColors(Color(236, 191, 235, 255)),
                        elevation = CardDefaults.cardElevation(15.dp, 2.dp, 3.dp, 4.dp, 5.dp, 6.dp)

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(model = selectedItem.imagem.toString()),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(130.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "${selectedItem.nome}, R$ ${selectedItem.valor}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = selectedItem.descricao.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

