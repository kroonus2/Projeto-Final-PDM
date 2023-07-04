package com.example.pesujo_fabricasapatos_rafaelcaroni.TelasCliente

import com.example.pesujo_fabricasapatos_rafaelcaroni.Classes.Cliente

sealed class ClienteDataState{
    class Success(val data : MutableList<Cliente>) : ClienteDataState()
    class Failure(val msg : String) : ClienteDataState()
    object Loanding : ClienteDataState()
    object Empty : ClienteDataState()
}
