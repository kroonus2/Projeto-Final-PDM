package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ProdutoController
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente.TelaCliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido.TelaPedido

class TelaProdutoInserir : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsercaoProdutos()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 11 && data != null) {
            var imagemBitmap = data.data
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsercaoProdutos() {
    val fieldNome: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldDescricao: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldValor: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldImage: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    var fieldId: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }

    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)
    var expanded by remember { mutableStateOf(false) }

    val produtoController = ProdutoController()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
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
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(234, 223, 235, 255),
            bottomBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Inserir Produtos",
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
                Column(modifier = Modifier.padding(70.dp, 0.dp)) {
                    Spacer(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(innerPadding)
                    )
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
                            keyboardType = KeyboardType.Text
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
                    Button(
                        onClick = {
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .padding(10.dp, 15.dp)
                            .align(Alignment.CenterHorizontally),
                        border = BorderStroke(1.dp, Color.Magenta),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Adicionar foto do produto",
                        )
                        Text(text = " Imagem", color = Color.DarkGray, fontSize = 16.sp)
                    }
                    imageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value =
                                MediaStore.Images.Media.getBitmap(contexto.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(contexto.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(250.dp, 150.dp)
                                    .padding(30.dp)
                                //.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Button(
                            onClick = {
                                val nome = fieldNome.value.text.toString()
                                val descricao = fieldDescricao.value.text.toString()
                                val valor = fieldValor.value.text.toString()
                                val imagem = fieldImage.value.text.toString()
                                val id = fieldId.value.text.toString()

                                if (nome.isNotEmpty() && descricao.isNotEmpty() && valor.isNotEmpty() && id.isNotEmpty()) {
                                    val produto =
                                        Produto(nome, descricao, valor.toFloat(), null, id.toLong())

                                    produtoController.inserirProduto(
                                        produto, contexto,
                                        bitmap.value!!
                                    ) { sucesso ->
                                        if (sucesso) {
                                            // Limpar os campos após a inserção
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
                            Text(text = "Inserir", color = Color.DarkGray, fontSize = 16.sp)
                        }
                    }
                }
            }
        )
    }
}
