package com.example.pesujo_fabricasapatos_rafaelcaroni.Classes

import java.time.LocalDate

data class Pedido(
    val idPedido: String,
    val data: LocalDate,
    val cpf: String,
    val produtos: ArrayList<QntdProduto>
)
