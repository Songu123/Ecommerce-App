# 🔧 KHẮC PHỤC LỖI: Server Connection & JWT Issues

## ❌ LỖI HIỆN TẠI

### Log từ app:
```
2026-02-03 15:45:19.265: HTTP 401 - Unauthorized
Error: "The signing key's size is 32 bits which is not secure enough 
for the HS256 algorithm. JWT JWA Specification requires keys >= 256 bits"
```

### Nguyên nhân:
✅ **Server ĐANG CHẠY** (không còn timeout)
❌ **JWT Secret Key trên server quá ngắn** (< 256 bits)
❌ **User chưa tồn tại trong database** hoặc sai password

---

## 🔧 GIẢI PHÁP

### OPTION 1: Fix Server (Khuyến nghị) ⭐

#### Bước 1: Fix JWT Secret Key

Mở file Spring Boot config: `application.properties` hoặc `application.yml`

**Cũ (❌ Lỗi):**
```properties
jwt.secret=mySecretKey123
```

**Mới (✅ Đúng):**
```properties
# JWT Secret phải >= 256 bits (32 ký tự)
jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH
```

**Hoặc generate key mới:**
```java
// Trong Java/Spring Boot
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

String secretKey = Base64.getEncoder().encodeToString(
    Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
);
System.out.println("New Secret Key: " + secretKey);
```

#### Bước 2: Restart Spring Boot Server
```bash
# Stop server (Ctrl+C)
# Start lại
./mvnw spring-boot:run
# hoặc
java -jar target/your-app.jar
```

#### Bước 3: Test lại từ Android app
1. Mở app
2. Click "Đăng ký ngay" 
3. Tạo tài khoản mới
4. Sẽ thành công!

---

### OPTION 2: Mock Authentication (Test nhanh)

Nếu bạn muốn test UI mà không cần server, tôi có thể tạo **MockAuthRepository** để bypass API call.

**Ưu điểm:**
- Test UI ngay lập tức
- Không cần server
- Persistent login vẫn hoạt động

**Nhược điểm:**
- Không test được API thật
- Chỉ dùng để phát triển UI

**Tôi có nên tạo MockAuthRepository không?** (Trả lời Yes/No)

---

### OPTION 3: Sử dụng server khác (Tạm thời)

Nếu không thể fix server ngay, có thể dùng backend demo:

1. Tôi có thể hướng dẫn deploy Spring Boot lên cloud (Railway, Render, Heroku)
2. Hoặc dùng API mock service (JSONPlaceholder, Mockoon)

---

## 📝 CHECKLIST KIỂM TRA SERVER

### 1. Server đang chạy? ✅
```bash
curl http://localhost:8080/api/auth/test
# Hoặc
curl http://localhost:8080/actuator/health
```

### 2. Database có dữ liệu?
```sql
-- Kiểm tra table users
SELECT * FROM users;

-- Nếu rỗng, insert test user
INSERT INTO users (username, email, password, full_name, role) 
VALUES ('testuser', 'test@test.com', '$2a$10$...', 'Test User', 'USER');
```

### 3. JWT Secret Key đủ dài? (>= 256 bits)
Xem file `application.properties`:
```properties
jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH
#         ^-- Phải >= 32 ký tự
```

### 4. API Endpoints hoạt động?
```bash
# Test register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@test.com","password":"password123","fullName":"Test User"}'

# Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test@test.com","password":"password123"}'
```

---

## 🎯 QUICK FIX STEP-BY-STEP

### Nếu bạn có quyền truy cập Spring Boot code:

```bash
# 1. Stop server
Ctrl + C

# 2. Mở application.properties
code src/main/resources/application.properties

# 3. Thay đổi jwt.secret
jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH1234567890

# 4. Save và restart
./mvnw spring-boot:run

# 5. Test register từ Android app
```

### Nếu KHÔNG có quyền truy cập server:

**→ Chọn OPTION 2** (Mock Authentication) để test UI trước.

---

## 🔍 LOG HIỆN TẠI CHO THẤY

✅ **Good:**
- Server đã kết nối thành công (không còn timeout!)
- API endpoint `/api/auth/login` hoạt động
- Response trả về HTTP 401 (có server response)

❌ **Bad:**
- JWT secret key < 256 bits → Server trả về error
- Login thất bại → Không lưu được vào SharedPreferences

---

## ⚡ HÀNH ĐỘNG TIẾP THEO

**Chọn 1 trong 3 options:**

### A. Fix Server (5-10 phút) ⭐ Khuyến nghị
1. Fix JWT secret key trong Spring Boot
2. Restart server  
3. Test lại app
**→ Nói "Fix server" để tôi hướng dẫn chi tiết**

### B. Mock Authentication (2 phút)
1. Tôi tạo MockAuthRepository
2. Test UI ngay lập tức
3. Không cần server
**→ Nói "Mock" để tôi tạo code**

### C. Deploy server mới (15-30 phút)
1. Deploy Spring Boot lên cloud
2. Sử dụng server online
**→ Nói "Deploy" để tôi hướng dẫn**

---

## 📞 BẠN MUỐN LÀM GÌ?

Trả lời một trong các từ khóa:
- **"Fix server"** - Hướng dẫn fix JWT secret key
- **"Mock"** - Tạo mock để test UI
- **"Deploy"** - Hướng dẫn deploy server lên cloud
- **"Help"** - Giải thích thêm

---

**Tạo bởi:** GitHub Copilot  
**Ngày:** February 3, 2026
