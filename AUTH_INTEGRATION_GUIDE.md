# 🔗 Integration Guide - Add Auth Check to App

## Tích hợp Authentication vào App

### 1. Thêm Auth Check vào MainActivityNew

Mở file `MainActivityNew.java` và thêm check user login:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // ⭐ ADD THIS: Check if user is logged in
    checkUserLogin();
    
    setContentView(R.layout.activity_main_new);
    // ... rest of code
}

private void checkUserLogin() {
    UserRepository userRepository = new UserRepositoryImpl(this);
    User currentUser = userRepository.getCurrentUser();
    
    if (currentUser == null) {
        // User not logged in, redirect to auth
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
```

---

### 2. Thêm Logout vào ProfileFragment

Trong `ProfileFragment.java`:

```java
private void setupLogoutButton() {
    Button buttonLogout = view.findViewById(R.id.buttonLogout);
    buttonLogout.setOnClickListener(v -> {
        new AlertDialog.Builder(requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc muốn đăng xuất?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> {
                logout();
            })
            .setNegativeButton("Hủy", null)
            .show();
    });
}

private void logout() {
    UserRepository userRepository = new UserRepositoryImpl(requireContext());
    userRepository.logout();
    
    Intent intent = new Intent(requireContext(), AuthActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    requireActivity().finish();
}
```

---

### 3. Update ProfileFragment để hiển thị user info

```java
@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    // Load user info
    UserRepository userRepository = new UserRepositoryImpl(requireContext());
    User currentUser = userRepository.getCurrentUser();
    
    if (currentUser != null) {
        TextView textViewName = view.findViewById(R.id.textViewUserName);
        TextView textViewEmail = view.findViewById(R.id.textViewUserEmail);
        
        textViewName.setText(currentUser.getFullName());
        textViewEmail.setText(currentUser.getEmail());
    }
}
```

---

### 4. Thêm "Login Required" cho Cart và Orders

Trong `CartFragment.java`:

```java
@Override
public void onResume() {
    super.onResume();
    
    User currentUser = userRepository.getCurrentUser();
    if (currentUser == null) {
        // Redirect to login
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
        return;
    }
    
    loadCart();
}
```

---

### 5. Optional: Cho phép browse products không cần login

Chỉ yêu cầu login khi:
- Add to cart
- View cart
- Place order
- View profile

```java
// In ProductDetailActivity
buttonAddToCart.setOnClickListener(v -> {
    User currentUser = userRepository.getCurrentUser();
    if (currentUser == null) {
        // Show login required dialog
        new AlertDialog.Builder(this)
            .setTitle("Đăng nhập")
            .setMessage("Vui lòng đăng nhập để thêm vào giỏ hàng")
            .setPositiveButton("Đăng nhập", (dialog, which) -> {
                Intent intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
            })
            .setNegativeButton("Hủy", null)
            .show();
        return;
    }
    
    // Proceed with add to cart
    presenter.onAddToCartClick(currentQuantity);
});
```

---

## 🎯 Recommended Flow

### Option 1: Force Login (Recommended for E-Commerce)
```
App Launch → Check Login → 
    ❌ Not logged in → AuthActivity
    ✅ Logged in → MainActivityNew
```

### Option 2: Guest Browsing (More flexible)
```
App Launch → MainActivityNew (allow browse)
    
When user tries to:
    - Add to cart → Check login → Show AuthActivity if needed
    - View cart → Check login → Show AuthActivity if needed
    - Checkout → Check login → Show AuthActivity if needed
```

---

## 📝 Implementation Steps

### Step 1: Update MainActivityNew.java
```java
// Add import
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;

// Add method
private void checkUserLogin() {
    UserRepository userRepository = new UserRepositoryImpl(this);
    User currentUser = userRepository.getCurrentUser();
    
    if (currentUser == null) {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}

// Call in onCreate
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    checkUserLogin(); // ⭐ ADD THIS LINE
    setContentView(R.layout.activity_main_new);
    // ... rest
}
```

### Step 2: Add Logout Button to Profile Layout

Mở `fragment_profile.xml` hoặc layout profile của bạn:

```xml
<Button
    android:id="@+id/buttonLogout"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:text="Đăng xuất"
    android:textColor="#F44336"
    android:background="@drawable/bg_button_outline"
    android:layout_margin="16dp" />
```

### Step 3: Implement in ProfileFragment
(Code như trên)

---

## ✅ Testing Checklist

- [ ] App launch → Login required
- [ ] Login success → Navigate to Home
- [ ] Close app and reopen → Auto logged in
- [ ] Logout → Back to login screen
- [ ] Cannot access cart without login
- [ ] Cannot add to cart without login
- [ ] User info displayed in profile
- [ ] Back button from home → Exit app (not back to login)

---

## 🔒 Security Notes

1. **Never store plain password** - UserRepository handles this
2. **Session expires** - Implement token refresh if needed
3. **Secure API calls** - Use HTTPS in production
4. **Validate on backend** - Don't trust client-side validation

---

## 📚 Complete Example

### MainActivityNew.java (Full example)

```java
package com.son.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;

public class MainActivityNew extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check user login first
        if (!isUserLoggedIn()) {
            redirectToAuth();
            return;
        }
        
        setContentView(R.layout.activity_main_new);
        
        // Rest of your initialization code
        initViews();
        setupBottomNavigation();
        loadDefaultFragment();
    }
    
    private boolean isUserLoggedIn() {
        UserRepository userRepository = new UserRepositoryImpl(this);
        return userRepository.getCurrentUser() != null;
    }
    
    private void redirectToAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
    
    // ... rest of your code
}
```

---

**✅ After integration, your app will have complete authentication flow!**
