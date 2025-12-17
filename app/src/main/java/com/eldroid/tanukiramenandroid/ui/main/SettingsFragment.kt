package com.eldroid.tanukiramenandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.databinding.FragmentFoodMenuBinding
import com.eldroid.tanukiramenandroid.databinding.FragmentSettingsBinding
import com.eldroid.tanukiramenandroid.ui.login.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val waiterName = (requireActivity() as? MainActivity)?.name ?: "Waiter"
        binding.waiterName.text = waiterName

        binding.logoutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            requireActivity().finish()
        }
    }
}