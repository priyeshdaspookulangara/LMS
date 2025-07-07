package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map; // For specifications

public class Product {

    @SerializedName("id") // Matches API field "id"
    private String productId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("category") // This might be a category ID string or a nested Category object
    private String category; // Assuming it's a category name or ID string as per "Laptop Pro" example

    @SerializedName("imageUrl")
    private String imageUrl; // API shows "imageUrl", was "imageUrlPrimary"

    @SerializedName("stock")
    private int stockQuantity;

    @SerializedName("specifications")
    private Map<String, String> specifications; // e.g., { "CPU": "Intel i7", "RAM": "16GB" }

    // Fields not directly in API spec but useful for client-side or previously used:
    private String sku; // Was in old model, not in API spec for GET /products/{id}
    private String brand; // Was in old model, not in API spec
    private String material; // Was in old model, not in API spec
    private String color; // Was in old model, not in API spec
    private List<String> sizesAvailable; // Was in old model, not in API spec
    private List<String> imageUrlsOther; // Was in old model, not in API spec
    private boolean isFeatured; // Client-side logic, or needs API field
    private double averageRating; // Client-side logic (from reviews), or needs API field


    // Default constructor for Gson
    public Product() {}

    // Example constructor (you might not need a verbose one if using Gson)
    public Product(String productId, String name, String description, double price, String category,
                   String imageUrl, int stockQuantity, Map<String, String> specifications) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.specifications = specifications;
        // Initialize other fields if necessary
        this.isFeatured = false; // Default
        this.averageRating = 0.0; // Default
    }

    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; } // If it's just a string
    public String getImageUrl() { return imageUrl; }
    public int getStockQuantity() { return stockQuantity; }
    public Map<String, String> getSpecifications() { return specifications; }

    // Getters for previously used fields (may return null or default values)
    public String getSku() { return sku; }
    public String getBrand() { return brand; }
    public String getMaterial() { return material; }
    public String getColor() { return color; }
    public List<String> getSizesAvailable() { return sizesAvailable; }
    public List<String> getImageUrlsOther() { return imageUrlsOther; } // Renamed from getImageUrlPrimary
    public boolean isFeatured() { return isFeatured; }
    public double getAverageRating() { return averageRating; }


    // Setters (mainly for fields not set by Gson or for client-side updates)
    public void setProductId(String productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }
    public void setSku(String sku) { this.sku = sku; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setMaterial(String material) { this.material = material; }
    public void setColor(String color) { this.color = color; }
    public void setSizesAvailable(List<String> sizesAvailable) { this.sizesAvailable = sizesAvailable; }
    public void setImageUrlPrimary(String imageUrl) { this.imageUrl = imageUrl; } // Keep compatibility if needed
    public void setFeatured(boolean featured) { isFeatured = featured; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }


    // For compatibility with ProductAdapter expecting getImageUrlPrimary()
    public String getImageUrlPrimary() { return imageUrl; }


    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stockQuantity +
                '}';
    }
}
