package com.example.pesujo_fabricasapatos_rafaelcaroni.Classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cliente(
    val cpf: String,
    val nome: String,
    val telefone: String,
    val endereco: String,
    val instagram: String? = ""
) : Parcelable


