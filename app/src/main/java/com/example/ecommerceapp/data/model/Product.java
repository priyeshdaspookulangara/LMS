package com.example.ecommerceapp.data.model;

import java.util.List;

public class Product {
    private String productId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private String categoryId; // Assuming a Category model/ID exists
    private String brand;
    private String material;
    private String color;
    private List<String> sizesAvailable; // e.g., ["S", "M", "L"]
    private int stockQuantity;
    private String imageUrlPrimary;
    private List<String> imageUrlsOther;
    private boolean isFeatured;
    private double averageRating; // Will be updated based on reviews

    // Constructors
    public Product(String productId, String name, String description, double price, String sku,
                   String categoryId, String brand, String material, String color,
                   List<String> sizesAvailable, int stockQuantity, String imageUrlPrimary,
                   List<String> imageUrlsOther, boolean isFeatured, double averageRating) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.categoryId = categoryId;
        this.brand = brand;
        this.material = material;
        this.color = color;
        this.sizesAvailable = sizesAvailable;
        this.stockQuantity = stockQuantity;
        this.imageUrlPrimary = imageUrlPrimary;
        this.imageUrlsOther = imageUrlsOther;
        this.isFeatured = isFeatured;
        this.averageRating = averageRating;
    }

    // Getters (and Setters if mutable properties are needed)
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getSku() { return sku; }
    public String getCategoryId() { return categoryId; }
    public String getBrand() { return brand; }
    public String getMaterial() { return material; }
    public String getColor() { return color; }
    public List<String> getSizesAvailable() { return sizesAvailable; }
    public int getStockQuantity() { return stockQuantity; }
    public String getImageUrlPrimary() { return imageUrlPrimary; }
    public List<String> getImageUrlsOther() { return imageUrlsOther; }
    public boolean isFeatured() { return isFeatured; }
    public double getAverageRating() { return averageRating; }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    // Basic toString for debugging
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
