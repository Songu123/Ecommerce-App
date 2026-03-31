# 🔍 HƯỚNG DẪN DEBUG: XEM CHI TIẾT SẢN PHẨM

## ✅ CÁC THAY ĐỔI ĐÃ THỰC HIỆN

### 1. **ProductDetailActivity.java** - Thêm Logging Chi Tiết
- ✅ Thêm log hiển thị API configuration (base URL, mode) khi activity khởi động
- ✅ Thêm log chi tiết trong `showProduct()` method
- ✅ Thêm log khi gọi `showError()`
- ✅ Thêm log khi `showLoading()` / `hideLoading()`

### 2. **ProductDetailPresenter.java** - Cải Thiện Error Handling
- ✅ Thêm kiểm tra `productId <= 0` với error message rõ ràng
- ✅ Thêm kiểm tra null cho `view` object
- ✅ Thêm kiểm tra null cho `product` object trong onSuccess
- ✅ Cải thiện error messages

### 3. **ProductRepositoryImpl.java** - Cải Thiện API Error Messages
- ✅ Thêm HTTP response code vào error message
- ✅ Thêm kiểm tra `response.body() == null`
- ✅ Cải thiện exception handling

---

## 🧪 CÁCH KIỂM TRA TRÊN THIẾT BỊ/EMULATOR

### Bước 1: Build & Run
```bash
# Từ PowerShell trong project root
.\gradlew assembleDebug
# Hoặc từ Android Studio: Build → Build Bundle(s) / APK(s) → Build APK(s)
```

### Bước 2: Cài & Chạy App
- Cài APK lên emulator hoặc thiết bị
- Mở app

### Bước 3: Xem Logs Chi Tiết
Mở Android Studio Logcat hoặc dùng adb:

```bash
# Filter logs bởi ProductDetailActivity
adb logcat | Select-String "ProductDetailActivity" -Context 2

# Hoặc trong Android Studio
# Logcat → Filter by Tag: "ProductDetailActivity"
```

---

## 📋 KHI BẠN VÀO SCREEN XEM CHI TIẾT SẢN PHẨM

### Bạn Sẽ Thấy Logs Như Sau:

```
D ProductDetailActivity: === ProductDetailActivity Started ===
D ProductDetailActivity: Mode: Auto-detected Emulator (10.0.2.2)
D ProductDetailActivity: URL: http://10.0.2.2:8080/
D ProductDetailActivity: Status: Development
D ProductDetailActivity: ===================================
D ProductDetailActivity: Received productId = 5
D ProductDetailActivity: showLoading() called
D ProductDetailPresenter: loadProduct() called with id: 5
D ProductRepositoryImpl: getProductById() called with id: 5
D ProductRepositoryImpl: getProductById() response code: 200
D ProductRepositoryImpl: getProductById() success: Product{id=5, name='...' ...}
D ProductDetailActivity: showProduct() called with product: Product{...}
D ProductDetailActivity: Setting product name: iPhone 15 Pro
D ProductDetailActivity: Setting product price: 299.999 VND
D ProductDetailActivity: Setting product description: ...
D ProductDetailActivity: Product is in stock: Còn hàng: 50
D ProductDetailActivity: Loading product image: http://...
D ProductDetailActivity: showProduct() completed successfully
D ProductDetailActivity: hideLoading() called
```

---

## ❌ NẾU CÓ LỖI - CÁC MESSAGES KHÁC

### Trường Hợp 1: Product ID Không Hợp Lệ
```
D ProductDetailActivity: Received productId = -1
(Toast: "Sản phẩm không hợp lệ")
```
**Nguyên nhân**: Product ID truyền vào <= 0  
**Cách fix**: Kiểm tra code onClick của product list adapter

### Trường Hợp 2: Network Error - Timeout
```
D ProductDetailPresenter: loadProduct() called with id: 5
E ProductRepositoryImpl: getProductById() failed: Network error: timeout
E ProductDetailPresenter: loadProduct() onError: Network error: timeout
D ProductDetailActivity: showError() called: Lỗi tải sản phẩm: Network error: timeout
```
**Nguyên nhân**: Server không reachable (kiểm tra IP, port, firewall)  
**Cách fix**: 
- Chắc backend (Spring Boot) đang chạy
- Kiểm tra IP trong ApiConfig.YOUR_PC_IP có đúng không
- Test ping server: `ping 172.20.10.8` (hoặc IP của bạn)

### Trường Hợp 3: 404 - Product Not Found
```
D ProductRepositoryImpl: getProductById() response code: 404
E ProductRepositoryImpl: Product not found (HTTP 404)
```
**Nguyên nhân**: Product ID không tồn tại trên server  
**Cách fix**: Kiểm tra endpoint `/api/products/{id}` có trả về product không

### Trường Hợp 4: 403/401 - Unauthorized
```
D ProductRepositoryImpl: getProductById() response code: 403
E ProductRepositoryImpl: Product not found (HTTP 403)
```
**Nguyên nhân**: Server yêu cầu authentication hoặc authorization  
**Cách fix**: Kiểm tra backend có middleware chặn public endpoints không

### Trường Hợp 5: Response Body Null
```
D ProductRepositoryImpl: getProductById() response code: 200
E ProductRepositoryImpl: Product not found (HTTP 200) - Response body is null
```
**Nguyên nhân**: Server trả về 200 nhưng body không chứa product object  
**Cách fix**: Kiểm tra JSON response format trên server

---

## 🔧 NẾU VẪN CÓ PROBLEM - DEBUG STEPS

### 1. Kiểm tra API Endpoint Trực Tiếp
```bash
# Test API từ Postman hoặc PowerShell
$headers = @{
    "Content-Type" = "application/json"
}

# Test product detail endpoint
Invoke-WebRequest -Uri "http://172.20.10.8:8080/api/products/5" `
                  -Headers $headers -Method GET
```

### 2. Kiểm tra JSON Response Format
Backend phải trả về:
```json
{
  "productId": 5,
  "name": "iPhone 15 Pro",
  "description": "Apple's latest flagship",
  "price": 299999,
  "quantity": 50,
  "image": "https://...",
  "categoryId": 2
}
```

### 3. Xem Full Response Log
Thêm HttpLoggingInterceptor level BODY trong RetrofitClient.java (nếu chưa có)

### 4. Kiểm tra JSON Deserialization
Đảm bảo Product.java có các @SerializedName annotations đúng

---

## 📱 TESTING CHECKLIST

- [ ] Build app thành công (no compile errors)
- [ ] App chạy được
- [ ] Có thể mở screen product list
- [ ] Click vào sản phẩm → mở ProductDetailActivity
- [ ] Xem logs ProductDetailActivity show base URL & mode đúng
- [ ] Xem logs showProduct() được gọi (không showError)
- [ ] Sản phẩm hiển thị: tên, giá (VND), mô tả, stock
- [ ] Có thể tăng/giảm số lượng
- [ ] Có thể add to cart

---

## 💡 TIPS

- **Logs mục tiêu**: Kiếm `ProductDetailActivity` hoặc `ProductDetailPresenter` trong Logcat
- **Nếu muốn xem full request/response body**: Search `OkHttp` logs (yêu cầu enable logging interceptor)
- **Offline test**: Tắt network → xem "Network error" message
- **Test invalid product ID**: Thay đổi intent extra EXTRA_PRODUCT_ID thành -1 → xem "Invalid Product ID" message

---

## ✅ TRẠNG THÁI

**Hoàn thành**: Thêm comprehensive logging vào ProductDetailActivity, Presenter, Repository  
**Compile Status**: ✅ No critical errors (chỉ warnings)  
**Testing**: Ready to deploy & run on device/emulator
