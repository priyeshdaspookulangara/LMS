package com.example.lmsapp.ui.courses.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lmsapp.courses.models.Course
import com.example.lmsapp.databinding.ItemCourseBinding
// import com.bumptech.glide.Glide // Example image loading library, add dependency if used

class CourseAdapter(private val onItemClicked: (Course) -> Unit) :
    ListAdapter<Course, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
        holder.itemView.setOnClickListener {
            onItemClicked(course)
        }
    }

    inner class CourseViewHolder(private val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.title ?: "No Title"
            binding.tvCourseCategory.text = course.category ?: "N/A"
            binding.tvCourseInstructor.text = course.instructorName ?: "Unknown Instructor"

            // Example using Glide to load image, uncomment and add dependency if you want this
            // Glide.with(binding.ivCourseThumbnail.context)
            //    .load(course.thumbnailUrl)
            //    .placeholder(R.drawable.ic_launcher_background) // Add a placeholder drawable
            //    .error(R.drawable.ic_launcher_foreground) // Add an error drawable
            //    .into(binding.ivCourseThumbnail)

            // Simple placeholder if no image loading library
            if (course.thumbnailUrl.isNullOrEmpty()) {
                // Optionally set a default image or hide ImageView
                 binding.ivCourseThumbnail.setImageResource(com.example.lmsapp.R.drawable.ic_launcher_background) // Example
            }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id && oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}
