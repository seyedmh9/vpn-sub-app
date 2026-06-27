package com.example.vpnsub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubscriptionAdapter(
    private val subscriptions: List<Subscription>,
    private val onClick: (Subscription) -> Unit
) : RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = subscriptions[position]
        holder.nameText.text = sub.name
        holder.itemView.setOnClickListener { onClick(sub) }
    }

    override fun getItemCount() = subscriptions.size
}