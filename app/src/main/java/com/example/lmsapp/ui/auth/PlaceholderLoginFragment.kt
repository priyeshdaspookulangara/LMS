package com.example.lmsapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lmsapp.R
import com.example.lmsapp.databinding.FragmentPlaceholderLoginBinding
import com.example.lmsapp.ui.courses.viewmodels.UiState

class PlaceholderLoginFragment : Fragment() {

    private var _binding: FragmentPlaceholderLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceholderLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val passkey = binding.etPasskey.text.toString().trim()
            viewModel.login(passkey)
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            if (state == null) return@observe

            binding.pbLoginLoading.isVisible = state is UiState.Loading
            binding.btnLogin.isEnabled = state !is UiState.Loading

            when (state) {
                is UiState.Success -> {
                    if (state.data) { // Login successful
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        // Navigate to CourseList, ensuring this fragment is removed from backstack
                        val action = PlaceholderLoginFragmentDirections.actionPlaceholderLoginFragmentToCourseListFragment(isGuest = false)
                        findNavController().navigate(action)
                        viewModel.onLoginAttemptComplete() // Reset state
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    viewModel.onLoginAttemptComplete() // Reset state to allow another attempt
                }
                is UiState.Loading -> {
                    // Handled by pbLoginLoading.isVisible
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
