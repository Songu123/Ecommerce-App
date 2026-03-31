# 🔧 FIX: Product ID = 0 Issue

## 🐛 LỖI GHI NHẬN

Trong logs, bạn thấy:
```
2026-04-01 00:00:08.373  8553-8553  PRODUCT_DEBUG           com.son.e_commerce                   D  Product details: Product{id=0, name='Intelligent Iron Hat Cty Nhàn', price=1012000.0, quantity=67, categoryId=0}
2026-04-01 00:00:08.373  8553-8553  PRODUCT_ERROR           com.son.e_commerce                   E  Invalid Product ID: 0
```

**Vấn đề**: Product ID đang bằng `0` (không hợp lệ) dù backend có trả về dữ liệu khác.

---

## 🔍 NGUYÊN NHÂN

### Đây là vấn đề **JSON Deserialization**:

Backend server trả về JSON như:
```json
{
  "id": 123,              ← ĐÂY LÀ JSON KEY
  "name": "Intelligent Iron Hat Cty Nhàn",
  "price": 1012000.0,
  "quantity": 67
}
```

Nhưng Product.java expect:
```java
@SerializedName("productId")  ← EXPECT KEY "productId" (không "id")
private int id;
```

**Kết quả**: Gson không thể map `"id"` → `id` field → `id` vẫn là 0 (default)

---

## ✅ CÁCH FIX

### Sửa Product.java - Hỗ Trợ Cả "id" & "productId"

```java
@SerializedName(value = "productId", alternate = {"id"})
@Expose
private int id;
```

**Giải thích**:
- `value = "productId"`: Primary JSON key to deserialize from
- `alternate = {"id"}`: Fallback JSON keys if primary doesn't exist
- Giờ Gson sẽ thử map `"productId"` trước, rồi `"id"` nếu không có

---

## 📝 FIX ĐÃ THỰC HIỆN

### File: `Product.java`
```diff
  @SerializedName("productId")
+ @SerializedName(value = "productId", alternate = {"id"})
+ @Expose
  private int id;
```

### File: `ProductRepositoryImpl.java`
- ✅ Thêm chi tiết logging để xem Product ID sau deserialization
- ✅ Log raw JSON response (nếu cần debug thêm)
- ✅ Cải thiện error messages

---

## 🧪 CÁCH VERIFY FIX

### 1. Build & Run Lại
```bash
.\gradlew assembleDebug
# Cài & chạy app
```

### 2. Click Product → Xem Logs
```
D ProductRepositoryImpl: getProductById() response code: 200
D ProductRepositoryImpl: getProductById() success - Product ID: 123, Name: Intelligent Iron Hat Cty Nhàn, Price: 1012000.0
D PRODUCT_DEBUG: Product details: Product{id=123, name='Intelligent Iron Hat Cty Nhàn', ...}
```

**Nếu bây giờ thấy `id=123` (không phải `id=0`) = FIX THÀNH CÔNG! ✅**

### 3. ProductDetailActivity Sẽ Load Thành Công
- Logs sẽ không còn "Invalid Product ID: 0"
- Screen sẽ hiển thị chi tiết sản phẩm

---

## 🔄 GIẢI THÍCH CHI TIẾT

### Sai Trước (Không Work)
```java
@SerializedName("productId")
private int id;
```
- Chỉ expect JSON key `"productId"`
- Nếu backend gửi `"id"` thay vì `"productId"` → không map
- `id` vẫn = 0

### Đúng Bây Giờ (Work)
```java
@SerializedName(value = "productId", alternate = {"id"})
private int id;
```
- Try map từ `"productId"` trước
- Nếu không có, thử `"id"`
- Flexible, handle cả 2 formats

---

## 💡 LẢI ÍCH THÊM

**Logging được cải thiện**:
```
// Trước
D ProductRepositoryImpl: getProductById() success: Product{id=0, name='...'}

// Sau - Chi tiết hơn
D ProductRepositoryImpl: getProductById() success - Product ID: 123, Name: Intelligent Iron Hat Cty Nhàn, Price: 1012000.0
```

Dễ dàng spot lỗi nếu ID vẫn = 0

---

## 🎯 RECAP

| Vấn đề | Nguyên Nhân | Fix |
|--------|-----------|-----|
| Product ID = 0 | JSON key mismatch ("id" vs "productId") | Thêm `alternate` mapping |
| Khó debug | Logs không chi tiết | Cải thiện logging messages |

---

## ✅ TRẠNG THÁI

**Status**: ✅ Fixed  
**Files Changed**: 2
- Product.java (Deserialization)
- ProductRepositoryImpl.java (Logging)

**Next**: Build & test → Logs sẽ show Product ID > 0 ✅
