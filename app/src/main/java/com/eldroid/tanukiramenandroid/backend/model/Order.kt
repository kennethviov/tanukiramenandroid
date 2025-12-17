package com.eldroid.tanukiramenandroid.backend.model

import java.time.LocalDateTime

data class Order(
    val orderId: Long,
    val user: User,
    val date: String,
    val status: String,
    val total: Double,
    val items: List<OrderItem>
)
