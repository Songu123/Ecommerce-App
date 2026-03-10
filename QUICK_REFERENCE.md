# 📋 Quick Reference - E-Commerce API Calls

## 🚀 Quick Start Checklist

### Before Running App
1. ✅ Start Spring Boot server: `./mvnw spring-boot:run`
2. ✅ Update BASE_URL in `RetrofitClient.java`
3. ✅ Build app: `./gradlew assembleDebug`
4. ✅ Run on device/emulator

---

## 📡 How to Call APIs

### 1. Load Products
```java
ProductRepository productRepo = new ProductRepositoryImpl();

productRepo.getAllProducts(new ProductRepository.OnProductsLoadedListener() {
    @Override
    public void onSuccess(List<Product> products) {
        // Update UI
        recyclerView.setAdapter(new ProductAdapter(products));
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 2. Load Categories
```java
CategoryRepository categoryRepo = new CategoryRepositoryImpl();

categoryRepo.getAllCategories(new CategoryRepository.OnCategoriesLoadedListener() {
    @Override
    public void onSuccess(List<Category> categories) {
        // Update UI
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 3. User Login
```java
UserRepository userRepo = new UserRepositoryImpl(context);

userRepo.login(username, password, new UserRepository.OnLoginListener() {
    @Override
    public void onSuccess(User user) {
        // Login success - navigate to home
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
    
    @Override
    public void onError(String error) {
        // Show error
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 4. User Register
```java
User newUser = new User();
newUser.setUsername("john_doe");
newUser.setEmail("john@example.com");
newUser.setPassword("password123");
newUser.setFullName("John Doe");

userRepo.register(newUser, new UserRepository.OnRegisterListener() {
    @Override
    public void onSuccess(User user) {
        // Registration success
        Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 5. Get Current User
```java
User currentUser = userRepo.getCurrentUser();
if (currentUser != null) {
    textViewName.setText(currentUser.getFullName());
    textViewEmail.setText(currentUser.getEmail());
} else {
    // User not logged in - redirect to login
    startActivity(new Intent(this, LoginActivity.class));
}
```

### 6. Logout
```java
userRepo.logout();
// Clear UI and redirect to login
startActivity(new Intent(this, LoginActivity.class));
finish();
```

### 7. Load User Orders
```java
OrderRepository orderRepo = new OrderRepositoryImpl();
int userId = userRepo.getCurrentUser().getId();

orderRepo.getOrdersByUser(userId, new OrderRepository.OnOrdersLoadedListener() {
    @Override
    public void onSuccess(List<Order> orders) {
        if (orders.isEmpty()) {
            showEmptyState();
        } else {
            recyclerView.setAdapter(new OrderAdapter(orders));
        }
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 8. Filter Orders by Status
```java
String status = "pending"; // pending, processing, shipped, delivered, cancelled

orderRepo.getOrdersByStatus(userId, status, new OrderRepository.OnOrdersLoadedListener() {
    @Override
    public void onSuccess(List<Order> orders) {
        // Update UI with filtered orders
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 9. Create New Order
```java
Order newOrder = new Order();
newOrder.setUserId(currentUser.getId());
newOrder.setStatus("pending");
newOrder.setTotalPrice(calculateTotal());
newOrder.setOrderItems(cartItems); // List of OrderItem

orderRepo.createOrder(newOrder, new OrderRepository.OnOrderCreatedListener() {
    @Override
    public void onSuccess(Order order) {
        Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
        // Clear cart and navigate to orders
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, "Đặt hàng thất bại: " + error, Toast.LENGTH_SHORT).show();
    }
});
```

### 10. Cancel Order
```java
orderRepo.cancelOrder(orderId, new OrderRepository.OnOrderUpdatedListener() {
    @Override
    public void onSuccess(boolean updated) {
        Toast.makeText(context, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
        // Refresh order list
        loadOrders();
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 11. Get Product by ID
```java
productRepo.getProductById(productId, new ProductRepository.OnProductLoadedListener() {
    @Override
    public void onSuccess(Product product) {
        // Display product details
        textViewName.setText(product.getName());
        textViewPrice.setText(product.getFormattedPrice());
        textViewDescription.setText(product.getDescription());
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
    }
});
```

### 12. Get Products by Category
```java
productRepo.getProductsByCategory(categoryId, new ProductRepository.OnProductsLoadedListener() {
    @Override
    public void onSuccess(List<Product> products) {
        // Display filtered products
        productAdapter.setProducts(products);
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 13. Search Products
```java
String searchQuery = editTextSearch.getText().toString();

productRepo.searchProducts(searchQuery, new ProductRepository.OnProductsLoadedListener() {
    @Override
    public void onSuccess(List<Product> products) {
        if (products.isEmpty()) {
            textViewNoResults.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(new ProductAdapter(products));
        }
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 14. Add to Cart
```java
CartRepositoryImpl cartRepo = new CartRepositoryImpl();
int userId = userRepo.getCurrentUser().getId();
int quantity = 1;

cartRepo.addToCart(userId, productId, quantity, 
    new CartRepositoryImpl.OnCartItemAddedListener() {
        @Override
        public void onSuccess(OrderItem item) {
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            updateCartBadge();
        }
        
        @Override
        public void onError(String error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
);
```

### 15. Get Cart Items
```java
cartRepo.getCart(userId, new CartRepositoryImpl.OnCartLoadedListener() {
    @Override
    public void onSuccess(List<OrderItem> cartItems) {
        if (cartItems.isEmpty()) {
            showEmptyCart();
        } else {
            recyclerView.setAdapter(new CartAdapter(cartItems));
            updateTotalPrice(cartItems);
        }
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 16. Update Cart Item Quantity
```java
cartRepo.updateCartItem(userId, productId, newQuantity, 
    new CartRepositoryImpl.OnCartItemUpdatedListener() {
        @Override
        public void onSuccess(OrderItem item) {
            // Refresh cart
            loadCart();
        }
        
        @Override
        public void onError(String error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
);
```

### 17. Remove Item from Cart
```java
cartRepo.removeFromCart(itemId, new CartRepositoryImpl.OnCartItemRemovedListener() {
    @Override
    public void onSuccess(boolean removed) {
        Toast.makeText(context, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
        // Refresh cart
        loadCart();
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 18. Clear Cart
```java
cartRepo.clearCart(userId, new CartRepositoryImpl.OnCartClearedListener() {
    @Override
    public void onSuccess(boolean cleared) {
        Toast.makeText(context, "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();
        showEmptyCart();
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

---

## 🎨 UI Patterns

### Show Loading
```java
progressBar.setVisibility(View.VISIBLE);
recyclerView.setVisibility(View.GONE);
```

### Hide Loading
```java
progressBar.setVisibility(View.GONE);
recyclerView.setVisibility(View.VISIBLE);
```

### Show Empty State
```java
recyclerView.setVisibility(View.GONE);
emptyStateLayout.setVisibility(View.VISIBLE);
```

### Show Error with Snackbar
```java
Snackbar.make(rootView, error, Snackbar.LENGTH_LONG)
    .setAction("Retry", v -> loadData())
    .show();
```

---

## 🔧 Common Utilities

### Calculate Total Price
```java
private double calculateTotal(List<OrderItem> items) {
    double total = 0;
    for (OrderItem item : items) {
        total += item.getSubtotal();
    }
    return total;
}
```

### Format Price
```java
String formattedPrice = String.format("$%.2f", price);
```

### Check Network Connection
```java
private boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) 
        getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
}
```

### Get Current User ID
```java
UserRepository userRepo = new UserRepositoryImpl(context);
User currentUser = userRepo.getCurrentUser();
int userId = (currentUser != null) ? currentUser.getId() : -1;
```

---

## 🐛 Debug Tips

### Enable Logging
Already enabled in `RetrofitClient.java`:
```java
HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
```

### View Logs in Logcat
Filter by: **OkHttp**

### Test API with Postman
```
GET  http://localhost:8080/api/products
GET  http://localhost:8080/api/categories
POST http://localhost:8080/api/users/login
```

---

## ⚡ Performance Tips

1. **Cache images** - Use Glide or Picasso
2. **Pagination** - Load products in batches
3. **Debounce search** - Wait before searching
4. **Cancel requests** - On activity destroy
5. **Use ViewHolder** - In RecyclerView adapters

---

## 📱 Base URL Configuration

**File:** `utils/network/RetrofitClient.java`

```java
// Emulator
private static final String BASE_URL = "http://10.0.2.2:8080/";

// Real device (use your PC's IP)
private static final String BASE_URL = "http://192.168.1.XXX:8080/";

// Production
private static final String BASE_URL = "https://api.yoursite.com/";
```

---

**Quick Reference v1.0** | Last Updated: 02/02/2026
