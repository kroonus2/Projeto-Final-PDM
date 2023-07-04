package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ProdutoController

class TelaProdutoAlterar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlterarProduto()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlterarProduto() {
    val fieldNome: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldDescricao: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldValor: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldImage: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    var fieldId: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)

    val produtoController = ProdutoController()

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
                text = "Inserir Produtos",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp
            )
        }
        Column(modifier = Modifier.padding(70.dp, 0.dp)) {
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = fieldId.value, onValueChange = {
                    fieldId.value = it
                },
                label = { Text(text = "#Id") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = fieldNome.value, onValueChange = {
                    fieldNome.value = it
                },
                label = { Text(text = "Nome") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = fieldDescricao.value, onValueChange = {
                    fieldDescricao.value = it
                },
                label = { Text(text = "Descrição") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = fieldValor.value, onValueChange = {
                    fieldValor.value = it
                },
                label = { Text(text = "Valor (R$)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = fieldImage.value, onValueChange = {
                    fieldImage.value = it
                },
                label = { Text(text = "Imagem/Foto (URL)") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                ),
                enabled = false

            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                        activity?.finish()
                    },
                    modifier = Modifier
                        .padding(15.dp, 15.dp),
                    border = BorderStroke(1.dp, Color.Magenta),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
                ) {
                    Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        val nome = fieldNome.value.text.toString()
                        val descricao = fieldDescricao.value.text.toString()
                        val valor = fieldValor.value.text.toString()
                        val imagem = fieldImage.value.text.toString()
                        val id = fieldId.value.text.toString()

                        if (nome.isNotEmpty() && descricao.isNotEmpty() && valor.isNotEmpty() && imagem.isNotEmpty() && id.isNotEmpty()) {
                            val produto =
                                Produto(nome, descricao, valor.toFloat(), imagem, id.toLong())
                            produtoController.alterarProduto(produto, contexto) { sucesso ->
                                if (sucesso) {
                                    // Limpar os campos após a atualização
                                    fieldNome.value = TextFieldValue("")
                                    fieldDescricao.value = TextFieldValue("")
                                    fieldValor.value = TextFieldValue("")
                                    fieldImage.value = TextFieldValue("")
                                } else {
                                    // Limpar os campos após a inserção
                                    fieldId.value = TextFieldValue("")
                                }
                            }
                        } else {
                            Toast.makeText(
                                contexto,
                                "Preencha todos os campos do produto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(15.dp, 15.dp),
                    border = BorderStroke(1.dp, Color.Magenta),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
                ) {
                    Text(text = "Alterar", color = Color.DarkGray, fontSize = 16.sp)
                }
            }
        }
    }
}
