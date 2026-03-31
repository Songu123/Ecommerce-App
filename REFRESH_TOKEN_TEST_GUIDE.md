# 🔐 HƯỚNG DẪN TEST REFRESH TOKEN - E-Commerce API

## 📝 TÓM TẮT

Hướng dẫn chi tiết cách test refresh token mechanism với Postman hoặc cURL, bao gồm:
- Đăng ký (Register)
- Đăng nhập & nhận Tokens (Login)  
- Làm mới Access Token (Refresh)
- Đăng xuất (Logout)

---

## 🚀 QUYTRÌNH TEST TỪNG BƯỚC

### Step 1️⃣: REGISTER (Đăng Ký)

**Endpoint**: `POST http://localhost:8080/api/auth/register`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "username": "user123",
  "password": "password123",
  "email": "user123@example.com",
  "fullName": "John Doe"
}
```

**Response Thành Công** (HTTP 201 Created):
```json
{
  "id": 1,
  "username": "user123",
  "email": "user123@example.com",
  "fullName": "John Doe",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

⚠️ **QUAN TRỌNG**: Backend cần trả về đầy đủ dữ liệu ở trên!

---

### Step 2️⃣: LOGIN (Đăng Nhập & Nhận Tokens)

**Endpoint**: `POST http://localhost:8080/api/auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "username": "user123@example.com",
  "password": "password123"
}
```

**Response Thành Công** (HTTP 200 OK):
```json
{
  "id": 1,
  "username": "user123",
  "email": "user123@example.com",
  "fullName": "John Doe",
  "role": "USER",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNzE0NTc1MjAwfQ.xxxx",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer"
}
```

🔑 **Lưu lại**: 
- `accessToken` - Dùng để gọi API (hạn 15 phút)
- `refreshToken` - Dùng để lấy access token mới (hạn 7 ngày)

---

### Step 3️⃣: REFRESH TOKEN (Lấy Access Token Mới)

Khi `accessToken` hết hạn (sau 15 phút), sử dụng `refreshToken` để lấy token mới **mà không cần login lại**.

**Endpoint**: `POST http://localhost:8080/api/auth/refresh`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)** - Paste `refreshToken` từ Step 2:
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response Thành Công** (HTTP 200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNzE0NTc1MzAwfQ.yyyy",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer"
}
```

✅ **Bạn đã có Access Token mới!** Tiếp tục sử dụng API bình thường.

---

### Step 4️⃣: SỬ DỤNG ACCESS TOKEN (Gọi Protected APIs)

Thêm header `Authorization` khi gọi API cần xác thực:

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Ví dụ - Gọi GET /api/products**:
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNzE0NTc1MzAwfQ.yyyy" \
  -H "Content-Type: application/json"
```

---

### Step 5️⃣: LOGOUT (Đăng Xuất)

**Endpoint**: `POST http://localhost:8080/api/auth/logout`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)** - Paste `refreshToken`:
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response Thành Công** (HTTP 200 OK):
```json
{
  "message": "Logged out successfully"
}
```

⚠️ **Refresh Token này sẽ không còn được sử dụng!** Phải login lại để lấy token mới.

---

## 📋 POSTMAN COLLECTION

### Cách Import vào Postman:

1. **Tạo New Collection**: "E-Commerce Auth"
2. **Tạo các requests theo thứ tự**:

#### Request 1: Register
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "user123",
  "password": "password123",
  "email": "user123@example.com",
  "fullName": "John Doe"
}
```

#### Request 2: Login
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "user123@example.com",
  "password": "password123"
}
```

**Trong Postman - Lưu tokens**:
- Sau khi Login, copy `accessToken` → Paste vào environment variable `{{accessToken}}`
- Copy `refreshToken` → Paste vào environment variable `{{refreshToken}}`

#### Request 3: Refresh Token
```
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "{{refreshToken}}"
}
```

**Response** → Copy `accessToken` mới → Update `{{accessToken}}`

#### Request 4: Test Protected API
```
GET http://localhost:8080/api/products
Authorization: Bearer {{accessToken}}
Content-Type: application/json
```

#### Request 5: Logout
```
POST http://localhost:8080/api/auth/logout
Content-Type: application/json

{
  "refreshToken": "{{refreshToken}}"
}
```

---

## 🧪 KỊCH BẢN TEST HOÀN CHỈNH

### Scenario 1: Toàn Bộ Flow (Register → Login → Refresh → Logout)

1. ✅ POST /api/auth/register
   - Input: username, password, email, fullName
   - Output: token (có thể sử dụng luôn)

2. ✅ POST /api/auth/login
   - Input: username/email, password
   - Output: accessToken (15 phút), refreshToken (7 ngày)

3. ✅ GET /api/products (với Authorization header)
   - Header: `Authorization: Bearer {accessToken}`
   - Output: Products list ✅

4. ⏳ Chờ access token hết hạn (hoặc modify token để simulate hết hạn)

5. ✅ POST /api/auth/refresh
   - Input: refreshToken
   - Output: accessToken mới, refreshToken mới (nếu có)

6. ✅ GET /api/products (với token mới)
   - Header: `Authorization: Bearer {newAccessToken}`
   - Output: Products list ✅

7. ✅ POST /api/auth/logout
   - Input: refreshToken
   - Output: "Logged out successfully"

8. ❌ POST /api/auth/refresh (với old refreshToken)
   - Output: Error "Refresh token not found" hoặc "Token expired" ✅

---

### Scenario 2: Token Expired Handling

1. Login → Nhận accessToken (15 phút)
2. Call API → Lỗi 401 "Token expired"
3. Call /api/auth/refresh → Nhận accessToken mới
4. Call API lại → Thành công ✅

---

## ❌ LỖI PHỔ BIẾN & GIẢI PHÁP

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-----------|---------|
| "Invalid email/password" | Email hoặc password sai | Kiểm tra lại thông tin |
| "Refresh token not found" | Refresh token không tồn tại/hết hạn | Logout & login lại |
| "Refresh token was expired" | Refresh token hết hạn (>7 ngày) | Login lại |
| "Connection refused" (Port 8080) | Server không chạy | `./gradlew bootRun` |
| 403 Forbidden | Spring Security chặn | Kiểm tra CORS/Security config |
| 401 Unauthorized | Access token hết hạn | Gọi /api/auth/refresh |

---

## 📊 TOKEN EXPIRATION TIMES

| Token | Duration | Sử Dụng |
|-------|----------|--------|
| Access Token | 15 phút (900 giây) | Gọi API |
| Refresh Token | 7 ngày (604800 giây) | Lấy access token mới |

---

## ✅ CHECKLIST IMPLEMENTATION

**Backend (Spring Boot)**:
- [ ] POST /api/auth/register - Tạo user + trả token
- [ ] POST /api/auth/login - Xác thực + trả tokens
- [ ] POST /api/auth/refresh - Làm mới access token
- [ ] POST /api/auth/logout - Revoke refresh token
- [ ] GET /api/auth/test - Test endpoint

**Android App (AuthRepositoryImpl)**:
- [ ] Login() - Lưu access + refresh tokens
- [ ] Register() - Lưu access + refresh tokens
- [ ] RefreshAccessToken() - Call refresh endpoint
- [ ] Logout() - Call logout endpoint + clear local
- [ ] JWT Interceptor - Auto attach access token to requests
- [ ] Auto Refresh - Tự động refresh token khi hết hạn

---

**Status**: ✅ Ready for Testing
**Test with**: Postman, cURL, Android App

---

**Hãy test từng step để đảm bảo toàn bộ flow hoạt động! 🚀**
