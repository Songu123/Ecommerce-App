# 🎨 HƯỚNG DẪN FIX LỖI JWT - CÓ HÌNH ẢNH

## 📱 LỖI HIỆN TẠI

```
┌─────────────────────────────────────────────────────────────┐
│  Android App                                                 │
│  ┌─────────────────┐                                        │
│  │ Login Screen    │                                        │
│  │                 │                                        │
│  │ Email: van@...  │  ──────────POST /login──────────►     │
│  │ Password: ***   │                                        │
│  │                 │                                        │
│  │  [Login Button] │                                        │
│  └─────────────────┘                                        │
│         │                                                    │
│         ▼                                                    │
│  ❌ HTTP 401 - Unauthorized                                 │
│  Error: JWT key too short                                   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  Spring Boot Server                                          │
│  ┌─────────────────┐                                        │
│  │ JwtUtils.java   │                                        │
│  │                 │                                        │
│  │ ❌ jwt.secret = "mySecretKey123"                         │
│  │    (Only 15 chars = 120 bits)                           │
│  │                                                          │
│  │ ❌ REQUIREMENT: >= 32 chars (256 bits)                   │
│  └─────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
```

---

## ✅ GIẢI PHÁP

```
┌─────────────────────────────────────────────────────────────┐
│  STEP 1: Generate JWT Secret Key                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ PowerShell                                          │   │
│  │ PS> .\generate_jwt_key.ps1                          │   │
│  │                                                     │   │
│  │ ✅ Generated Key:                                   │   │
│  │ 3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ...       │   │
│  │                                                     │   │
│  │ Length: 44 characters (352 bits) ✓                 │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  STEP 2: Update application.properties                      │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ File: src/main/resources/application.properties    │   │
│  │                                                     │   │
│  │ BEFORE (❌):                                        │   │
│  │ jwt.secret=mySecretKey123                           │   │
│  │                                                     │   │
│  │ AFTER (✅):                                         │   │
│  │ jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU...   │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  STEP 3: Restart Spring Boot                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Terminal                                            │   │
│  │ $ ./mvnw spring-boot:run                            │   │
│  │                                                     │   │
│  │ ...                                                 │   │
│  │ ✅ Started Application in 3.456 seconds             │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  STEP 4: Test from Android App                              │
│  ┌─────────────────┐                                        │
│  │ Register Screen │                                        │
│  │                 │                                        │
│  │ Email: test@... │  ──────POST /register──────►          │
│  │ Password: ***   │                                        │
│  │                 │                                        │
│  │  [Register]     │  ◄─────HTTP 200 + Token────────       │
│  └─────────────────┘                                        │
│         │                                                    │
│         ▼                                                    │
│  ✅ SUCCESS! Navigate to Home                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔍 FLOW DIAGRAM

```
┌──────────────┐
│ Android App  │
└──────┬───────┘
       │ 1. POST /api/auth/login
       │    {username, password}
       ▼
┌────────────────────────┐
│  Spring Boot Server    │
│  ┌──────────────────┐  │
│  │ AuthController   │  │
│  └────────┬─────────┘  │
│           │             │
│           ▼             │
│  ┌──────────────────┐  │
│  │ JwtUtils         │  │
│  │                  │  │
│  │ ❌ OLD:          │  │
│  │ key too short    │  │
│  │                  │  │
│  │ ✅ NEW:          │  │
│  │ key >= 256 bits  │  │
│  └────────┬─────────┘  │
│           │             │
│           ▼             │
│  ┌──────────────────┐  │
│  │ Generate Token   │  │
│  └────────┬─────────┘  │
└───────────┼────────────┘
            │ 2. Return JWT Token
            ▼
┌──────────────────────┐
│ Android App          │
│ ✅ Save to           │
│ SharedPreferences    │
└──────────────────────┘
```

---

## 📋 COMPARISON TABLE

| Aspect | ❌ BEFORE (Error) | ✅ AFTER (Fixed) |
|--------|------------------|------------------|
| **JWT Secret** | `mySecretKey123` | `3K9mNpQ7rS...` (44 chars) |
| **Key Length** | 15 characters | 44 characters |
| **Bits** | 120 bits | 352 bits |
| **HS256 Compliant** | ❌ NO (< 256 bits) | ✅ YES (>= 256 bits) |
| **Login Status** | ❌ HTTP 401 Error | ✅ HTTP 200 Success |
| **Token Generated** | ❌ NO | ✅ YES |
| **App Working** | ❌ NO | ✅ YES |

---

## 🎯 FILE LOCATIONS

```
Spring Boot Project/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/yourcompany/
│       │       ├── controller/
│       │       │   └── AuthController.java
│       │       ├── security/
│       │       │   └── JwtUtils.java
│       │       └── config/
│       │           └── SecurityConfig.java
│       └── resources/
│           └── application.properties  ◄── EDIT THIS FILE!
│               jwt.secret=...          ◄── CHANGE THIS LINE!
└── target/
```

---

## 🚀 COMMAND CHEATSHEET

### Generate JWT Key
```powershell
# Windows PowerShell
.\generate_jwt_key.ps1

# Or manual:
$bytes = New-Object byte[] 32; (New-Object Random).NextBytes($bytes); [Convert]::ToBase64String($bytes)
```

### Update Config
```bash
# Open file
code src/main/resources/application.properties

# Or with vim
vim src/main/resources/application.properties
```

### Restart Server
```bash
# Maven
./mvnw spring-boot:run

# Gradle
./gradlew bootRun

# JAR file
java -jar target/your-app.jar
```

### Test API
```bash
# Test endpoint
curl http://localhost:8080/api/auth/test

# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"pass123","fullName":"Test User"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test@test.com","password":"pass123"}'
```

---

## 🎨 ANDROID ERROR MESSAGES

### Before Fix:
```
┌────────────────────────────────────────┐
│  ❌ Login Failed                       │
│                                        │
│  Error: The signing key's size is     │
│  32 bits which is not secure enough   │
│  for the HS256 algorithm...           │
│                                        │
│  [ OK ]                                │
└────────────────────────────────────────┘
```

### After Android App Updates:
```
┌────────────────────────────────────────┐
│  ⚠️ Lỗi cấu hình server!               │
│                                        │
│  Server JWT secret key không đúng.    │
│  Vui lòng liên hệ admin để fix        │
│  server.                               │
│                                        │
│  [ OK ]                                │
└────────────────────────────────────────┘
```

### After Server Fix:
```
┌────────────────────────────────────────┐
│  ✅ Đăng nhập thành công!              │
│                                        │
│  Chuyển đến trang chủ...              │
└────────────────────────────────────────┘
```

---

## 🔧 TROUBLESHOOTING VISUAL

```
Problem: Can't connect to server
┌─────────────────────────────────────┐
│  Check 1: Server Running?           │
│  $ ps aux | grep spring-boot        │
│  ✅ Running  or  ❌ Not found        │
└─────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────┐
│  Check 2: Port 8080 Open?           │
│  $ netstat -ano | findstr :8080     │
│  ✅ LISTENING  or  ❌ Nothing        │
└─────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────┐
│  Check 3: Firewall Blocking?        │
│  Windows Firewall settings          │
│  ✅ Allowed  or  ❌ Blocked          │
└─────────────────────────────────────┘
```

---

## ✅ SUCCESS INDICATORS

After fix is complete, you should see:

```
✅ Spring Boot Log:
   Started Application in 3.456 seconds
   Tomcat started on port(s): 8080

✅ Test API Response:
   GET /api/auth/test
   → HTTP 200 OK
   → "Auth API is working!"

✅ Register Response:
   POST /api/auth/register
   → HTTP 200 OK
   → {"token":"eyJhbGciOiJIUzI1NiIs...","id":1,...}

✅ Login Response:
   POST /api/auth/login
   → HTTP 200 OK
   → {"token":"eyJhbGciOiJIUzI1NiIs...","id":1,...}

✅ Android App:
   → Register successful!
   → Login successful!
   → Navigate to MainActivity
   → Token saved in SharedPreferences
```

---

**Quick Links:**
- Main Guide: `FIX_LOI_JWT_TOM_TAT.md`
- Urgent Fix: `JWT_SERVER_FIX_URGENT.md`
- Key Generator: `generate_jwt_key.ps1`
- Key Guide: `GENERATE_JWT_KEY.md`
