package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id") // Matches API field "id"
    private String categoryId;

    @SerializedName("name")
    private String name;

    // Fields from old model, not in API spec for GET /categories:
    private String description;
    private String parentCategoryId;

    // Default constructor for Gson
    public Category() {}

    // Constructor for API fields
    public Category(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Getters
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }

    // Getters for old fields (may return null)
    public String getDescription() { return description; }
    public String getParentCategoryId() { return parentCategoryId; }

    // Setters (if needed)
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setParentCategoryId(String parentCategoryId) { this.parentCategoryId = parentCategoryId; }


    @Override
    public String toString() {
        // Used by ArrayAdapter in ProductListActivity filter dialog
        return name != null ? name : "Unknown Category";
    }
}
