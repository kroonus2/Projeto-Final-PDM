package com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ClienteController {
    val refCliente = Firebase.database.getReference("Clientes")

    fun inserirCliente(cliente: Cliente, contexto : Context, callback: (Boolean) -> Unit){
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
                            callback(true)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                contexto,
                                "Erro ao Inserir o cliente",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(false)
                        }
                } else {
                    Toast.makeText(
                        contexto,
                        "CPF${cliente.cpf} já Inserido!",
                        Toast.LENGTH_LONG
                    ).show()
                    callback(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    contexto,
                    "Erro ao Inserir o cliente",
                    Toast.LENGTH_SHORT
                ).show()
                callback(false)
            }
        })
    }

    fun deletarCliente(cliente: Cliente, contexto : Context, callback: (Boolean) -> Unit){

        refCliente.child(cliente.cpf).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    refCliente.child(cliente.cpf).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                contexto,
                                "Cliente Deletado Com Sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(true) // Sucesso na exclusão
                        }
                        .addOnFailureListener{
                            Toast.makeText(
                                contexto,
                                "Erro ao Deletar o cliente",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(false) // Falha na exclusão
                        }
                }else{
                    Toast.makeText(
                        contexto,
                        "Cliente não encontrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false) // Falha na exclusão
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    contexto,
                    "Erro ao buscar o cliente",
                    Toast.LENGTH_SHORT
                ).show()
                callback(false) // Falha na exclusão
            }
        })
    }

    //Finalizar
    fun alterarCliente(cliente: Cliente, contexto: Context, callback: (Boolean) -> Unit){
        refCliente.child(cliente.cpf).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    refCliente.child(cliente.cpf).setValue(cliente)
                        .addOnSuccessListener {
                            Toast.makeText(
                                contexto,
                                "Cliente Atualizado Com Sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(true)
//                            // Limpar os campos após a atualização
//                            fieldCpf.value = TextFieldValue("")
//                            fieldNome.value = TextFieldValue("")
//                            fieldTelefone.value = TextFieldValue("")
//                            fieldEndereco.value = TextFieldValue("")
//                            fieldInsta.value = TextFieldValue("")
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                contexto,
                                "Erro ao atualizar o cliente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        contexto,
                        "Cliente não encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    contexto,
                    "Erro ao buscar o cliente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    suspend fun carregarListaClientes(): ArrayList<Cliente> = suspendCoroutine{continuation ->
        val listaRetorno : ArrayList<Cliente> = ArrayList()

        refCliente.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var gson = Gson()

                    for (i in snapshot.children) {
                        val json = gson.toJson(i.value)
                        val cliente = gson.fromJson(json, Cliente::class.java)

                        listaRetorno.add(
                            Cliente(
                                cliente.cpf,
                                cliente.nome,
                                cliente.telefone,
                                cliente.endereco,
                                cliente.instagram
                            )
                        )
                    }
                    continuation.resume(listaRetorno)
                }else{
                    continuation.resumeWithException(Exception("Nenhum Cliente encontrado no banco de dados."))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    @Composable
    fun LoadingScreen() {
        val activity: Activity? = (LocalContext.current as? Activity)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Carregando produtos...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(150.dp))
                Button(
                    onClick = { activity?.finish() },
                    modifier = Modifier.width(150.dp),
                    border = BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                ) {
                    Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
                }
            }
        }
    }
}