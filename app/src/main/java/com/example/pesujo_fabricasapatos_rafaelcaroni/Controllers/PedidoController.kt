package com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Pedido
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PedidoController {
    private val refPedidos = Firebase.database.getReference("Pedidos")

    fun inserirPedido(pedido: Pedido, contexto: Context, callback: (Boolean) -> Unit) {
        refPedidos.child(pedido.idPedido)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        refPedidos.child(pedido.idPedido).setValue(pedido)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Pedido Realizado Com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Realizar o pedido",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false)
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Id do Pedido #${pedido.idPedido} já Inserido!",
                            Toast.LENGTH_LONG
                        ).show()
                        callback(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        contexto,
                        "Erro ao Realizar o pedido",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("Error", "${error}")
                    callback(false)
                }
            })
    }

    fun alterarPedido(pedido: Pedido, contexto: Context, callback: (Boolean) -> Unit) {

        refPedidos.child(pedido.idPedido)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        refPedidos.child(pedido.idPedido).setValue(pedido)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Pedido #${pedido.idPedido} Alterado com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true) // Sucesso na alteração
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Alterar o Pedido",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false) // Falha na alteração
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Pedido #${pedido.idPedido} não encontrado",
                            Toast.LENGTH_LONG
                        ).show()
                        callback(false) // Falha na alteração
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        contexto,
                        "Erro ao Alterar o Pedido",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false) // Falha na alteração
                }
            })
    }


    fun deletarPedido(pedido: Pedido, contexto: Context, callback: (Boolean) -> Unit) {
        refPedidos.child(pedido.idPedido)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        refPedidos.child(pedido.idPedido).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Pedido Deletado Com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true) // Sucesso na exclusão
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Deletar o pedido",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false) // Falha na exclusão
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Pedido não encontrado!",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback(false) // Falha na exclusão
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        contexto,
                        "Erro ao buscar o pedido",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false) // Falha na exclusão
                }
            })
    }


    suspend fun carregarListaPedidos(): ArrayList<Pedido> = suspendCoroutine { continuation ->
        val listaRetorno: ArrayList<Pedido> = ArrayList()

        refPedidos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var gson = Gson()

                    for (i in snapshot.children) {
                        val json = gson.toJson(i.value)
                        val pedido = gson.fromJson(json, Pedido::class.java)

                        listaRetorno.add(
                            Pedido(
                                pedido.idPedido,
                                pedido.data.toString(),
                                pedido.cpf,
                                pedido.produtos
                            )
                        )
                        Log.i("ListaRetornoPedidos", "${listaRetorno}")
                    }
                    continuation.resume(listaRetorno)
                } else {
                    continuation.resumeWithException(Exception("Nenhum Pedido encontrado no banco de dados."))
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