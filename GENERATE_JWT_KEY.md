# 🔑 JWT Secret Key Generator

## Generate New JWT Secret Key (256-bit)

```powershell
# Windows PowerShell - Run this command:
$bytes = New-Object byte[] 32
(New-Object Random).NextBytes($bytes)
$key = [Convert]::ToBase64String($bytes)
Write-Host "Your new JWT Secret Key:"
Write-Host $key
Write-Host "`nLength: $($key.Length) characters"
Write-Host "`nCopy this to your application.properties:"
Write-Host "jwt.secret=$key"
```

## Quick Fix - Copy and Paste này vào Spring Boot

```properties
# application.properties
jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT
```

hoặc

```properties
# application.properties  
jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH1234567890SecureKey
```

## ✅ Valid JWT Secret Keys (Pick one):

```properties
# Option 1: Random Base64 (Recommended)
jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT

# Option 2: Alphanumeric
jwt.secret=MyVerySecureJWTSecretKey123456789ABCDEFGHIJKLMNOP

# Option 3: Long string
jwt.secret=ThisIsAVeryLongAndSecureSecretKeyForJWTTokenGeneration2024

# Option 4: Mixed
jwt.secret=JWT_s3cR3t_K3y_2024_V3ry_S3cUr3_L0ng_K3y_F0r_Pr0ducT10n
```

## Steps to Fix:

1. **Stop Spring Boot server** (Ctrl+C)

2. **Open** `src/main/resources/application.properties`

3. **Replace** the line:
   ```properties
   jwt.secret=mySecretKey
   ```
   
   **With:**
   ```properties
   jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT
   ```

4. **Save** the file

5. **Restart** Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Wait** for server to start completely

7. **Test** from Android app again

---

**Note:** Key PHẢI có ít nhất 32 ký tự (256 bits) để đáp ứng chuẩn JWT HS256!
