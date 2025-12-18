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
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {

    private val allOrders = mutableListOf<Order>()
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

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                val filtered = when (tab.position) {
                    0 -> allOrders // All
                    1 -> {
                        allOrders.filter { it.status == "Pending" }
                        allOrders.filter { it.status == "Preparing" }
                    }
                    2 -> allOrders.filter { it.status == "Ready" }
                    3 -> allOrders.filter { it.status == "Served" }
                    else -> allOrders
                }

                orderAdapter.submitList(filtered)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })

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
                allOrders.clear()
                allOrders.addAll(response.body()!!)

                orderAdapter.notifyDataSetChanged()
                orderAdapter.submitList(allOrders.toList())
                Log.d("OrderFragment", "Orders fetched successfully: $allOrders")
            } else {
                Log.e("OrderFragment", "Error fetching orders: ${response.code()}")
            }
        }

        binding.swipeRefreshLayout.isRefreshing = false
    }
}