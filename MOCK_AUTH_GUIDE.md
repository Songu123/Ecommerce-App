# 🎭 MockAuthRepository - Test App Ngay Không Cần Server!

## 🎯 MỤC ĐÍCH

**MockAuthRepository** cho phép bạn:
- ✅ Test app UI ngay lập tức
- ✅ Không cần Spring Boot server
- ✅ Không cần fix server config
- ✅ Login/Register work ngay
- ✅ Token vẫn được lưu persistent

**Khi nào dùng:**
- 🔧 Đang fix server
- 🧪 Test UI
- 🚀 Demo app
- ⏰ Không có thời gian setup server

---

## 🚀 CÁCH SỬ DỤNG (2 PHÚT)

### Bước 1: Mở LoginFragment.java

**File:** `app/src/main/java/com/son/e_commerce/view/fragment/LoginFragment.java`

### Bước 2: Tìm dòng này:

```java
private void setupRepository() {
    authRepository = new AuthRepositoryImpl(requireContext());
}
```

### Bước 3: Thay bằng:

```java
private void setupRepository() {
    // Dùng MOCK để test UI (không cần server)
    authRepository = new MockAuthRepository(requireContext());
    
    // Uncomment dòng dưới khi server đã sẵn sàng:
    // authRepository = new AuthRepositoryImpl(requireContext());
}
```

### Bước 4: Làm tương tự với RegisterFragment.java

**File:** `app/src/main/java/com/son/e_commerce/view/fragment/RegisterFragment.java`

```java
private void setupRepository() {
    // Dùng MOCK để test UI
    authRepository = new MockAuthRepository(requireContext());
}
```

### Bước 5: Rebuild & Install

```bash
cd D:\ECommerce
.\gradlew installDebug
```

### Bước 6: Test App!

1. Mở app
2. Register bất kỳ email nào → ✅ WORKS!
3. Login với email đó → ✅ WORKS!
4. Navigate to MainActivity → ✅ WORKS!

---

## 👥 TEST ACCOUNTS (Pre-registered)

### Account 1:
```
Email: test@test.com
Password: password123
Name: Test User
```

### Account 2:
```
Email: admin@test.com
Password: admin123
Name: Admin User
```

**Bạn có thể login ngay với accounts này!**

---

## ✨ FEATURES

### ✅ Register
- Tạo user mới
- Auto generate ID
- Save to mock database (in-memory)
- Generate mock JWT token
- Save to SharedPreferences
- Navigate to home

### ✅ Login  
- Check email exists
- Verify password
- Generate mock JWT token
- Save to SharedPreferences
- Navigate to home

### ✅ Logout
- Clear session
- Clear SharedPreferences

### ✅ Persistent Login
- Token saved across app restarts
- getCurrentUser() works
- isLoggedIn() works

---

## 🔄 CHUYỂN ĐỔI GIỮA MOCK VÀ REAL API

### Dùng Mock (Không cần server):
```java
authRepository = new MockAuthRepository(requireContext());
```

### Dùng Real API (Khi server ready):
```java
authRepository = new AuthRepositoryImpl(requireContext());
```

**Chỉ cần thay 1 dòng code!**

---

## 🧪 TEST SCENARIOS

### Scenario 1: Register User Mới
```
1. Click "Đăng ký ngay"
2. Email: newuser@gmail.com
3. Password: password123
4. Full Name: New User
5. Submit
→ ✅ SUCCESS! Navigate to MainActivity
```

### Scenario 2: Login với Pre-registered Account
```
1. Email: test@test.com
2. Password: password123
3. Submit
→ ✅ SUCCESS! Navigate to MainActivity
```

### Scenario 3: Wrong Password
```
1. Email: test@test.com
2. Password: wrongpassword
3. Submit
→ ❌ "Email hoặc mật khẩu không đúng"
```

### Scenario 4: Duplicate Email
```
1. Register với email: test@test.com (đã tồn tại)
→ ❌ "Email đã được đăng ký"
```

### Scenario 5: Persistent Login
```
1. Login thành công
2. Close app
3. Open app again
→ ✅ Vẫn đăng nhập (navigate to MainActivity)
```

---

## 💾 DATA PERSISTENCE

Mock repository vẫn lưu data vào SharedPreferences:
- ✅ JWT token
- ✅ User ID
- ✅ Email
- ✅ Full Name
- ✅ Login status

**Giống hệt Real API!**

---

## 🔍 DEBUG

Check Logcat để thấy:
```
D/MockAuthRepository: 🎭 MOCK register() called with email: test@test.com
D/MockAuthRepository: ✅ Mock register successful for: test@test.com
D/MockAuthRepository: 💾 Saving mock auth data
D/MockAuthRepository: ✅ Mock user saved - ID: 1, Email: test@test.com
```

Emoji 🎭 cho biết đang dùng MOCK mode!

---

## ⚠️ LIMITATIONS

MockAuthRepository KHÔNG:
- ❌ Gọi real API
- ❌ Test network errors
- ❌ Verify JWT signature
- ❌ Connect to database
- ❌ Sync data với server

**Chỉ dùng để:**
- ✅ Test UI flow
- ✅ Test navigation
- ✅ Demo app
- ✅ Develop UI

---

## 🔄 KHI NÀO CHUYỂN VỀ REAL API?

Khi server đã sẵn sàng với:
1. ✅ Spring Security config (permitAll)
2. ✅ JWT secret key (>= 256 bits)
3. ✅ server.address=0.0.0.0
4. ✅ Firewall allowed

**Thì chỉ cần:**
```java
// LoginFragment.java và RegisterFragment.java
authRepository = new AuthRepositoryImpl(requireContext());
```

Rebuild và done!

---

## 📋 QUICK REFERENCE

### Enable Mock:
```java
// In LoginFragment.java and RegisterFragment.java
authRepository = new MockAuthRepository(requireContext());
```

### Test Accounts:
```
test@test.com / password123
admin@test.com / admin123
```

### Check Mode:
```
Logcat: Look for 🎭 emoji = Mock mode
        Look for 🔐 or normal logs = Real API mode
```

---

## ✅ CHECKLIST

### To Use Mock:
- [ ] ⏳ Open LoginFragment.java
- [ ] ⏳ Change to MockAuthRepository
- [ ] ⏳ Open RegisterFragment.java  
- [ ] ⏳ Change to MockAuthRepository
- [ ] ⏳ Rebuild: `.\gradlew installDebug`
- [ ] ⏳ Test app
- [ ] ⏳ ✅ Everything works!

### When Server Ready:
- [ ] Change back to AuthRepositoryImpl
- [ ] Rebuild
- [ ] Test with real API

---

**Created:** March 9, 2026
**Purpose:** Test app without server
**Status:** ✅ READY TO USE
**Time to implement:** 2 minutes
