package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
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

    private var allMenuItems = mutableListOf<MenuItem>()
    private var filteredMenuItems = mutableListOf<MenuItem>()
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

        adapter = FoodMenuAdapter(filteredMenuItems) { foodItem ->
            cartViewModel.add(foodItem)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        loadMenuItemsFromBE()

        binding.searchBar.addTextChangedListener(object: android.text.TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterMenu(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadMenuItemsFromBE()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterMenu(query: String) {
        val lowerQuery = query.lowercase()

        filteredMenuItems.clear()

        if (lowerQuery.isEmpty()) {
            filteredMenuItems.addAll(allMenuItems)
        } else {
            filteredMenuItems.addAll(allMenuItems.filter {
                it.name.lowercase().contains(lowerQuery)
            })
        }

        adapter.notifyDataSetChanged()
    }

    private fun loadMenuItemsFromBE() {
        binding.swipeRefreshLayout.isRefreshing = true

        lifecycleScope.launch {
            val response = Repository().fetchMenu()

            if (response.isSuccessful) {
                allMenuItems.clear()
                allMenuItems.addAll(response.body()!!)

                filteredMenuItems.clear()
                filteredMenuItems.addAll(allMenuItems)

                adapter.notifyDataSetChanged()
                Log.d("FoodMenuFragment", "Menu items fetched successfully: $allMenuItems")
            } else {
                Log.e("FoodMenuFragment", "Error fetching menu items: ${response.code()}")
            }

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}