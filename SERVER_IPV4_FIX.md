# 🔧 FIX: Server Only Binding IPv6

## ❌ VẤN ĐỀ

Server Spring Boot đang chạy nhưng CHỈ bind IPv6:
```
TCP    [::]:8080              [::]:0                 LISTENING
```

**Cần bind IPv4:**
```
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
```

---

## ✅ GIẢI PHÁP

### Option 1: Thêm cấu hình vào application.properties ⭐

**File:** `src/main/resources/application.properties`

Thêm dòng này:
```properties
server.address=0.0.0.0
```

Hoặc nếu dùng `application.yml`:
```yaml
server:
  address: 0.0.0.0
```

### Option 2: Java command line argument

Khi start server:
```bash
java -jar your-app.jar --server.address=0.0.0.0

# Hoặc với Maven
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.address=0.0.0.0
```

### Option 3: System property

```bash
java -Dserver.address=0.0.0.0 -jar your-app.jar
```

---

## 🔄 APPLY FIX

### Bước 1: Stop server hiện tại
Trong terminal đang chạy server, nhấn `Ctrl+C`

### Bước 2: Thêm config

**Mở:** `application.properties`

**Thêm:**
```properties
# Server Configuration
server.address=0.0.0.0
server.port=8080

# JWT Configuration (nếu chưa có)
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

### Bước 3: Restart server
```bash
./mvnw spring-boot:run
```

### Bước 4: Verify binding

Mở terminal mới:
```powershell
netstat -ano | findstr 8080
```

**Expected:**
```
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
hoặc cả 2:
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
TCP    [::]:8080              [::]:0                 LISTENING
```

---

## 🧪 TEST

### Test 1: Từ localhost (PC)
```bash
curl http://localhost:8080/api/auth/test
```

### Test 2: Từ IP local
```bash
curl http://10.0.3.238:8080/api/auth/test
```

### Test 3: Từ điện thoại browser
```
http://10.0.3.238:8080/api/auth/test
```

Tất cả phải work!

---

## 💡 ALTERNATIVE: Dùng IPv4 Preference

Nếu không thể sửa application.properties, dùng Java argument:

```bash
java -Djava.net.preferIPv4Stack=true -jar your-app.jar
```

Hoặc với Maven:
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.net.preferIPv4Stack=true"
```

---

## ✅ VERIFICATION

Sau khi restart server với config mới:

```powershell
# Check binding
netstat -ano | findstr :8080

# Should see:
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
```

Then test from phone!

---

**Status:** ⚠️ SERVER NEEDS RESTART WITH IPv4 BINDING
**Fix:** Add `server.address=0.0.0.0` to application.properties
**Then:** Restart server and test!
