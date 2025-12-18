package com.eldroid.tanukiramenandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.eldroid.tanukiramenandroid.databinding.ItemFoodMenuBinding
import com.eldroid.tanukiramenandroid.backend.model.MenuItem
import com.eldroid.tanukiramenandroid.backend.network.NetworkConfig


class FoodMenuAdapter(
    private val menuItems: List<MenuItem>,
    private val onOrderClick: (MenuItem) -> Unit
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
        fun bind(item: MenuItem) {

            binding.foodName.text = item.name
            binding.foodPrice.text = String.format("%.2f", item.price)
            binding.stockText.text = item.stockQuantity.toString()

            val imageUrl = "${NetworkConfig.BASE_URL}/uploads/${item.imagePath}"
            val glideUrl = GlideUrl(imageUrl)

            Glide.with(binding.root.context)
                .load(glideUrl)
                .placeholder(com.eldroid.tanukiramenandroid.R.drawable.img_loading)
                .error(com.eldroid.tanukiramenandroid.R.drawable.img_broken)
                .into(binding.foodImage)

            binding.orderButton.setOnClickListener {
                if (item.stockQuantity > 0) {
                    onOrderClick(item)
                }
            }
        }
    }
}