package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class TelaClienteMostrar : ComponentActivity() {
    lateinit var listaCliente: ArrayList<Cliente>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listaCliente = intent.getParcelableArrayListExtra("listaRetorno")!!

        setContent {
            ListagemDeClientes(listaCliente)
            Log.i("Teste Listagem", "" + listaCliente)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ListagemDeClientes(listaClientes: ArrayList<Cliente>) {
    val listState: LazyListState = rememberLazyListState()
    val activity: Activity? = (LocalContext.current as? Activity)

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
                text = "Listagem de Clientes",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 32.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(70.dp, 0.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = { activity?.finish() },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                state = listState,
                contentPadding = PaddingValues(6.dp, 3.dp),
            ) {
                items(
                    items = listaClientes,
                    itemContent = {
                        Card(
                            modifier = Modifier
                                .padding(15.dp, 5.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                            border = BorderStroke(1.dp, color = Color.Magenta)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = it.nome.toString(), style = typography.titleLarge)
                                Text(
                                    text = it.cpf.toString(),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = it.telefone.toString(),
                                    style = typography.bodySmall
                                )
                                Text(
                                    text = it.endereco.toString(),
                                    style = typography.bodySmall
                                )
                                Text(
                                    text = it.instagram.toString(),
                                    style = typography.bodySmall
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
