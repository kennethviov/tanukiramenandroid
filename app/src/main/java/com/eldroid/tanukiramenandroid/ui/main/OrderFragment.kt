package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.databinding.FragmentOrderBinding
import com.eldroid.tanukiramenandroid.backend.model.Order
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.ui.adapter.OrderAdapter
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {

    val orders: MutableList<Order> = mutableListOf()
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter { order ->
            val intent = OrderDetailsActivity.newIntent(requireContext(), order.orderId.toString())
            startActivity(intent)
        }
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRecyclerView.adapter = orderAdapter

        loadOrdersFromBE()

        orderAdapter.submitList(orders)

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadOrdersFromBE()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadOrdersFromBE() {
        binding.swipeRefreshLayout.isRefreshing = true

        lifecycleScope.launch {
            val response = Repository().fetchOrders()

            if (response.isSuccessful) {
                orders.clear()
                orders.addAll(response.body()!!)
                orderAdapter.notifyDataSetChanged()
                Log.d("OrderFragment", "Orders fetched successfully: $orders")
            } else {
                Log.e("OrderFragment", "Error fetching orders: ${response.code()}")
            }
        }

        binding.swipeRefreshLayout.isRefreshing = false
    }
}