package com.example.lmsapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lmsapp.R
import com.example.lmsapp.databinding.FragmentLoginRegisterBinding

class LoginRegisterFragment : Fragment() {

    private var _binding: FragmentLoginRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginWithPasskey.setOnClickListener {
            // Navigate to the placeholder login screen
            findNavController().navigate(R.id.action_loginRegisterFragment_to_placeholderLoginFragment)
        }

        binding.btnContinueAsGuest.setOnClickListener {
            // Navigate to CourseListFragment as a guest
            // We need to use the generated NavDirections class to pass the argument
            val action = LoginRegisterFragmentDirections.actionLoginRegisterFragmentToCourseListFragment(isGuest = true)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
