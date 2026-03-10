# 🚨 KHẨN CẤP: Lỗi JWT Secret Key - Server Backend

## ❌ LỖI HIỆN TẠI

```
HTTP 401 - Unauthorized
{"error":"The signing key's size is 32 bits which is not secure enough 
for the HS256 algorithm. The JWT JWA Specification (RFC 7518, Section 3.2) 
states that keys used with HS256 MUST have a size >= 256 bits"}
```

## 🔍 NGUYÊN NHÂN

- ✅ Android app hoạt động ĐÚNG
- ✅ Kết nối đến server THÀNH CÔNG (không còn timeout)
- ❌ **Server JWT secret key QUÁ NGẮN** (< 256 bits)

## 🛠️ GIẢI PHÁP - FIX SERVER

### Step 1: Tìm file config của Spring Boot

Tìm một trong các file sau trong Spring Boot project:
- `src/main/resources/application.properties`
- `src/main/resources/application.yml`
- `src/main/resources/application-dev.properties`

### Step 2: Thay đổi JWT Secret Key

**❌ CŨ (Lỗi):**
```properties
jwt.secret=mySecretKey
# hoặc
jwt.secret=mySecretKey123
```
*Key này chỉ có 11-16 ký tự = 88-128 bits < 256 bits*

**✅ MỚI (Đúng):**
```properties
# JWT Secret phải >= 32 ký tự (256 bits)
jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH1234567890

# Hoặc sử dụng key random này:
jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT
```

**Lưu ý:** Key phải có ít nhất 32 ký tự (256 bits)

### Step 3: Restart Spring Boot Server

```bash
# Stop server hiện tại (Ctrl+C)

# Restart server
./mvnw spring-boot:run

# Hoặc nếu dùng Gradle
./gradlew bootRun

# Hoặc chạy JAR file
java -jar target/your-app.jar
```

### Step 4: Kiểm tra server đã hoạt động

Mở browser hoặc Postman test:

```bash
# Test 1: Health check
GET http://localhost:8080/api/auth/test

# Test 2: Register (tạo user mới)
POST http://localhost:8080/api/auth/register
Body: {
  "username": "testuser",
  "email": "test@test.com", 
  "password": "password123",
  "fullName": "Test User"
}

# Test 3: Login
POST http://localhost:8080/api/auth/login
Body: {
  "username": "test@test.com",
  "password": "password123"
}
```

Nếu response trả về HTTP 200 với JWT token → Server đã fix XONG!

---

## 🎯 GENERATE JWT SECRET KEY MỚI

### Cách 1: Dùng Java Code

```java
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        String key = Base64.getEncoder().encodeToString(
            Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );
        System.out.println("JWT Secret Key: " + key);
    }
}
```

### Cách 2: Dùng Online Tool

Truy cập: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx

- Chọn: **256-bit**
- Format: **Base64**
- Click Generate
- Copy key vào `jwt.secret`

### Cách 3: Dùng Command Line

**Linux/Mac:**
```bash
openssl rand -base64 32
```

**Windows PowerShell:**
```powershell
# Generate 32 random bytes, convert to base64
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## 📋 CHECKLIST SAU KHI FIX

- [ ] JWT secret key >= 32 ký tự trong `application.properties`
- [ ] Server restart thành công
- [ ] Test API register thành công (HTTP 200)
- [ ] Test API login thành công (HTTP 200)
- [ ] Android app có thể register user mới
- [ ] Android app có thể login
- [ ] Token được lưu vào SharedPreferences

---

## 🔄 NẾU VẪN BỊ LỖI

### Lỗi 401 - User không tồn tại

Nếu sau khi fix JWT secret key vẫn bị 401 với lỗi khác:

1. **Thử đăng ký user mới trước** (POST /api/auth/register)
2. **Rồi mới login** (POST /api/auth/login)

### Lỗi 500 - Server Error

1. Check log của Spring Boot
2. Check database connection
3. Check tất cả dependencies trong `pom.xml` hoặc `build.gradle`

### Vẫn timeout

1. Check firewall Windows
2. Check antivirus
3. Thử đổi port khác (8081, 8082...)

---

## 📞 HỖ TRỢ

Nếu bạn không có quyền truy cập Spring Boot code hoặc không thể fix server:

**Option A: Tôi có thể tạo MockAuthRepository**
- Simulate API responses
- Test UI ngay lập tức
- Không cần server

**Option B: Hướng dẫn deploy server lên cloud**
- Railway.app (free tier)
- Render.com (free tier)
- Heroku (paid)

**Option C: Cung cấp Spring Boot code mẫu**
- Full working backend với JWT fix
- Ready to run

---

## ✅ KẾT LUẬN

**VẤN ĐỀ:** Server backend JWT secret key không đủ dài

**GIẢI PHÁP:**
1. Fix `jwt.secret` trong `application.properties` thành >= 32 ký tự
2. Restart Spring Boot server
3. Test lại từ Android app

**Android app code KHÔNG CẦN SỬA**, nó đã hoạt động đúng!

---

**Created:** Feb 03, 2026 | **Priority:** 🚨 URGENT
