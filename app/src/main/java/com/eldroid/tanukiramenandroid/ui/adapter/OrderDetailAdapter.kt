package com.eldroid.tanukiramenandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.tanukiramenandroid.backend.model.OrderItem
import com.eldroid.tanukiramenandroid.databinding.ItemOrderDetailBinding

class OrderDetailAdapter : ListAdapter<OrderItem, OrderDetailAdapter.OrderDetailViewHolder>(OrderDetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class OrderDetailViewHolder(private val binding: ItemOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderItem) {
            binding.foodName.text = item.menuItem.name
            binding.foodQuantity.text = item.quantity.toString()
        }
    }
}

class OrderDetailDiffCallback : DiffUtil.ItemCallback<OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.menuItem.name == newItem.menuItem.name
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }
}