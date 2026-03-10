# 🔍 Cart Troubleshooting Checklist - Giải quyết vấn đề Giỏ hàng

## ⚡ Quick Diagnosis (5 phút)

### Test 1: Backend có chạy không?
```bash
# Test trong browser hoặc terminal:
curl http://localhost:8080/api/products

# Expected: JSON array of products
# If failed: Start backend server
```

### Test 2: API có hoạt động không?
```bash
# Test add to cart
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 5, "quantity": 1}'

# Expected: OrderItem JSON
# If 404: Check endpoint exists in backend
# If 500: Check backend logs
```

### Test 3: Cart có items không?
```bash
# Test get cart
curl http://localhost:8080/api/cart/1

# Expected: Array of OrderItem with Product details
# If empty []: Items not saved or wrong userId
# If Product null: Backend not populating Product
```

### Test 4: App có connect được backend không?
1. Mở app
2. Login
3. Check Logcat:
   ```bash
   adb logcat | grep "OkHttp"
   ```
4. Should see:
   ```
   --> POST http://10.0.2.2:8080/api/auth/login
   <-- 200 OK
   ```

## 🎯 Common Issues & Instant Fixes

### ❌ Issue 1: "Network error: Failed to connect"

**Cause:** Backend không chạy hoặc URL sai

**Fix:**
1. Start backend:
   ```bash
   cd your-spring-boot-project
   ./mvnw spring-boot:run
   ```

2. Verify URL trong `RetrofitClient.java`:
   ```java
   // Cho emulator
   private static final String BASE_URL = "http://10.0.2.2:8080/";
   
   // Cho device (thay bằng IP máy của bạn)
   // private static final String BASE_URL = "http://192.168.1.XXX:8080/";
   ```

3. Get your IP:
   ```bash
   # Windows
   ipconfig
   # Look for IPv4 Address
   
   # Mac/Linux
   ifconfig | grep "inet "
   ```

---

### ❌ Issue 2: Cart shows "Product #123" instead of product name

**Cause:** Backend không trả về Product object trong OrderItem

**Symptoms trong Logcat:**
```
D/CartAdapter: Product object is null, using fallback name
```

**Fix Backend:**
```java
// Trong CartController hoặc CartService
@GetMapping("/api/cart/{userId}")
public List<OrderItem> getCart(@PathVariable int userId) {
    List<OrderItem> items = cartService.getCart(userId);
    
    // IMPORTANT: Populate Product for each item
    for (OrderItem item : items) {
        Product product = productService.findById(item.getProductId());
        item.setProduct(product);  // ← Add this!
    }
    
    return items;
}
```

**Or use JPA relationships:**
```java
// OrderItem entity
@ManyToOne(fetch = FetchType.EAGER)  // ← Add this
@JoinColumn(name = "product_id")
private Product product;
```

---

### ❌ Issue 3: Cart is empty after adding items

**Cause:** UserId mismatch hoặc items không được lưu

**Check Logcat:**
```bash
adb logcat | grep -E "(addToCart|getCart)"
```

**Expected:**
```
D/CartRepositoryImpl: addToCart - UserId: 1, ProductId: 5
D/CartRepositoryImpl: addToCart success. Item ID: 123
D/CartRepositoryImpl: getCart - UserId: 1
D/CartRepositoryImpl: getCart success. Items count: 1
```

**If addToCart fails:**
- Check backend logs
- Verify productId exists
- Check userId is valid

**If getCart returns 0 items:**
1. Check database:
   ```sql
   SELECT * FROM order_items WHERE user_id = 1;
   ```
2. Verify same userId in both calls
3. Check if items have orderId = 0 (for cart items)

---

### ❌ Issue 4: "Vui lòng đăng nhập" toast

**Cause:** User session lost

**Fix:**
1. Login again
2. Check SharedPreferences:
   ```bash
   adb shell
   run-as com.son.e_commerce
   cat shared_prefs/UserPrefs.xml
   ```
3. Should contain user_id
4. If empty: Login persistence issue

---

### ❌ Issue 5: Items appear then disappear

**Cause:** Cart loading before add completes

**Symptoms:**
- Success toast shows
- Cart refreshes too fast
- Items not yet in backend

**Fix:** Already handled by `onResume()`, but verify:
```java
// ProductDetailPresenter
@Override
public void onSuccess(OrderItem item) {
    view.hideLoading();
    view.showAddedToCart();
    // Don't navigate to cart immediately
    // Let user navigate when ready
}
```

---

### ❌ Issue 6: App crashes when viewing cart

**Check Logcat for:**
```
java.lang.NullPointerException
```

**Common causes:**
1. `item.getProduct()` is null → See Issue 2
2. `cartItems` list is null → Already fixed with null check
3. View not initialized → Check lifecycle

**Already handled:**
```java
public void setCartItems(List<OrderItem> items) {
    this.cartItems = items != null ? items : new ArrayList<>();
    notifyDataSetChanged();
}
```

---

## 🔧 Advanced Debugging

### Enable verbose logging
```bash
# Clear logcat first
adb logcat -c

# Watch all cart-related logs
adb logcat | grep -E "(Cart|OrderItem|Product)"

# Or save to file
adb logcat | grep -E "(Cart|OrderItem|Product)" > cart_logs.txt
```

### Check database directly
```sql
-- View all cart items
SELECT oi.*, p.name 
FROM order_items oi 
LEFT JOIN products p ON oi.product_id = p.id 
WHERE oi.order_id = 0;  -- Cart items have orderId = 0

-- View by user
SELECT * FROM order_items WHERE user_id = 1 AND order_id = 0;
```

### Test API with Postman

**1. Add to Cart:**
```
POST http://localhost:8080/api/cart/add
Content-Type: application/json

{
  "userId": 1,
  "productId": 5,
  "quantity": 1
}
```

**2. Get Cart:**
```
GET http://localhost:8080/api/cart/1
```

**Expected Response:**
```json
[
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
]
```

---

## 📊 Diagnostic Command Sequence

Run these commands in order to diagnose:

```bash
# 1. Check backend
curl http://localhost:8080/api/products
# ✅ If OK: Backend is running
# ❌ If failed: Start backend

# 2. Test cart API
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 5, "quantity": 1}'
# ✅ If OK: API works
# ❌ If failed: Check backend logs

# 3. Get cart
curl http://localhost:8080/api/cart/1
# ✅ If has items: Backend OK, check Android app
# ❌ If empty: Issue with add or database

# 4. Check Android logs
adb logcat -c
adb logcat | grep -E "(CartRepositoryImpl|CartFragment|CartAdapter)"
# Look for errors or unexpected values

# 5. Check app data
adb shell run-as com.son.e_commerce
cat shared_prefs/UserPrefs.xml
# Verify user is logged in
```

---

## ✅ When Everything Works

**Log sequence you should see:**

### 1. Adding to Cart:
```
D/ProductDetailActivity: Add to cart clicked. Product: iPhone 15 Pro Max, Quantity: 1
D/ProductDetailPresenter: onAddToCartClick called with quantity: 1
D/ProductDetailPresenter: Adding to cart - UserId: 1, ProductId: 5, Quantity: 1
D/CartRepositoryImpl: addToCart - UserId: 1, ProductId: 5, Quantity: 1
D/OkHttp: --> POST http://10.0.2.2:8080/api/cart/add
D/OkHttp: {"userId":1,"productId":5,"quantity":1}
D/OkHttp: <-- 200 OK (45ms)
D/CartRepositoryImpl: addToCart response code: 200
D/CartRepositoryImpl: addToCart success. Item ID: 123
D/ProductDetailPresenter: Successfully added to cart. Item ID: 123
Toast: "Đã thêm vào giỏ hàng"
```

### 2. Viewing Cart:
```
D/CartFragment: loadCart() called
D/CartFragment: Loading cart for user ID: 1
D/CartRepositoryImpl: getCart - UserId: 1
D/OkHttp: --> GET http://10.0.2.2:8080/api/cart/1
D/OkHttp: <-- 200 OK (32ms)
D/CartRepositoryImpl: getCart response code: 200
D/CartRepositoryImpl: getCart success. Items count: 1
D/CartRepositoryImpl: Cart Item - ID: 123, ProductID: 5, Quantity: 1, Price: 999.99, Has Product: true
D/CartFragment: Cart loaded successfully. Items count: 1
D/CartFragment: Showing cart with 1 items
D/CartFragment: showCart() - Setting visibility and updating adapter
D/CartAdapter: setCartItems() called with 1 items
D/CartAdapter: Adapter updated. Item count: 1
D/CartAdapter: Binding item - ID: 123, ProductID: 5, Quantity: 1, Price: 999.99, Has Product: true
D/CartAdapter: Product name: iPhone 15 Pro Max
D/CartFragment: Cart displayed with 1 items
```

---

## 🎯 Still Not Working?

1. **Share your logs:**
   ```bash
   adb logcat > full_logs.txt
   # Send this file for analysis
   ```

2. **Test minimal case:**
   - Fresh install
   - Login
   - Add ONE product
   - View cart
   - Share logs

3. **Check backend thoroughly:**
   - Look at backend console
   - Check database with SQL
   - Test API with Postman
   - Compare API response with expected

4. **Verify environment:**
   - Emulator: Use `10.0.2.2:8080`
   - Device: Use correct IP
   - Same network for device and PC
   - Firewall not blocking

---

## 📞 Getting Help

When asking for help, provide:

1. **Logcat output:**
   ```bash
   adb logcat | grep -E "(Cart|OrderItem|Product)" > logs.txt
   ```

2. **API test results:**
   - Postman screenshots
   - curl outputs

3. **Backend response:**
   - What `/api/cart/1` returns
   - Backend console logs

4. **Environment:**
   - Emulator or device?
   - BASE_URL setting
   - Backend running status

---

**✨ Với checklist này, 99% các vấn đề sẽ được giải quyết!**
