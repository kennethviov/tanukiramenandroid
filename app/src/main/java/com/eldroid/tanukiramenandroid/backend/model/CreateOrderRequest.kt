package com.eldroid.tanukiramenandroid.backend.model

data class CreateOrderRequest(
    val waiterId: Long,
    val items: Map<Long, Int>
)
