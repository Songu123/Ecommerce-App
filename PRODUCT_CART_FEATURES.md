# 🛍️ Product Detail & Cart Features - Implementation Guide

## ✅ Đã hoàn thành

### 1️⃣ Product Detail Screen (Chi tiết sản phẩm)

**Files Created:**
- ✅ `ProductDetailActivity.java` - Activity hiển thị chi tiết
- ✅ `ProductDetailPresenter.java` - Business logic
- ✅ `activity_product_detail.xml` - Layout
- ✅ `ic_add.xml`, `ic_remove.xml` - Icons
- ✅ `quantity_background.xml` - Drawable

**Features:**
- ✅ Hiển thị thông tin sản phẩm (tên, giá, mô tả, stock)
- ✅ Selector số lượng (+ / -)
- ✅ Button "Thêm vào giỏ hàng"
- ✅ Button "Mua ngay"
- ✅ Rating và reviews display
- ✅ Stock status với màu sắc
- ✅ Back button
- ✅ Favorite button (UI only)
- ✅ Loading state
- ✅ Error handling

### 2️⃣ Cart Screen (Giỏ hàng)

**Files Created:**
- ✅ `CartActivity.java` - Cart management
- ✅ `CartAdapter.java` - RecyclerView adapter
- ✅ `activity_cart.xml` - Cart layout
- ✅ `item_cart.xml` - Cart item layout
- ✅ `ic_delete.xml` - Delete icon

**Features:**
- ✅ Hiển thị danh sách items trong giỏ
- ✅ Update số lượng (+/-)
- ✅ Remove item khỏi giỏ
- ✅ Tính tổng giá trị
- ✅ Empty state
- ✅ Checkout button (UI only)
- ✅ Continue shopping button
- ✅ Loading state

---

## 🎯 User Flow

### Flow 1: Xem chi tiết sản phẩm
```
MainActivity/ExploreActivity
    ↓ Click product
ProductDetailActivity
    ↓ View details
    • Product image (placeholder)
    • Name, price, description
    • Rating & stock status
    • Quantity selector
    • Add to cart / Buy now buttons
```

### Flow 2: Thêm vào giỏ hàng
```
ProductDetailActivity
    ↓ Select quantity (1-50)
    ↓ Click "Thêm vào giỏ"
    ↓ API call: POST /api/cart/add
CartRepositoryImpl
    ↓ Success
Show toast: "Đã thêm vào giỏ hàng"
```

### Flow 3: Xem giỏ hàng
```
MainActivity
    ↓ Click cart icon (top right)
CartActivity
    ↓ Load cart from API
    ↓ GET /api/cart/{userId}
Display cart items
    • Product image & name
    • Price & quantity
    • Subtotal per item
    • Total price
    • Update/Remove buttons
```

### Flow 4: Quản lý giỏ hàng
```
CartActivity
├─ Update quantity
│   ↓ Click +/-
│   ↓ PUT /api/cart/update
│   ↓ Reload cart
│
├─ Remove item
│   ↓ Click delete icon
│   ↓ DELETE /api/cart/{itemId}
│   ↓ Reload cart
│
└─ Checkout
    ↓ Click "Thanh toán"
    ↓ TODO: Navigate to checkout
```

---

## 📱 Screenshots Description

### Product Detail Screen
```
┌─────────────────────────────────┐
│ [←]                    [♥]      │
│                                 │
│     [Product Image Large]       │
│                                 │
├─────────────────────────────────┤
│ iPhone 15 Pro Max              │
│ ⭐ 4.8 (1,234 đánh giá)        │
│ Còn hàng: 50                   │
│                                 │
│ $1,199.99                       │
├─────────────────────────────────┤
│ Mô tả sản phẩm                 │
│ Siêu phẩm từ Apple với chip... │
├─────────────────────────────────┤
│ Số lượng:          [-] 1 [+]   │
├─────────────────────────────────┤
│ [Thêm vào giỏ]  [Mua ngay]    │
└─────────────────────────────────┘
```

### Cart Screen
```
┌─────────────────────────────────┐
│ [←]      Giỏ hàng              │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │ [IMG] iPhone 15 Pro Max  [X]│ │
│ │       $1,199.99             │ │
│ │       [-] 2 [+]  $2,399.98  │ │
│ └─────────────────────────────┘ │
│ ┌─────────────────────────────┐ │
│ │ [IMG] Samsung Galaxy    [X] │ │
│ │       $899.99               │ │
│ │       [-] 1 [+]  $899.99    │ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ Tổng cộng:          $3,299.97  │
│ [     Thanh toán     ]          │
└─────────────────────────────────┘
```

---

## 🔧 API Integration

### Add to Cart
```java
// In ProductDetailActivity
cartRepository.addToCart(
    userId,
    productId,
    quantity,
    new CartRepositoryImpl.OnCartItemAddedListener() {
        @Override
        public void onSuccess(OrderItem item) {
            Toast.makeText(context, "Đã thêm vào giỏ hàng", 
                Toast.LENGTH_SHORT).show();
        }
        
        @Override
        public void onError(String error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
);
```

**API Call:**
```
POST /api/cart/add
Body: {
    "userId": 1,
    "productId": 5,
    "quantity": 2
}
Response: OrderItem object
```

### Get Cart
```java
// In CartActivity
cartRepository.getCart(userId, 
    new CartRepositoryImpl.OnCartLoadedListener() {
        @Override
        public void onSuccess(List<OrderItem> cartItems) {
            showCart(cartItems);
            updateTotal(cartItems);
        }
        
        @Override
        public void onError(String error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
);
```

**API Call:**
```
GET /api/cart/{userId}
Response: List<OrderItem>
```

### Update Cart Item
```java
cartRepository.updateCartItem(
    userId,
    productId,
    newQuantity,
    listener
);
```

**API Call:**
```
PUT /api/cart/update
Body: {
    "userId": 1,
    "productId": 5,
    "quantity": 3
}
```

### Remove from Cart
```java
cartRepository.removeFromCart(itemId, listener);
```

**API Call:**
```
DELETE /api/cart/{itemId}
```

---

## 💻 Code Examples

### Navigate to Product Detail
```java
// From any Activity with Product object
Intent intent = new Intent(this, ProductDetailActivity.class);
intent.putExtra("product", product);
startActivity(intent);

// Or with product ID
Intent intent = new Intent(this, ProductDetailActivity.class);
intent.putExtra("product_id", productId);
startActivity(intent);
```

### Open Cart
```java
// From MainActivity or any Activity
Intent intent = new Intent(this, CartActivity.class);
startActivity(intent);
```

### Quantity Selector Logic
```java
// Increase quantity
buttonPlus.setOnClickListener(v -> {
    if (currentQuantity < product.getQuantity()) {
        currentQuantity++;
        updateQuantityDisplay();
    } else {
        Toast.makeText(this, "Đã đạt số lượng tối đa", 
            Toast.LENGTH_SHORT).show();
    }
});

// Decrease quantity
buttonMinus.setOnClickListener(v -> {
    if (currentQuantity > 1) {
        currentQuantity--;
        updateQuantityDisplay();
    }
});
```

---

## 🎨 UI Components

### Product Detail Screen

**Components:**
- `ImageView` - Product image (400dp height)
- `TextView` - Product name (24sp, bold)
- `TextView` - Rating with star icon
- `TextView` - Stock status with color
- `TextView` - Price (32sp, primary color)
- `TextView` - Description
- Quantity selector with +/- buttons
- 2 action buttons (Add to Cart, Buy Now)
- Back button (top left)
- Favorite button (top right)
- ProgressBar for loading

**Colors:**
- Stock available: Green
- Stock out: Red
- Price: Primary color
- Buttons: Material Design theme

### Cart Screen

**Components:**
- RecyclerView for cart items
- Each item shows:
  - Product image (80x80dp)
  - Name, price
  - Quantity controls (+/-)
  - Subtotal
  - Remove button
- Total price display
- Checkout button
- Empty state layout
- ProgressBar

---

## 🔄 State Management

### Product Detail States
```java
// Loading
showLoading()
    - Show ProgressBar
    - Disable buttons

// Success
showProduct(product)
    - Hide ProgressBar
    - Populate data
    - Enable buttons

// Out of Stock
showOutOfStock()
    - Disable add to cart
    - Show "Hết hàng" message
    - Change button text

// Error
showError(message)
    - Hide ProgressBar
    - Show toast
```

### Cart States
```java
// Loading
showLoading()
    - Show ProgressBar
    - Hide RecyclerView

// Cart with items
showCart(items)
    - Hide ProgressBar
    - Show RecyclerView
    - Update total

// Empty cart
showEmptyCart()
    - Hide RecyclerView
    - Show empty state
    - Show "Continue shopping" button
```

---

## ✅ Testing Checklist

### Product Detail
- [ ] Open product from home screen
- [ ] See product details loaded
- [ ] Increase/decrease quantity
- [ ] Check quantity limits (1 to stock)
- [ ] Click "Thêm vào giỏ" → Success toast
- [ ] Check API call in Logcat
- [ ] Test with out-of-stock product
- [ ] Test back button

### Cart
- [ ] Click cart icon from home
- [ ] See cart items loaded
- [ ] Update quantity (+/-)
- [ ] Remove item (click X)
- [ ] Check total updates correctly
- [ ] Test empty cart state
- [ ] Click "Continue shopping" → Back to home
- [ ] Check API calls in Logcat

---

## 🐛 Common Issues & Solutions

### Issue 1: Cannot add to cart - User not logged in
```
Error: "Vui lòng đăng nhập để thêm vào giỏ hàng"

Solution:
✅ Check: User is logged in
✅ Check: userRepository.getCurrentUser() != null
✅ If needed: Create mock user for testing
```

### Issue 2: Product not Serializable
```
Error: java.io.NotSerializableException: Product

Solution:
✅ Add: implements Serializable to Product class
✅ Already done in our implementation
```

### Issue 3: Cart not loading
```
Error: Network error or empty response

Solution:
✅ Check: Spring Boot backend running
✅ Check: User has items in cart
✅ Check: API endpoint /api/cart/{userId}
✅ Verify: userId is valid
```

---

## 🚀 Next Steps

### Phase 1: Current Features
- [x] Product detail screen
- [x] Add to cart
- [x] View cart
- [x] Update cart quantities
- [x] Remove from cart
- [x] Calculate totals

### Phase 2: Enhancements (Next)
- [ ] Add Glide for image loading
- [ ] Implement favorite functionality
- [ ] Add product reviews section
- [ ] Image gallery/zoom
- [ ] Related products
- [ ] Share product

### Phase 3: Checkout (Future)
- [ ] Checkout screen
- [ ] Shipping address
- [ ] Payment method selection
- [ ] Order confirmation
- [ ] Payment integration

### Phase 4: Polish
- [ ] Add animations
- [ ] Implement pull-to-refresh
- [ ] Add cart badge counter
- [ ] Persistent cart (Room DB)
- [ ] Cart sync across devices

---

## 📊 Architecture

### MVP Pattern Implementation

```
ProductDetailActivity (View)
    ↓
ProductDetailPresenter
    ↓
ProductRepository + CartRepository
    ↓
Retrofit API Service
    ↓
Spring Boot Backend
```

### Data Flow
```
User Action
    ↓
View calls Presenter method
    ↓
Presenter calls Repository
    ↓
Repository makes API call
    ↓
Callback returns to Presenter
    ↓
Presenter updates View
    ↓
UI Updates
```

---

## 📝 Notes

### Important
- Product must implement Serializable for Intent extras
- User must be logged in to add to cart
- Quantity cannot exceed stock
- Cart operations require valid userId

### Best Practices
- Always show loading state
- Handle errors gracefully
- Validate quantities
- Update totals after changes
- Clear loading state on error

### Future Improvements
- Add image caching
- Implement offline cart
- Add cart sync
- Optimize API calls
- Add analytics tracking

---

**Status:** ✅ Complete & Ready for Testing  
**Date:** 02/02/2026  
**Features:** Product Detail + Cart Management  
**API Integration:** Full CRUD operations

---

**Test these features now! 🚀**

1. Run app
2. Click any product → See detail
3. Add to cart → Success
4. Click cart icon → See cart
5. Update quantities → Works
6. Remove items → Updates

**Happy Shopping! 🛍️**
