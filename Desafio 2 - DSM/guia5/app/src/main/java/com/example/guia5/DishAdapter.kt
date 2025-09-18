package com.example.guia5

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DishAdapter(
    private val items: List<Pair<String, Dish>>
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDish: ImageView = itemView.findViewById(R.id.ivDish)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val (key, dish) = items[position]
        holder.tvName.text = dish.name
        holder.tvPrice.text = "Precio: ${dish.price}"
        holder.tvStock.text = "Stock: ${dish.stock}"
        val ctx = holder.itemView.context
        val resId = ctx.resources.getIdentifier(dish.imageName, "drawable", ctx.packageName)
        holder.ivDish.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)

        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, DetailActivity::class.java).apply {
                putExtra("name", dish.name)
                putExtra("description", dish.description)
                putExtra("price", dish.price)
                putExtra("imageName", dish.imageName)
            }
            ctx.startActivity(intent)
        }

        holder.btnEdit.setOnClickListener {
            val intent = Intent(ctx, EditDishActivity::class.java).apply {
                putExtra("key", key)
            }
            ctx.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            DishRepository.deleteDish(key) { success ->

            }
        }
    }

    override fun getItemCount(): Int = items.size
}
