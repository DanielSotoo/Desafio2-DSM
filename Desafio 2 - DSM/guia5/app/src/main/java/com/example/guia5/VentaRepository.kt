package com.example.guia5

import com.google.firebase.database.*

object VentaRepository {
    private val database = FirebaseDatabase
        .getInstance("https://desafio02-dsm-default-rtdb.firebaseio.com")
    private val ventasRef = database.getReference("ventas")

    fun addVenta(venta: Venta, callback: (Boolean) -> Unit) {
        ventasRef.child(venta.id).setValue(venta)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun loadVentas(callback: (List<Venta>) -> Unit) {
        ventasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ventas = mutableListOf<Venta>()
                try {
                    for (ventaSnapshot in snapshot.children) {
                        val venta = ventaSnapshot.getValue(Venta::class.java)
                        venta?.let { ventas.add(it) }
                    }
                    callback(ventas)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                callback(emptyList())
            }
        })
    }
}