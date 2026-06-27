package com.example.vpnsub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConfigAdapter(
    private val configs: List<Config>,
    private val onClick: (Config) -> Unit
) : RecyclerView.Adapter<ConfigAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val config = configs[position]
        holder.nameText.text = config.name
        holder.itemView.setOnClickListener { onClick(config) }
    }

    override fun getItemCount() = configs.size
}