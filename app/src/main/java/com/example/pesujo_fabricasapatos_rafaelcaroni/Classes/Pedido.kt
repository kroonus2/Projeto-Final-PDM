package com.example.pesujo_fabricasapatos_rafaelcaroni.Classes

import org.threeten.bp.LocalDateTime;

data class Pedido(
    val idPedido: String,
    val data: String,
    val cpf: String,
    val produtos: ArrayList<QntdProduto>
)
