package com.example.guia5

import com.google.firebase.database.*

object DishRepository {
    private val database = FirebaseDatabase
        .getInstance("https://desafio02-dsm-default-rtdb.firebaseio.com")
    private val dishesRef = database.getReference("productos")

    val dishes = mutableListOf<Dish>()

    fun addDish(dish: Dish, callback: (Boolean) -> Unit) {
        val key = dishesRef.push().key ?: return callback(false)
        dishesRef.child(key).setValue(dish)
            .addOnSuccessListener {
                dishes.add(dish)
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun updateDish(key: String, updatedDish: Dish, callback: (Boolean) -> Unit) {
        dishesRef.child(key).setValue(updatedDish)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun deleteDish(key: String, callback: (Boolean) -> Unit) {
        dishesRef.child(key).removeValue()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun loadDishes(callback: (List<Pair<String, Dish>>) -> Unit) {
        dishesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dishes.clear()
                val result = mutableListOf<Pair<String, Dish>>()
                for (dishSnapshot in snapshot.children) {
                    val dish = dishSnapshot.getValue(Dish::class.java)
                    dish?.let { result.add(Pair(dishSnapshot.key!!, it)) }
                }
                callback(result)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}
