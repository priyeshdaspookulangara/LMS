package com.example.lmsapp.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.lmsapp.databinding.FragmentCourseDetailBinding
import com.example.lmsapp.courses.models.Course
// import com.bumptech.glide.Glide // Add if using Glide
import com.example.lmsapp.ui.courses.viewmodels.CourseDetailViewModel
import com.example.lmsapp.ui.courses.viewmodels.UiState

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseDetailViewModel by viewModels()
    private val args: CourseDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseKey = args.courseKey
        viewModel.fetchCourseDetails(courseKey)

        viewModel.courseDetails.observe(viewLifecycleOwner) { state ->
            binding.pbCourseDetailLoading.isVisible = state is UiState.Loading
            binding.tvCourseDetailError.isVisible = state is UiState.Error

            when (state) {
                is UiState.Success -> {
                    binding.ivCourseCoverImage.isVisible = true
                    binding.tvCourseDetailTitle.isVisible = true
                    binding.tvCourseDetailInstructor.isVisible = true
                    binding.tvCourseDetailDescription.isVisible = true
                    binding.tvSectionsPlaceholder.isVisible = true // Show sections placeholder for now
                    displayCourseDetails(state.data)
                }
                is UiState.Error -> {
                    binding.tvCourseDetailError.text = state.message
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    hideCourseContent()
                }
                is UiState.Loading -> {
                    hideCourseContent()
                }
            }
        }
    }

    private fun displayCourseDetails(course: Course) {
        // Load image with Glide or similar library
        // Glide.with(this)
        //    .load(course.coverImageUrl ?: course.thumbnailUrl)
        //    .placeholder(R.drawable.ic_launcher_background) // Replace with actual placeholder
        //    .error(R.drawable.ic_launcher_foreground) // Replace with actual error image
        //    .into(binding.ivCourseCoverImage)

        binding.tvCourseDetailTitle.text = course.title ?: "N/A"
        binding.tvCourseDetailInstructor.text = "Instructor: ${course.instructorDetails?.name ?: course.instructorName ?: "N/A"}"
        binding.tvCourseDetailDescription.text = course.fullDescription ?: course.shortDescription ?: "No description available."

        // TODO: Setup RecyclerView for sections and lessons using course.sections
    }

    private fun hideCourseContent() {
        binding.ivCourseCoverImage.isVisible = false
        binding.tvCourseDetailTitle.isVisible = false
        binding.tvCourseDetailInstructor.isVisible = false
        binding.tvCourseDetailDescription.isVisible = false
        binding.tvSectionsPlaceholder.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
