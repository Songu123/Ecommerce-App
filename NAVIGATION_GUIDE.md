# Hướng dẫn Bottom Navigation

## Tổng quan

Dự án E-Commerce đã được cấu hình với Bottom Navigation để điều hướng giữa 4 màn hình chính:
- **Home (Trang chủ)** - MainActivity
- **Explore (Khám phá)** - ExploreActivity  
- **Orders (Đơn hàng)** - OrdersActivity
- **Profile (Hồ sơ)** - ProfileActivity

## Cấu trúc

### 1. Activities đã tạo

#### MainActivity.java
- Màn hình trang chủ với banner khuyến mãi, danh mục và sản phẩm đề xuất
- BottomNavigationView ID: `bottomNavigation`
- Menu item ID: `R.id.nav_home`

#### ExploreActivity.java
- Màn hình khám phá với tìm kiếm và lọc sản phẩm
- Hiển thị tất cả danh mục và sản phẩm
- Layout: `activity_explore.xml`
- BottomNavigationView ID: `bottomNavigation`
- Menu item ID: `R.id.nav_explore`

#### OrdersActivity.java
- Màn hình quản lý đơn hàng
- Tabs để lọc theo trạng thái: Tất cả, Chờ xác nhận, Đang giao, Hoàn thành, Đã hủy
- Layout: `activity_orders.xml`
- BottomNavigationView ID: `bottomNavigation`
- Menu item ID: `R.id.nav_orders`

#### ProfileActivity.java
- Màn hình hồ sơ người dùng
- Layout: `activity_profile.xml` (đã tồn tại)
- BottomNavigationView ID: `bottomNavigationProfile`
- Menu item ID: `R.id.nav_profile`

### 2. Navigation Flow

Mỗi Activity đều implement phương thức `setupBottomNavigation()` để:
1. Thiết lập item được chọn tương ứng với màn hình hiện tại
2. Xử lý sự kiện click vào các tab khác
3. Sử dụng `overridePendingTransition(0, 0)` để tắt animation chuyển màn hình
4. Gọi `finish()` sau khi start Activity mới để tránh tích lũy Activity trong back stack

### 3. Menu Configuration

File: `res/menu/bottom_navigation_menu.xml`

```xml
- nav_home: Trang chủ (icon: ic_home)
- nav_explore: Khám phá (icon: ic_explore)
- nav_orders: Đơn hàng (icon: ic_orders)
- nav_profile: Hồ sơ (icon: ic_profile)
```

### 4. AndroidManifest.xml

Tất cả Activities đã được đăng ký:
- MainActivity: exported=true (launcher activity)
- ExploreActivity: exported=false
- OrdersActivity: exported=false
- ProfileActivity: exported=false

## Cách hoạt động

1. Người dùng click vào một tab trong BottomNavigationView
2. `setOnItemSelectedListener` được trigger
3. Activity tương ứng được start với Intent
4. Animation chuyển màn hình bị tắt để có cảm giác mượt mà như switching tabs
5. Activity hiện tại được finish để giải phóng bộ nhớ
6. Activity mới được hiển thị với tab tương ứng được highlight

## Lưu ý

- Các layout đều sử dụng CoordinatorLayout để hỗ trợ scroll behavior
- BottomNavigationView được đặt ở bottom với `layout_gravity="bottom"`
- Sử dụng `@color/bottom_nav_selector` để tạo hiệu ứng màu cho item được chọn
- Padding bottom của content được set để tránh bị che bởi bottom navigation

## Mở rộng

Để thêm tính năng vào các Activity:
1. Thêm logic xử lý business vào onCreate() hoặc các phương thức khác
2. Bind views và setup RecyclerView adapters
3. Implement các click listeners cho các nút và items
4. Thêm ViewModel và LiveData nếu cần theo MVVM pattern
