package com.example.guia5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DishAdapter
    private var dishesList: List<Pair<String, Dish>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargar datos en tiempo real desde Firebase
        DishRepository.loadDishes { lista ->
            dishesList = lista
            adapter = DishAdapter(dishesList)
            recyclerView.adapter = adapter
        }

        val btnAddDish = findViewById<Button>(R.id.btnAddDish)
        btnAddDish?.setOnClickListener {
            val intent = Intent(this, EditDishActivity::class.java)
            startActivity(intent)
        }

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        DishRepository.loadDishes { lista ->
            dishesList = lista
            adapter = DishAdapter(dishesList)
            recyclerView.adapter = adapter
        }
    }
}
