# Cấu trúc Dự án E-Commerce - MVP Architecture

## 📁 Cấu trúc Package

```
com.son.e_commerce/
├── 📂 model/
│   ├── 📂 entity/
│   │   ├── 📄 User.java                    (Entity: Người dùng)
│   │   ├── 📄 Product.java                 (Entity: Sản phẩm)
│   │   ├── 📄 Category.java                (Entity: Danh mục)
│   │   ├── 📄 Order.java                   (Entity: Đơn hàng)
│   │   └── 📄 OrderItem.java               (Entity: Chi tiết đơn hàng)
│   └── 📂 repository/
│       ├── 📄 UserRepository.java          (Interface: User data access)
│       ├── 📄 ProductRepository.java       (Interface: Product data access)
│       ├── 📄 CategoryRepository.java      (Interface: Category data access)
│       └── 📄 OrderRepository.java         (Interface: Order data access)
│
├── 📂 view/
│   ├── 📂 adapter/                         (RecyclerView Adapters - chưa tạo)
│   ├── 📄 MainActivity.java                (Activity: Trang chủ)
│   ├── 📄 ExploreActivity.java             (Activity: Khám phá)
│   ├── 📄 OrdersActivity.java              (Activity: Đơn hàng)
│   └── 📄 ProfileActivity.java             (Activity: Hồ sơ)
│
├── 📂 presenter/
│   ├── 📂 contract/
│   │   ├── 📄 MainContract.java            (Contract: MainActivity)
│   │   ├── 📄 ExploreContract.java         (Contract: ExploreActivity)
│   │   ├── 📄 OrdersContract.java          (Contract: OrdersActivity)
│   │   ├── 📄 ProfileContract.java         (Contract: ProfileActivity)
│   │   └── 📄 ProductDetailContract.java   (Contract: ProductDetail)
│   ├── 📄 MainPresenter.java               (Presenter: Home logic)
│   ├── 📄 ExplorePresenter.java            (Presenter: Explore logic)
│   ├── 📄 OrdersPresenter.java             (Presenter: Orders logic)
│   └── 📄 ProfilePresenter.java            (Presenter: Profile logic)
│
├── 📂 data/
│   ├── 📄 UserRepositoryImpl.java          (Impl: User repository với mock data)
│   ├── 📄 ProductRepositoryImpl.java       (Impl: Product repository với mock data)
│   ├── 📄 CategoryRepositoryImpl.java      (Impl: Category repository với mock data)
│   └── 📄 OrderRepositoryImpl.java         (Impl: Order repository với mock data)
│
└── 📂 utils/                                (Utility classes - trống)
```

## 📊 Database Schema đã map

### 1. Users Table
```sql
- id (PK)
- username
- email
- password
- full_name
- role
- enabled
```
**→ User.java Entity**

### 2. Categories Table
```sql
- id (PK)
- name
- slug
```
**→ Category.java Entity** (+ iconResId cho mobile)

### 3. Products Table
```sql
- id (PK)
- name
- description
- price
- quantity
- image
- category_id (FK)
```
**→ Product.java Entity**

### 4. Orders Table
```sql
- id (PK)
- user_id (FK)
- created_at
- status
- total_price
```
**→ Order.java Entity**

### 5. Order_Items Table
```sql
- id (PK)
- order_id (FK)
- product_id (FK)
- quantity
- price
```
**→ OrderItem.java Entity**

## 🎯 Các màn hình đã implement

### 1. MainActivity (Home)
- **View**: MainActivity.java
- **Presenter**: MainPresenter.java
- **Contract**: MainContract.java
- **Layout**: activity_main.xml
- **Chức năng**:
  - Hiển thị banner khuyến mãi
  - Danh sách categories (horizontal scroll)
  - Sản phẩm đề xuất (grid layout)
  - Search bar
  - Cart badge
  - Bottom navigation

### 2. ExploreActivity (Khám phá)
- **View**: ExploreActivity.java
- **Presenter**: ExplorePresenter.java
- **Contract**: ExploreContract.java
- **Layout**: activity_explore.xml
- **Chức năng**:
  - Tìm kiếm sản phẩm
  - Lọc theo danh mục
  - Hiển thị tất cả sản phẩm
  - Filter button
  - Bottom navigation

### 3. OrdersActivity (Đơn hàng)
- **View**: OrdersActivity.java
- **Presenter**: OrdersPresenter.java
- **Contract**: OrdersContract.java
- **Layout**: activity_orders.xml
- **Chức năng**:
  - Hiển thị danh sách đơn hàng
  - Tab filter theo status
  - Empty state
  - Hủy đơn hàng
  - Bottom navigation

### 4. ProfileActivity (Hồ sơ)
- **View**: ProfileActivity.java
- **Presenter**: ProfilePresenter.java
- **Contract**: ProfileContract.java
- **Layout**: activity_profile.xml
- **Chức năng**:
  - Hiển thị thông tin user
  - Edit profile
  - Xem đơn hàng
  - Settings
  - Logout
  - Bottom navigation

## 🔄 MVP Flow

```
┌──────────┐      ┌───────────┐      ┌────────────┐      ┌──────────┐
│   View   │─────▶│ Presenter │─────▶│ Repository │─────▶│   Data   │
│(Activity)│      │  (Logic)  │      │(Interface) │      │ (Source) │
└──────────┘      └───────────┘      └────────────┘      └──────────┘
     ▲                  │                    │                   │
     │                  │                    │                   │
     └──────────────────┴────────────────────┴───────────────────┘
                    Callback chain
```

## 📦 Dependencies hiện tại

```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.activity:activity:1.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```

## 🎨 Resources đã tạo

### Layouts
- ✅ activity_main.xml
- ✅ activity_explore.xml
- ✅ activity_orders.xml
- ✅ activity_profile.xml
- ✅ item_category.xml
- ✅ item_product.xml

### Drawables/Icons
- ✅ ic_home.xml
- ✅ ic_explore.xml
- ✅ ic_orders.xml
- ✅ ic_profile.xml
- ✅ ic_phone.xml
- ✅ ic_laptop.xml
- ✅ ic_tablet.xml
- ✅ ic_accessories.xml
- ✅ ic_watch.xml
- ✅ ic_tv.xml
- ✅ ic_filter.xml
- ✅ ic_search.xml
- ✅ ic_shopping_cart.xml
- ✅ ic_notifications.xml

### Menus
- ✅ bottom_navigation_menu.xml

## 🚀 Mock Data

### Categories (6 items)
1. Điện thoại
2. Laptop
3. Tablet
4. Phụ kiện
5. Đồng hồ
6. TV & Audio

### Products (10 items)
- iPhone 15 Pro Max ($1199.99)
- Samsung Galaxy S24 Ultra ($1099.99)
- Xiaomi 14 Pro ($799.99)
- MacBook Pro M3 ($2499.99)
- Dell XPS 15 ($1799.99)
- Lenovo ThinkPad X1 ($1599.99)
- iPad Pro 12.9 ($1099.99)
- Samsung Galaxy Tab S9 ($899.99)
- AirPods Pro 2 ($249.99)
- Apple Watch Series 9 ($399.99)

### Orders (5 items)
- Status: pending, processing, shipped, delivered
- Mock data cho demo

### User (Demo account)
- Username: demo
- Password: demo123

## ✨ Tính năng đã implement

### ✅ Navigation
- Bottom Navigation working
- Chuyển đổi mượt mà giữa các màn hình
- Proper back stack management

### ✅ MVP Architecture
- Model layer với entities và repositories
- View layer với activities implement contracts
- Presenter layer xử lý business logic
- Clear separation of concerns

### ✅ Data Layer
- Repository pattern
- Mock data implementations
- Callback-based async operations
- SharedPreferences cho user session

### ✅ UI Components
- Material Design components
- CoordinatorLayout với NestedScrollView
- RecyclerView setup
- BottomNavigationView
- Tabs for orders

## 📝 Còn thiếu (TODO)

### 1. RecyclerView Adapters
- [ ] CategoryAdapter
- [ ] ProductAdapter
- [ ] OrderAdapter

### 2. Additional Screens
- [ ] ProductDetailActivity
- [ ] CartActivity
- [ ] CheckoutActivity
- [ ] LoginActivity
- [ ] RegisterActivity

### 3. Backend Integration
- [ ] Retrofit setup
- [ ] API service interfaces
- [ ] Network error handling
- [ ] Loading states

### 4. Local Database
- [ ] Room database setup
- [ ] DAOs
- [ ] Offline caching

### 5. Advanced Features
- [ ] Image loading (Glide/Picasso)
- [ ] Search functionality
- [ ] Filter/Sort products
- [ ] Add to cart
- [ ] Order tracking
- [ ] Push notifications

## 🛠️ Hướng dẫn Build

```bash
# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Clean build
./gradlew clean
```

## 📚 Tài liệu tham khảo

1. **MVP_ARCHITECTURE.md** - Chi tiết về MVP pattern
2. **NAVIGATION_GUIDE.md** - Hướng dẫn navigation
3. **DESIGN_SUMMARY.md** - Tổng quan thiết kế UI
4. **NEW_SCREENS_GUIDE.md** - Hướng dẫn tạo màn hình mới

## 🎓 Best Practices được áp dụng

✅ Separation of Concerns  
✅ Single Responsibility Principle  
✅ Dependency Injection (manual)  
✅ Interface Segregation  
✅ Null Safety checks  
✅ Proper resource management  
✅ Callback pattern cho async operations  
✅ Error handling  

## 📧 Contact & Support

Để hỗ trợ thêm về dự án, vui lòng tham khảo các file documentation trong thư mục gốc.

---

**Last Updated:** 02/02/2026  
**Version:** 1.0  
**Architecture:** MVP (Model-View-Presenter)  
**Min SDK:** 30  
**Target SDK:** 36
