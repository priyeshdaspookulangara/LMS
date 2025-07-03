package com.example.lmsapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lmsapp.R
import com.example.lmsapp.databinding.FragmentAuthCheckBinding

class AuthCheckFragment : Fragment() {

    private var _binding: FragmentAuthCheckBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthCheckViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationCommand.observe(viewLifecycleOwner) { command ->
            if (command == null) return@observe

            when (command) {
                is AuthCheckViewModel.NavigationCommand.ToLoginRegister -> {
                    findNavController().navigate(R.id.action_authCheckFragment_to_loginRegisterFragment)
                }
                is AuthCheckViewModel.NavigationCommand.ToCourseList -> {
                    // If already authenticated, pass isGuest = false (or handle differently)
                    val action = AuthCheckFragmentDirections.actionAuthCheckFragmentToCourseListFragment(isGuest = false)
                    findNavController().navigate(action)
                }
            }
            viewModel.onNavigationComplete() // Reset the command after navigation
        }

    // viewModel.checkAuthState() // Removed as it's called in ViewModel's init block
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
