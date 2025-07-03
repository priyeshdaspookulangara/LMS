package com.example.ecommerceapp.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Review;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends ListAdapter<Review, ReviewAdapter.ReviewViewHolder> {

    public ReviewAdapter() {
        super(DIFF_CALLBACK);
    }

    public void updateReviews(List<Review> reviews) {
        submitList(reviews);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        holder.bind(review);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewDate, textViewComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewReviewUsername);
            textViewDate = itemView.findViewById(R.id.textViewReviewDate);
            textViewComment = itemView.findViewById(R.id.textViewReviewComment);
            ratingBar = itemView.findViewById(R.id.ratingBarReview);
        }

        public void bind(Review review) {
            textViewUsername.setText(review.getUsername());
            textViewComment.setText(review.getComment());
            ratingBar.setRating((float) review.getRating());

            if (review.getReviewDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                textViewDate.setText(dateFormat.format(review.getReviewDate()));
            } else {
                textViewDate.setText("");
            }
        }
    }

    private static final DiffUtil.ItemCallback<Review> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Review>() {
                @Override
                public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    return oldItem.getReviewId().equals(newItem.getReviewId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    return oldItem.getRating() == newItem.getRating() &&
                           oldItem.getComment().equals(newItem.getComment()) &&
                           oldItem.getUsername().equals(newItem.getUsername()) &&
                           (oldItem.getReviewDate() != null ? oldItem.getReviewDate().equals(newItem.getReviewDate()) : newItem.getReviewDate() == null);
                }
            };
}
