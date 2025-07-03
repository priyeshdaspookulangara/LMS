package com.example.ecommerceapp.data.model;

public class Category {
    private String categoryId;
    private String name;
    private String description;
    private String parentCategoryId; // For sub-categories, null if it's a top-level category

    public Category(String categoryId, String name, String description, String parentCategoryId) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }

    // Getters
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getParentCategoryId() { return parentCategoryId; }

    @Override
    public String toString() {
        return name; // Simple representation for Spinners or lists
    }
}
