# API Integration Guide - E-Commerce Android App

## 📡 Tổng quan

Ứng dụng Android E-Commerce đã được tích hợp hoàn chỉnh với Spring Boot REST API sử dụng **Retrofit** và **OkHttp**.

## 🏗️ Cấu trúc API Layer

```
data/
├── api/                        # API Service Interfaces
│   ├── ProductApiService.java
│   ├── CategoryApiService.java
│   ├── UserApiService.java
│   ├── OrderApiService.java
│   └── CartApiService.java
├── dto/                        # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── CartItemRequest.java
│   └── OrderStatusRequest.java
├── response/                   # Response Models
│   └── ApiResponse.java
└── Repository Implementations
    ├── ProductRepositoryImpl.java
    ├── CategoryRepositoryImpl.java
    ├── UserRepositoryImpl.java
    ├── OrderRepositoryImpl.java
    └── CartRepositoryImpl.java

utils/network/
├── RetrofitClient.java        # Retrofit configuration
└── ApiClient.java             # API service provider
```

## 🔌 API Endpoints

### 1. Product API (`/api/products`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Lấy tất cả sản phẩm |
| GET | `/api/products/{id}` | Lấy sản phẩm theo ID |
| GET | `/api/products/category/{categoryId}` | Lấy sản phẩm theo danh mục |
| POST | `/api/products` | Tạo sản phẩm mới (Admin) |
| PUT | `/api/products/{id}` | Cập nhật sản phẩm (Admin) |
| DELETE | `/api/products/{id}` | Xóa sản phẩm (Admin) |

**Usage Example:**
```java
ProductRepository productRepo = new ProductRepositoryImpl();
productRepo.getAllProducts(new ProductRepository.OnProductsLoadedListener() {
    @Override
    public void onSuccess(List<Product> products) {
        // Handle products
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 2. Category API (`/api/categories`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Lấy tất cả danh mục |
| GET | `/api/categories/{id}` | Lấy danh mục theo ID |
| POST | `/api/categories` | Tạo danh mục mới (Admin) |
| PUT | `/api/categories/{id}` | Cập nhật danh mục (Admin) |
| DELETE | `/api/categories/{id}` | Xóa danh mục (Admin) |

**Usage Example:**
```java
CategoryRepository categoryRepo = new CategoryRepositoryImpl();
categoryRepo.getAllCategories(new CategoryRepository.OnCategoriesLoadedListener() {
    @Override
    public void onSuccess(List<Category> categories) {
        // Handle categories
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 3. User/Auth API (`/api/users`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Đăng ký tài khoản |
| POST | `/api/users/login` | Đăng nhập |
| GET | `/api/users/{id}` | Lấy thông tin user |
| PUT | `/api/users/{id}` | Cập nhật thông tin user |
| GET | `/api/users` | Lấy danh sách users (Admin) |

**Usage Example - Login:**
```java
UserRepository userRepo = new UserRepositoryImpl(context);
userRepo.login("username", "password", new UserRepository.OnLoginListener() {
    @Override
    public void onSuccess(User user) {
        // User logged in successfully
        // User info is automatically saved in SharedPreferences
    }
    
    @Override
    public void onError(String error) {
        // Handle login error
    }
});
```

**Usage Example - Register:**
```java
User newUser = new User();
newUser.setUsername("john_doe");
newUser.setEmail("john@example.com");
newUser.setPassword("password123");
newUser.setFullName("John Doe");

userRepo.register(newUser, new UserRepository.OnRegisterListener() {
    @Override
    public void onSuccess(User user) {
        // Registration successful
    }
    
    @Override
    public void onError(String error) {
        // Handle registration error
    }
});
```

### 4. Order API (`/api/orders`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Lấy tất cả đơn hàng (Admin) |
| GET | `/api/orders/{id}` | Chi tiết đơn hàng |
| GET | `/api/orders/user/{userId}` | Đơn hàng của user |
| POST | `/api/orders` | Tạo đơn hàng mới |
| PUT | `/api/orders/{id}/status` | Cập nhật trạng thái |
| DELETE | `/api/orders/{id}` | Hủy đơn hàng |

**Usage Example:**
```java
OrderRepository orderRepo = new OrderRepositoryImpl();
int userId = userRepo.getCurrentUser().getId();

orderRepo.getOrdersByUser(userId, new OrderRepository.OnOrdersLoadedListener() {
    @Override
    public void onSuccess(List<Order> orders) {
        // Display orders
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
});
```

### 5. Cart API (`/api/cart`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart/{userId}` | Xem giỏ hàng |
| POST | `/api/cart/add` | Thêm sản phẩm vào giỏ |
| PUT | `/api/cart/update` | Cập nhật số lượng |
| DELETE | `/api/cart/{itemId}` | Xóa sản phẩm khỏi giỏ |
| DELETE | `/api/cart/clear/{userId}` | Xóa toàn bộ giỏ hàng |

**Usage Example:**
```java
CartRepositoryImpl cartRepo = new CartRepositoryImpl();

// Add to cart
cartRepo.addToCart(userId, productId, quantity, 
    new CartRepositoryImpl.OnCartItemAddedListener() {
        @Override
        public void onSuccess(OrderItem item) {
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        }
        
        @Override
        public void onError(String error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
);
```

## ⚙️ Configuration

### 1. Thay đổi Base URL

Mở file `RetrofitClient.java` và cập nhật `BASE_URL`:

```java
// Cho máy thật (sử dụng IP local network của máy chạy Spring Boot)
private static final String BASE_URL = "http://192.168.1.100:8080/";

// Cho Android Emulator (10.0.2.2 trỏ đến localhost của máy host)
private static final String BASE_URL = "http://10.0.2.2:8080/";

// Cho production
private static final String BASE_URL = "https://your-api-domain.com/";
```

### 2. Dependencies đã thêm

File `app/build.gradle.kts`:
```kotlin
// Retrofit for API calls
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp for logging
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Gson for JSON parsing
implementation("com.google.code.gson:gson:2.10.1")
```

### 3. Permissions

File `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🔐 Authentication Flow

### Login Process
1. User nhập username/password
2. App gọi `userRepository.login(username, password, listener)`
3. Retrofit gửi POST request đến `/api/users/login`
4. Server xác thực và trả về User object
5. App lưu user info vào SharedPreferences
6. User được redirect đến Home screen

### Session Management
- User info được lưu trong **SharedPreferences**
- Các field được lưu:
  - `user_id`
  - `username`
  - `email`
  - `full_name`
  - `role`
  - `is_logged_in`

### Logout
```java
userRepository.logout(); // Clears SharedPreferences
```

## 📊 Data Models Mapping

### Product Entity
```java
public class Product {
    private int id;                  // Từ database
    private String name;             // Từ database
    private String description;      // Từ database
    private double price;            // Từ database
    private int quantity;            // Từ database
    private String image;            // Từ database
    private int categoryId;          // Foreign key
}
```

### Category Entity
```java
public class Category {
    private int id;                  // Từ database
    private String name;             // Từ database
    private String slug;             // Từ database
    private int iconResId;           // Mobile only (not from API)
}
```

### Order Entity
```java
public class Order {
    private int id;                  // Từ database
    private int userId;              // Foreign key
    private Date createdAt;          // Từ database
    private String status;           // pending/processing/shipped/delivered/cancelled
    private double totalPrice;       // Từ database
    private List<OrderItem> orderItems; // Related items
}
```

## 🔄 Retrofit Configuration Details

### RetrofitClient.java
```java
// Logging Interceptor - Debug API calls
HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

// OkHttp Client with timeout
OkHttpClient okHttpClient = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(loggingInterceptor)
    .build();

// Gson with date format
Gson gson = new GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .setLenient()
    .create();

// Build Retrofit
retrofit = new Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build();
```

## 🐛 Error Handling

### Network Errors
```java
@Override
public void onFailure(Call<T> call, Throwable t) {
    if (t instanceof IOException) {
        // Network error
        listener.onError("Không thể kết nối đến server. Kiểm tra internet của bạn.");
    } else {
        listener.onError("Lỗi: " + t.getMessage());
    }
}
```

### HTTP Errors
```java
@Override
public void onResponse(Call<T> call, Response<T> response) {
    if (response.isSuccessful() && response.body() != null) {
        listener.onSuccess(response.body());
    } else {
        switch (response.code()) {
            case 400:
                listener.onError("Dữ liệu không hợp lệ");
                break;
            case 401:
                listener.onError("Chưa đăng nhập hoặc phiên đã hết hạn");
                break;
            case 404:
                listener.onError("Không tìm thấy dữ liệu");
                break;
            case 500:
                listener.onError("Lỗi server");
                break;
            default:
                listener.onError("Lỗi: " + response.code());
        }
    }
}
```

## 🧪 Testing API

### 1. Test với Postman
Trước khi chạy app, test API endpoints với Postman:
```
GET http://localhost:8080/api/products
GET http://localhost:8080/api/categories
POST http://localhost:8080/api/users/login
```

### 2. Check Logs
Trong Logcat, search tag: `OkHttp`
- Request URL
- Request Headers
- Request Body
- Response Code
- Response Body

### 3. Common Issues

#### Cannot connect to server
```
✅ Check: Server Spring Boot đang chạy?
✅ Check: Base URL đúng chưa?
✅ Check: Emulator dùng 10.0.2.2 thay vì localhost
✅ Check: Firewall không block port 8080
```

#### 404 Not Found
```
✅ Check: Endpoint path đúng chưa?
✅ Check: Spring Boot controller có mapping đúng không?
```

#### JSON parsing error
```
✅ Check: Entity fields match với JSON response?
✅ Check: Date format trong Gson config đúng chưa?
```

## 📱 Example Usage in Activity

### MainActivity với API
```java
public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize repositories with real API
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        
        // Initialize presenter
        presenter = new MainPresenter(this, categoryRepo, productRepo);
        
        // Load data from API
        presenter.loadCategories();
        presenter.loadRecommendedProducts();
    }
    
    @Override
    public void showLoading() {
        // Show progress bar
    }
    
    @Override
    public void hideLoading() {
        // Hide progress bar
    }
    
    @Override
    public void showCategories(List<Category> categories) {
        // Update RecyclerView with data from API
    }
    
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
```

## 🚀 Next Steps

### 1. Implement Missing Features
- [ ] Add to Cart functionality
- [ ] Checkout process
- [ ] Order tracking
- [ ] User profile editing

### 2. Add Advanced Features
- [ ] JWT Token authentication
- [ ] Refresh token mechanism
- [ ] Image loading với Glide/Picasso
- [ ] Offline caching với Room Database
- [ ] Push notifications

### 3. Optimize Performance
- [ ] Implement pagination for product lists
- [ ] Add request caching
- [ ] Optimize image loading
- [ ] Add retry mechanism for failed requests

## 📝 Notes

- **Async operations**: Tất cả API calls đều async, không block UI thread
- **Error handling**: Luôn handle cả success và error cases
- **Loading states**: Show loading indicator khi gọi API
- **User feedback**: Hiển thị toast/snackbar cho user biết kết quả

---

**Last Updated:** 02/02/2026  
**Version:** 1.0  
**Retrofit Version:** 2.9.0  
**OkHttp Version:** 4.12.0
