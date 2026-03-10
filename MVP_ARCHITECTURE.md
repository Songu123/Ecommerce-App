# Kiến trúc MVP - E-Commerce App

## Tổng quan

Dự án E-Commerce đã được cấu trúc theo mô hình **MVP (Model-View-Presenter)** để tách biệt logic nghiệp vụ khỏi giao diện người dùng, giúp code dễ bảo trì, test và mở rộng.

## Cấu trúc thư mục

```
com.son.e_commerce/
├── model/
│   ├── entity/                 # Các class đối tượng dữ liệu
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   └── repository/             # Interface định nghĩa data access
│       ├── UserRepository.java
│       ├── ProductRepository.java
│       ├── CategoryRepository.java
│       └── OrderRepository.java
├── view/
│   ├── adapter/                # RecyclerView Adapters
│   ├── MainActivity.java       # Home screen
│   ├── ExploreActivity.java    # Explore/Search screen
│   ├── OrdersActivity.java     # Orders management
│   └── ProfileActivity.java    # User profile
├── presenter/
│   ├── contract/               # View-Presenter contracts
│   │   ├── MainContract.java
│   │   ├── ExploreContract.java
│   │   ├── OrdersContract.java
│   │   ├── ProfileContract.java
│   │   └── ProductDetailContract.java
│   ├── MainPresenter.java
│   ├── ExplorePresenter.java
│   ├── OrdersPresenter.java
│   └── ProfilePresenter.java
├── data/                       # Repository implementations
│   ├── UserRepositoryImpl.java
│   ├── ProductRepositoryImpl.java
│   ├── CategoryRepositoryImpl.java
│   └── OrderRepositoryImpl.java
└── utils/                      # Utility classes
```

## Các thành phần chính

### 1. Model Layer

#### Entities (model/entity/)
Các class POJO đại diện cho dữ liệu trong database:

- **User**: Thông tin người dùng
- **Product**: Sản phẩm
- **Category**: Danh mục sản phẩm
- **Order**: Đơn hàng
- **OrderItem**: Chi tiết sản phẩm trong đơn hàng

#### Repositories (model/repository/)
Interface định nghĩa các phương thức truy xuất dữ liệu:

- **UserRepository**: Login, register, get/update user
- **ProductRepository**: CRUD operations cho products
- **CategoryRepository**: Lấy danh mục
- **OrderRepository**: Quản lý đơn hàng

### 2. View Layer

Activities implement View interface từ Contract:

```java
public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize repositories
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        
        // Initialize presenter
        presenter = new MainPresenter(this, categoryRepo, productRepo);
        
        // Load data
        presenter.loadCategories();
        presenter.loadRecommendedProducts();
    }
    
    @Override
    public void showCategories(List<Category> categories) {
        // Update UI with categories
    }
    
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
```

### 3. Presenter Layer

#### Contracts (presenter/contract/)
Interface định nghĩa communication giữa View và Presenter:

```java
public interface MainContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showCategories(List<Category> categories);
        void showError(String message);
    }
    
    interface Presenter {
        void loadCategories();
        void onCategoryClick(Category category);
        void onDestroy();
    }
}
```

#### Presenters
Class implement business logic:

```java
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private CategoryRepository categoryRepository;
    
    public MainPresenter(MainContract.View view, CategoryRepository repo) {
        this.view = view;
        this.categoryRepository = repo;
    }
    
    @Override
    public void loadCategories() {
        view.showLoading();
        categoryRepository.getAllCategories(new OnCategoriesLoadedListener() {
            @Override
            public void onSuccess(List<Category> categories) {
                view.hideLoading();
                view.showCategories(categories);
            }
            
            @Override
            public void onError(String error) {
                view.hideLoading();
                view.showError(error);
            }
        });
    }
}
```

### 4. Data Layer

Repository implementations với mock data:

```java
public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public void getAllCategories(OnCategoriesLoadedListener listener) {
        // Simulate network call
        new Handler().postDelayed(() -> {
            List<Category> categories = getMockCategories();
            listener.onSuccess(categories);
        }, 500);
    }
    
    private List<Category> getMockCategories() {
        // Return mock data
    }
}
```

## Luồng dữ liệu (Data Flow)

```
User Action → View → Presenter → Repository → Data Source
                ↑                      ↓
                └──────── Callback ────┘
```

### Ví dụ: Load danh mục sản phẩm

1. **View**: User mở MainActivity
2. **View → Presenter**: `presenter.loadCategories()`
3. **Presenter → Repository**: `categoryRepository.getAllCategories()`
4. **Repository**: Lấy dữ liệu (từ API/Database)
5. **Repository → Presenter**: Callback với dữ liệu
6. **Presenter → View**: `view.showCategories(categories)`
7. **View**: Hiển thị categories trên UI

## Ưu điểm của MVP

### ✅ Separation of Concerns
- View chỉ xử lý UI
- Presenter xử lý business logic
- Model quản lý dữ liệu

### ✅ Testability
- Dễ dàng unit test Presenter mà không cần Android framework
- Mock View và Repository để test

### ✅ Maintainability
- Code rõ ràng, dễ đọc
- Dễ thêm tính năng mới
- Thay đổi UI không ảnh hưởng business logic

### ✅ Reusability
- Repository có thể tái sử dụng cho nhiều Presenter
- Presenter logic có thể tái sử dụng

## Hướng dẫn sử dụng

### Tạo một màn hình mới

#### Bước 1: Tạo Contract
```java
public interface NewScreenContract {
    interface View {
        void showData(List<Data> data);
        void showError(String error);
    }
    
    interface Presenter {
        void loadData();
        void onDestroy();
    }
}
```

#### Bước 2: Tạo Presenter
```java
public class NewScreenPresenter implements NewScreenContract.Presenter {
    private NewScreenContract.View view;
    private DataRepository repository;
    
    public NewScreenPresenter(NewScreenContract.View view, DataRepository repo) {
        this.view = view;
        this.repository = repo;
    }
    
    @Override
    public void loadData() {
        repository.getData(new OnDataLoadedListener() {
            @Override
            public void onSuccess(List<Data> data) {
                if (view != null) {
                    view.showData(data);
                }
            }
            
            @Override
            public void onError(String error) {
                if (view != null) {
                    view.showError(error);
                }
            }
        });
    }
    
    @Override
    public void onDestroy() {
        view = null;
    }
}
```

#### Bước 3: Implement View trong Activity
```java
public class NewScreenActivity extends AppCompatActivity 
    implements NewScreenContract.View {
    
    private NewScreenPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen);
        
        DataRepository repository = new DataRepositoryImpl();
        presenter = new NewScreenPresenter(this, repository);
        
        presenter.loadData();
    }
    
    @Override
    public void showData(List<Data> data) {
        // Update UI
    }
    
    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
```

## Database Schema Mapping

### Users Table → User Entity
```java
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private boolean enabled;
}
```

### Products Table → Product Entity
```java
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image;
    private int categoryId;
}
```

### Categories Table → Category Entity
```java
public class Category {
    private int id;
    private String name;
    private String slug;
    private int iconResId;
}
```

### Orders Table → Order Entity
```java
public class Order {
    private int id;
    private int userId;
    private Date createdAt;
    private String status;
    private double totalPrice;
    private List<OrderItem> orderItems;
}
```

### Order_Items Table → OrderItem Entity
```java
public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private Product product;
}
```

## Tích hợp với Backend

Để kết nối với backend API thực tế:

### 1. Thêm Retrofit dependency
```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

### 2. Tạo API Service
```java
public interface ApiService {
    @GET("products")
    Call<List<Product>> getProducts();
    
    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);
    
    @POST("orders")
    Call<Order> createOrder(@Body Order order);
}
```

### 3. Update Repository Implementation
```java
public class ProductRepositoryImpl implements ProductRepository {
    private ApiService apiService;
    
    @Override
    public void getAllProducts(OnProductsLoadedListener listener) {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to load products");
                }
            }
            
            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }
}
```

## Best Practices

1. **Always null-check view** trong Presenter callbacks
2. **Call presenter.onDestroy()** trong Activity/Fragment onDestroy
3. **Use dependency injection** (Dagger/Hilt) cho production app
4. **Implement error handling** đầy đủ
5. **Use RxJava/Coroutines** cho async operations phức tạp
6. **Write unit tests** cho Presenter logic
7. **Keep Presenter lifecycle-aware** để tránh memory leaks

## Mở rộng

- Thêm **Repository pattern with caching** (Room Database)
- Implement **Dependency Injection** với Dagger/Hilt
- Chuyển sang **MVVM** với LiveData/ViewModel
- Thêm **Navigation Component** cho navigation
- Implement **Paging** cho danh sách dài
- Thêm **WorkManager** cho background tasks

## Testing

### Unit Test Presenter
```java
@Test
public void loadCategories_success() {
    // Given
    MainPresenter presenter = new MainPresenter(view, categoryRepo);
    List<Category> mockCategories = Arrays.asList(new Category(...));
    
    // When
    when(categoryRepo.getAllCategories(any())).thenAnswer(invocation -> {
        OnCategoriesLoadedListener listener = invocation.getArgument(0);
        listener.onSuccess(mockCategories);
        return null;
    });
    
    presenter.loadCategories();
    
    // Then
    verify(view).showLoading();
    verify(view).showCategories(mockCategories);
    verify(view).hideLoading();
}
```

---

**Tài liệu được tạo bởi:** AI Assistant  
**Ngày tạo:** 02/02/2026  
**Version:** 1.0
