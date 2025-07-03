package com.example.lmsapp.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lmsapp.databinding.FragmentCourseListBinding
import com.example.lmsapp.ui.courses.adapters.CourseAdapter
import com.example.lmsapp.ui.courses.viewmodels.CourseViewModel
import com.example.lmsapp.ui.courses.viewmodels.UiState

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseViewModel by viewModels()
    private lateinit var courseAdapter: CourseAdapter
    private val args: CourseListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val isGuest = args.isGuest
        if (isGuest) {
            // You might want to change the title or show a message for guest users
            Toast.makeText(context, "Viewing as Guest", Toast.LENGTH_SHORT).show()
        }

        viewModel.courses.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvNoCourses.visibility = View.GONE
                    binding.rvCourses.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        binding.tvNoCourses.visibility = View.VISIBLE
                        binding.rvCourses.visibility = View.GONE
                        binding.tvNoCourses.text = if (isGuest) "No public courses available." else "No courses found."
                    } else {
                        binding.tvNoCourses.visibility = View.GONE
                        binding.rvCourses.visibility = View.VISIBLE
                        courseAdapter.submitList(state.data)
                    }
                }
                is UiState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvNoCourses.visibility = View.VISIBLE
                    binding.rvCourses.visibility = View.GONE
                    binding.tvNoCourses.text = state.message
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadCourses(isGuest)
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter { course ->
            // Handle course item click - e.g., navigate to course details
            if (course.key.isNullOrBlank()) {
                Toast.makeText(context, "Course key is missing, cannot open details.", Toast.LENGTH_SHORT).show()
                return@CourseAdapter
            }
            val action = CourseListFragmentDirections.actionCourseListFragmentToCourseDetailFragment(
                courseKey = course.key,
                title = course.title ?: "Course Details" // Pass title for AppBar
            )
            findNavController().navigate(action)
        }
        binding.rvCourses.apply {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
