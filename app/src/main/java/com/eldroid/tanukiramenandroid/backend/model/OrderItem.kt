package com.eldroid.tanukiramenandroid.backend.model

data class OrderItem(
    val orderItemId: Long,
    val order: Order,
    val menuItem: MenuItem,
    val quantity: Int,
    val itemPrice: Double,
    val subtotal: Double
)
