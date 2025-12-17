package com.eldroid.tanukiramenandroid.backend.model

import java.time.LocalDateTime

data class Payment(
    val paymentId: Long,
    val order: Order,
    val amount: Double,
    val paymentStatus: String,
    val paymentMethod: String,
    val cashier: User,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime
)
