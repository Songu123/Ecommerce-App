# ✅ HOÀN THÀNH: Authentication Flow - Login & Register với SharedPreferences

## 🎯 TỔNG QUAN

Đã thiết lập hoàn chỉnh luồng authentication cho app:
1. **SplashActivity** - Màn hình khởi động, kiểm tra đăng nhập
2. **AuthActivity** - Container cho Login & Register fragments
3. **LoginFragment** - Đăng nhập
4. **RegisterFragment** - Đăng ký
5. **MainActivityNew** - Màn hình chính sau khi đăng nhập
6. **ProfileFragment** - Hiển thị thông tin user và logout
7. **TokenManager** - Quản lý JWT token và user data trong SharedPreferences

---

## 📊 LUỒNG HOẠT ĐỘNG

```
┌─────────────────────────────────────────────────────────────────┐
│                      KHỞI ĐỘNG APP                              │
└─────────────────────────────────────────────────────────────────┘
                            │
                            ▼
                   ┌────────────────┐
                   │ SplashActivity │
                   └────────────────┘
                            │
                ┌───────────┴───────────┐
                │                       │
         ┌──────▼──────┐        ┌──────▼──────┐
         │  Đã đăng    │        │ Chưa đăng   │
         │   nhập?     │        │   nhập?     │
         └──────┬──────┘        └──────┬──────┘
                │ YES                   │ NO
                │                       │
                ▼                       ▼
      ┌──────────────────┐    ┌──────────────────┐
      │ MainActivityNew  │    │   AuthActivity   │
      │  (Home Screen)   │    │ (Login/Register) │
      └──────────────────┘    └──────────────────┘
                │                       │
                │                       ├─► LoginFragment
                │                       │
                │                       └─► RegisterFragment
                │                                │
                │                                │
                │◄───────────────────────────────┘
                │         (Login/Register Success)
                │
                ├─► HomeFragment
                ├─► ExploreFragment
                ├─► CartFragment
                ├─► OrdersFragment
                └─► ProfileFragment
                         │
                         ├─► View User Info
                         └─► Logout ───► AuthActivity
```

---

## 📁 FILES ĐÃ TẠO/CẬP NHẬT

### Mới tạo:
1. ✅ **SplashActivity.java** - Màn hình splash, kiểm tra auth
2. ✅ **activity_splash.xml** - Layout cho splash
3. ✅ **circular_background.xml** - Drawable cho profile icon
4. ✅ **button_primary.xml** - Drawable cho buttons

### Đã cập nhật:
5. ✅ **TokenManager.java** - Thêm methods lưu/đọc user info
6. ✅ **ProfileFragment.java** - Hiển thị user info và logout
7. ✅ **fragment_profile.xml** - UI cho profile với user info
8. ✅ **AndroidManifest.xml** - SplashActivity là LAUNCHER
9. ✅ **colors.xml** - Thêm colors: background, divider, error

### Đã có sẵn (không thay đổi):
- ✅ **AuthActivity.java** - Container cho Login/Register
- ✅ **LoginFragment.java** - Đăng nhập với API
- ✅ **RegisterFragment.java** - Đăng ký với API
- ✅ **AuthRepositoryImpl.java** - Tự động lưu token vào SharedPreferences
- ✅ **MainActivityNew.java** - Main screen với bottom navigation

---

## 🔐 SHARED PREFERENCES STRUCTURE

**Preference Name:** `ECommerceAuthPrefs`

**Keys được lưu:**
```java
jwt_token      → String   // JWT token từ server
is_logged_in   → Boolean  // Trạng thái đăng nhập
user_id        → Integer  // ID của user
username       → String   // Tên đăng nhập
email          → String   // Email
full_name      → String   // Họ tên
role           → String   // Vai trò (USER/ADMIN)
```

---

## 🚀 CÁCH HOẠT ĐỘNG

### 1️⃣ Khi vào app lần đầu:
```
SplashActivity khởi động
    ↓
TokenManager.isLoggedIn() = false
    ↓
Chuyển đến AuthActivity (Login screen)
```

### 2️⃣ Khi đăng nhập/đăng ký thành công:
```
User nhập thông tin → API call
    ↓
Server trả về AuthResponse (token + user info)
    ↓
AuthRepositoryImpl.saveAuthData() tự động lưu vào SharedPreferences:
  - jwt_token
  - user_id, username, email, full_name, role
  - is_logged_in = true
    ↓
Navigate to MainActivityNew
```

### 3️⃣ Khi mở app lần sau:
```
SplashActivity khởi động
    ↓
TokenManager.isLoggedIn() = true (đọc từ SharedPreferences)
    ↓
Chuyển thẳng đến MainActivityNew (không cần login lại)
```

### 4️⃣ Khi logout:
```
User click Logout trong ProfileFragment
    ↓
TokenManager.clearToken() xóa tất cả data trong SharedPreferences
    ↓
Chuyển về AuthActivity (Login screen)
```

---

## 💾 CODE EXAMPLES

### Kiểm tra đã đăng nhập:
```java
TokenManager tokenManager = new TokenManager(context);
if (tokenManager.isLoggedIn()) {
    // User đã đăng nhập
    String username = tokenManager.getUsername();
    String email = tokenManager.getEmail();
}
```

### Lưu auth data (tự động trong AuthRepositoryImpl):
```java
// Được gọi tự động sau khi login/register thành công
tokenManager.saveAuthData(
    token,      // JWT token
    userId,     // User ID
    username,   // Username
    email,      // Email
    fullName,   // Full name
    role        // Role
);
```

### Logout:
```java
TokenManager tokenManager = new TokenManager(context);
tokenManager.clearToken(); // Xóa tất cả auth data
// Sau đó chuyển về AuthActivity
```

### Lấy token để gọi API:
```java
TokenManager tokenManager = new TokenManager(context);
String authHeader = tokenManager.getAuthorizationHeader();
// Returns: "Bearer eyJhbGc..."
```

---

## 🎨 UI COMPONENTS

### SplashActivity:
- Logo app (shopping bag icon)
- App name
- Tagline: "Shop Everything You Need"
- Loading progress bar
- Background: Primary color

### ProfileFragment:
- Profile icon (circular background)
- Full name display
- Username display
- Email display
- Logout button (màu đỏ)
- Card design với dividers

---

## ⚙️ ANDROID MANIFEST CHANGES

```xml
<!-- SplashActivity là LAUNCHER (màn hình đầu tiên) -->
<activity
    android:name=".SplashActivity"
    android:exported="true"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<!-- AuthActivity (Login/Register) -->
<activity
    android:name=".AuthActivity"
    android:exported="true"
    android:windowSoftInputMode="adjustResize"/>

<!-- MainActivityNew (sau khi login) -->
<activity
    android:name=".MainActivityNew"
    android:exported="false"/>
```

---

## ✅ TESTING CHECKLIST

### Test Case 1: Lần đầu mở app
- [ ] App mở SplashActivity
- [ ] Sau 1.5s chuyển đến AuthActivity (LoginFragment)
- [ ] Có thể chuyển qua RegisterFragment
- [ ] Có thể chuyển lại LoginFragment

### Test Case 2: Đăng ký tài khoản mới
- [ ] Nhập thông tin: Full Name, Email, Password, Confirm Password
- [ ] Check điều khoản (required)
- [ ] Click "Đăng ký"
- [ ] Hiển thị loading
- [ ] Đăng ký thành công → Toast message
- [ ] Tự động lưu vào SharedPreferences
- [ ] Chuyển đến MainActivityNew

### Test Case 3: Đăng nhập
- [ ] Nhập Email và Password
- [ ] Click "Đăng nhập"
- [ ] Hiển thị loading
- [ ] Đăng nhập thành công → Toast message
- [ ] Tự động lưu vào SharedPreferences
- [ ] Chuyển đến MainActivityNew

### Test Case 4: Kiểm tra SharedPreferences
- [ ] Đóng app (force close)
- [ ] Mở lại app
- [ ] SplashActivity kiểm tra isLoggedIn()
- [ ] Tự động chuyển đến MainActivityNew (không cần login lại)

### Test Case 5: Xem thông tin Profile
- [ ] Click tab Profile trong bottom navigation
- [ ] Hiển thị thông tin: Full Name, Username, Email
- [ ] Thông tin khớp với data đã đăng ký/đăng nhập

### Test Case 6: Logout
- [ ] Trong ProfileFragment, click "Đăng xuất"
- [ ] Hiển thị dialog xác nhận
- [ ] Click "Đăng xuất"
- [ ] Toast message "Đã đăng xuất"
- [ ] SharedPreferences bị xóa
- [ ] Chuyển về AuthActivity (LoginFragment)
- [ ] Không thể back về MainActivityNew

### Test Case 7: Sau khi logout, mở lại app
- [ ] Đóng app
- [ ] Mở lại app
- [ ] SplashActivity kiểm tra isLoggedIn() = false
- [ ] Chuyển đến AuthActivity (cần login lại)

---

## 🔍 DEBUGGING

### Xem SharedPreferences data:
```bash
# Trên device/emulator đang chạy
adb shell
run-as com.son.e_commerce
cat shared_prefs/ECommerceAuthPrefs.xml
```

### Logcat tags:
```
SplashActivity      - Kiểm tra authentication
AuthRepositoryImpl  - Login/Register API calls
TokenManager        - Save/Load token operations
LoginFragment       - Login process
RegisterFragment    - Register process
ProfileFragment     - Logout operations
```

### Log khi login thành công:
```
D/AuthRepositoryImpl: login() successful - Token: eyJhbGc...
D/AuthRepositoryImpl: User saved - ID: 1, Username: john_doe
```

### Log khi mở app (đã login):
```
D/SplashActivity: User đã đăng nhập - Username: john_doe
```

### Log khi logout:
```
D/TokenManager: Clearing all auth data
```

---

## 📱 USER EXPERIENCE

### Lần đầu sử dụng app:
1. Mở app → Splash screen (1.5s)
2. Login screen hiển thị
3. Chuyển sang Register nếu chưa có tài khoản
4. Đăng ký xong → Tự động đăng nhập
5. Vào được app

### Lần sau mở app:
1. Mở app → Splash screen (1.5s)
2. **Tự động vào thẳng main screen** (không cần login)
3. Sử dụng app bình thường

### Khi muốn đổi tài khoản:
1. Vào Profile
2. Click Logout
3. Login bằng tài khoản khác

---

## 🎉 KẾT QUẢ

✅ **Authentication flow hoàn chỉnh:**
- Splash → Check auth → Login/Main
- Login/Register với JWT token
- Tự động lưu vào SharedPreferences
- Persistent login (không cần login lại mỗi lần mở app)
- Profile với user info
- Logout và clear session

✅ **Security:**
- JWT token được lưu an toàn trong SharedPreferences
- Token được gửi trong header cho các API calls
- Logout xóa hoàn toàn session

✅ **User Experience:**
- Chỉ cần login một lần
- Mở app nhanh (tự động vào main nếu đã login)
- Thông tin user luôn hiển thị trong Profile
- Logout dễ dàng

---

## 📞 API ENDPOINTS ĐƯỢC SỬ DỤNG

1. **POST /api/auth/login**
   - Body: `{username, password}`
   - Response: `{token, id, username, email, fullName, role}`

2. **POST /api/auth/register**
   - Body: `{username, email, password, fullName}`
   - Response: `{token, id, username, email, fullName, role}`

---

## 🔗 RELATED DOCUMENTS

- `JWT_AUTH_GUIDE.md` - Hướng dẫn JWT Authentication
- `AUTH_IMPLEMENTATION_COMPLETE.md` - Chi tiết Auth UI
- `REGISTER_API_GUIDE.md` - Hướng dẫn gọi API đăng ký

---

**Created:** February 3, 2026  
**Status:** ✅ COMPLETED AND READY TO USE
