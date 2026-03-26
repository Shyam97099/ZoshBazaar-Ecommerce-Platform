# 🛒 Ecommerce Zosh Bazzar Application

A full-stack **Ecommerce Zosh Bazzar Application** built with **Spring Boot**, enabling multiple vendors to manage products while users can browse, purchase, and manage their orders seamlessly.

---

## 📌 Project Overview

This application provides a complete e-commerce ecosystem with three distinct roles — **Admin**, **Customer**, and **User** — each with dedicated features and secure access control using JWT-based authentication.

---

## 🚀 Features

### 👤 User
- Register & Login with JWT Authentication
- Browse and search products
- Add to Cart & Place Orders
- View Order History

### 🛠️ Admin
- Manage Users, Products & Orders
- View Dashboard Analytics
- Full platform control

### 🏪 Customer
- Add & manage own products
- Track and manage orders
- Dedicated vendor dashboard

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot |
| Security | Spring Security (JWT) |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL |
| Build Tool | Maven |
| IDE | IntelliJ IDEA |
| API Testing | Postman |

---

## 📂 Project Structure

```
Ecommerce Multivender/
├── ecommerce-multivender/
│   ├── .mvn/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com.zosh/
│   │       │       ├── config/          # Security & App configuration
│   │       │       ├── controller/      # REST API Controllers
│   │       │       ├── domain/          # Enums & domain types
│   │       │       ├── exceptions/      # Custom exception handlers
│   │       │       ├── model/           # Entity classes (JPA models)
│   │       │       ├── repository/      # Spring Data JPA repositories
│   │       │       ├── request/         # Request DTOs
│   │       │       ├── response/        # Response DTOs
│   │       │       ├── service/         # Business logic (interfaces)
│   │       │       ├── utils/           # Utility/helper classes
│   │       │       └── EcommerceMultivenderApplication.java
│   │       └── resources/
│   │           └── application.properties
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── HELP.md
└── README.md
```

---

## 🔐 Authentication & Authorization

- **JWT-based** stateless authentication
- **Role-based access control** with 3 roles:
  - `ROLE_ADMIN` — Full platform access
  - `ROLE_CUSTOMER` — Product & order management
  - `ROLE_USER` — Shopping & order tracking
- All secured endpoints require a valid Bearer token

---

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA (recommended)

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/Shyam97099/ecommerce-multivender.git
cd ecommerce-multivender
```

**2. Configure the database**

Open `src/main/resources/application.properties` and update:
```properties
spring.application.name=ecommerce-multivender
server.port=8080

# Database Configuration
spring.datasource.url=
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Mail Configuration
spring.mail.host=
spring.mail.port=
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# JWT Configuration
jwt.secret=

# Razorpay Configuration
razorpay.key.id=
razorpay.key.secret=
```

**3. Run the application**
```bash
mvn spring-boot:run
```

The server will start at: `http://localhost:8080`

---

## 📬 API Testing

Use **Postman** to test the REST APIs.

> **Note:** Include JWT token in the Authorization header for protected routes:
> `Authorization: Bearer <your_token>`

---

### 🏠 HomeCategoryController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/home/categories` | Create home categories | Public |
| GET | `/admin/home-category` | Get all home categories | Admin |
| PATCH | `/admin/home-category/{id}` | Update home category | Admin |

---

### 🛡️ AdminController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| PATCH | `/api/seller/{id}/status/{status}` | Update seller account status | Admin |

---

### 🎟️ AdminCouponController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/apply/coupons` | Apply or remove coupon on cart | User |
| POST | `/api/admin/create` | Create new coupon | Admin |
| DELETE | `/api/admin/delete/{id}` | Delete coupon by ID | Admin |
| GET | `/api/admin/all` | Get all coupons | Admin |

---

### 🔐 AuthController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/auth/signup` | Register new user | Public |
| POST | `/auth/sent/login-signup-otp` | Send OTP for login/signup | Public |
| POST | `/auth/signing` | Login with OTP & get JWT | Public |

---

### 🛒 CartController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/cart` | Get user's cart | User |
| PUT | `/api/cart/add` | Add item to cart | User |
| DELETE | `/api/cart/item/{cartItemId}` | Remove item from cart | User |
| PUT | `/api/cart/item/{cartItemId}` | Update cart item quantity | User |

---

### 🔥 DealController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/admin/deals` | Create new deal | Admin |
| PATCH | `/admin/deals/{id}` | Update deal by ID | Admin |
| DELETE | `/admin/deals/{id}` | Delete deal by ID | Admin |

---

### 📦 OrderController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/` | Create order (Razorpay/Stripe) | User |
| GET | `/user` | Get user order history | User |
| GET | `/{orderId}` | Get order by ID | User |
| GET | `/item/{orderItemId}` | Get order item by ID | User |
| PUT | `/{orderId}/cancel` | Cancel order | User |

---

### 💳 PaymentController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/payment/{paymentId}` | Handle payment success (Razorpay/Stripe) | User |

---

### 🛍️ ProductController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/products/{productId}` | Get product by ID | Public |
| GET | `/products/search?query=` | Search products by keyword | Public |
| GET | `/products` | Get all products with filters (category, brand, color, size, price, discount, sort, stock, page) | Public |

---

### ⭐ ReviewController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/products/{productId}/reviews` | Get all reviews for a product | Public |
| POST | `/api/products/{productId}/reviews` | Write a review for a product | User |
| PATCH | `/api/reviews/{reviewId}` | Update a review | User |
| DELETE | `/api/reviews/{reviewId}` | Delete a review | User |

---

### 🏪 SellerController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/sellers/login` | Seller login | Public |
| PATCH | `/sellers/verify/{otp}` | Verify seller email via OTP | Public |
| POST | `/sellers` | Register new seller | Public |
| GET | `/sellers/{id}` | Get seller by ID | Public |
| GET | `/sellers/profile` | Get seller profile via JWT | Seller |
| GET | `/sellers/report` | Get seller report | Seller |
| GET | `/sellers` | Get all sellers (filter by status) | Admin |
| PATCH | `/sellers` | Update seller profile | Seller |
| DELETE | `/sellers/{id}` | Delete seller by ID | Admin |

---

### 📦 SellerProductController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/sellers/products` | Get all products by seller | Seller |
| POST | `/api/sellers/products` | Create new product | Seller |
| DELETE | `/api/sellers/products/{productId}` | Delete product by ID | Seller |
| PUT | `/api/sellers/products/{productId}` | Update product by ID | Seller |

---

### 🧾 SellerOrderController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/seller/orders` | Get all orders for seller | Seller |
| PATCH | `/api/seller/orders/{orderId}/status/{orderStatus}` | Update order status | Seller |

---

### 👤 UserController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/users/profile` | Get user profile via JWT | User |

---

### ❤️ WishlistController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/wishlist` | Get user's wishlist | User |
| POST | `/api/wishlist/add-product/{productId}` | Add product to wishlist | User |

---

### 💰 TransactionController

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/transactions/seller` | Get transactions by seller | Seller |
| GET | `/api/transactions` | Get all transactions | Admin |

---

## 📈 Future Enhancements

- [ ] Payment Gateway Integration (Razorpay / Stripe)
- [ ] 🚧 React Frontend *(In Progress)*
- [ ] Email Notifications
- [ ] Docker Deployment
- [ ] Product Reviews & Ratings
- [ ] Advanced Search & Filters

---

## 👨‍💻 Author

**Shyam Kumar Sharma**
- 🔗 LinkedIn: [linkedin.com/in/shyamkumar97](https://linkedin.com/in/shyamkumar97)
- 💻 GitHub: [github.com/Shyam97099](https://github.com/Shyam97099)
- 📧 shyamkumarsharma069@gmail.com

---

## ⭐ Support

If you find this project helpful, please give it a ⭐ on [GitHub](https://github.com/Shyam97099/Zosh Bazzar)!
