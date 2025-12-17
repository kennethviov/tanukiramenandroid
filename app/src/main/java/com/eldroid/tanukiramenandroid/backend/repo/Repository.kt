package com.eldroid.tanukiramenandroid.backend.repo

import com.eldroid.tanukiramenandroid.backend.model.CreateOrderRequest
import com.eldroid.tanukiramenandroid.backend.model.MarkServedRequest
import com.eldroid.tanukiramenandroid.backend.network.RetrofitClient

class Repository {

    suspend fun login(username: String) =
        RetrofitClient.api.getUserByUsername(username)

    suspend fun fetchMenu() =
        RetrofitClient.api.getMenu()

    suspend fun fetchItem(id: Long) =
        RetrofitClient.api.getMenuItemById(id)

    suspend fun createOrder(request: CreateOrderRequest) =
        RetrofitClient.api.createOrder(request)

    suspend fun fetchOrders() =
        RetrofitClient.api.getAllOrders()

    suspend fun fetchOrder(id: Long) =
        RetrofitClient.api.getOrderById(id)

    suspend fun fetchOrdersByStatus(status: String) =
        RetrofitClient.api.getOrderByStatus(status)

    suspend fun markServed(orderId: Long, request: MarkServedRequest) =
        RetrofitClient.api.markOrderAsServed(orderId, request)

}