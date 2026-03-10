# Giao diện Marketly E-Commerce App

## Tổng quan
Đã tạo lại giao diện ứng dụng e-commerce theo thiết kế Marketly với giao diện hiện đại, chuyên nghiệp.

## Các thành phần chính

### 1. Layout Files
- **activity_main.xml** - Màn hình chính với:
  - Top bar cố định với logo, thông báo, giỏ hàng (có badge số lượng)
  - Thanh tìm kiếm với icon search và voice search
  - Banner khuyến mãi
  - Danh sách danh mục (horizontal RecyclerView)
  - Danh sách sản phẩm gợi ý (grid 2 cột)
  - Bottom Navigation với 4 tab

- **item_category.xml** - Item cho danh mục:
  - Card view tròn với background màu
  - Icon danh mục
  - Tên danh mục

- **item_product.xml** - Item cho sản phẩm:
  - Hình ảnh sản phẩm
  - Badge "BÁN CHẠY" hoặc giảm giá (ví dụ: "-15%")
  - Icon yêu thích (trái tim)
  - Tên sản phẩm
  - Đánh giá sao + số lượng review
  - Giá sản phẩm

- **item_promo_banner.xml** - Banner khuyến mãi

### 2. Drawables
**Icons:**
- ic_shopping_bag.xml - Logo app
- ic_notifications.xml - Thông báo
- ic_shopping_cart.xml - Giỏ hàng
- ic_search.xml - Tìm kiếm
- ic_mic.xml - Voice search
- ic_star.xml - Đánh giá sao
- ic_favorite.xml - Yêu thích
- ic_home.xml - Tab trang chủ
- ic_explore.xml - Tab khám phá
- ic_orders.xml - Tab đơn hàng
- ic_profile.xml - Tab cá nhân

**Category Icons:**
- ic_electronics.xml - Điện tử (màu xanh dương)
- ic_fashion.xml - Thời trang (màu vàng)
- ic_home_category.xml - Nhà cửa (màu xanh lá)
- ic_beauty.xml - Làm đẹp (màu hồng)
- ic_games.xml - Trò chơi (màu tím)

**Backgrounds & Badges:**
- badge_background.xml - Background cho cart badge (đỏ tròn)
- favorite_background.xml - Background cho nút yêu thích (trắng tròn)
- hot_deal_badge.xml - Badge "BÁN CHẠY" (xanh dương)
- discount_badge.xml - Badge giảm giá (đỏ)
- promo_background.xml - Background gradient cho banner

### 3. Colors
```xml
- primary: #2563EB (Xanh dương chủ đạo)
- text_primary: #1F2937 (Đen chữ chính)
- text_secondary: #6B7280 (Xám chữ phụ)
- badge_red: #EF4444 (Đỏ badge)
- rating_yellow: #FBBF24 (Vàng sao đánh giá)
- discount_red: #DC2626 (Đỏ giảm giá)
- category_bg_1-5: Màu nền pastel cho các danh mục
```

### 4. Strings (Tiếng Việt)
- Tên app: "Marketly"
- Các label: "Danh mục", "Gợi ý cho bạn", "Xem tất cả", "Xem thêm"
- Search hint: "Tìm kiếm sản phẩm…"
- Bottom nav: "Trang chủ", "Khám phá", "Đơn hàng", "Cá nhân"
- Và nhiều string khác

## Tính năng giao diện

### Header (Fixed)
- Logo Marketly với icon túi xách
- Nút thông báo
- Nút giỏ hàng với badge hiển thị số lượng (3)
- Thanh tìm kiếm với icon search và voice search

### Banner khuyến mãi
- Thiết kế gradient tối
- Text "LIMITED OFFER"
- "Giảm giá đến 50%"
- "Áp dụng cho mọi đơn hàng điện tử"

### Danh mục
- 5 danh mục: Điện tử, Thời trang, Nhà cửa, Làm đẹp, Trò chơi
- Mỗi danh mục có icon và màu nền riêng
- Scroll ngang

### Sản phẩm gợi ý
- Hiển thị dạng grid 2 cột
- Mỗi sản phẩm có:
  - Hình ảnh lớn
  - Nút yêu thích (trái tim trắng)
  - Badge "BÁN CHẠY" hoặc giảm giá
  - Tên sản phẩm (tối đa 2 dòng)
  - Đánh giá sao + số lượng review
  - Giá tiền (màu xanh dương)

### Bottom Navigation
- 4 tab với icon và label
- Trang chủ, Khám phá, Đơn hàng, Cá nhân
- Active state màu xanh dương

## Build Status
✅ **BUILD SUCCESSFUL** - Tất cả resources đã được tạo và linking thành công.

## Hướng dẫn sử dụng
1. Mở project trong Android Studio
2. Sync Gradle
3. Chạy app trên emulator hoặc thiết bị thật
4. Giao diện sẽ hiển thị theo đúng design Marketly

## Lưu ý
- Để hiển thị dữ liệu thực, cần implement RecyclerView Adapter cho categories và products
- Banner có thể thay bằng ViewPager2 cho nhiều banner
- Các nút click cần implement listener trong MainActivity
