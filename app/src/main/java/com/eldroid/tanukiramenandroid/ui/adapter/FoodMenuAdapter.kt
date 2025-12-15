package com.eldroid.tanukiramenandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.databinding.ItemFoodMenuBinding
import com.eldroid.tanukiramenandroid.model.FoodMenuItem

class FoodMenuAdapter(
    private val menuItems: List<FoodMenuItem>
) : RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodMenuViewHolder {
        val binding = ItemFoodMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodMenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount() = menuItems.size

    inner class FoodMenuViewHolder(private val binding: ItemFoodMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodMenuItem) {
            binding.foodName.text = item.name
            binding.foodPrice.text = String.format("P%.2f", item.price)
            binding.quantityText.text = item.quantity.toString()

            // You can use a library like Glide or Picasso to load the image
            // For now, it will use the placeholder
            // Glide.with(binding.root.context).load(item.imageUrl).into(binding.foodImage)

            binding.plusButton.setOnClickListener {
                item.quantity++
                binding.quantityText.text = item.quantity.toString()
            }

            binding.minusButton.setOnClickListener {
                if (item.quantity > 0) {
                    item.quantity--
                    binding.quantityText.text = item.quantity.toString()
                }
            }
        }
    }
}