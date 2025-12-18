package com.eldroid.tanukiramenandroid.backend.network

import com.eldroid.tanukiramenandroid.backend.model.CreateOrderRequest
import com.eldroid.tanukiramenandroid.backend.model.MarkServedRequest
import com.eldroid.tanukiramenandroid.backend.model.MenuItem
import com.eldroid.tanukiramenandroid.backend.model.Order
import com.eldroid.tanukiramenandroid.backend.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //USER
    @GET("users/username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): Response<User>

    //MENU
    @GET("menu")
    suspend fun  getMenu(): Response<List<MenuItem>>

    @GET("menu/{id}")
    suspend fun getMenuItemById(@Path("id") id: Long): Response<MenuItem>

    //ORDER
    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<Order>

    @GET("api/orders")
    suspend fun getAllOrders(): Response<List<Order>>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): Response<Order>

    @GET("api/orders/status")
    suspend fun getOrderByStatus(@Query("status") status: String): Response<List<Order>>

    @PUT("api/orders/{orderId}/served")
    suspend fun markOrderAsServed(
        @Path("orderId") orderId: Long,
        @Body request: MarkServedRequest
    ): Response<Order>

}