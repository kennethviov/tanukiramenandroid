package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.tanukiramenandroid.databinding.FragmentFoodMenuBinding
import com.eldroid.tanukiramenandroid.model.FoodMenuItem
import com.eldroid.tanukiramenandroid.ui.adapter.FoodMenuAdapter

class FoodMenuFragment : Fragment() {

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

        val mockFoodItems = listOf(
            FoodMenuItem("Shio Ramen", 450.00, "", 1),
            FoodMenuItem("Shoyu Ramen", 450.00, "", 0),
            FoodMenuItem("Miso Ramen", 480.00, "", 0),
            FoodMenuItem("Tantanmen", 500.00, "", 0),
            FoodMenuItem("Gyoza", 200.00, "", 0)
        )

        val adapter = FoodMenuAdapter(mockFoodItems)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}