# 🔧 FIX: Connection Timeout - Server Not Reachable

## ❌ LỖI HIỆN TẠI

```
SocketTimeoutException: failed to connect to /10.0.2.2 (port 8080) 
from /172.16.1.15 (port 38116) after 30000ms
```

**Phân tích:**
- App IP: `172.16.1.15` → Đang dùng **THIẾT BỊ THẬT** (không phải emulator)
- Server IP: `10.0.2.2:8080` → Địa chỉ này **CHỈ DÀNH CHO EMULATOR**
- Timeout: 30 giây → **KHÔNG KẾT NỐI ĐƯỢC**

**Nguyên nhân:**
1. ❌ Dùng sai IP (10.0.2.2 chỉ hoạt động trên emulator)
2. ❌ Server Spring Boot không đang chạy
3. ❌ Firewall chặn kết nối
4. ❌ Không cùng mạng với máy chạy server

---

## ✅ GIẢI PHÁP

### BƯỚC 1: Tìm IP Máy Tính Chạy Server

#### Windows (PowerShell):
```powershell
ipconfig | Select-String "IPv4"
```

**Hoặc:**
```powershell
(Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.IPAddress -like "192.168.*" -or $_.IPAddress -like "172.*"}).IPAddress
```

#### Kết quả mẫu:
```
IPv4 Address: 192.168.1.105
hoặc
IPv4 Address: 172.16.1.100
```

### BƯỚC 2: Cập Nhật BASE_URL

**File:** `RetrofitClient.java`

**Thay đổi từ:**
```java
private static final String BASE_URL = "http://10.0.2.2:8080/";
```

**Thành (dùng IP máy tính của bạn):**
```java
private static final String BASE_URL = "http://192.168.1.105:8080/";
// HOẶC
private static final String BASE_URL = "http://172.16.1.100:8080/";
```

**Lưu ý:** Thay `192.168.1.105` bằng IP thực tế của máy tính bạn!

### BƯỚC 3: Kiểm Tra Server Đang Chạy

```powershell
# Check port 8080
netstat -ano | findstr :8080

# Nếu thấy:
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
# → Server đang chạy ✅

# Nếu không thấy gì:
# → Server KHÔNG chạy ❌
```

### BƯỚC 4: Start Spring Boot Server

```bash
cd your-spring-boot-project
./mvnw spring-boot:run
```

Đợi đến khi thấy:
```
Started Application in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port(s): 8080 (http)
```

### BƯỚC 5: Cho Phép Firewall

```powershell
# Windows Firewall - Allow port 8080
netsh advfirewall firewall add rule name="Spring Boot Server" dir=in action=allow protocol=TCP localport=8080

# Verify
netsh advfirewall firewall show rule name="Spring Boot Server"
```

### BƯỚC 6: Test Kết Nối

Từ điện thoại, mở browser và truy cập:
```
http://192.168.1.105:8080/api/auth/test
```

**Nếu thấy response → Kết nối OK ✅**
**Nếu timeout → Check lại các bước trên**

---

## 🎯 QUICK FIX COMMANDS

### Tìm IP máy tính:
```powershell
ipconfig | Select-String "IPv4"
```

### Check server running:
```powershell
netstat -ano | findstr :8080
```

### Allow firewall:
```powershell
netsh advfirewall firewall add rule name="Spring Boot" dir=in action=allow protocol=TCP localport=8080
```

### Start server:
```bash
cd your-spring-boot-project
./mvnw spring-boot:run
```

---

## 📱 FIX APP (3 OPTIONS)

### Option 1: Cập nhật IP cố định (Simple)

**File:** `RetrofitClient.java`

```java
// Thay bằng IP máy tính của bạn
private static final String BASE_URL = "http://192.168.1.105:8080/";
```

**Rebuild app:**
```bash
cd D:\ECommerce
.\gradlew clean assembleDebug
```

### Option 2: IP động với BuildConfig (Recommended)

Tôi sẽ tạo giải pháp này cho bạn!

### Option 3: Config trong app (Advanced)

Cho phép user nhập IP trong app settings.

---

## 🔧 COMMON IPs

| Môi trường | IP Address | Notes |
|------------|------------|-------|
| Emulator | `10.0.2.2:8080` | Localhost của máy host |
| Real Device (WiFi) | `192.168.1.xxx:8080` | Cùng WiFi với PC |
| Real Device (Hotspot) | `172.16.1.xxx:8080` | PC connect hotspot phone |
| Localhost | `127.0.0.1:8080` | Chỉ trên máy PC |
| Production | `https://api.domain.com` | Deploy lên cloud |

---

## 📋 TROUBLESHOOTING

### 1. Tìm IP của máy tính
```powershell
ipconfig
```
Tìm dòng "IPv4 Address" trong phần "Wireless LAN adapter Wi-Fi" hoặc "Ethernet adapter"

### 2. Ping từ điện thoại
```bash
# Từ terminal trên Android hoặc dùng app Network Utilities
ping 192.168.1.105
```

### 3. Test API từ browser điện thoại
```
http://192.168.1.105:8080/api/auth/test
```

### 4. Đảm bảo cùng WiFi
- PC và điện thoại phải cùng mạng WiFi
- Không dùng VPN
- Không dùng mobile data

---

## 🎯 GIẢI PHÁP TỐI ƯU

Tôi sẽ tạo một cấu hình linh hoạt để dễ dàng chuyển đổi giữa emulator và real device!

Xem file tiếp theo...
