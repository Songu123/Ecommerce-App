# ✅ FIX HOÀN TẤT: Connection Timeout

## 🎯 ĐÃ LÀM GÌ

### 1. ✅ Tạo ApiConfig.java
**Path:** `app/src/main/java/com/son/e_commerce/utils/network/ApiConfig.java`

**Features:**
- ⭐ Dễ dàng chuyển đổi giữa EMULATOR và REAL_DEVICE
- ⭐ Chỉ cần thay 2 dòng code
- ⭐ Không cần rebuild nhiều lần

**Đã cấu hình:**
```java
private static final Mode CURRENT_MODE = Mode.REAL_DEVICE;
private static final String YOUR_PC_IP = "10.0.3.238";
```

### 2. ✅ Cập nhật RetrofitClient.java
- Sử dụng ApiConfig thay vì hardcode URL
- Thêm logging để debug
- Thêm method reset() để reload

### 3. ✅ Tạo find_ip.ps1
Script PowerShell tự động tìm IP máy tính

### 4. ✅ Build Thành Công
```
BUILD SUCCESSFUL in 9s
33 actionable tasks completed
```

---

## 🔍 IPs TÌM THẤY

```
✅ 10.0.3.238 - Có thể là WiFi/LAN thực
   192.168.225.1 - Virtual network (VMware/VirtualBox)
   192.168.220.1 - Virtual network (VMware/VirtualBox)
```

**Đã chọn:** `10.0.3.238` (khuyến nghị)

**URL mới:** `http://10.0.3.238:8080/`

---

## 📋 CHECKLIST TRƯỚC KHI TEST

### 1. ✅ Đảm bảo Spring Boot Server đang chạy

```bash
# Start server
cd your-spring-boot-project
./mvnw spring-boot:run

# Đợi đến khi thấy:
Started Application in X.XXX seconds
Tomcat started on port(s): 8080
```

### 2. ✅ Test từ browser PC

Mở browser trên PC và truy cập:
```
http://localhost:8080/api/auth/test
```

**Expected:** Phải thấy response (không timeout)

### 3. ✅ Allow Firewall

```powershell
netsh advfirewall firewall add rule name="Spring Boot Server" dir=in action=allow protocol=TCP localport=8080
```

### 4. ✅ Test từ browser điện thoại

Mở browser trên điện thoại và truy cập:
```
http://10.0.3.238:8080/api/auth/test
```

**Expected:** 
- ✅ Thấy response → Connection OK!
- ❌ Timeout → Check WiFi và firewall

### 5. ✅ Rebuild và Install App

```bash
cd D:\ECommerce
.\gradlew installDebug
```

---

## 🧪 TEST CONNECTION

### Option 1: Từ Browser Điện Thoại
```
http://10.0.3.238:8080/api/auth/test
```

### Option 2: Dùng curl (nếu có)
```bash
curl http://10.0.3.238:8080/api/auth/test
```

### Option 3: Postman Mobile
```
GET http://10.0.3.238:8080/api/auth/test
```

---

## 🔧 NẾU VẪN TIMEOUT

### Check 1: PC và Điện thoại cùng WiFi?
```
PC WiFi: [Tên WiFi]
Phone WiFi: [Tên WiFi]
→ Phải giống nhau!
```

### Check 2: Server có chạy không?
```powershell
netstat -ano | findstr :8080
```

Phải thấy:
```
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
```

### Check 3: Firewall có chặn không?
```powershell
# Tạm thời tắt firewall để test
netsh advfirewall set allprofiles state off

# Test app

# Bật lại firewall
netsh advfirewall set allprofiles state on
```

### Check 4: Thử các IP khác

Nếu `10.0.3.238` không work, thử:

**Option A: 192.168.225.1**
```java
// ApiConfig.java
private static final String YOUR_PC_IP = "192.168.225.1";
```

**Option B: 192.168.220.1**
```java
// ApiConfig.java
private static final String YOUR_PC_IP = "192.168.220.1";
```

Sau đó rebuild:
```bash
.\gradlew installDebug
```

---

## 🎯 THAY ĐỔI NHANH IP

### Nếu cần đổi IP:

1. **Mở file:** `ApiConfig.java`

2. **Tìm dòng:**
```java
private static final String YOUR_PC_IP = "10.0.3.238";
```

3. **Thay đổi IP:**
```java
private static final String YOUR_PC_IP = "192.168.x.x";  // IP mới
```

4. **Rebuild:**
```bash
.\gradlew installDebug
```

---

## 🔄 CHUYỂN ĐỔI MODE

### Khi dùng Emulator:
```java
private static final Mode CURRENT_MODE = Mode.EMULATOR;
// Sẽ tự động dùng: http://10.0.2.2:8080/
```

### Khi dùng Real Device:
```java
private static final Mode CURRENT_MODE = Mode.REAL_DEVICE;
// Sẽ dùng: http://YOUR_PC_IP:8080/
```

### Production:
```java
private static final Mode CURRENT_MODE = Mode.PRODUCTION;
// Sẽ dùng: https://your-api-domain.com/
```

---

## 📊 CONNECTION DEBUG

App sẽ tự động log URL đang dùng:

```
D/RetrofitClient: Using base URL: http://10.0.3.238:8080/ (Mode: Real Device (10.0.3.238))
```

Check log này trong Logcat để verify!

---

## ✅ FINAL CHECKLIST

Trước khi test app:

- [x] ✅ ApiConfig.java đã tạo
- [x] ✅ RetrofitClient.java đã update
- [x] ✅ IP đã được detect: 10.0.3.238
- [x] ✅ Mode đã set: REAL_DEVICE
- [x] ✅ Build successful
- [ ] ⏳ Spring Boot server chạy (BẠN CẦN LÀM)
- [ ] ⏳ Test từ browser điện thoại (BẠN CẦN LÀM)
- [ ] ⏳ Firewall allowed port 8080 (BẠN CẦN LÀM)
- [ ] ⏳ Install app và test (BẠN CẦN LÀM)

---

## 🚀 NEXT STEPS (QUAN TRỌNG!)

### Bước 1: Start Spring Boot Server
```bash
cd your-spring-boot-project
./mvnw spring-boot:run
```

### Bước 2: Allow Firewall
```powershell
netsh advfirewall firewall add rule name="Spring Boot Server" dir=in action=allow protocol=TCP localport=8080
```

### Bước 3: Test từ điện thoại
Mở browser điện thoại:
```
http://10.0.3.238:8080/api/auth/test
```

Phải thấy response! Nếu OK → Install app:

### Bước 4: Install App
```bash
cd D:\ECommerce
.\gradlew installDebug
```

### Bước 5: Test Login
1. Mở app
2. Nhập email và password
3. ✅ Phải connect được!

---

## 💡 IMPORTANT NOTES

### ⚠️ Nếu IP thay đổi (khi đổi WiFi):
1. Chạy lại: `ipconfig | Select-String "IPv4"`
2. Update `YOUR_PC_IP` trong `ApiConfig.java`
3. Rebuild: `.\gradlew installDebug`

### ⚠️ Khi dùng Emulator:
1. Đổi `CURRENT_MODE = Mode.EMULATOR` trong `ApiConfig.java`
2. Rebuild app

### ⚠️ WiFi Issues:
- PC và điện thoại **PHẢI cùng WiFi**
- Không dùng VPN
- Không dùng mobile data trên điện thoại

---

## 📞 TROUBLESHOOTING

### Vẫn timeout?

1. **Ping test từ điện thoại:**
   - Install "Network Utilities" app
   - Ping: 10.0.3.238
   - Nếu fail → Network issue

2. **Try other IPs:**
   ```java
   // Try each IP one by one
   YOUR_PC_IP = "192.168.225.1"  // Test này
   YOUR_PC_IP = "192.168.220.1"  // Hoặc này
   ```

3. **Check server binding:**
   Server phải bind 0.0.0.0, không phải 127.0.0.1
   
   ```properties
   # application.properties
   server.address=0.0.0.0
   ```

---

**Status:** ✅ CODE FIXED & READY
**Build:** ✅ SUCCESSFUL
**Next:** 🚀 START SERVER & TEST!

IP được chọn: **10.0.3.238**
URL: **http://10.0.3.238:8080/**
