package com.example.guia5


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val ivDish = findViewById<ImageView>(R.id.ivDish)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val name = intent.getStringExtra("name") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val imageName = intent.getStringExtra("imageName") ?: ""

        tvName.text = name
        tvDescription.text = description
        tvPrice.text = price

        val resId = resources.getIdentifier(imageName, "drawable",
            packageName)
        if (resId != 0) ivDish.setImageResource(resId)
        else ivDish.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
