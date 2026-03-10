# 🛒 Hướng dẫn Debug Giỏ hàng - Cart Debugging Guide

## ❗ Vấn đề (Problem)
Khi thêm sản phẩm vào giỏ hàng, sản phẩm không xuất hiện khi vào xem giỏ hàng.
(Products added to cart don't appear when viewing the cart)

## ✅ Các cải thiện đã thực hiện (Improvements Made)

### 1. Thêm Logging chi tiết (Detailed Logging)

#### CartRepositoryImpl.java
- ✅ Thêm TAG cho logging
- ✅ Log chi tiết trong `getCart()`:
  - Request userId
  - Response code
  - Số lượng items nhận được
  - Chi tiết từng item (ID, ProductID, Quantity, Price, Product object)
  - Lỗi network nếu có

#### CartFragment.java
- ✅ Thêm TAG cho logging
- ✅ Log trong `loadCart()`:
  - Khi method được gọi
  - User ID đang load
  - Số lượng items nhận được
  - Hiển thị cart hay empty state
  - Lỗi nếu có

#### CartAdapter.java
- ✅ Thêm TAG cho logging
- ✅ Log trong `setCartItems()`:
  - Số lượng items được set
  - Item count sau khi update
- ✅ Log trong `bind()`:
  - Chi tiết từng item được bind
  - Product name hoặc fallback
  - Cảnh báo nếu Product object null

### 2. Cải thiện Error Handling

#### CartRepositoryImpl.java
```java
// Kiểm tra null và log chi tiết
if (response.isSuccessful() && response.body() != null) {
    List<OrderItem> items = response.body();
    Log.d(TAG, "getCart success. Items count: " + items.size());
    
    // Log each item
    for (OrderItem item : items) {
        Log.d(TAG, "Cart Item - ID: " + item.getId() + 
            ", ProductID: " + item.getProductId() + 
            ", Quantity: " + item.getQuantity() + 
            ", Has Product: " + (item.getProduct() != null));
    }
    
    listener.onSuccess(items);
}
```

#### CartAdapter.java
```java
public void setCartItems(List<OrderItem> items) {
    // Kiểm tra null để tránh crash
    this.cartItems = items != null ? items : new ArrayList<>();
    notifyDataSetChanged();
}
```

## 🔍 Cách Debug (How to Debug)

### Bước 1: Thêm sản phẩm vào giỏ
1. Mở app và đăng nhập
2. Vào chi tiết sản phẩm
3. Chọn số lượng và click "Thêm vào giỏ hàng"
4. Kiểm tra Logcat với tag `ProductDetailPresenter`

**Expected logs:**
```
D/ProductDetailPresenter: onAddToCartClick called with quantity: 1
D/ProductDetailPresenter: Adding to cart - UserId: 1, ProductId: 5, Quantity: 1
D/CartRepositoryImpl: addToCart - UserId: 1, ProductId: 5, Quantity: 1
D/CartRepositoryImpl: addToCart response code: 200
D/CartRepositoryImpl: addToCart success. Item ID: 123
D/ProductDetailPresenter: Successfully added to cart. Item ID: 123
```

### Bước 2: Xem giỏ hàng
1. Click vào tab Cart hoặc Cart icon
2. Kiểm tra Logcat với các tags:
   - `CartFragment`
   - `CartRepositoryImpl`
   - `CartAdapter`

**Expected logs:**
```
D/CartFragment: loadCart() called
D/CartFragment: Loading cart for user ID: 1
D/CartRepositoryImpl: getCart - UserId: 1
D/CartRepositoryImpl: getCart response code: 200
D/CartRepositoryImpl: getCart success. Items count: 2
D/CartRepositoryImpl: Cart Item - ID: 123, ProductID: 5, Quantity: 1, Price: 999.99, Has Product: true
D/CartRepositoryImpl: Cart Item - ID: 124, ProductID: 7, Quantity: 2, Price: 499.99, Has Product: true
D/CartFragment: Cart loaded successfully. Items count: 2
D/CartFragment: Showing cart with 2 items
D/CartFragment: showCart() - Setting visibility and updating adapter
D/CartAdapter: setCartItems() called with 2 items
D/CartAdapter: Adapter updated. Item count: 2
D/CartAdapter: Binding item - ID: 123, ProductID: 5, Quantity: 1, Price: 999.99, Has Product: true
D/CartAdapter: Product name: iPhone 15 Pro Max
D/CartAdapter: Binding item - ID: 124, ProductID: 7, Quantity: 2, Price: 499.99, Has Product: true
D/CartAdapter: Product name: Samsung Galaxy S24
D/CartFragment: Cart displayed with 2 items
```

### Bước 3: Xác định vấn đề

#### Trường hợp 1: Giỏ hàng rỗng
```
D/CartRepositoryImpl: getCart success. Items count: 0
D/CartFragment: Cart is empty, showing empty state
```
**→ Vấn đề:** Backend không trả về items hoặc addToCart không thành công

**Giải pháp:**
- Kiểm tra API response khi addToCart
- Kiểm tra database backend
- Verify userId đúng

#### Trường hợp 2: Items không có Product object
```
D/CartRepositoryImpl: Cart Item - ID: 123, ProductID: 5, ..., Has Product: false
D/CartAdapter: Product object is null, using fallback name
```
**→ Vấn đề:** Backend không populate Product object trong OrderItem

**Giải pháp:**
- Sửa backend API để include Product details
- Hoặc fetch Product separately ở frontend

#### Trường hợp 3: Network Error
```
E/CartRepositoryImpl: getCart failed: Network error: Failed to connect to /10.0.2.2:8080
```
**→ Vấn đề:** Backend server không chạy hoặc URL sai

**Giải pháp:**
- Kiểm tra backend server đang chạy
- Verify BASE_URL trong RetrofitClient
- Kiểm tra network permissions

#### Trường hợp 4: User chưa đăng nhập
```
E/CartFragment: No user logged in
```
**→ Vấn đề:** User session hết hạn hoặc chưa login

**Giải pháp:**
- Đăng nhập lại
- Kiểm tra SharedPreferences

## 🔧 Common Issues & Solutions

### Issue 1: Cart appears empty but items were added
**Symptoms:**
- addToCart returns success
- getCart returns empty list

**Debug:**
```bash
# Check logs
adb logcat | grep -E "(CartRepositoryImpl|CartFragment)"
```

**Possible causes:**
1. UserId mismatch between add and get
2. Backend cart storage issue
3. Items added to different user's cart

**Solution:**
- Verify same userId in both operations
- Check backend logs
- Clear app data and try again

### Issue 2: Items appear but Product info is missing
**Symptoms:**
- Cart shows "Product #123" instead of product name
- Price might be $0.00

**Debug:**
```
D/CartAdapter: Product object is null, using fallback name
```

**Solution:**
Update backend to include Product in OrderItem response:
```java
// Backend - OrderItem should include product
public class OrderItem {
    private Product product; // Make sure this is populated
    // ...
}
```

### Issue 3: Cart doesn't refresh after adding item
**Symptoms:**
- Item added successfully
- Cart screen doesn't show new item until restart

**Solution:**
The cart already refreshes in `onResume()`, but you can also:
1. Broadcast event when item added
2. Navigate to cart after adding
3. Add refresh button

## 📱 Testing Checklist

- [ ] Add product to cart → See success toast
- [ ] Navigate to cart → See items appear
- [ ] Check Logcat for errors
- [ ] Update quantity → See cart refresh
- [ ] Remove item → See cart update
- [ ] Add multiple products → All appear
- [ ] Logout/Login → Cart persists
- [ ] Test with empty cart → See empty state

## 🌐 API Endpoints to Verify

### Add to Cart
```
POST http://10.0.2.2:8080/api/cart/add
Body: {"userId": 1, "productId": 5, "quantity": 1}
Response: OrderItem with ID
```

### Get Cart
```
GET http://10.0.2.2:8080/api/cart/{userId}
Response: List<OrderItem> with Product details
```

**Expected OrderItem structure:**
```json
{
  "id": 123,
  "orderId": 0,
  "productId": 5,
  "quantity": 1,
  "price": 999.99,
  "product": {
    "id": 5,
    "name": "iPhone 15 Pro Max",
    "description": "...",
    "price": 999.99,
    "quantity": 50,
    "image": null,
    "categoryId": 1
  }
}
```

## 🎯 Next Steps

1. **Run the app** and check Logcat
2. **Add a product** and watch the logs
3. **View cart** and compare with expected logs
4. **Identify the issue** based on log patterns
5. **Fix backend/frontend** accordingly

## 📝 Quick Test Script

```bash
# Clear app data
adb shell pm clear com.son.e_commerce

# Start app
adb shell am start -n com.son.e_commerce/.LoginActivity

# Watch logs
adb logcat | grep -E "(CartRepositoryImpl|CartFragment|CartAdapter|ProductDetailPresenter)"
```

---

## 🆘 Still Not Working?

If sau khi thực hiện tất cả các bước trên mà vẫn không thấy items trong cart:

1. **Check Backend Server**
   - Verify server is running on `localhost:8080`
   - Test API with Postman/curl
   - Check database has the cart items

2. **Check Network Configuration**
   - Emulator should use `10.0.2.2:8080`
   - Physical device should use your computer's IP
   - Check `network_security_config.xml`

3. **Check User Session**
   - Verify user is logged in
   - Check SharedPreferences for user data
   - Confirm userId is correct

4. **Debug Backend**
   - Add logs in backend API methods
   - Check database queries
   - Verify data is actually saved

Chúc may mắn! 🍀
