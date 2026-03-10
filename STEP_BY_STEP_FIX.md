# 🎯 LÀM THEO TỪNG BƯỚC - FIX CONNECTION TIMEOUT

## ✅ PHẦN 1: ANDROID APP (ĐÃ XONG)

```
✅ ApiConfig.java created
✅ RetrofitClient.java updated
✅ ConnectionTester.java created
✅ IP detected: 10.0.3.238
✅ Mode set: REAL_DEVICE
✅ BUILD SUCCESSFUL
```

---

## ⏳ PHẦN 2: FIX SERVER (BẠN CẦN LÀM NGAY)

### Bước 1: Tìm Spring Boot Project

Tìm folder Spring Boot project của bạn (folder có file `pom.xml` hoặc `build.gradle`)

### Bước 2: Mở application.properties

**File location:**
```
your-spring-boot-project/
└── src/
    └── main/
        └── resources/
            └── application.properties  ← MỞ FILE NÀY
```

### Bước 3: Thêm/Sửa Config

**COPY & PASTE đoạn này vào application.properties:**

```properties
# ============================================================
# SERVER CONFIGURATION
# ============================================================
# QUAN TRỌNG: Bind 0.0.0.0 để accept connection từ network
server.address=0.0.0.0
server.port=8080

# ============================================================
# JWT CONFIGURATION
# ============================================================
# JWT Secret key phải >= 256 bits (32 characters)
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
jwt.expiration=86400000
```

**Save file (Ctrl+S)**

### Bước 4: Stop Server Hiện Tại

Nếu server đang chạy, trong terminal nhấn:
```
Ctrl + C
```

### Bước 5: Start Server Mới

```bash
# Nếu dùng Maven:
./mvnw spring-boot:run

# Nếu dùng Gradle:
./gradlew bootRun

# Windows (nếu không có ./mvnw):
mvnw.cmd spring-boot:run
```

**Đợi đến khi thấy:**
```
Started Application in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port(s): 8080 (http) with context path ''
```

### Bước 6: Verify Server Binding

**Mở PowerShell mới** và chạy:
```powershell
netstat -ano | findstr :8080
```

**Phải thấy:**
```
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING    ✅
```

**Nếu thấy:**
```
TCP    [::]:8080              [::]:0                 LISTENING    ❌
```
→ Quay lại bước 3, check lại `server.address=0.0.0.0`

---

## ⏳ PHẦN 3: FIREWALL (BẠN CẦN LÀM)

### Bước 7: Allow Port 8080

**Mở PowerShell as Administrator** và chạy:

```powershell
netsh advfirewall firewall add rule name="Spring Boot Server" dir=in action=allow protocol=TCP localport=8080
```

**Expected output:**
```
Ok.
```

### Bước 8 (Optional): Verify Firewall Rule

```powershell
netsh advfirewall firewall show rule name="Spring Boot Server"
```

---

## ⏳ PHẦN 4: TEST CONNECTION (QUAN TRỌNG!)

### Bước 9: Test từ PC Browser

Mở browser trên PC và truy cập:
```
http://localhost:8080/api/auth/test
```

**Expected:** 
- ✅ Thấy response (text hoặc JSON)
- ❌ Error 404 → OK (server chạy, nhưng endpoint có thể khác)
- ❌ Timeout → Server chưa chạy hoặc sai port

### Bước 10: Test từ IP Local

Mở browser trên PC và truy cập:
```
http://10.0.3.238:8080/api/auth/test
```

**Expected:** Giống như bước 9

### Bước 11: Test từ Điện Thoại

**QUAN TRỌNG NHẤT!**

1. Đảm bảo điện thoại và PC **CÙNG WIFI**
2. Tắt mobile data trên điện thoại
3. Mở browser trên điện thoại
4. Truy cập:
```
http://10.0.3.238:8080/api/auth/test
```

**Expected:**
- ✅ Thấy response → CONNECTION OK! Tiếp tục bước 12
- ❌ Timeout → Check lại WiFi hoặc firewall

---

## ⏳ PHẦN 5: INSTALL APP (BẠN CẦN LÀM)

### Bước 12: Install APK

**Mở PowerShell:**
```powershell
cd D:\ECommerce
.\gradlew installDebug
```

**Hoặc nếu có APK built:**
```
D:\ECommerce\app\build\outputs\apk\debug\app-debug.apk
```
Copy file này vào điện thoại và install.

### Bước 13: Test Login

1. Mở app trên điện thoại
2. Nhập email: `test@gmail.com`
3. Nhập password: `password123`
4. Click "Đăng nhập"

**Expected:**
- ✅ Login thành công → Done! 🎉
- ❌ Timeout → Check Logcat (bước 14)

### Bước 14: Check Logcat (Nếu Lỗi)

Kết nối điện thoại với PC và chạy:
```bash
adb logcat | findstr "RetrofitClient"
```

**Phải thấy:**
```
D/RetrofitClient: Using base URL: http://10.0.3.238:8080/ (Mode: Real Device (10.0.3.238))
```

Nếu vẫn thấy `10.0.2.2` → App chưa rebuild đúng, chạy lại:
```bash
.\gradlew clean installDebug
```

---

## 🔧 NẾU VẪN KHÔNG WORK

### Try Different IP

**Edit file:** `ApiConfig.java`

**Location:** `app/src/main/java/com/son/e_commerce/utils/network/ApiConfig.java`

**Try IP #1:**
```java
private static final String YOUR_PC_IP = "10.0.3.238";
```

**Try IP #2 (nếu #1 không work):**
```java
private static final String YOUR_PC_IP = "192.168.225.1";
```

**Try IP #3 (nếu #2 không work):**
```java
private static final String YOUR_PC_IP = "192.168.220.1";
```

**Sau mỗi thay đổi, chạy:**
```bash
.\gradlew installDebug
```

### Disable Firewall Temporarily

```powershell
# Tắt firewall để test
netsh advfirewall set allprofiles state off

# Test từ điện thoại browser:
# http://10.0.3.238:8080/api/auth/test

# Nếu OK → Firewall là vấn đề
# Bật lại firewall
netsh advfirewall set allprofiles state on

# Add rule:
netsh advfirewall firewall add rule name="Spring Boot" dir=in action=allow protocol=TCP localport=8080
```

---

## ✅ CHECKLIST HOÀN THÀNH

### Server:
- [ ] ⏳ application.properties có `server.address=0.0.0.0`
- [ ] ⏳ application.properties có JWT secret key
- [ ] ⏳ Server restart thành công
- [ ] ⏳ `netstat` show `0.0.0.0:8080`
- [ ] ⏳ Test từ localhost OK
- [ ] ⏳ Firewall rule added

### Connection Test:
- [ ] ⏳ Test từ PC browser OK
- [ ] ⏳ Test từ IP local OK
- [ ] ⏳ Test từ phone browser OK ⭐ (QUAN TRỌNG)

### App:
- [ ] ⏳ App installed on phone
- [ ] ⏳ Test login
- [ ] ⏳ Login success! 🎉

---

## 🎯 SUMMARY

**Đã làm (Android):**
- ✅ Tạo ApiConfig với IP: 10.0.3.238
- ✅ Update RetrofitClient
- ✅ Build thành công

**Cần làm (Server):**
1. Add `server.address=0.0.0.0` to application.properties
2. Add JWT secret key
3. Restart server
4. Allow firewall port 8080

**Cần làm (Testing):**
1. Test từ phone browser: `http://10.0.3.238:8080/api/auth/test`
2. Install app: `.\gradlew installDebug`
3. Test login

**Thời gian:** ~10 phút

---

## 📞 QUICK COMMANDS

```powershell
# Check server running
netstat -ano | findstr :8080

# Allow firewall
netsh advfirewall firewall add rule name="Spring Boot" dir=in action=allow protocol=TCP localport=8080

# Install app
cd D:\ECommerce
.\gradlew installDebug

# Check logcat
adb logcat | findstr "RetrofitClient"
```

---

**Created:** March 9, 2026
**Status:** ✅ APP READY | ⏳ SERVER NEEDS CONFIG
**Next:** Follow steps 1-14 above!
