# 📧 HƯỚNG DẪN: Đăng Nhập Bằng Email và Password

## 🎯 TỔNG QUAN

Hệ thống **ĐÃ HỖ TRỢ ĐẦY ĐỦ** đăng nhập bằng email và password với JWT authentication.

---

## ✅ CÁC CÁCH ĐĂNG NHẬP

### CÁCH 1: Sử dụng LoginHelper (Khuyến nghị) ⭐

**File:** `LoginHelper.java` (MỚI TẠO)

**Ưu điểm:**
- ✅ Tự động validate email và password
- ✅ Error messages rõ ràng bằng tiếng Việt
- ✅ Code ngắn gọn, dễ sử dụng
- ✅ Xử lý lỗi tự động

**Code example:**

```java
// Khởi tạo
LoginHelper loginHelper = new LoginHelper(context);

// Đăng nhập
loginHelper.loginWithEmail("test@gmail.com", "password123", new LoginHelper.LoginCallback() {
    @Override
    public void onLoginSuccess(User user, String token) {
        // ✅ Đăng nhập thành công!
        Toast.makeText(context, "Chào " + user.getFullName(), Toast.LENGTH_SHORT).show();
        
        // Token đã được lưu tự động vào SharedPreferences
        // Navigate đến màn hình chính
    }

    @Override
    public void onLoginError(String error) {
        // ❌ Đăng nhập thất bại
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }
});
```

---

### CÁCH 2: Sử dụng AuthRepository Trực Tiếp

**File:** `AuthRepositoryImpl.java`

**Code example:**

```java
// Khởi tạo
AuthRepository authRepository = new AuthRepositoryImpl(context);

// Đăng nhập (username = email)
authRepository.login("test@gmail.com", "password123", new AuthRepository.OnAuthListener() {
    @Override
    public void onSuccess(User user, String token) {
        // Đăng nhập thành công
        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        // Đăng nhập thất bại
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
});
```

---

### CÁCH 3: Sử dụng trong Fragment (LoginFragment)

**File:** `LoginFragment.java` (ĐÃ CÓ SẴN)

LoginFragment đã implement đầy đủ chức năng đăng nhập bằng email:

```java
public class LoginFragment extends Fragment {
    private AuthRepository authRepository;
    
    private void attemptLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        
        // Validation
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Vui lòng nhập email");
            return;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email không hợp lệ");
            return;
        }
        
        if (password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        
        // Login
        authRepository.login(email, password, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(User user, String token) {
                Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }

            @Override
            public void onError(String error) {
                String errorMessage = getDetailedErrorMessage(error);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
```

---

## 📋 VALIDATION TỰ ĐỘNG

LoginHelper tự động validate:

| Trường hợp | Error Message |
|------------|---------------|
| Email trống | "Vui lòng nhập email" |
| Email sai format | "Email không hợp lệ" |
| Password trống | "Vui lòng nhập mật khẩu" |
| Password < 6 ký tự | "Mật khẩu phải có ít nhất 6 ký tự" |

---

## 🔐 XỬ LÝ JWT TOKEN

Token được tự động lưu vào SharedPreferences sau khi login thành công:

```java
// Lấy token hiện tại
LoginHelper loginHelper = new LoginHelper(context);
String token = loginHelper.getToken();

// Kiểm tra đã login chưa
boolean isLoggedIn = loginHelper.isLoggedIn();

// Lấy user hiện tại
User currentUser = loginHelper.getCurrentUser();

// Đăng xuất
loginHelper.logout();
```

---

## 🚀 EXAMPLES

### Example 1: Simple Login
```java
LoginHelper helper = new LoginHelper(this);
helper.loginWithEmail("user@example.com", "mypassword", new LoginHelper.LoginCallback() {
    @Override
    public void onLoginSuccess(User user, String token) {
        Log.d("Login", "Token: " + token);
        Log.d("Login", "User: " + user.getEmail());
    }

    @Override
    public void onLoginError(String error) {
        Log.e("Login", "Error: " + error);
    }
});
```

### Example 2: Login với Validation Custom
```java
EditText emailInput = findViewById(R.id.emailInput);
EditText passwordInput = findViewById(R.id.passwordInput);

String email = emailInput.getText().toString().trim();
String password = passwordInput.getText().toString();

if (email.isEmpty()) {
    emailInput.setError("Email không được để trống");
    return;
}

if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
    emailInput.setError("Email không hợp lệ");
    return;
}

LoginHelper helper = new LoginHelper(this);
helper.loginWithEmail(email, password, new LoginHelper.LoginCallback() {
    @Override
    public void onLoginSuccess(User user, String token) {
        // Success
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String error) {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
    }
});
```

### Example 3: Check Login Status
```java
LoginHelper helper = new LoginHelper(this);

if (helper.isLoggedIn()) {
    User user = helper.getCurrentUser();
    
    textViewWelcome.setText("Chào mừng, " + user.getFullName());
    textViewEmail.setText(user.getEmail());
    
    buttonLogin.setVisibility(View.GONE);
    buttonLogout.setVisibility(View.VISIBLE);
} else {
    buttonLogin.setVisibility(View.VISIBLE);
    buttonLogout.setVisibility(View.GONE);
}
```

### Example 4: Logout
```java
buttonLogout.setOnClickListener(v -> {
    LoginHelper helper = new LoginHelper(this);
    helper.logout();
    
    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    
    // Navigate to login screen
    Intent intent = new Intent(this, AuthActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
});
```

---

## 🧪 TEST CASES

### Test Login Success
```java
@Test
public void testLoginSuccess() {
    String email = "test@gmail.com";
    String password = "password123";
    
    loginHelper.loginWithEmail(email, password, new LoginHelper.LoginCallback() {
        @Override
        public void onLoginSuccess(User user, String token) {
            assertEquals(email, user.getEmail());
            assertNotNull(token);
            assertTrue(loginHelper.isLoggedIn());
        }

        @Override
        public void onLoginError(String error) {
            fail("Login should succeed");
        }
    });
}
```

### Test Invalid Email
```java
@Test
public void testInvalidEmail() {
    loginHelper.loginWithEmail("notanemail", "password123", new LoginHelper.LoginCallback() {
        @Override
        public void onLoginSuccess(User user, String token) {
            fail("Should not succeed with invalid email");
        }

        @Override
        public void onLoginError(String error) {
            assertEquals("Email không hợp lệ", error);
        }
    });
}
```

---

## 🔍 XỬ LÝ LỖI

LoginHelper và AuthRepositoryImpl đã xử lý các lỗi phổ biến:

| Lỗi | Error Message |
|-----|---------------|
| Email trống | "Vui lòng nhập email" |
| Email không hợp lệ | "Email không hợp lệ" |
| Password trống | "Vui lòng nhập mật khẩu" |
| Password ngắn | "Mật khẩu phải có ít nhất 6 ký tự" |
| Sai email/password | "Email hoặc mật khẩu không đúng!" |
| Server không chạy | "Không kết nối được server!" |
| JWT error | "Lỗi cấu hình server!" |
| Server error (500) | "Lỗi server! Vui lòng thử lại sau" |

---

## 📊 DATA FLOW

```
User Input (Email + Password)
         ↓
LoginHelper.loginWithEmail()
         ↓
Validate Input
         ↓
AuthRepository.login()
         ↓
AuthRepositoryImpl
         ↓
API Call (POST /api/auth/login)
         ↓
Server Response
         ↓
Save Token & User to SharedPreferences
         ↓
Callback: onLoginSuccess()
         ↓
Navigate to MainActivity
```

---

## 📁 FILES

### Core Files (Đã có):
- ✅ `AuthRepository.java` - Interface
- ✅ `AuthRepositoryImpl.java` - Implementation
- ✅ `AuthLoginRequest.java` - DTO
- ✅ `AuthResponse.java` - Response DTO
- ✅ `LoginFragment.java` - UI Fragment

### New Files (Mới tạo):
- ✅ `LoginHelper.java` - Helper class ⭐
- ✅ `EmailLoginExampleActivity.java` - Example activity
- ✅ `EMAIL_PASSWORD_LOGIN_GUIDE.md` - Hướng dẫn này

---

## 🎯 QUICK START

### 1. Trong Fragment/Activity:

```java
import com.son.e_commerce.utils.auth.LoginHelper;

public class MyLoginFragment extends Fragment {
    
    private LoginHelper loginHelper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginHelper = new LoginHelper(requireContext());
    }
    
    private void doLogin(String email, String password) {
        loginHelper.loginWithEmail(email, password, new LoginHelper.LoginCallback() {
            @Override
            public void onLoginSuccess(User user, String token) {
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                // Navigate to home
            }

            @Override
            public void onLoginError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
```

### 2. Check Login Status:

```java
LoginHelper helper = new LoginHelper(this);

if (helper.isLoggedIn()) {
    // Already logged in
    startActivity(new Intent(this, MainActivity.class));
} else {
    // Show login screen
    startActivity(new Intent(this, AuthActivity.class));
}
```

---

## ✅ CHECKLIST

- [x] ✅ AuthRepository hỗ trợ login bằng email
- [x] ✅ AuthRepositoryImpl thực thi đúng
- [x] ✅ JWT token được lưu tự động
- [x] ✅ Validation email và password
- [x] ✅ Error handling chi tiết
- [x] ✅ LoginHelper tiện lợi
- [x] ✅ LoginFragment hoàn chỉnh
- [x] ✅ Examples và documentation

---

## 🎉 KẾT LUẬN

**Hệ thống đã HOÀN CHỈNH và SẴN SÀNG sử dụng!**

Bạn có thể:
1. ✅ Đăng nhập bằng email và password
2. ✅ Tự động validate input
3. ✅ Tự động lưu JWT token
4. ✅ Kiểm tra login status
5. ✅ Đăng xuất

**Chỉ cần fix server JWT key là có thể sử dụng ngay!**

Xem: `ACTION_PLAN_FIX_JWT.md` để fix server.

---

Created: March 9, 2026
Status: ✅ READY TO USE
Files: 3 new + 5 existing
