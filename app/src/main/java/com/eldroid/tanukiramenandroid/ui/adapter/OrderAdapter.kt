package com.eldroid.tanukiramenandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.tanukiramenandroid.databinding.ItemOrderBinding
import com.eldroid.tanukiramenandroid.backend.model.Order

class OrderAdapter(
    private val onOrderClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.orderId.text = order.orderId.toString()
            binding.orderTotal.text = String.format("%.2f", order.total)
            binding.orderStatus.text = order.status
            binding.root.setOnClickListener {
                onOrderClick(order)
            }
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}