# 🔧 FIX ĐĂNG KÝ - CẬP NHẬT VÀ KIỂM TRA DATABASE

## ✅ THAY ĐỔI CHÍNH

### 1. **RegisterFragment.java** - Chuyển sang Real API
```diff
- authRepository = new MockAuthRepository(requireContext());  // MOCK MODE
+ authRepository = new AuthRepositoryImpl(requireContext());   // REAL API
```
**Vấn đề**: Fragment đang dùng MockAuthRepository (giả lập fake) thay vì API thật  
**Fix**: Chuyển sang AuthRepositoryImpl để call endpoint `/api/auth/register` thực

### 2. **RegisterFragment.java** - Thêm Chi Tiết Logging
```java
Log.d("RegisterFragment", "=== Attempting Register ===");
Log.d("RegisterFragment", "Email: " + email);
Log.d("RegisterFragment", "Full Name: " + fullName);
// ... chi tiết từng bước
```
**Lợi ích**: Dễ dàng track flow đăng ký & debug vấn đề

### 3. **AuthRepositoryImpl.java** - Cải Thiện Register Logging
```java
Log.d(TAG, "========== REGISTER START ==========");
Log.d(TAG, "Response Code: " + response.code());
Log.d(TAG, "✅ Register successful!");
// ... chi tiết response từ server
Log.d(TAG, "========== REGISTER END (SUCCESS) ==========");
```
**Lợi ích**: Thấy response từ backend, dễ spot lỗi

---

## 🧪 CÁCH KIỂM TRA

### Step 1: Build & Run
```bash
.\gradlew assembleDebug
# Run app on emulator/device
```

### Step 2: Mở Logcat & Kiểm Tra Logs
```bash
adb logcat | Select-String "RegisterFragment|AuthRepositoryImpl"
```

### Step 3: Đăng Ký Tài Khoản
- Nhấn "Đăng ký"
- Nhập: Email, mật khẩu, xác nhận, tên đầy đủ
- Nhấn "Đăng ký"

### Step 4: Xem Logs
**Trường Hợp 1: Success (Lưu vào Database Thành Công)** ✅
```
D RegisterFragment: === Attempting Register ===
D RegisterFragment: Email: test@example.com
D RegisterFragment: Full Name: Test User
D AuthRepositoryImpl: ========== REGISTER START ==========
D AuthRepositoryImpl: Response Code: 201
D AuthRepositoryImpl: Is Successful: true
D AuthRepositoryImpl: ✅ Register successful!
D AuthRepositoryImpl: Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
D AuthRepositoryImpl: User ID: 10
D AuthRepositoryImpl: Username: test@example.com
D RegisterFragment: ✅ Register successful!
D RegisterFragment: User ID: 10, Username: test@example.com
```
→ **Điều này có nghĩa**: Backend đã nhận request, lưu vào database, và trả về user data + token

**Trường Hợp 2: Email Đã Tồn Tại** ⚠️
```
D AuthRepositoryImpl: Response Code: 409
D AuthRepositoryImpl: Error: Email already exists
E RegisterFragment: ❌ Register failed: Email đã được đăng ký
```
→ **Fix**: Sử dụng email khác để đăng ký

**Trường Hợp 3: Server Error (500)** ❌
```
D AuthRepositoryImpl: Response Code: 500
E RegisterFragment: ❌ Register failed: Lỗi server
```
→ **Fix**: Kiểm tra server logs & kiểm tra database connection

**Trường Hợp 4: Connection Error** ❌
```
E AuthRepositoryImpl: ❌ Register API call failed!
E AuthRepositoryImpl: Error: Network error: Connection refused
```
→ **Fix**:
- Kiểm tra server có chạy? (`java -jar backend.jar`)
- Kiểm tra IP/port đúng không?
- Kiểm tra firewall cho phép port 8080?

---

## 📋 KIỂM TRA DATABASE (Backend Side)

Sau khi đăng ký thành công, kiểm tra user có được lưu vào database:

### MySQL Query
```sql
SELECT * FROM users WHERE email = 'test@example.com';
```
**Nếu có user → Đăng ký thành công!** ✅

### MongoDB Query (nếu dùng MongoDB)
```javascript
db.users.findOne({ email: 'test@example.com' });
```

### Kiểm tra PostgreSQL
```sql
SELECT id, username, email, full_name, created_at FROM "user" WHERE email = 'test@example.com';
```

---

## 🔍 DEBUGGING NẾU CÒN VẤN ĐỀ

### 1. Nếu Logs Show "Connection refused"
**Nguyên nhân**: Server không chạy hoặc IP sai  
**Fix**:
```bash
# Check server chạy
netstat -ano | Select-String ":8080"

# Check API endpoint
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d '{"username":"test","email":"test@test.com","password":"123456","fullName":"Test User"}'
```

### 2. Nếu Logs Show "Response body is null"
**Nguyên nhân**: Server trả về 200 nhưng body rỗng  
**Fix**: Kiểm tra backend endpoint `/api/auth/register` có trả về JSON đúng format không
```json
{
  "id": 10,
  "username": "test@example.com",
  "email": "test@example.com",
  "fullName": "Test User",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Nếu Logs Show "Response Code: 403"
**Nguyên nhân**: Server reject request (có thể JWT config sai)  
**Fix**: Kiểm tra backend spring-security / CORS config

### 4. Xem Raw JSON Response
Edit `AuthRepositoryImpl.java`, thêm:
```java
try {
    String rawBody = response.raw().body() != null ? response.raw().body().string() : "null";
    Log.d(TAG, "Raw response body: " + rawBody);
} catch (Exception e) {
    Log.d(TAG, "Could not read raw body");
}
```

---

## ✅ TRẠNG THÁI

| Thành Phần | Status |
|-----------|--------|
| Switch to Real API | ✅ Done |
| Detailed Logging | ✅ Enhanced |
| Ready to Test | ✅ Yes |

---

## 🚀 NEXT STEPS

1. **Build**: `.\gradlew assembleDebug`
2. **Run**: App on emulator/device
3. **Register**: Đăng ký email mới
4. **Check Logs**: Xem response từ backend
5. **Verify DB**: Query database xem user có được lưu không
6. **If Error**: Refer to "DEBUGGING" section trên

**Nếu logs show "Response Code: 201" + "Token received" → THÀNH CÔNG! 🎉**
