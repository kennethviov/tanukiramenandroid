package com.eldroid.tanukiramenandroid.model

data class FoodMenuItem(
    val name: String,
    val price: Double,
    val imageUrl: String, // Or Int for local drawables
    var quantity: Int = 0
)