# 🔍 DEBUG: Không Load Product

## Các Bước Debug

### Bước 1: Xem Log Trong Android Studio
Mở Logcat và filter theo:
```
ProductRepositoryImpl
```

### Bước 2: Kiểm Tra Backend
```bash
# Test endpoint products
curl http://10.0.3.238:8080/api/products
```

### Bước 3: Kiểm Tra Network
```
Logcat filter: okhttp.OkHttpClient
```

## Checklist
- [ ] Backend đang chạy ở port 8080
- [ ] IP trong ApiConfig đúng: 10.0.3.238
- [ ] Network security config cho phép HTTP
- [ ] Product có @SerializedName annotations

## Log Cần Thấy
```
ProductRepositoryImpl: getAllProducts() called
ProductRepositoryImpl: getAllProducts() response code: 200
ProductRepositoryImpl: getAllProducts() success. Count: X
```

## Nếu Thấy Lỗi
- response code: 404 → Endpoint sai
- response code: 500 → Backend error
- Network error → Connection timeout
