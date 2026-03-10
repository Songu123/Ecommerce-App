# 🔧 Network Security Configuration - Troubleshooting

## ❌ Lỗi: CLEARTEXT communication not permitted

### Thông báo lỗi:
```
java.net.UnknownServiceException: CLEARTEXT communication to 10.0.2.2 
not permitted by network security policy
```

### 🔍 Nguyên nhân:
Từ Android 9 (API 28) trở lên, Android mặc định **không cho phép HTTP cleartext traffic** vì lý do bảo mật. Chỉ HTTPS mới được phép.

---

## ✅ GIẢI PHÁP (Đã áp dụng)

### Bước 1: Tạo Network Security Config

**File:** `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">192.168.0.0</domain>
        <domain includeSubdomains="true">192.168.1.0</domain>
    </domain-config>
</network-security-config>
```

**Giải thích:**
- `10.0.2.2` - Địa chỉ localhost cho Android Emulator
- `localhost` - Local development
- `192.168.x.x` - Local network (cho real devices)
- `cleartextTrafficPermitted="true"` - Cho phép HTTP

### Bước 2: Cập nhật AndroidManifest.xml

```xml
<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"
    ...>
```

**Giải thích:**
- `usesCleartextTraffic="true"` - Cho phép HTTP traffic
- `networkSecurityConfig` - Reference đến config file

---

## 🎯 Khi nào dùng giải pháp này?

### ✅ Development/Testing
- Testing với local backend (localhost, 10.0.2.2)
- Testing với dev server không có SSL
- Rapid prototyping

### ⚠️ Production
**KHÔNG NÊN** dùng cleartext traffic trong production!

**Thay vào đó:**
1. Sử dụng HTTPS với SSL certificate hợp lệ
2. Cấu hình backend với HTTPS
3. Remove cleartext config trước khi release

---

## 🔐 Giải pháp cho Production

### Option 1: Sử dụng HTTPS (Recommended)

```java
// RetrofitClient.java
private static final String BASE_URL = "https://your-api.com/";
```

### Option 2: Certificate Pinning (Advanced)

```xml
<network-security-config>
    <domain-config>
        <domain includeSubdomains="true">your-api.com</domain>
        <pin-set>
            <pin digest="SHA-256">base64_encoded_pin</pin>
        </pin-set>
    </domain-config>
</network-security-config>
```

---

## 🔄 Các trường hợp sử dụng

### 1. Emulator (10.0.2.2)
```xml
<domain includeSubdomains="true">10.0.2.2</domain>
```

### 2. Real Device - Same Network
```xml
<domain includeSubdomains="true">192.168.1.0</domain>
```
Thay đổi IP theo network của bạn (192.168.0.x, 192.168.1.x, etc.)

### 3. Development Server
```xml
<domain includeSubdomains="true">dev.yourcompany.com</domain>
```

### 4. Allow All (Chỉ development)
```xml
<base-config cleartextTrafficPermitted="true">
    <trust-anchors>
        <certificates src="system" />
    </trust-anchors>
</base-config>
```

**⚠️ Warning:** Không dùng trong production!

---

## 🐛 Troubleshooting

### Vẫn bị lỗi sau khi config?

#### 1. Clean & Rebuild
```bash
.\gradlew clean
.\gradlew assembleDebug
.\gradlew installDebug
```

#### 2. Uninstall & Reinstall
```bash
# Uninstall app trên device/emulator
adb uninstall com.son.e_commerce

# Reinstall
.\gradlew installDebug
```

#### 3. Check XML file location
```
✅ Must be: app/src/main/res/xml/network_security_config.xml
❌ NOT: app/res/xml/...
```

#### 4. Check AndroidManifest syntax
```xml
<!-- Correct -->
android:networkSecurityConfig="@xml/network_security_config"

<!-- Wrong -->
android:networkSecurityConfig="@xml/network-security-config"
```

#### 5. Check BASE_URL
```java
// Correct for emulator
private static final String BASE_URL = "http://10.0.2.2:8080/";

// Wrong
private static final String BASE_URL = "http://localhost:8080/";
```

---

## 📱 Testing Checklist

### Before Running
- [x] network_security_config.xml created
- [x] AndroidManifest.xml updated
- [x] Clean & Rebuild done
- [x] App reinstalled

### Verify Setup
```bash
# Check Logcat for:
✅ OkHttp: --> GET http://10.0.2.2:8080/api/products
✅ OkHttp: <-- 200 OK

# Should NOT see:
❌ CLEARTEXT communication not permitted
❌ UnknownServiceException
```

---

## 🔍 Debug Tips

### 1. View Network Logs
```java
// Already enabled in RetrofitClient.java
HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
```

### 2. Check Logcat
```
Filter: OkHttp
Look for: --> and <-- symbols
```

### 3. Test Backend Directly
```bash
# From terminal (outside app)
curl http://10.0.2.2:8080/api/products

# Should return JSON data
```

### 4. Verify Spring Boot Running
```bash
# In browser
http://localhost:8080/api/products

# Should show data
```

---

## 🎯 Quick Fix Summary

### Problem
```
CLEARTEXT communication not permitted
```

### Solution (3 steps)
1. Create `res/xml/network_security_config.xml`
2. Update `AndroidManifest.xml` with:
   - `android:usesCleartextTraffic="true"`
   - `android:networkSecurityConfig="@xml/network_security_config"`
3. Clean, rebuild, reinstall app

### Result
```
✅ HTTP requests work
✅ Can connect to local backend
✅ API calls succeed
```

---

## ⚠️ Important Notes

### Development
- ✅ OK to use cleartext for local testing
- ✅ OK for emulator (10.0.2.2)
- ✅ OK for local network

### Production
- ❌ Do NOT use cleartext
- ✅ Use HTTPS only
- ✅ Remove cleartext config
- ✅ Add certificate pinning

### Security Best Practices
1. Use HTTPS in production
2. Validate SSL certificates
3. Implement certificate pinning
4. Use secure connections
5. Never expose sensitive data over HTTP

---

## 📚 More Info

### Android Documentation
- [Network Security Configuration](https://developer.android.com/training/articles/security-config)
- [Security Best Practices](https://developer.android.com/topic/security/best-practices)

### Common Network Issues
- Cannot connect: Check BASE_URL
- Timeout: Check backend running
- 404 Error: Check endpoint path
- Certificate error: Use cleartext config for dev

---

## ✅ Verification

After applying fix, you should see in Logcat:

```
I/OkHttp: --> GET http://10.0.2.2:8080/api/products http/1.1
I/OkHttp: --> END GET
I/OkHttp: <-- 200 OK http://10.0.2.2:8080/api/products (123ms)
I/OkHttp: Content-Type: application/json
I/OkHttp: [{"id":1,"name":"Product 1",...}]
I/OkHttp: <-- END HTTP (1234-byte body)
```

**✅ Success!** App can now connect to backend!

---

**Fixed:** 02/02/2026  
**Status:** ✅ Resolved  
**Solution:** Network Security Config for HTTP cleartext
