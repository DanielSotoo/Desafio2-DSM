package com.example.guia5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnMenuIcon = findViewById<Button>(R.id.btnMenuIcon)
        btnMenuIcon.setOnClickListener {
            val popup = PopupMenu(this, btnMenuIcon)
            popup.menu.add("Platillos")
            popup.menu.add("Clientes")
            popup.menu.add("Ventas")

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Platillos" -> startActivity(Intent(this, MainActivity::class.java))
                    "Clientes" -> startActivity(Intent(this, ClientesActivity::class.java))
                    "Ventas" -> startActivity(Intent(this, VentasActivity::class.java))
                }
                true
            }

            popup.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_products -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_clients -> {
                startActivity(Intent(this, ClientesActivity::class.java))
                true
            }
            R.id.menu_sales -> {
                startActivity(Intent(this, VentasActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
