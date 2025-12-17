package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.tanukiramenandroid.databinding.FragmentFoodMenuBinding
import com.eldroid.tanukiramenandroid.backend.model.MenuItem
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.ui.adapter.FoodMenuAdapter
import com.eldroid.tanukiramenandroid.ui.viewmodel.CartViewModel
import kotlinx.coroutines.launch

class FoodMenuFragment : Fragment() {

    private var menuItems: MutableList<MenuItem> = mutableListOf()
    private lateinit var adapter: FoodMenuAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private var _binding: FragmentFoodMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FoodMenuAdapter(menuItems) { foodItem ->
            cartViewModel.add(foodItem)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        loadMenuItemsFromBE()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadMenuItemsFromBE()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadMenuItemsFromBE() {
        binding.swipeRefreshLayout.isRefreshing = true

        lifecycleScope.launch {
            val response = Repository().fetchMenu()

            if (response.isSuccessful) {
                menuItems.clear()
                menuItems.addAll(response.body()!!)
                adapter.notifyDataSetChanged()
                Log.d("FoodMenuFragment", "Menu items fetched successfully: $menuItems")
            } else {
                Log.e("FoodMenuFragment", "Error fetching menu items: ${response.code()}")
            }

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}