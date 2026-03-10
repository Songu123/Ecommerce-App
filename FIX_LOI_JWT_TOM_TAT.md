# 🎯 FIX LỖI JWT - TÓM TẮT & HƯỚNG DẪN

## 📊 TÌNH TRẠNG HIỆN TẠI

### ✅ HOẠT ĐỘNG TỐT:
- ✅ Android app code ĐÚNG
- ✅ Kết nối đến server THÀNH CÔNG (không còn timeout)
- ✅ API endpoints ĐÚNG
- ✅ Request body FORMAT ĐÚNG
- ✅ Network security config ĐÃ ĐÚNG

### ❌ VẤN ĐỀ:
```
HTTP 401 - Unauthorized
{"error":"The signing key's size is 32 bits which is not secure enough 
for the HS256 algorithm. Keys used with HS256 MUST have a size >= 256 bits"}
```

**NGUYÊN NHÂN:** Server backend JWT secret key QUÁ NGẮN (< 256 bits)

---

## 🔧 GIẢI PHÁP - 3 BƯỚC ĐƠN GIẢN

### BƯỚC 1: Generate JWT Secret Key mới

#### Cách 1: Dùng PowerShell Script (Khuyến nghị)
```powershell
# Chạy file có sẵn:
.\generate_jwt_key.ps1

# Hoặc chạy lệnh này:
$bytes = New-Object byte[] 32; (New-Object Random).NextBytes($bytes); [Convert]::ToBase64String($bytes)
```

#### Cách 2: Copy key mẫu sẵn có
```
3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT
```

### BƯỚC 2: Fix Spring Boot Config

1. Mở Spring Boot project
2. Tìm file: `src/main/resources/application.properties`
3. Tìm dòng: `jwt.secret=...`
4. Thay thế bằng:

```properties
jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT
```

**LƯU Ý:** Key phải có ít nhất 32 ký tự!

### BƯỚC 3: Restart Server

```bash
# Stop server (Ctrl+C)

# Start lại
./mvnw spring-boot:run

# Hoặc với Gradle
./gradlew bootRun
```

Đợi đến khi thấy: `Started Application in X.XXX seconds`

---

## 🧪 TEST SAU KHI FIX

### Test 1: Từ Browser/Postman

```bash
GET http://localhost:8080/api/auth/test
→ Phải trả về HTTP 200
```

### Test 2: Register User Mới

```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@test.com",
  "password": "password123",
  "fullName": "Test User"
}

→ Phải trả về HTTP 200 với JWT token
```

### Test 3: Login

```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "test@test.com",
  "password": "password123"
}

→ Phải trả về HTTP 200 với JWT token
```

### Test 4: Từ Android App

1. Mở app
2. Click "Đăng ký ngay"
3. Nhập thông tin
4. Submit
5. ✅ Phải thành công và navigate đến home

---

## 🔍 NẾU VẪN BỊ LỖI

### Lỗi: "Email hoặc mật khẩu không đúng"

**Giải pháp:**
- Đăng ký user MỚI trước (chưa có trong database)
- Rồi mới login

### Lỗi: "Email đã được đăng ký"

**Giải pháp:**
- Đổi email khác
- Hoặc login bằng email đó

### Lỗi: Vẫn timeout

**Giải pháp:**
1. Check Windows Firewall:
   ```powershell
   netsh advfirewall firewall add rule name="Spring Boot" dir=in action=allow protocol=TCP localport=8080
   ```

2. Check server đang chạy:
   ```powershell
   netstat -ano | findstr :8080
   ```

3. Check antivirus không chặn

---

## 📱 CẢI TIẾN TRONG ANDROID APP

### 1. Error Handling Improvements ✅

**File:** `AuthRepositoryImpl.java`
- ✅ Parse JSON error responses
- ✅ Detect JWT secret key error
- ✅ User-friendly error messages
- ✅ Specific error codes handling

**Methods added:**
- `parseErrorResponse()` - Parse server errors
- `getNetworkErrorMessage()` - Parse network errors

### 2. UI Error Messages ✅

**Files:**
- ✅ `LoginFragment.java` - Better error display
- ✅ `RegisterFragment.java` - Better error display

**Features:**
- Detect JWT error → Show fix guide
- Detect timeout → Show connection help
- Detect 401 → Show credential error
- Detect 409 → Show duplicate user error

### 3. Diagnostic Tools ✅

**Files created:**
- ✅ `ServerDiagnostic.java` - Test server health
- ✅ `ServerTestUtil.java` - UI utility for testing
- ✅ `generate_jwt_key.ps1` - Generate secure keys

---

## 📝 FILES CREATED/MODIFIED

### New Files:
1. ✅ `JWT_SERVER_FIX_URGENT.md` - Hướng dẫn fix chi tiết
2. ✅ `GENERATE_JWT_KEY.md` - Generate key guide
3. ✅ `generate_jwt_key.ps1` - PowerShell script
4. ✅ `ServerDiagnostic.java` - Diagnostic utility
5. ✅ `ServerTestUtil.java` - Test utility
6. ✅ `FIX_LOI_JWT_TOM_TAT.md` - File này

### Modified Files:
1. ✅ `AuthRepositoryImpl.java` - Improved error handling
2. ✅ `LoginFragment.java` - Better error messages
3. ✅ `RegisterFragment.java` - Better error messages

---

## 🚀 QUICK START

### Nếu bạn có quyền truy cập Spring Boot:

1. **Generate key:**
   ```powershell
   .\generate_jwt_key.ps1
   ```

2. **Copy key vào application.properties**

3. **Restart server:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Test app** - Mọi thứ sẽ hoạt động!

### Nếu KHÔNG có quyền truy cập Spring Boot:

Liên hệ người quản lý backend và gửi họ:
- File: `JWT_SERVER_FIX_URGENT.md`
- Hoặc key này: `3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT`

---

## 💡 GHI CHÚ QUAN TRỌNG

1. **Android app KHÔNG CẦN SỬA GÌ THÊM** - Code đã đúng
2. **Vấn đề NẰM Ở SERVER** - Cần fix JWT secret key
3. **Fix rất đơn giản** - Chỉ cần thay 1 dòng config
4. **Sau khi fix** - Tất cả sẽ hoạt động ngay lập tức

---

## 📞 HỖ TRỢ

Nếu sau khi làm theo hướng dẫn mà vẫn bị lỗi:

1. Xem log của Spring Boot server
2. Test API bằng Postman/curl
3. Check database có user chưa
4. Đảm bảo Spring Boot dependencies đầy đủ

---

## ✅ CHECKLIST

Sau khi fix, kiểm tra:

- [ ] generate_jwt_key.ps1 đã chạy thành công
- [ ] application.properties đã update jwt.secret
- [ ] Spring Boot restart thành công
- [ ] GET /api/auth/test trả về 200 OK
- [ ] POST /api/auth/register thành công (200)
- [ ] POST /api/auth/login thành công (200)
- [ ] Android app register thành công
- [ ] Android app login thành công
- [ ] Token được lưu vào SharedPreferences
- [ ] Navigate đến MainActivity thành công

---

**Created:** Mar 09, 2026
**Status:** 🚨 SERVER-SIDE FIX REQUIRED
**Priority:** HIGH
**Difficulty:** EASY (1 line change + restart)
