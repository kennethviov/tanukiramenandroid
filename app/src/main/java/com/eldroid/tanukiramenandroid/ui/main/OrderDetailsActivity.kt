package com.eldroid.tanukiramenandroid.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.tanukiramenandroid.backend.model.MarkServedRequest
import com.eldroid.tanukiramenandroid.backend.model.OrderItem
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.databinding.ActivityOrderDetailsBinding
import com.eldroid.tanukiramenandroid.ui.adapter.OrderDetailAdapter
import kotlinx.coroutines.launch

class OrderDetailsActivity : AppCompatActivity() {

    private var orderItems: MutableList<OrderItem> = mutableListOf()
    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getStringExtra(EXTRA_ORDER_ID)

        orderDetailAdapter = OrderDetailAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderDetailsActivity)
            adapter = orderDetailAdapter
        }

        loadOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.servedButton.setOnClickListener {
            updateStatus()
        }
    }

    private fun loadOrderDetails() {
        orderId?.let { id ->
            lifecycleScope.launch {
                val response = Repository().fetchOrder(id.toLong())
                if (response.isSuccessful) {
                    response.body()?.let { order ->
                        binding.orderId.text = order.orderId.toString()
                        binding.orderTotal.text = String.format("%.2f", order.total)
                        binding.orderStatus.text = order.status

                        orderItems.clear()
                        orderItems.addAll(order.items)
                        orderDetailAdapter.submitList(orderItems.toList())
                    }
                } else {
                    Log.e("OrderDetailsActivity", "Error fetching order details: ${response.code()}")
                }
            }
        }
    }

    private fun updateStatus() {
        lifecycleScope.launch {
            val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val userId = prefs.getLong("USER_ID", 0)

            val request = MarkServedRequest(
                waiterId = userId
            )

            val response = Repository().markServed(orderId!!.toLong(), request)

            if (response.isSuccessful) {
                Log.d("OrderDetailsActivity", "Order marked as served successfully")
                finish()
            } else {
                Log.e("OrderDetailsActivity", "Error marking order as served: ${response.code()}")
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_ID = "extra_order_id"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(EXTRA_ORDER_ID, orderId)
            return intent
        }
    }
}
