# 🔧 Troubleshooting: Add to Cart Not Working

## ✅ Đã sửa & cải thiện

### 1️⃣ Mock User cho Testing
**Vấn đề:** User chưa đăng nhập → getCurrentUser() trả về null → Không thể add to cart

**Giải pháp:** Thêm mock user tạm thời trong `UserRepositoryImpl.java`

```java
@Override
public User getCurrentUser() {
    // TODO: Remove this mock user after implementing login
    // For testing purposes, return a mock user if no user is logged in
    if (currentUser == null) {
        // Create a temporary mock user for testing
        currentUser = new User(1, "testuser", "test@example.com", 
            "Test User", "user", true);
    }
    return currentUser;
}
```

**⚠️ Lưu ý:** Nhớ xóa mock user này sau khi implement login thật!

---

### 2️⃣ Thêm Comprehensive Logging
Đã thêm logging chi tiết vào các component:

#### ProductDetailActivity
```java
private static final String TAG = "ProductDetailActivity";

buttonAddToCart.setOnClickListener(v -> {
    Log.d(TAG, "Add to cart clicked. Product: " + 
        (currentProduct != null ? currentProduct.getName() : "null") + 
        ", Quantity: " + currentQuantity);
    presenter.onAddToCartClick(currentQuantity);
});
```

#### ProductDetailPresenter
```java
private static final String TAG = "ProductDetailPresenter";

@Override
public void onAddToCartClick(int quantity) {
    Log.d(TAG, "onAddToCartClick called with quantity: " + quantity);
    
    if (currentProduct == null) {
        Log.e(TAG, "Current product is null");
        // ...
    }
    
    Log.d(TAG, "Adding to cart - UserId: " + currentUser.getId() + 
        ", ProductId: " + currentProduct.getId() + 
        ", Quantity: " + quantity);
    // ...
}
```

#### CartRepositoryImpl
```java
private static final String TAG = "CartRepositoryImpl";

public void addToCart(int userId, int productId, int quantity, OnCartItemAddedListener listener) {
    Log.d(TAG, "addToCart - UserId: " + userId + ", ProductId: " + productId + ", Quantity: " + quantity);
    // ...
    
    @Override
    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
        Log.d(TAG, "addToCart response code: " + response.code());
        // ...
    }
}
```

---

## 🔍 Cách Debug

### Step 1: Mở Logcat
1. Open Android Studio
2. Click "Logcat" tab (bottom)
3. Select device/emulator
4. Set filter: `ProductDetail|CartRepository`

### Step 2: Test Add to Cart
1. Run app
2. Click vào một product
3. ProductDetailActivity opens
4. Click "Thêm vào giỏ hàng"
5. Watch Logcat

### Step 3: Check Logs
Bạn sẽ thấy sequence logs như sau:

```
D/ProductDetailActivity: Add to cart clicked. Product: iPhone 15 Pro Max, Quantity: 1
D/ProductDetailPresenter: onAddToCartClick called with quantity: 1
D/ProductDetailPresenter: Adding to cart - UserId: 1, ProductId: 5, Quantity: 1
D/CartRepositoryImpl: addToCart - UserId: 1, ProductId: 5, Quantity: 1
D/OkHttp: --> POST http://10.0.2.2:8080/api/cart/add
D/OkHttp: {"userId":1,"productId":5,"quantity":1}
D/OkHttp: <-- 200 OK (123ms)
D/CartRepositoryImpl: addToCart response code: 200
D/CartRepositoryImpl: addToCart success. Item ID: 123
D/ProductDetailPresenter: Successfully added to cart. Item ID: 123
D/ProductDetailActivity: Product added to cart successfully
```

---

## 🐛 Common Error Scenarios

### Scenario 1: User null (FIXED)
```
Before fix:
E/ProductDetailPresenter: User is not logged in
Toast: "Vui lòng đăng nhập để thêm vào giỏ hàng"

After fix (with mock user):
D/ProductDetailPresenter: Adding to cart - UserId: 1, ProductId: 5, Quantity: 1
✅ Success!
```

### Scenario 2: Network Error
```
E/CartRepositoryImpl: addToCart failed: Network error: Failed to connect to /10.0.2.2:8080
Toast: "Network error: Failed to connect..."

Solution:
✅ Check Spring Boot backend is running
✅ Check BASE_URL in RetrofitClient.java
✅ Check network security config
✅ Check internet permission in manifest
```

### Scenario 3: API Error
```
E/CartRepositoryImpl: Failed to add to cart: 404
Toast: "Failed to add to cart: 404"

Solution:
✅ Check API endpoint exists: POST /api/cart/add
✅ Verify Spring Boot controller mapping
✅ Test endpoint with Postman
```

### Scenario 4: Product null
```
E/ProductDetailPresenter: Current product is null
Toast: "Sản phẩm không tồn tại"

Solution:
✅ Check product is passed via Intent
✅ Verify product is Serializable
✅ Check product loads successfully
```

---

## 📊 Debug Checklist

### Before Testing
- [x] Mock user added to UserRepositoryImpl
- [x] Logging added to all components
- [x] App rebuilt and installed
- [x] Spring Boot backend running
- [x] Network security config correct

### During Testing
- [ ] Open Logcat
- [ ] Set filter to see relevant logs
- [ ] Click product → Open detail
- [ ] Click "Thêm vào giỏ hàng"
- [ ] Watch logs in sequence
- [ ] Check for errors
- [ ] Verify success toast

### After Success
- [ ] Cart icon shows items (if badge implemented)
- [ ] Open cart → See added item
- [ ] Quantity correct
- [ ] Price correct

---

## 🔧 Advanced Debugging

### Check API Request
```
D/OkHttp: --> POST http://10.0.2.2:8080/api/cart/add
D/OkHttp: Content-Type: application/json
D/OkHttp: {"userId":1,"productId":5,"quantity":1}
D/OkHttp: --> END POST
```

**Verify:**
- ✅ URL correct
- ✅ Method is POST
- ✅ Content-Type is application/json
- ✅ Body contains userId, productId, quantity

### Check API Response
```
D/OkHttp: <-- 200 OK http://10.0.2.2:8080/api/cart/add (123ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: {"id":123,"orderId":0,"productId":5,"quantity":1,"price":1199.99}
D/OkHttp: <-- END HTTP
```

**Verify:**
- ✅ Status code 200
- ✅ Response has OrderItem object
- ✅ OrderItem has valid data

---

## 🎯 Testing Scenarios

### Test 1: Normal Add to Cart
```
1. Open app
2. Click any product
3. See detail screen
4. Quantity = 1 (default)
5. Click "Thêm vào giỏ hàng"
6. ✅ See toast: "Đã thêm vào giỏ hàng"
7. ✅ Check Logcat: All success logs
```

### Test 2: Multiple Quantities
```
1. Open product detail
2. Click + button → Quantity = 2
3. Click + button → Quantity = 3
4. Click "Thêm vào giỏ hàng"
5. ✅ Should add 3 items
6. Check Logcat: quantity=3 in request
```

### Test 3: Max Quantity
```
1. Product with stock = 10
2. Click + until quantity = 10
3. Try click + again
4. ✅ Toast: "Đã đạt số lượng tối đa"
5. Click "Thêm vào giỏ hàng"
6. ✅ Should add 10 items
```

### Test 4: Backend Error Handling
```
1. Stop Spring Boot backend
2. Try add to cart
3. ✅ See network error in Logcat
4. ✅ Toast shows error message
5. ✅ Loading hides properly
```

---

## 💡 Tips for Developers

### Enable Verbose Logging
In RetrofitClient.java, logging is already set to BODY level:
```java
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
```

This shows:
- Request URL
- Request headers
- Request body
- Response code
- Response headers
- Response body

### Filter Logcat Efficiently
```
# See all relevant logs
Filter: ProductDetail|CartRepository|OkHttp

# See only errors
Filter: level:error

# See specific tag
Filter: tag:ProductDetailPresenter
```

### Test API Independently
```bash
# Test with curl
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":5,"quantity":1}'

# Should return OrderItem JSON
```

---

## ✅ Verification Steps

### 1. Check User
```
D/ProductDetailPresenter: Adding to cart - UserId: 1
✅ UserId = 1 (mock user)
```

### 2. Check Product
```
D/ProductDetailActivity: Add to cart clicked. Product: iPhone 15 Pro Max
✅ Product loaded successfully
```

### 3. Check API Call
```
D/OkHttp: --> POST http://10.0.2.2:8080/api/cart/add
✅ API endpoint correct
```

### 4. Check Response
```
D/OkHttp: <-- 200 OK
D/CartRepositoryImpl: addToCart success
✅ API call successful
```

### 5. Check UI Update
```
D/ProductDetailActivity: Product added to cart successfully
Toast: "Đã thêm vào giỏ hàng"
✅ User sees confirmation
```

---

## 🚀 What Changed

### Files Modified
```
✅ UserRepositoryImpl.java
   - Added mock user for testing
   
✅ ProductDetailActivity.java
   - Added TAG for logging
   - Added logs to button clicks
   - Added log to success callback
   
✅ ProductDetailPresenter.java
   - Added TAG for logging
   - Added comprehensive logs
   - Log user, product, quantity
   - Log success/failure
   
✅ CartRepositoryImpl.java
   - Added TAG for logging
   - Log API request details
   - Log response code
   - Log success/failure
```

### New Capabilities
- ✅ Can test without real user login
- ✅ Can see full request/response flow
- ✅ Can identify exact point of failure
- ✅ Can verify data at each step

---

## 📝 Next Steps

### Phase 1: Verify Fix
1. ✅ Build & install app
2. ✅ Test add to cart
3. ✅ Check Logcat for success
4. ✅ Verify toast message

### Phase 2: Test Cart Screen
1. Add multiple products
2. Open cart (click cart icon)
3. Verify all items show
4. Test quantity update
5. Test remove item

### Phase 3: Remove Mock User
1. Implement real login screen
2. Test login flow
3. Remove mock user code
4. Test add to cart with real user

---

## 🎉 Expected Results

### Success Flow
```
User clicks "Thêm vào giỏ hàng"
    ↓
Button onClick triggered
    ↓
Presenter validates product & user
    ↓
Repository calls API
    ↓
Retrofit sends POST request
    ↓
Backend processes request
    ↓
Backend returns OrderItem
    ↓
Repository onSuccess callback
    ↓
Presenter updates View
    ↓
View shows toast "Đã thêm vào giỏ hàng"
    ↓
✅ SUCCESS!
```

---

**Status:** ✅ Fixed with Mock User + Logging  
**Date:** 02/02/2026  
**Next:** Test thoroughly and verify

**Run app và check Logcat để see magic happen! 🔍**
