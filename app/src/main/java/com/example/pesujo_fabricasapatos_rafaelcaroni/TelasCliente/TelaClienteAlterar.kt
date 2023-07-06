package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers.ClienteController
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasPedido.TelaPedido
import com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto.TelaProduto
import kotlinx.coroutines.launch

class TelaClienteAlterar : ComponentActivity() {
    lateinit var listaClientes: ArrayList<Cliente>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clienteController = ClienteController()
        lifecycleScope.launch {
            try {
                val cpfCliente: String? = intent.getStringExtra("cpfCliente")
                listaClientes = clienteController.carregarListaClientes()
                setContent {
                    alterarCliente(listaClientes, cpfCliente)
                }
            } catch (e: Exception) {
                setContent {
                    clienteController.LoadingScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun alterarCliente(
    listaClientes: ArrayList<Cliente>,
    clienteExistente: String? = null,
) {

    val fieldCpf: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldNome: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldTelefone: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldEndereco: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldInsta: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }

    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)
    var expanded by remember { mutableStateOf(false) }
    val clienteController = ClienteController()

    val clienteExistente: Cliente? = listaClientes.find { it.cpf == clienteExistente }

    // Preencher os campos com os valores do pedido existente, se disponível
    clienteExistente?.let { cliente ->
        fieldCpf.value = TextFieldValue(cliente.cpf)
        fieldNome.value = TextFieldValue(cliente.nome)
        fieldTelefone.value = TextFieldValue(cliente.telefone)
        fieldEndereco.value = TextFieldValue(cliente.endereco)
        fieldInsta.value = TextFieldValue(cliente.instagram.toString())
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
                Column(modifier = Modifier.padding(70.dp, 0.dp)) {
                    Spacer(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(innerPadding)
                    )
                    OutlinedTextField(
                        value = fieldCpf.value, onValueChange = {
                            fieldCpf.value = it
                        },
                        label = { Text(text = "CPF") },
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
                        value = fieldTelefone.value, onValueChange = {
                            fieldTelefone.value = it
                        },
                        label = { Text(text = "Telefone") },
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
                        value = fieldEndereco.value, onValueChange = {
                            fieldEndereco.value = it
                        },
                        label = { Text(text = "Endereço") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Magenta,
                            unfocusedBorderColor = Color(145, 89, 150, 255)
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    OutlinedTextField(
                        value = fieldInsta.value, onValueChange = {
                            fieldInsta.value = it
                        },
                        label = { Text(text = "Instagram") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Magenta,
                            unfocusedBorderColor = Color(145, 89, 150, 255)
                        )
                    )
                    Spacer(modifier = Modifier.height(150.dp))
                    Button(
                        onClick = {
                            val cpf = fieldCpf.value.text.toString()
                            val nome = fieldNome.value.text.toString()
                            val telefone = fieldTelefone.value.text.toString()
                            val endereco = fieldEndereco.value.text.toString()
                            val insta = fieldInsta.value.text.toString()

                            if (cpf.isNotEmpty() && nome.isNotEmpty() && telefone.isNotEmpty() && endereco.isNotEmpty() && insta.isNotEmpty()) {
                                val cliente = Cliente(cpf, nome, telefone, endereco, insta)

                                clienteController.alterarCliente(cliente, contexto) { sucesso ->
                                    if (sucesso) {
                                        // Limpar os campos após a atualização
                                        fieldCpf.value = TextFieldValue("")
                                        fieldNome.value = TextFieldValue("")
                                        fieldTelefone.value = TextFieldValue("")
                                        fieldEndereco.value = TextFieldValue("")
                                        fieldInsta.value = TextFieldValue("")

                                        //activity?.finish()
                                        Intent().apply {
                                            activity?.setResult(Activity.RESULT_OK, this)
                                        }
                                        activity?.finish()
                                    } else {
                                        // Limpar os campos após a atualização
                                        fieldCpf.value = TextFieldValue("")
                                        activity?.finish()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    contexto,
                                    "Preencha todos os campos do cliente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        border = BorderStroke(1.dp, Color.Magenta),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Magenta
                        )
                    ) {
                        Text(
                            text = "Alterar Cliente", color = Color.DarkGray,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        )
    }
}


