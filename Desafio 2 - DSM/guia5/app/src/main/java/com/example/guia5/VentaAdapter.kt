package com.example.guia5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VentaAdapter(private val ventas: List<Venta>) :
    RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    class VentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvVentaId)
        val tvCliente: TextView = itemView.findViewById(R.id.tvVentaCliente)
        val tvFecha: TextView = itemView.findViewById(R.id.tvVentaFecha)
        val tvTotal: TextView = itemView.findViewById(R.id.tvVentaTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = ventas[position]

        // Validaciones para evitar nulls o valores vacíos
        holder.tvId.text = "ID: ${if (venta.id.isNotEmpty()) venta.id else "N/A"}"
        holder.tvCliente.text = "Cliente: ${if (venta.cliente.isNotEmpty()) venta.cliente else "N/A"}"
        holder.tvFecha.text = "Fecha: ${if (venta.fecha.isNotEmpty()) venta.fecha else "N/A"}"
        holder.tvTotal.text = "Total: $${String.format("%.2f", venta.total)}"
    }

    override fun getItemCount(): Int = ventas.size
}