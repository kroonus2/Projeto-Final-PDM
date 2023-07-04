package com.example.pesujo_fabricasapatos_rafaelcaroni.Classes

import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Parcelize
data class Produto(
    val nome: String,
    val descricao: String,
    val valor: Float,
    val imagem: String?,// Novo atributo para armazenar o código URI da imagem
    val id: Long
) : Parcelable

