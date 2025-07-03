# E-Commerce App Database Schema

This document outlines the database schema for the E-Commerce Android application.

## Tables

### 1. Users
Stores information about registered users.

| Column Name      | Data Type     | Constraints              | Description                                  |
|------------------|---------------|--------------------------|----------------------------------------------|
| `user_id`        | INTEGER       | PRIMARY KEY, AUTOINCREMENT | Unique identifier for the user.              |
| `username`       | TEXT          | NOT NULL, UNIQUE         | User's chosen username.                      |
| `email`          | TEXT          | NOT NULL, UNIQUE         | User's email address.                        |
| `password_hash`  | TEXT          | NOT NULL                 | Hashed password for security.                |
| `full_name`      | TEXT          |                          | User's full name.                            |
| `address_line1`  | TEXT          |                          | User's primary shipping address line.        |
| `address_line2`  | TEXT          |                          | User's secondary shipping address line.      |
| `city`           | TEXT          |                          | City for shipping address.                   |
| `state`          | TEXT          |                          | State/Province for shipping address.         |
| `postal_code`    | TEXT          |                          | Postal code for shipping address.            |
| `country`        | TEXT          |                          | Country for shipping address.                |
| `phone_number`   | TEXT          |                          | User's phone number.                         |
| `created_at`     | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| Timestamp of when the user account was created.|
| `updated_at`     | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| Timestamp of the last update to user info.   |

### 2. Products
Stores information about the garments and stitched cloths available for sale.

| Column Name         | Data Type     | Constraints              | Description                                     |
|---------------------|---------------|--------------------------|-------------------------------------------------|
| `product_id`        | INTEGER       | PRIMARY KEY, AUTOINCREMENT | Unique identifier for the product.              |
| `name`              | TEXT          | NOT NULL                 | Name of the product.                            |
| `description`       | TEXT          |                          | Detailed description of the product.            |
| `price`             | REAL          | NOT NULL, CHECK(`price` > 0) | Price of the product.                         |
| `sku`               | TEXT          | UNIQUE                   | Stock Keeping Unit for the product.             |
| `category_id`       | INTEGER       | FOREIGN KEY (`Categories`) | Identifier for the product's category.        |
| `brand`             | TEXT          |                          | Brand of the product.                           |
| `material`          | TEXT          |                          | Material(s) the product is made of.             |
| `color`             | TEXT          |                          | Primary color of the product.                   |
| `sizes_available`   | TEXT          |                          | Comma-separated list of available sizes (e.g., "S,M,L,XL"). |
| `stock_quantity`    | INTEGER       | NOT NULL, DEFAULT 0      | Number of items currently in stock.             |
| `image_url_primary` | TEXT          |                          | URL for the primary product image.              |
| `image_urls_other`  | TEXT          |                          | Comma-separated URLs for additional images.     |
| `created_at`        | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| Timestamp of when the product was added.        |
| `updated_at`        | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| Timestamp of the last update to product info.   |
| `is_featured`       | BOOLEAN       | DEFAULT FALSE            | Whether the product is featured.                |
| `average_rating`    | REAL          | DEFAULT 0.0              | Average user rating for the product.            |

### 3. Categories
Stores product categories.

| Column Name   | Data Type     | Constraints              | Description                               |
|---------------|---------------|--------------------------|-------------------------------------------|
| `category_id` | INTEGER       | PRIMARY KEY, AUTOINCREMENT | Unique identifier for the category.       |
| `name`        | TEXT          | NOT NULL, UNIQUE         | Name of the category (e.g., "Shirts", "Dresses"). |
| `description` | TEXT          |                          | Description of the category.              |
| `parent_category_id` | INTEGER | FOREIGN KEY (`Categories`) | For sub-categories (optional).            |
| `created_at`  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| Timestamp of when the category was created. |

### 4. Cart
Stores items that users have added to their shopping cart but not yet purchased.

| Column Name   | Data Type     | Constraints                                  | Description                                     |
|---------------|---------------|----------------------------------------------|-------------------------------------------------|
| `cart_id`     | INTEGER       | PRIMARY KEY, AUTOINCREMENT                     | Unique identifier for the cart session.         |
| `user_id`     | INTEGER       | FOREIGN KEY (`Users`), NULLABLE                | Identifier for the user (if logged in).       |
| `session_id`  | TEXT          |                                              | Identifier for anonymous user sessions.         |
| `created_at`  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                    | Timestamp of when the cart was created.         |
| `updated_at`  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                    | Timestamp of the last update to the cart.       |

### 5. CartItems
Stores individual product items within a shopping cart.

| Column Name   | Data Type     | Constraints                                   | Description                                     |
|---------------|---------------|-----------------------------------------------|-------------------------------------------------|
| `cart_item_id`| INTEGER       | PRIMARY KEY, AUTOINCREMENT                      | Unique identifier for the cart item.            |
| `cart_id`     | INTEGER       | FOREIGN KEY (`Cart`), NOT NULL                | Identifier for the cart this item belongs to.   |
| `product_id`  | INTEGER       | FOREIGN KEY (`Products`), NOT NULL              | Identifier for the product added to the cart.   |
| `quantity`    | INTEGER       | NOT NULL, CHECK(`quantity` > 0), DEFAULT 1    | Number of units of the product in the cart.     |
| `price_at_addition` | REAL    | NOT NULL                                      | Price of the product when it was added to cart. |
| `added_at`    | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                     | Timestamp of when the item was added to cart.   |

*Composite unique constraint on (`cart_id`, `product_id`)*

### 6. Orders
Stores information about completed customer orders.

| Column Name        | Data Type     | Constraints                                  | Description                                        |
|--------------------|---------------|----------------------------------------------|----------------------------------------------------|
| `order_id`         | INTEGER       | PRIMARY KEY, AUTOINCREMENT                     | Unique identifier for the order.                   |
| `user_id`          | INTEGER       | FOREIGN KEY (`Users`), NOT NULL                | Identifier for the user who placed the order.      |
| `order_date`       | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                    | Date and time when the order was placed.           |
| `total_amount`     | REAL          | NOT NULL, CHECK(`total_amount` >= 0)         | Total amount for the order.                        |
| `status`           | TEXT          | NOT NULL, DEFAULT 'Pending'                  | Current status of the order (e.g., "Pending", "Shipped", "Delivered", "Cancelled"). |
| `shipping_address_line1` | TEXT    | NOT NULL                                     | Shipping address line 1 for this order.            |
| `shipping_address_line2` | TEXT    |                                              | Shipping address line 2 for this order.            |
| `shipping_city`    | TEXT          | NOT NULL                                     | Shipping city for this order.                      |
| `shipping_state`   | TEXT          | NOT NULL                                     | Shipping state/province for this order.            |
| `shipping_postal_code`| TEXT       | NOT NULL                                     | Shipping postal code for this order.               |
| `shipping_country` | TEXT          | NOT NULL                                     | Shipping country for this order.                   |
| `payment_method`   | TEXT          |                                              | Payment method used (e.g., "Credit Card", "PayPal").|
| `payment_status`   | TEXT          | NOT NULL, DEFAULT 'Pending'                  | Status of the payment (e.g., "Pending", "Completed", "Failed"). |
| `transaction_id`   | TEXT          |                                              | Transaction ID from the payment gateway.           |
| `created_at`       | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                    | Timestamp of when the order record was created.    |
| `updated_at`       | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                    | Timestamp of the last update to the order.         |

### 7. OrderItems
Stores individual product items within a completed order.

| Column Name       | Data Type     | Constraints                               | Description                                       |
|-------------------|---------------|-------------------------------------------|---------------------------------------------------|
| `order_item_id`   | INTEGER       | PRIMARY KEY, AUTOINCREMENT                  | Unique identifier for the order item.             |
| `order_id`        | INTEGER       | FOREIGN KEY (`Orders`), NOT NULL            | Identifier for the order this item belongs to.    |
| `product_id`      | INTEGER       | FOREIGN KEY (`Products`), NOT NULL          | Identifier for the product ordered.               |
| `quantity`        | INTEGER       | NOT NULL, CHECK(`quantity` > 0)           | Number of units of the product ordered.           |
| `price_per_unit`  | REAL          | NOT NULL, CHECK(`price_per_unit` >= 0)    | Price of one unit of the product at time of order.|
| `subtotal`        | REAL          | NOT NULL, CHECK(`subtotal` >= 0)          | `quantity` * `price_per_unit`.                    |

*Composite unique constraint on (`order_id`, `product_id`)*

### 8. Reviews
Stores user reviews and ratings for products.

| Column Name   | Data Type     | Constraints                               | Description                                     |
|---------------|---------------|-------------------------------------------|-------------------------------------------------|
| `review_id`   | INTEGER       | PRIMARY KEY, AUTOINCREMENT                  | Unique identifier for the review.               |
| `product_id`  | INTEGER       | FOREIGN KEY (`Products`), NOT NULL          | Identifier for the product being reviewed.      |
| `user_id`     | INTEGER       | FOREIGN KEY (`Users`), NOT NULL             | Identifier for the user who wrote the review.   |
| `rating`      | INTEGER       | NOT NULL, CHECK (`rating` >= 1 AND `rating` <= 5) | User's rating for the product (e.g., 1-5 stars).|
| `comment`     | TEXT          |                                           | User's review comment.                          |
| `review_date` | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                 | Date and time when the review was submitted.    |

*Composite unique constraint on (`product_id`, `user_id`) to ensure one review per user per product.*

### 9. Wishlist
Stores products that users have added to their wishlist.

| Column Name   | Data Type     | Constraints                               | Description                                     |
|---------------|---------------|-------------------------------------------|-------------------------------------------------|
| `wishlist_id` | INTEGER       | PRIMARY KEY, AUTOINCREMENT                  | Unique identifier for the wishlist entry.       |
| `user_id`     | INTEGER       | FOREIGN KEY (`Users`), NOT NULL             | Identifier for the user.                        |
| `product_id`  | INTEGER       | FOREIGN KEY (`Products`), NOT NULL          | Identifier for the product in the wishlist.     |
| `added_at`    | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP                 | Timestamp of when product was added to wishlist.|

*Composite unique constraint on (`user_id`, `product_id`)*

## Relationships

- **Users to Orders**: One-to-Many (A user can have multiple orders; an order belongs to one user).
- **Users to Cart**: One-to-One (A user has one active cart at a time, or an anonymous session has one cart).
- **Users to Reviews**: One-to-Many (A user can write multiple reviews; a review is written by one user).
- **Users to Wishlist**: One-to-Many (A user can have multiple products in their wishlist).
- **Products to Categories**: Many-to-One (A product belongs to one category; a category can have multiple products). *Can be Many-to-Many if a product can belong to multiple categories, requiring a junction table.*
- **Products to CartItems**: One-to-Many (A product can be in multiple cart items - across different carts; a cart item refers to one product).
- **Products to OrderItems**: One-to-Many (A product can be in multiple order items - across different orders; an order item refers to one product).
- **Products to Reviews**: One-to-Many (A product can have multiple reviews; a review is for one product).
- **Products to Wishlist**: One-to-Many (A product can be in multiple wishlists).
- **Categories to Categories**: One-to-Many (Self-referencing for parent-child category relationships).
- **Cart to CartItems**: One-to-Many (A cart can contain multiple cart items; a cart item belongs to one cart).
- **Orders to OrderItems**: One-to-Many (An order can contain multiple order items; an order item belongs to one order).

## Notes
- `TIMESTAMP` fields for `created_at` and `updated_at` are good practice for tracking changes.
- `password_hash` should store securely hashed passwords (e.g., using bcrypt or scrypt), not plain text.
- `sizes_available` and `image_urls_other` are stored as TEXT (comma-separated) for simplicity. For more complex scenarios, especially if searching/filtering by size or managing many images, separate tables (`ProductSizes`, `ProductImages`) might be more appropriate.
- The `Cart` table has a `user_id` (nullable) and `session_id` to handle both logged-in and anonymous users. A mechanism to merge carts upon login would be needed.
- The `price_at_addition` in `CartItems` and `price_per_unit` in `OrderItems` store the price at the time of action to handle price fluctuations.
- The `average_rating` in `Products` can be a calculated field, updated via triggers or application logic when new reviews are added.
- For payment processing, sensitive payment details (like full credit card numbers) should **not** be stored in this database. They should be handled by a compliant payment gateway. Only store transaction IDs or tokens.
- This schema assumes a relational database. If using NoSQL, the structure would differ.
- Further normalization or denormalization might be necessary based on specific query patterns and performance requirements.Tool output for `create_file_with_block`:
