package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente

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
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TelaClienteInserir : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsercaoClientes()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsercaoClientes() {
    val fieldCpf: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldNome: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldTelefone: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldEndereco: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val fieldInsta: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }

    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)

    val refCliente = Firebase.database.getReference("Clientes")

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
                text = "Inserir Clientes",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp
            )
        }
        Column(modifier = Modifier.padding(70.dp, 0.dp)) {
            Spacer(modifier = Modifier.height(50.dp))
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
                    //Icon(painter = Icons.Rounded.ShoppingCart, contentDescription =  R.string.shopping_cart_content_desc)
                    //Image(painterResource(id = R.drawable.ic_cart) , contentDescription = )
                    Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        val cpf = fieldCpf.value.text.toString()
                        val nome = fieldNome.value.text.toString()
                        val telefone = fieldTelefone.value.text.toString()
                        val endereco = fieldEndereco.value.text.toString()
                        val insta = fieldInsta.value.text.toString()

                        if (cpf.isNotEmpty() && nome.isNotEmpty() && telefone.isNotEmpty() && endereco.isNotEmpty() && insta.isNotEmpty()) {
                            val cliente = Cliente(cpf, nome, telefone, endereco, insta)

                            refCliente.child(cliente.cpf).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (!snapshot.exists()) {
                                        refCliente.child(cliente.cpf).setValue(cliente)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    contexto,
                                                    "Cliente Inserido Com Sucesso!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // Limpar os campos após a atualização
                                                fieldCpf.value = TextFieldValue("")
                                                fieldNome.value = TextFieldValue("")
                                                fieldTelefone.value = TextFieldValue("")
                                                fieldEndereco.value = TextFieldValue("")
                                                fieldInsta.value = TextFieldValue("")
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    contexto,
                                                    "Erro ao Inserir o cliente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    } else {
                                        Toast.makeText(
                                            contexto,
                                            "Cliente já Inserido!",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Limpar os campos após a atualização
                                        fieldCpf.value = TextFieldValue("")
                                        fieldNome.value = TextFieldValue("")
                                        fieldTelefone.value = TextFieldValue("")
                                        fieldEndereco.value = TextFieldValue("")
                                        fieldInsta.value = TextFieldValue("")
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        contexto,
                                        "Erro ao Inserir o cliente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        } else {
                            Toast.makeText(
                                contexto,
                                "Preencha todos os campos do cliente",
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
}
