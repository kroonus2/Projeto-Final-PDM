package com.example.pesujo_fabricasapatos_rafaelcaroni.Controllers

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Produto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ProdutoController {
    private val refProdutos = Firebase.database.getReference("Produtos")
    private val storageRef = Firebase.storage.getReference("images")

    fun inserirProduto(
        produto: Produto,
        contexto: Context,
        imagem: Bitmap,
        callback: (Boolean) -> Unit
    ) {
        salvarImagemNoStorage(imagem) { imagemUri ->
            if (imagemUri != null) {
                val produtoComImagem = produto.copy(imagem = imagemUri)
                refProdutos.child(produtoComImagem.id.toString())
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                refProdutos.child(produtoComImagem.id.toString())
                                    .setValue(produtoComImagem)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            contexto,
                                            "Produto #${produtoComImagem.id}, Inserido Com Sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        callback(true)
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            contexto,
                                            "Erro ao Inserir o Produto",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        callback(false)
                                    }
                            } else {
                                Toast.makeText(
                                    contexto,
                                    "Produto #${produtoComImagem.id}, já Existe!",
                                    Toast.LENGTH_LONG
                                ).show()
                                callback(false)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                contexto, "Erro ao Inserir o Produto", Toast.LENGTH_SHORT
                            ).show()
                            callback(false)
                        }
                    })
            } else {
                Toast.makeText(contexto, "Erro ao salvar a imagem", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun alterarProduto(produto: Produto, contexto: Context, callback: (Boolean) -> Unit) {

        refProdutos.child(produto.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        refProdutos.child(produto.id.toString()).setValue(produto)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Produto #${produto.id}, Alterado com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true) // Sucesso na alteração
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Alterar o Produto",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false) // Falha na alteração
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Produto #${produto.id} não encontrado",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        callback(false) // Falha na alteração
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(contexto, "Erro ao Alterar o Produto", Toast.LENGTH_SHORT).show()
                    callback(false) // Falha na alteração
                }
            })
    }

    fun deletarProduto(produto: Produto, contexto: Context, callback: (Boolean) -> Unit) {
        refProdutos.child(produto.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        refProdutos.child(produto.id.toString()).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Produto Deletado com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true) // Sucesso na exclusão
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Deletar o Produto",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false) // Falha na exclusão
                            }
                    } else {
                        Toast.makeText(contexto, "Produto não encontrado!", Toast.LENGTH_SHORT)
                            .show()
                        callback(false) // Falha na exclusão
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(contexto, "Erro ao buscar o Produto", Toast.LENGTH_SHORT).show()
                    callback(false) // Falha na exclusão
                }
            })
    }

    suspend fun carregarListaProdutos(): ArrayList<Produto> = suspendCoroutine { continuation ->

        val listaRetorno: ArrayList<Produto> = ArrayList()

        refProdutos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val gson = Gson()

                    for (i in snapshot.children) {
                        val json = gson.toJson(i.value)
                        val produto = gson.fromJson(json, Produto::class.java)

                        listaRetorno.add(
                            Produto(
                                produto.nome.toString(),
                                produto.descricao.toString(),
                                produto.valor.toFloat(),
                                produto.imagem,
                                produto.id.toLong()
                            )
                        )
                    }
                    continuation.resume(listaRetorno)
                } else {
                    continuation.resumeWithException(Exception("Nenhum item encontrado no banco de dados."))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    fun salvarImagemNoStorage(imagem: Bitmap, callback: (String?) -> Unit) {
        val imagemRef = storageRef.child("${System.currentTimeMillis()}.jpg")
        try {
            val bytes = ByteArrayOutputStream()
            imagem.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val uploadTask = imagemRef.putBytes(bytes.toByteArray()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imagemRef.downloadUrl.addOnCompleteListener { downloadTask ->
                        if (downloadTask.isSuccessful) {
                            val downloadUrl = downloadTask.result
                            val imagemUri = downloadUrl.toString()
                            callback(imagemUri) // Chama o callback com o URI da imagem
                        } else {
                            callback(null) // Trata falha ao obter a URL da imagem
                        }
                    }
                } else {
                    callback(null) // Trata falha ao fazer o upload da imagem
                }
            }
        } catch (e: Exception) {
            callback(null) // Trata exceção ao salvar a imagem
        }
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