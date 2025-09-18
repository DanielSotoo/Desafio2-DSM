package com.example.guia5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ClientesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        recyclerView = findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnAgregar = findViewById<Button>(R.id.btnAgregarCliente)
        val btnVolver = findViewById<Button>(R.id.btnVolverClientes)

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarClienteActivity::class.java))
        }

        btnVolver.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        ClienteRepository.loadClientes { lista ->
            adapter = ClienteAdapter(lista)
            recyclerView.adapter = adapter
        }
    }
}
