package com.eldroid.tanukiramenandroid.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.backend.model.CreateOrderRequest
import com.eldroid.tanukiramenandroid.backend.repo.Repository
import com.eldroid.tanukiramenandroid.databinding.FragmentCartBottomSheetBinding
import com.eldroid.tanukiramenandroid.ui.adapter.CartAdapter
import com.eldroid.tanukiramenandroid.ui.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch

class CartBottomSheetFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private var _binding: FragmentCartBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheet = requireActivity().findViewById<FrameLayout>(R.id.cart_bottom_sheet)

        cartAdapter = CartAdapter(
            onIncrement = { cartItem -> cartViewModel.increment(cartItem) },
            onDecrement = { cartItem -> cartViewModel.decrement(cartItem) }
        )

        binding.cartRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        binding.confirmButton.setOnClickListener {
            val cartItems = cartViewModel.items.value
            if (!cartItems.isNullOrEmpty()) {

                lifecycleScope.launch {

                    val userId = (requireActivity() as? MainActivity)?.userId ?: 6

                    val order = CreateOrderRequest(
                        userId,
                        cartItems.associate { it.menuItem.menuItemId to it.quantity }
                    )

                    val response = Repository().createOrder(order)
                    if (response.isSuccessful) {
                        Log.d("CartBottomSheetFragment", "Order created successfully")
                        // ✅ Clear the cart using the ViewModel method
                        cartViewModel.clear()

                        // ✅ Collapse the bottom sheet
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    } else {
                        Log.e("CartBottomSheetFragment", "Error creating order: ${response.code()}")
                    }
                }
            }
        }

        behavior = BottomSheetBehavior.from(sheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isDraggable = true
            halfExpandedRatio = 0.5f
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> setCollapsedUI()
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> setHalfUI()
                    BottomSheetBehavior.STATE_EXPANDED -> setExpandedUI()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        cartViewModel.items.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)

            if (items.isNotEmpty()) {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCollapsedUI() {
        binding.cartRecycler.visibility = View.GONE
        binding.confirmButton.isEnabled = false
    }

    private fun setHalfUI() {
        binding.cartRecycler.visibility = View.VISIBLE
        binding.confirmButton.isEnabled = true
        resetConfirmPosition()
    }

    private fun setExpandedUI() {
        binding.cartRecycler.visibility = View.VISIBLE
        binding.confirmButton.isEnabled = true
        moveConfirmToCorner()
    }

    private fun resetConfirmPosition() {
        val params = binding.confirmButton.layoutParams as ConstraintLayout.LayoutParams

        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.UNSET

        binding.confirmButton.layoutParams = params
    }

    private fun moveConfirmToCorner() {
        val params = binding.confirmButton.layoutParams as ConstraintLayout.LayoutParams

        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        params.startToStart = ConstraintLayout.LayoutParams.UNSET
        params.topToTop = ConstraintLayout.LayoutParams.UNSET

        binding.confirmButton.layoutParams = params
    }

}