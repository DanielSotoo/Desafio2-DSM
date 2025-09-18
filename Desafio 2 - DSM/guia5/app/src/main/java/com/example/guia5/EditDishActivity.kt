package com.example.guia5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditDishActivity : AppCompatActivity() {
    private var dishKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_dish)

        val etName = findViewById<EditText>(R.id.etName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etImage = findViewById<EditText>(R.id.etImage)
        val etStock = findViewById<EditText>(R.id.etStock)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnBack = findViewById<Button>(R.id.btnBack)

        dishKey = intent.getStringExtra("key")

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val image = etImage.text.toString().trim()
            val stockText = etStock.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || image.isEmpty() || stockText.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val stock = stockText.toIntOrNull()
            val price = priceText.toDoubleOrNull()

            if (stock == null || stock <= 0) {
                Toast.makeText(this, "El stock debe ser un número mayor que 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (price == null || price < 0.0) {
                Toast.makeText(this, "El precio debe ser un número mayor o igual a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dish = Dish(name, description, priceText, image, stock)

            if (dishKey == null) {
                DishRepository.addDish(dish) { success ->
                    if (success) finish()
                    else Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                DishRepository.updateDish(dishKey!!, dish) { success ->
                    if (success) finish()
                    else Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
