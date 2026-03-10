# 🚨 URGENT: Fix HTTP 403 Forbidden - Spring Security

## ✅ ANDROID APP ĐÃ FIXED & INSTALLED!

```
✅ network_security_config.xml updated
✅ 403 error handling added
✅ BUILD SUCCESSFUL
✅ APK INSTALLED on device
```

---

## ❌ VẤN ĐỀ HIỆN TẠI: SERVER

```json
HTTP 403 Forbidden
{
  "status": 403,
  "error": "Forbidden", 
  "path": "/api/auth/register"
}
```

**Nguyên nhân:** Spring Security đang BLOCK endpoint `/api/auth/register`

---

## 🔧 GIẢI PHÁP - 2 FILES CẦN SỬA

### 📝 FILE 1: SecurityConfig.java

**Location:** `src/main/java/com/yourpackage/config/SecurityConfig.java`

**NẾU FILE CHƯA TỒN TẠI → TẠO MỚI:**

```java
package com.yourpackage.config;  // ← Đổi thành package của bạn

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())
            
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // ⭐ PUBLIC ENDPOINTS - No authentication needed
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/test").permitAll()
                
                // All other endpoints need authentication
                .anyRequest().authenticated()
            )
            
            // Stateless session for JWT
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**NẾU FILE ĐÃ TỒN TẠI → KIỂM TRA:**

Đảm bảo có dòng này:
```java
.requestMatchers("/api/auth/**").permitAll()
```

Hoặc với Spring Security 5.x (old version):
```java
.antMatchers("/api/auth/**").permitAll()
```

---

### 📝 FILE 2: application.properties

**Location:** `src/main/resources/application.properties`

**THÊM HOẶC UPDATE:**

```properties
# ========================================
# SERVER CONFIGURATION
# ========================================
server.address=0.0.0.0
server.port=8080

# ========================================
# JWT CONFIGURATION  
# ========================================
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
jwt.expiration=86400000
```

---

## 🔄 RESTART SERVER

```bash
# 1. Stop server
Ctrl + C

# 2. Start server
./mvnw spring-boot:run

# Hoặc với Gradle:
./gradlew bootRun

# Hoặc với Windows Maven:
mvnw.cmd spring-boot:run
```

**Đợi đến khi thấy:**
```
Started Application in X.XXX seconds
Tomcat started on port(s): 8080
```

---

## 🧪 TEST NGAY

### Test 1: Từ PC terminal

```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"test\",\"email\":\"test@test.com\",\"password\":\"pass123\",\"fullName\":\"Test\"}"
```

**Expected:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "id": 1,
  "username": "test",
  "email": "test@test.com"
}
```

**Status:** HTTP 200 ✅

### Test 2: Từ Phone Browser

Mở browser trên điện thoại:
```
http://10.0.3.238:8080/api/auth/test
```

Should see response ✅

### Test 3: Từ Android App

1. Mở app (đã installed)
2. Click "Đăng ký ngay"
3. Nhập:
   - Email: `test123@gmail.com`
   - Password: `password123`
   - Full Name: `Test User`
4. Click "ĐĂNG KÝ"
5. ✅ **Should SUCCESS!**

---

## 🔥 NẾU KHÔNG TÌM THẤY SecurityConfig.java

### Cách 1: Tìm các file Security config

```bash
# Tìm trong project
find . -name "*Security*.java"
find . -name "*Config*.java" | grep -i security
```

### Cách 2: Check dependencies

**Mở:** `pom.xml` hoặc `build.gradle`

**Tìm:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Nếu KHÔNG có dependency này:**
→ Không có Spring Security → Lỗi 403 do nguyên nhân khác

### Cách 3: Disable Spring Security tạm thời

**Trong `pom.xml` comment out:**
```xml
<!--
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
-->
```

Restart server và test lại.

---

## 📋 COMPLETE CHECKLIST

### Android (DONE ✅):
- [x] ✅ network_security_config.xml fixed
- [x] ✅ 403 error handling added
- [x] ✅ Build successful
- [x] ✅ **APK INSTALLED on device**

### Server (TO DO ⏳):
- [ ] ⏳ Find/Create SecurityConfig.java
- [ ] ⏳ Add `.requestMatchers("/api/auth/**").permitAll()`
- [ ] ⏳ Update application.properties (server.address + jwt.secret)
- [ ] ⏳ Restart server
- [ ] ⏳ Test with curl → HTTP 200

### Testing (TO DO ⏳):
- [ ] ⏳ curl register → 200 OK
- [ ] ⏳ curl login → 200 OK
- [ ] ⏳ Android app register → Success
- [ ] ⏳ Android app login → Success

---

## 🎯 WHAT TO DO NOW (5 MINUTES)

### 1️⃣ Tìm Spring Boot project folder

### 2️⃣ Tạo/Update 2 files:

**SecurityConfig.java** (code ở trên)
**application.properties** (config ở trên)

### 3️⃣ Restart server:
```bash
./mvnw spring-boot:run
```

### 4️⃣ Test với curl:
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"test\",\"email\":\"test@test.com\",\"password\":\"pass123\",\"fullName\":\"Test\"}"
```

Phải trả về HTTP 200 + token!

### 5️⃣ Test từ Android app:
App đã installed → Mở và test register!

---

## 📞 QUICK COPY-PASTE

### SecurityConfig.java KEY LINES:
```java
.csrf(csrf -> csrf.disable())
.requestMatchers("/api/auth/**").permitAll()
```

### application.properties:
```properties
server.address=0.0.0.0
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

---

## 💡 TÓM TẮT

**Android:** ✅ DONE & INSTALLED
**Server:** ⏳ Need SecurityConfig fix (5 phút)

**After fix:** Register sẽ work ngay! 🎉

**Files to edit:**
1. SecurityConfig.java
2. application.properties

**Time:** 5 minutes
**Difficulty:** Easy (copy-paste)
