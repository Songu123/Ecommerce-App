# ⚡ START HERE - E-Commerce Android App

## 🎯 Bạn là developer mới? Bắt đầu từ đây!

---

## 📋 Checklist 5 phút

### ✅ Step 1: Cài đặt môi trường
- [ ] Android Studio installed
- [ ] JDK 11+ installed
- [ ] Spring Boot backend ready
- [ ] Git installed (optional)

### ✅ Step 2: Mở project
```bash
# Clone or open existing project
cd D:\ECommerce

# Open in Android Studio
# File -> Open -> Select D:\ECommerce folder
```

### ✅ Step 3: Cấu hình API
**Mở file:** `app/src/main/java/com/son/e_commerce/utils/network/RetrofitClient.java`

**Thay đổi dòng 14:**
```java
// Cho Android Emulator (mặc định)
private static final String BASE_URL = "http://10.0.2.2:8080/";

// Hoặc cho thiết bị thật (thay XXX bằng IP máy của bạn)
// private static final String BASE_URL = "http://192.168.1.XXX:8080/";
```

### ✅ Step 4: Start backend
```bash
# Trong thư mục Spring Boot project
cd your-spring-boot-project
./mvnw spring-boot:run

# Wait for: "Started Application in X.XXX seconds"
```

### ✅ Step 5: Build & Run
```
1. Click "Sync Project with Gradle Files" (🐘 icon)
2. Wait for sync to complete
3. Click "Run" button (▶️) or press Shift+F10
4. Select emulator or device
5. Wait for app to install and launch
```

---

## 📚 Documentation theo mức độ

### 🟢 Beginner (Bắt đầu đây!)
1. **README.md** ← Đọc đầu tiên
2. **QUICK_REFERENCE.md** ← Copy-paste code examples
3. **NAVIGATION_GUIDE.md** ← Hiểu navigation

### 🟡 Intermediate (Sau khi hiểu cơ bản)
1. **MVP_ARCHITECTURE.md** ← Hiểu kiến trúc
2. **PROJECT_STRUCTURE.md** ← Biết file nằm ở đâu
3. **API_INTEGRATION_GUIDE.md** ← Chi tiết API

### 🔴 Advanced (Cho developer có kinh nghiệm)
1. **API_INTEGRATION_COMPLETED.md** ← Technical details
2. **COMPLETION_SUMMARY.md** ← Full summary

---

## 🔥 Quick Examples

### Example 1: Hiển thị danh sách sản phẩm
```java
// In MainActivity.java
ProductRepository productRepo = new ProductRepositoryImpl();

productRepo.getAllProducts(new ProductRepository.OnProductsLoadedListener() {
    @Override
    public void onSuccess(List<Product> products) {
        // TODO: Create ProductAdapter and set to RecyclerView
        Log.d("MainActivity", "Loaded " + products.size() + " products");
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }
});
```

### Example 2: Login user
```java
// In LoginActivity.java (bạn cần tạo)
UserRepository userRepo = new UserRepositoryImpl(this);

String username = editTextUsername.getText().toString();
String password = editTextPassword.getText().toString();

userRepo.login(username, password, new UserRepository.OnLoginListener() {
    @Override
    public void onSuccess(User user) {
        Toast.makeText(LoginActivity.this, "Welcome " + user.getFullName(), 
            Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
    
    @Override
    public void onError(String error) {
        Toast.makeText(LoginActivity.this, "Login failed: " + error, 
            Toast.LENGTH_SHORT).show();
    }
});
```

---

## 🐛 Common Issues & Solutions

### ❌ Issue 1: Cannot connect to server
```
Error: java.net.ConnectException: Failed to connect

✅ Solution:
1. Check Spring Boot is running: http://localhost:8080/api/products
2. For emulator: BASE_URL = "http://10.0.2.2:8080/"
3. For real device: Use your PC's IP (find with: ipconfig)
4. Check Windows Firewall allows port 8080
```

### ❌ Issue 2: Build error - Unresolved reference
```
Error: Cannot resolve symbol 'R'

✅ Solution:
1. File -> Invalidate Caches -> Invalidate and Restart
2. Build -> Clean Project
3. Build -> Rebuild Project
4. Check res/ folder has no errors
```

### ❌ Issue 3: App crashes on launch
```
✅ Solution:
1. Check Logcat for error message
2. Verify AndroidManifest.xml has INTERNET permission
3. Check BASE_URL is correct
4. Verify Spring Boot server is running
```

### ❌ Issue 4: Gradle sync failed
```
✅ Solution:
1. Check internet connection
2. File -> Settings -> Build Tools -> Gradle
3. Select "Gradle JDK": Use JDK 11 or higher
4. Try sync again
```

---

## 📱 Test Your Setup

### Test 1: Backend is running
```bash
# Open browser or use curl
curl http://localhost:8080/api/products

# Should return JSON array of products
```

### Test 2: Build success
```bash
cd D:\ECommerce
.\gradlew assembleDebug

# Should see: BUILD SUCCESSFUL
```

### Test 3: App installs
```bash
.\gradlew installDebug

# Should see app icon on device/emulator
```

---

## 🎯 What to do next?

### ✅ Phase 1: Understanding (Day 1)
- [ ] Read README.md
- [ ] Explore project structure
- [ ] Run the app
- [ ] Test bottom navigation
- [ ] Check Logcat for API calls

### ✅ Phase 2: Learning (Day 2-3)
- [ ] Read MVP_ARCHITECTURE.md
- [ ] Understand Repository pattern
- [ ] Study one Activity (MainActivity)
- [ ] Study one Presenter (MainPresenter)
- [ ] Study one Repository (ProductRepositoryImpl)

### ✅ Phase 3: Coding (Week 1)
- [ ] Create ProductAdapter (RecyclerView)
- [ ] Create CategoryAdapter
- [ ] Integrate adapters into MainActivity
- [ ] Add image loading with Glide
- [ ] Test with real backend data

### ✅ Phase 4: Advanced (Week 2+)
- [ ] Create ProductDetailActivity
- [ ] Create CartActivity
- [ ] Create LoginActivity
- [ ] Implement search
- [ ] Add more features

---

## 💡 Pro Tips

### 🔍 Debugging
1. **Logcat filter:** `OkHttp` - See all API requests/responses
2. **Logcat filter:** `MainActivity` - See your logs
3. Add `Log.d("TAG", "message")` everywhere for debugging

### 🚀 Productivity
1. Use **Ctrl+Click** to jump to definition
2. Use **Ctrl+B** to find usages
3. Use **Alt+Enter** for quick fixes
4. Use **Ctrl+Space** for auto-complete

### 📖 Learning Path
```
1. Start with MainActivity → understand View
2. Then MainPresenter → understand Presenter
3. Then ProductRepositoryImpl → understand Model
4. Then ProductApiService → understand API
5. Connect the dots → understand MVP flow
```

---

## 🆘 Need Help?

### Documentation
1. **Check README.md** - Overview
2. **Check QUICK_REFERENCE.md** - Code examples
3. **Check API_INTEGRATION_GUIDE.md** - API details

### Code Examples
- Look in `presenter/` folder for business logic examples
- Look in `data/` folder for API call examples
- Look in `view/` folder for UI examples

### Debugging
```java
// Add this to see what's happening:
Log.d("DEBUG", "Method called");
Log.d("DEBUG", "Value: " + variable);
Log.d("DEBUG", "List size: " + list.size());
```

---

## ✅ Success Indicators

### You're on the right track if:
- ✅ App builds without errors
- ✅ App installs on device/emulator
- ✅ Bottom navigation works
- ✅ You can see API logs in Logcat
- ✅ No crash on launch

### You might need help if:
- ❌ Cannot build project
- ❌ App crashes immediately
- ❌ No API calls in Logcat
- ❌ Cannot connect to backend

---

## 🎓 Learning Resources

### Android Official
- [Android Developer Guide](https://developer.android.com/guide)
- [Material Design](https://material.io/develop/android)
- [RecyclerView Guide](https://developer.android.com/guide/topics/ui/layout/recyclerview)

### Libraries
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Gson Guide](https://github.com/google/gson)

### Patterns
- [MVP Pattern Guide](https://medium.com/android-news/android-mvp-pattern-6b7f90c62fc7)
- [Repository Pattern](https://developer.android.com/topic/architecture/data-layer)

---

## 🚦 Status Check

Before asking for help, check:

```
✅ Spring Boot running? → http://localhost:8080/api/products
✅ BASE_URL correct? → Check RetrofitClient.java
✅ Internet permission? → Check AndroidManifest.xml
✅ Gradle synced? → Click sync button
✅ No compile errors? → Check Problems tab
✅ Logcat filter set? → Filter: "OkHttp"
```

---

## 🎉 You're Ready!

Nếu bạn đã:
- ✅ Đọc file này
- ✅ Cấu hình BASE_URL
- ✅ Start Spring Boot
- ✅ Build thành công
- ✅ App chạy được

**→ Bạn đã sẵn sàng để code! 🚀**

Bắt đầu với **QUICK_REFERENCE.md** để xem code examples!

---

**Happy Coding! 💻**

*Remember: Every expert was once a beginner!*
