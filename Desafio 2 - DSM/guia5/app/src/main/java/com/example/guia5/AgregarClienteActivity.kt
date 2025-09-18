package com.example.guia5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etId = findViewById<EditText>(R.id.etId)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCliente)
        val btnVolver = findViewById<Button>(R.id.btnVolverFormulario)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val id = etId.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty() || correo.isEmpty() || id.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!(telefono.matches(Regex("\\d{8}")) || telefono.matches(Regex("\\d{4}-\\d{4}")))) {
                Toast.makeText(this, "El número de teléfono debe tener 8 dígitos o formato XXXX-XXXX", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ClienteRepository.clientes.any { it.id == id }) {
                Toast.makeText(this, "Ya existe un cliente con ese ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mostrar loading
            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            val cliente = Cliente(nombre, correo, id, telefono)
            ClienteRepository.addCliente(cliente) { success ->
                btnGuardar.isEnabled = true
                btnGuardar.text = "Guardar"

                if (success) {
                    Toast.makeText(this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
