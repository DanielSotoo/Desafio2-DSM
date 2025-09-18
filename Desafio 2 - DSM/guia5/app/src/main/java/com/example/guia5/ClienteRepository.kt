package com.example.guia5

import com.google.firebase.database.*

object ClienteRepository {
    private val database = FirebaseDatabase
        .getInstance("https://desafio02-dsm-default-rtdb.firebaseio.com")
    private val clientesRef = database.getReference("clientes")

    val clientes = mutableListOf<Cliente>()

    fun addCliente(cliente: Cliente, callback: (Boolean) -> Unit) {
        clientesRef.child(cliente.id).setValue(cliente)
            .addOnSuccessListener {
                clientes.add(cliente)
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun loadClientes(callback: (List<Cliente>) -> Unit) {
        clientesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clientes.clear()
                for (clienteSnapshot in snapshot.children) {
                    val cliente = clienteSnapshot.getValue(Cliente::class.java)
                    cliente?.let { clientes.add(it) }
                }
                callback(clientes.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun loadClientesOnce(callback: (List<Cliente>) -> Unit) {
        clientesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clientes.clear()
                for (clienteSnapshot in snapshot.children) {
                    val cliente = clienteSnapshot.getValue(Cliente::class.java)
                    cliente?.let { clientes.add(it) }
                }
                callback(clientes.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}
