package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasProduto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TelaProduto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElementosDaTela()
        }
    }
}

@Composable
private fun ElementosDaTela() {
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)
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
            Text(
                text = "Produtos",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(400.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp
            )
        }
        Column(modifier = Modifier.padding(70.dp, 0.dp)) {
            Spacer(modifier = Modifier.height(75.dp))
            Button(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaProdutoInserir::class.java
                        )
                    )
                },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Cadastrar Produto", color = Color.DarkGray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaProdutoMostrar::class.java
                        )
                    )
                },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Listar Produtos", color = Color.DarkGray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaProdutoAlterar::class.java
                        )
                    )
                },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Alterar Produto", color = Color.DarkGray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaProdutoDeletar::class.java
                        )
                    )
                },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Deletar Produto", color = Color.DarkGray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(150.dp))
            Button(
                onClick = { activity?.finish() },
                modifier = Modifier.width(440.dp),
                border = BorderStroke(1.dp, Color.Magenta),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta),
            ) {
                Text(text = "Voltar", color = Color.DarkGray, fontSize = 16.sp)
            }
        }
    }
}
