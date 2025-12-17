package com.eldroid.tanukiramenandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.tanukiramenandroid.databinding.ItemCartBinding
import com.eldroid.tanukiramenandroid.backend.model.CartItem

class CartAdapter(
    private val onIncrement: (CartItem) -> Unit,
    private val onDecrement: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.foodName.text = cartItem.menuItem.name
            binding.foodPrice.text = String.format("%.2f", cartItem.menuItem.price)
            binding.quantityText.text = cartItem.quantity.toString()
            binding.foodSubtotal.text = String.format("%.2f", cartItem.menuItem.price * cartItem.quantity)

            binding.plusButton.setOnClickListener {
                onIncrement(cartItem)
            }

            binding.minusButton.setOnClickListener {
                onDecrement(cartItem)
            }
        }
    }
}

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.menuItem.name == newItem.menuItem.name
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}