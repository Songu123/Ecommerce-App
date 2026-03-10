# 🎨 Authentication UI - Login & Register Fragments

## ✅ Hoàn thành

Đã thiết kế 2 Fragment (Login và Register) trong 1 Activity (AuthActivity) theo design modern.

### 📁 Files đã tạo

#### Layouts
- ✅ `activity_auth.xml` - Container activity với back button và title
- ✅ `fragment_login.xml` - Login form với header gradient, email, password, social login
- ✅ `fragment_register.xml` - Register form với full name, email, password, confirm password, terms checkbox

#### Drawables
- ✅ `bg_auth_header.xml` - Gradient background cho header
- ✅ `bg_input_field.xml` - Background cho input fields
- ✅ `bg_button_primary.xml` - Gradient background cho buttons
- ✅ `bg_social_button.xml` - Background cho social login buttons
- ✅ `ic_email.xml` - Email icon
- ✅ `ic_lock.xml` - Lock/password icon
- ✅ `ic_person.xml` - Person icon
- ✅ `ic_visibility.xml` - Eye icon for password toggle
- ✅ `ic_shopping_bag_large.xml` - Large shopping bag icon cho header
- ✅ `ic_back.xml` - Back arrow icon

#### Java Files
- ✅ `AuthActivity.java` - Main container activity
- ✅ `LoginFragment.java` - Login fragment với validation
- ✅ `RegisterFragment.java` - Register fragment với validation

---

## 🎨 Design Features

### LoginFragment
1. **Header với gradient background** - Shopping bag icon
2. **Welcome message** - "Chào mừng quay trở lại"
3. **Email input** - Với icon và hint
4. **Password input** - Với icon và show/hide toggle
5. **Forgot password link** - Blue clickable text
6. **Login button** - Gradient blue button
7. **Social login buttons** - Google, Facebook, Apple
8. **Register link** - "Chưa có tài khoản? Đăng ký ngay"

### RegisterFragment
1. **Title** - "Đăng ký Tài khoản"
2. **Subtitle** - Motivational text
3. **Full Name input** - Với person icon
4. **Email input** - Với email icon và example hint
5. **Password input** - Với lock icon và show/hide toggle
6. **Confirm Password** - Với lock icon và show/hide toggle
7. **Terms checkbox** - "Tôi đồng ý với Điều khoản & Điều kiện"
8. **Register button** - Gradient blue button
9. **Login link** - "Bạn đã có tài khoản? Đăng nhập ngay"

---

## 🔧 Features

### Validation
#### LoginFragment
- ✅ Empty email check
- ✅ Valid email format
- ✅ Empty password check
- ✅ Minimum password length (6 chars)

#### RegisterFragment
- ✅ Empty full name check
- ✅ Minimum name length (3 chars)
- ✅ Empty email check
- ✅ Valid email format
- ✅ Empty password check
- ✅ Minimum password length (6 chars)
- ✅ Confirm password match
- ✅ Terms checkbox validation

### Loading States
- ✅ ProgressBar visibility
- ✅ Disable all inputs during loading
- ✅ Disable buttons during loading

### Navigation
- ✅ Switch between Login and Register
- ✅ Navigate to Home after success
- ✅ Back button handling
- ✅ Clear back stack after login

---

## 🚀 Cách sử dụng

### Từ bất kỳ Activity nào:

```java
// Navigate to Login
Intent intent = new Intent(this, AuthActivity.class);
startActivity(intent);

// Navigate to Register directly
Intent intent = new Intent(this, AuthActivity.class);
intent.putExtra("action", "register");
startActivity(intent);
```

### Trong MainActivity hoặc HomeFragment:

```java
// Check if user is logged in
User currentUser = userRepository.getCurrentUser();
if (currentUser == null) {
    // Not logged in, redirect to auth
    Intent intent = new Intent(this, AuthActivity.class);
    startActivity(intent);
    finish();
} else {
    // User logged in, show content
    textViewWelcome.setText("Xin chào, " + currentUser.getFullName());
}
```

---

## 📱 UI/UX Features

### Material Design
- ✅ Material TextInputLayout với hint animation
- ✅ Password toggle eye icon
- ✅ Smooth transitions between fragments
- ✅ Elevation on buttons
- ✅ Ripple effects

### Color Scheme
- Primary: Blue gradient (#2196F3 → #1976D2)
- Header: Blue gradient (#2196F3 → #64B5F6)
- Text: Dark gray (#212121)
- Hint: Light gray (#9E9E9E)
- Background: Off-white (#FAFAFA)
- Input bg: Light gray (#F5F5F5)

### Typography
- Title: 28sp, Bold
- Subtitle: 14sp, Regular
- Input: 15sp
- Button: 16sp, Bold
- Links: 14sp, Bold, Blue

---

## 🔄 Fragment Communication

### AuthActivity methods:
```java
public void showLoginFragment()    // Switch to login
public void showRegisterFragment()  // Switch to register
```

### Called from fragments:
```java
// In LoginFragment
((AuthActivity) getActivity()).showRegisterFragment();

// In RegisterFragment
((AuthActivity) getActivity()).showLoginFragment();
```

---

## 🏗️ Build Instructions

### 1. Sync Gradle
```bash
cd D:\ECommerce
.\gradlew build
```

### 2. Clean and Rebuild
Trong Android Studio:
- Build → Clean Project
- Build → Rebuild Project

### 3. Run
- Click Run button
- Hoặc: `.\gradlew installDebug`

---

## 🎯 Testing Checklist

### LoginFragment
- [ ] Open AuthActivity
- [ ] See login form
- [ ] Test email validation (empty, invalid format)
- [ ] Test password validation (empty, too short)
- [ ] Click "Quên mật khẩu?" → See toast
- [ ] Click "Đăng nhập" with valid data → API call
- [ ] Click "Đăng ký ngay" → Switch to register
- [ ] See loading indicator during API call
- [ ] After success → Navigate to Home

### RegisterFragment
- [ ] Click "Đăng ký ngay" from login
- [ ] See register form
- [ ] Test full name validation
- [ ] Test email validation
- [ ] Test password validation
- [ ] Test confirm password match
- [ ] Test terms checkbox
- [ ] Click "Đăng ký" with valid data → API call
- [ ] Click "Đăng nhập ngay" → Switch to login
- [ ] After success → Navigate to Home

### Navigation
- [ ] Back button from login → Exit app
- [ ] Back button from register → Go to login
- [ ] Switch between fragments → Smooth animation
- [ ] After login → Cannot go back to auth screen

---

## 🔐 Security Notes

1. **Password Handling:**
   - Passwords are sent via HTTPS (configure in production)
   - No plain text storage
   - Backend handles hashing

2. **Session Management:**
   - User info stored in SharedPreferences
   - Auto-login on app restart
   - Logout clears all data

3. **API Communication:**
   - Uses Retrofit with HTTPS (in production)
   - Error handling for network issues
   - Timeout configuration

---

## 📝 Next Steps

### Optional Enhancements:
1. **Social Login Integration:**
   - Google Sign-In
   - Facebook Login
   - Apple Sign-In

2. **Forgot Password:**
   - Email verification
   - Reset password flow
   - OTP verification

3. **Email Verification:**
   - Send verification email
   - Verify before login
   - Resend verification

4. **Biometric Auth:**
   - Fingerprint
   - Face ID
   - Save credentials

5. **Remember Me:**
   - Checkbox to stay logged in
   - Auto-fill credentials
   - Secure storage

---

## 🐛 Known Issues

1. **R.layout references:** 
   - Need to sync Gradle to generate R class
   - Solution: Build → Clean Project → Rebuild

2. **onBackPressed deprecated:**
   - Will migrate to OnBackPressedDispatcher
   - Current implementation works but shows warning

3. **Social login buttons:**
   - Currently placeholders with toast
   - Need to integrate actual SDKs

---

## 📚 Related Files

- `UserRepository.java` - Handles API calls
- `UserRepositoryImpl.java` - Implementation with SharedPreferences
- `LoginRequest.java` - DTO for login
- `RegisterRequest.java` - DTO for register
- `User.java` - User entity
- `UserApiService.java` - Retrofit API interface

---

**✨ UI/UX thiết kế theo modern design với Material Design principles!**

**Created:** February 3, 2026
**Status:** ✅ Complete (pending Gradle sync)
