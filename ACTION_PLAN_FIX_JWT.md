# ✅ ACTION PLAN - FIX LỖI JWT NGAY LẬP TỨC

## 🎯 JWT KEY ĐÃ ĐƯỢC GENERATE!

```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

**Key Info:**
- ✅ Length: 44 characters
- ✅ Security: 256 bits (HS256 compliant)
- ✅ Ready to use!

---

## 📋 LÀM THEO CÁC BƯỚC SAU (5 PHÚT)

### BƯỚC 1: Mở Spring Boot Project ⏱️ 30s

```bash
# Mở folder Spring Boot project
cd /path/to/your/spring-boot-project

# Hoặc mở bằng IDE
code .
```

### BƯỚC 2: Tìm file application.properties ⏱️ 30s

**Location:**
```
src/main/resources/application.properties
```

Hoặc có thể là:
- `src/main/resources/application.yml`
- `src/main/resources/application-dev.properties`

### BƯỚC 3: Thay đổi jwt.secret ⏱️ 1m

**Tìm dòng này:**
```properties
jwt.secret=mySecretKey
# hoặc
jwt.secret=mySecretKey123
# hoặc bất kỳ key nào hiện tại
```

**Thay bằng dòng này:**
```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

**Save file (Ctrl+S)**

### BƯỚC 4: Restart Spring Boot Server ⏱️ 2m

**Nếu server đang chạy:**
```bash
# Stop server
Ctrl + C
```

**Start lại:**
```bash
# Maven
./mvnw spring-boot:run

# Gradle
./gradlew bootRun

# JAR file
java -jar target/your-app.jar
```

**Đợi đến khi thấy log:**
```
Started Application in X.XXX seconds (JVM running for X.XXX)
```

### BƯỚC 5: Test từ Android App ⏱️ 1m

1. Mở Android app
2. Click "Đăng ký ngay"
3. Nhập thông tin:
   - Email: `test123@gmail.com`
   - Password: `password123`
   - Full Name: `Test User`
4. Click **ĐĂNG KÝ**
5. ✅ Phải thành công và chuyển đến màn hình chính!

---

## 🧪 VERIFICATION CHECKLIST

Sau khi làm xong, check từng bước:

- [ ] **Step 1:** application.properties đã được mở
- [ ] **Step 2:** Tìm thấy dòng `jwt.secret=...`
- [ ] **Step 3:** Đã thay bằng key mới (44 ký tự)
- [ ] **Step 4:** Đã save file
- [ ] **Step 5:** Server đã restart
- [ ] **Step 6:** Thấy log "Started Application"
- [ ] **Step 7:** Test API /auth/test → HTTP 200
- [ ] **Step 8:** Android app register → Success
- [ ] **Step 9:** Android app login → Success
- [ ] **Step 10:** Navigate to MainActivity → OK

---

## 🔍 QUICK TEST COMMANDS

### Test 1: Server Health Check
```bash
curl http://localhost:8080/api/auth/test
```
**Expected:** HTTP 200 OK

### Test 2: Register API
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@test.com",
    "password": "password123",
    "fullName": "Test User"
  }'
```
**Expected:** HTTP 200 + JWT token

### Test 3: Login API
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@test.com",
    "password": "password123"
  }'
```
**Expected:** HTTP 200 + JWT token

---

## 📊 BEFORE vs AFTER

### ❌ BEFORE (Current State)

```
Android App → POST /login → Spring Boot
                               ↓
                          ❌ HTTP 401
                          Error: JWT key too short
                               ↓
                          Android shows error
```

### ✅ AFTER (Fixed State)

```
Android App → POST /login → Spring Boot
                               ↓
                          ✅ HTTP 200
                          Token: eyJhbGci...
                               ↓
                          Save to SharedPreferences
                               ↓
                          Navigate to MainActivity
```

---

## 🚨 NẾU VẪN CÓ LỖI

### Lỗi: "Email hoặc mật khẩu không đúng"

**Nguyên nhân:** User chưa tồn tại trong database

**Giải pháp:** 
1. Dùng REGISTER trước (tạo user mới)
2. Sau đó mới LOGIN

### Lỗi: "Email đã được đăng ký"

**Nguyên nhân:** Email đã tồn tại

**Giải pháp:**
1. Đổi email khác để register
2. Hoặc dùng LOGIN với email đó

### Lỗi: Vẫn timeout

**Nguyên nhân:** Server không chạy hoặc firewall chặn

**Giải pháp:**
```bash
# Check server running
netstat -ano | findstr :8080

# Check firewall
netsh advfirewall firewall add rule name="Spring Boot" dir=in action=allow protocol=TCP localport=8080
```

---

## 📞 HỖ TRỢ THÊM

### Nếu không tìm thấy application.properties

**Check các location sau:**
1. `src/main/resources/application.properties`
2. `src/main/resources/application.yml`
3. `src/main/resources/application-dev.properties`
4. `src/main/resources/config/application.properties`

### Nếu không tìm thấy jwt.secret trong file

**Thêm dòng này vào cuối file:**
```properties
# JWT Configuration
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
jwt.expiration=86400000
```

### Nếu server không restart được

**Check log để tìm lỗi:**
```bash
# Xem log
tail -f logs/spring-boot.log

# Hoặc chạy với debug
./mvnw spring-boot:run -X
```

---

## 📝 COPY-PASTE READY CONFIGS

### Option 1: Minimal Config
```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

### Option 2: Full Config
```properties
# JWT Configuration
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
jwt.expiration=86400000
jwt.header=Authorization
jwt.prefix=Bearer
```

### Option 3: With Comments
```properties
# JWT Secret Key (256-bit for HS256)
# Generated on: 2026-03-09
# DO NOT commit this key to public repositories
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=

# JWT Expiration (24 hours in milliseconds)
jwt.expiration=86400000
```

---

## ⏰ ESTIMATED TIME

| Task | Time | Status |
|------|------|--------|
| 1. Open Spring Boot project | 30s | ⏳ |
| 2. Find application.properties | 30s | ⏳ |
| 3. Update jwt.secret | 1m | ⏳ |
| 4. Restart server | 2m | ⏳ |
| 5. Test from Android app | 1m | ⏳ |
| **TOTAL** | **5m** | ⏳ |

---

## 🎯 SUCCESS CRITERIA

You'll know it's fixed when:

✅ Server starts without JWT errors
✅ `curl http://localhost:8080/api/auth/test` returns 200
✅ Android app can register new users
✅ Android app can login
✅ Token is saved in SharedPreferences
✅ App navigates to MainActivity after login

---

## 💾 BACKUP

**Before making changes, backup the file:**
```bash
cp src/main/resources/application.properties src/main/resources/application.properties.backup
```

**To restore:**
```bash
cp src/main/resources/application.properties.backup src/main/resources/application.properties
```

---

**Generated JWT Key:**
```
tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

**Copy this entire line to application.properties:**
```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

---

## 🚀 START NOW!

1. ✅ JWT key đã được generate
2. 📋 Copy key từ trên
3. 🔧 Mở Spring Boot project
4. ✏️ Update application.properties
5. 🔄 Restart server
6. 🧪 Test từ Android app
7. 🎉 DONE!

**Thời gian: 5 phút | Độ khó: Dễ | Priority: URGENT**

---

Created: March 9, 2026
Status: ✅ KEY GENERATED - READY TO APPLY
