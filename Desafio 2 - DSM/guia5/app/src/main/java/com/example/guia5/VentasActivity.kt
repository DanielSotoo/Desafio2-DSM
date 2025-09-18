package com.example.guia5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VentasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: VentaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        recyclerView = findViewById(R.id.recyclerViewVentas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<Button>(R.id.btnBackVentas)
        val btnAddVenta = findViewById<Button>(R.id.btnAddVenta)

        btnBack.setOnClickListener { finish() }
        btnAddVenta.setOnClickListener {
            startActivity(Intent(this, AgregarVentaActivity::class.java))
        }

        // Cargar ventas en onCreate también
        loadVentas()
    }

    override fun onResume() {
        super.onResume()
        loadVentas()
    }

    private fun loadVentas() {
        VentaRepository.loadVentas { lista ->
            runOnUiThread {
                adapter = VentaAdapter(lista)
                recyclerView.adapter = adapter
            }
        }
    }
}