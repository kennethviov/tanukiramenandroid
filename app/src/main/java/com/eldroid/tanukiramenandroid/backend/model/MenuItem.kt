package com.eldroid.tanukiramenandroid.backend.model

data class MenuItem(
    val menuItemId: Long,
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    val imagePath: String,
    val stockQuantity: Int
)