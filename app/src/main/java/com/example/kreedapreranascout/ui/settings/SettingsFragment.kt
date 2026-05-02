package com.example.kreedapreranascout.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.databinding.FragmentSettingsBinding
import com.example.kreedapreranascout.util.SessionManager

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        val sessionManager = SessionManager(requireContext())

        binding.logoutBtn.setOnClickListener {
            sessionManager.logout()
            findNavController().navigate(R.id.loginFragment) 
        }

        binding.editProfileBtn.setOnClickListener {
            // Implementation for profile editing
        }

        binding.switchAccountBtn.setOnClickListener {
            sessionManager.logout()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
