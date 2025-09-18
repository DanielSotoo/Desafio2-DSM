package com.example.guia5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AgregarVentaActivity : AppCompatActivity() {
    private lateinit var spinnerClientes: Spinner
    private lateinit var listViewProductos: ListView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private val productosSeleccionados = mutableMapOf<String, Int>()
    private val productosMap = mutableMapOf<String, Dish>() // id -> Dish
    private val clientesList = mutableListOf<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_venta)

        spinnerClientes = findViewById(R.id.spinnerClientes)
        listViewProductos = findViewById(R.id.listViewProductos)
        btnGuardar = findViewById(R.id.btnGuardarVenta)
        btnCancelar = findViewById(R.id.btnCancelarVenta)

        // Cargar clientes desde Firebase
        ClienteRepository.loadClientes { clientes ->
            clientesList.clear()
            clientesList.addAll(clientes)
            val nombres = clientesList.map { "${it.id} - ${it.nombre}" }
            spinnerClientes.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombres)
        }

        // Cargar productos desde Firebase
        DishRepository.loadDishes { lista ->
            productosMap.clear()
            val productosNombres = mutableListOf<String>()
            for ((key, dish) in lista) {
                productosMap[key] = dish
                productosNombres.add("${dish.name} - $${dish.price} (Stock: ${dish.stock})")
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, productosNombres)
            listViewProductos.adapter = adapter
            listViewProductos.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        }

        btnGuardar.setOnClickListener {
            val clienteIndex = spinnerClientes.selectedItemPosition
            if (clienteIndex == -1) {
                Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cliente = clientesList[clienteIndex]
            productosSeleccionados.clear()

            for (i in 0 until listViewProductos.count) {
                if (listViewProductos.isItemChecked(i)) {
                    val key = productosMap.keys.elementAt(i)
                    productosSeleccionados[key] = 1 // por simplicidad 1 unidad
                }
            }

            if (productosSeleccionados.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = productosSeleccionados.entries.sumOf { (id, cantidad) ->
                val dish = productosMap[id]
                (dish?.price?.toDoubleOrNull() ?: 0.0) * cantidad
            }

            val idVenta = FirebaseDatabase.getInstance().reference.push().key ?: UUID.randomUUID().toString()
            val fecha = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())

            val venta = Venta(
                id = idVenta,
                cliente = cliente.id,
                fecha = fecha,
                productos = productosSeleccionados,
                total = total
            )

            VentaRepository.addVenta(venta) { success ->
                if (success) {
                    Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar venta", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
