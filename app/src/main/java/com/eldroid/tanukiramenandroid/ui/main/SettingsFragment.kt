package com.eldroid.tanukiramenandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eldroid.tanukiramenandroid.R
import com.eldroid.tanukiramenandroid.backend.network.NetworkConfig
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
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = (requireActivity() as? MainActivity)?.name ?: "Name"
        val role = (requireActivity() as? MainActivity)?.role ?: "Role"
        val userData = (requireActivity() as? MainActivity)?.userData ?: "UserData"


        binding.waiterName.text = userName
        binding.roleName.text = role

        if ("MANAGER" == role) {
            binding.webviewButton.visibility = View.VISIBLE
        } else {
            binding.webviewButton.visibility = View.GONE
        }

        binding.webviewButton.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra(
                "URL",
                "${NetworkConfig.FRONTEND_URL}/Frontend/restaurant/public/manager.html"
            )
            intent.putExtra(
                "BACKEND_URL",
                NetworkConfig.BASE_URL
            )
            intent.putExtra("USER_DATA", userData)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {

            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    requireActivity().finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}