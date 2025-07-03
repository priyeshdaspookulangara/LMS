package com.example.ecommerceapp.data.model;

import java.util.Date;
import java.util.Objects;

public class Review {
    private String reviewId;
    private String productId;
    private String userId; // ID of the user who wrote the review
    private String username; // Username of the reviewer (denormalized for display)
    private int rating; // e.g., 1 to 5
    private String comment;
    private Date reviewDate;

    public Review(String reviewId, String productId, String userId, String username, int rating, String comment, Date reviewDate) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters
    public String getReviewId() { return reviewId; }
    public String getProductId() { return productId; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Date getReviewDate() { return reviewDate; }

    // Setters might not be needed if reviews are immutable after creation,
    // or only specific fields are updatable (e.g., by admin)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return reviewId.equals(review.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId);
    }
}
