# Debugging Guide: Product Detail & Cart Issues

## Quick Debug Commands

### 1. Check Logcat for Product Click
```bash
adb logcat | findstr "PRODUCT_DEBUG PRODUCT_ERROR"
```

**Expected output when clicking a product:**
```
PRODUCT_DEBUG: onProductClick called
PRODUCT_DEBUG: Product details: Product{id=5, name='Gaming Laptop', price=299.99, quantity=10, categoryId=2}
PRODUCT_DEBUG: Navigating to product detail with ID: 5
PRODUCT_DEBUG: Receive productId = 5
```

**If you see "Invalid Product ID":**
```
PRODUCT_ERROR: Invalid Product ID: 0
```
→ The product ID is not being set correctly from API. Check your backend response.

---

### 2. Check Logcat for Add to Cart
```bash
adb logcat | findstr "ProductDetailPresenter CartRepositoryImpl"
```

**Expected output when adding to cart:**
```
ProductDetailPresenter: onAddToCartClick called with quantity: 2
ProductDetailPresenter: Adding to cart - UserId: 1, ProductId: 5, Quantity: 2
CartRepositoryImpl: addToCart - UserId: 1, ProductId: 5, Quantity: 2
CartRepositoryImpl: addToCart response code: 200
CartRepositoryImpl: addToCart success. Item ID: 1
ProductDetailPresenter: Successfully added to cart. Item ID: 1
```

---

### 3. Check Logcat for Cart Display
```bash
adb logcat | findstr "CartFragment CartAdapter"
```

**Expected output when viewing cart:**
```
CartFragment: loadCart() called
CartFragment: Loading cart for user ID: 1
CartFragment: Cart loaded successfully. Items count: 3
CartFragment: Showing cart with 3 items
CartAdapter: setCartItems() called with 3 items
CartAdapter: Adapter updated. Item count: 3
CartAdapter: Binding item - ID: 1, ProductID: 5, Quantity: 2, Price: 299.99, Has Product: true
CartAdapter: Product name: Gaming Laptop
```

**If cart appears empty but items were added:**
```
CartFragment: Cart loaded successfully. Items count: 0
CartFragment: Cart is empty, showing empty state
```
→ Check if backend is returning items for the correct user ID.

**If product names show as "Product #123":**
```
CartAdapter: Product object is null, using fallback name
```
→ Backend is not returning the `product` object. See Backend Fix section below.

---

## Common Issues & Solutions

### Issue 1: "Invalid Product ID" Error

**Symptom:** Toast message "Invalid Product ID" appears when clicking product.

**Possible Causes:**
1. Product ID from API is 0 or negative
2. Product object is null
3. Backend not setting `productId` field

**Debug Steps:**
```bash
# Check what product data is received
adb logcat | findstr "PRODUCT_DEBUG"
```

**Fix:**
- Verify backend API returns valid `productId` > 0
- Check JSON field name matches `@SerializedName("productId")` in Product.java
- Ensure API endpoint is `/api/products` or `/api/products/recommended`

---

### Issue 2: Products Not Showing in Cart

**Symptom:** Cart shows empty after adding items, or shows "Product #123" instead of names.

**Possible Causes:**
1. Backend not returning `product` object with cart items
2. Wrong user ID being used
3. JSON deserialization issue

**Debug Steps:**
```bash
# Check cart API response
adb logcat | findstr "CartFragment CartAdapter"
```

**Fix Option 1: Backend Returns Product Object (RECOMMENDED)**
Update your Spring Boot backend to include product in cart response:

```java
@GetMapping("/api/cart/{userId}")
public List<OrderItem> getCart(@PathVariable int userId) {
    List<OrderItem> items = cartService.getCartItems(userId);
    
    // Populate product object for each item
    for (OrderItem item : items) {
        Product product = productService.getProductById(item.getProductId());
        item.setProduct(product);
    }
    
    return items;
}
```

**Fix Option 2: Fetch Products in Android App**
Modify `CartRepositoryImpl.getCart()` to fetch products after getting cart:

```java
public void getCart(int userId, OnCartLoadedListener listener) {
    apiService.getCart(userId).enqueue(new Callback<List<OrderItem>>() {
        @Override
        public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
            if (response.isSuccessful() && response.body() != null) {
                List<OrderItem> items = response.body();
                
                // Fetch product details for each item
                fetchProductsForCartItems(items, listener);
            } else {
                listener.onError("Failed to load cart: " + response.code());
            }
        }
        
        // ... rest of implementation
    });
}

private void fetchProductsForCartItems(List<OrderItem> items, OnCartLoadedListener listener) {
    // TODO: Fetch products and attach to items
    // For now, use Option 1 (backend solution) instead
    listener.onSuccess(items);
}
```

---

### Issue 3: Network Timeout / Connection Failed

**Symptom:**
```
SocketTimeoutException: failed to connect to /10.0.2.2 (port 8080)
```

**Solutions:**

**For Emulator:**
```java
// In ApiClient.java, BASE_URL should be:
private static final String BASE_URL = "http://10.0.2.2:8080/";
```

**For Real Device:**
1. Get computer's IP address:
```powershell
ipconfig
# Look for IPv4 Address: 192.168.x.x
```

2. Update BASE_URL:
```java
private static final String BASE_URL = "http://192.168.1.100:8080/";
```

3. Add network security config (already done in your app).

---

### Issue 4: Images Not Loading

**Symptom:** Product images show placeholder icon.

**Check:**
1. Is image URL valid? 
```bash
adb logcat | findstr "Picasso"
```

2. Is network security config allowing HTTP?
```xml
<!-- res/xml/network_security_config.xml -->
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

3. Test image URL in browser first.

---

## Verification Checklist

### ✅ Product Detail Working
- [ ] Log shows: `PRODUCT_DEBUG: onProductClick called`
- [ ] Log shows: `PRODUCT_DEBUG: Product details: Product{id=X, ...}`
- [ ] Log shows: `PRODUCT_DEBUG: Receive productId = X` (in ProductDetailActivity)
- [ ] Product name, price, description appear
- [ ] Product image loads (or shows placeholder)

### ✅ Add to Cart Working
- [ ] Log shows: `ProductDetailPresenter: onAddToCartClick called`
- [ ] Log shows: `CartRepositoryImpl: addToCart response code: 200`
- [ ] Log shows: `ProductDetailPresenter: Successfully added to cart`
- [ ] Toast message "Đã thêm vào giỏ hàng" appears

### ✅ Cart Display Working
- [ ] Log shows: `CartFragment: Cart loaded successfully. Items count: X`
- [ ] Log shows: `CartAdapter: Has Product: true` (NOT null!)
- [ ] Log shows: `CartAdapter: Product name: XXXXX` (actual name, not "Product #123")
- [ ] Cart shows product images
- [ ] Quantities and prices display correctly

---

## Test API Endpoints

### Test with Postman or curl:

**1. Get Products:**
```bash
curl http://localhost:8080/api/products
```

**2. Add to Cart:**
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 5,
    "quantity": 2
  }'
```

**3. Get Cart:**
```bash
curl http://localhost:8080/api/cart/1
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "orderId": 0,
    "productId": 5,
    "quantity": 2,
    "price": 299.99,
    "product": {
      "productId": 5,
      "name": "Gaming Laptop",
      "description": "High performance gaming laptop",
      "price": 299.99,
      "quantity": 10,
      "image": "http://example.com/images/laptop.jpg",
      "categoryId": 2
    }
  }
]
```

**⚠️ IMPORTANT:** If `"product": null`, your backend needs to be fixed!

---

## Backend Fix (Spring Boot)

If your backend doesn't return the product object, update your `OrderItem` entity:

```java
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "price")
    private Double price;
    
    // Add this relationship
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    // Getters and setters...
}
```

Or manually set it in your service:

```java
@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<OrderItem> getCartItems(int userId) {
        List<OrderItem> items = cartRepository.findByUserId(userId);
        
        // Populate product for each item
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                .orElse(null);
            item.setProduct(product);
        }
        
        return items;
    }
}
```

---

## Still Having Issues?

### 1. Check All Logs
```bash
adb logcat -v time | findstr "com.son.e_commerce"
```

### 2. Clear App Data
```bash
adb shell pm clear com.son.e_commerce
```

### 3. Reinstall App
```bash
.\gradlew installDebug
```

### 4. Verify Backend is Running
```bash
curl http://localhost:8080/api/products
# Should return products list
```

### 5. Check Database
```sql
-- Check if products have valid IDs
SELECT * FROM products;

-- Check if cart items exist
SELECT * FROM order_items WHERE order_id = 0;
```

---

## Success Indicators

**When everything works correctly, you should see:**

1. ✅ Products have valid IDs (> 0) when loaded
2. ✅ Clicking product navigates to detail page
3. ✅ Product details display with image
4. ✅ "Add to Cart" shows success message
5. ✅ Cart tab shows added products with names and images
6. ✅ No "Product #123" fallback names
7. ✅ No "Invalid Product ID" errors
8. ✅ No null pointer exceptions in logs

---

## Quick Reference

**Android Side Fixed:** ✅
- Product ID passing standardized
- Gson annotations added
- Logging enhanced

**Backend Side Required:** ⚠️
- Must return `product` object with cart items
- Product IDs must be > 0
- All API endpoints must work

**See:** `PRODUCT_DETAIL_CART_FIX_SUMMARY.md` for complete details.
