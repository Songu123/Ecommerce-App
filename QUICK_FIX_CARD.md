# 🚀 QUICK FIX CARD - JWT ERROR

## ⚡ 1-MINUTE SUMMARY

**Problem:** Login returns HTTP 401 - JWT key too short

**Solution:** Update jwt.secret in Spring Boot

**Time:** 5 minutes

---

## 📝 COPY THIS KEY

```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

---

## 🔧 3 SIMPLE STEPS

### 1️⃣ EDIT FILE
```
Open: src/main/resources/application.properties
Find:  jwt.secret=...
```

### 2️⃣ PASTE KEY
```properties
jwt.secret=tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

### 3️⃣ RESTART
```bash
./mvnw spring-boot:run
```

---

## ✅ VERIFY

Test from browser:
```
http://localhost:8080/api/auth/test
→ Should return: HTTP 200 OK
```

Test from Android app:
```
Register → Should work ✅
Login → Should work ✅
```

---

## 📚 MORE HELP?

Read detailed guides:
- `ACTION_PLAN_FIX_JWT.md` - Step-by-step
- `FIX_LOI_JWT_TOM_TAT.md` - Complete guide
- `VISUAL_FIX_GUIDE.md` - Visual diagrams

---

## 🆘 STILL STUCK?

1. Check server is running
2. Check port 8080 is open
3. Check firewall settings
4. See troubleshooting in `FIX_LOI_JWT_TOM_TAT.md`

---

**Generated Key (44 chars, 256-bit):**
```
tTyE0NF2jwO9y6Y7QGwumABXM2RBEu5tVEXqyyLNMbY=
```

**Estimated Fix Time:** ⏱️ 5 minutes
**Difficulty:** ⭐ Easy
**Priority:** 🚨 Urgent

---

✅ **Android app is WORKING** - No changes needed
❌ **Server needs UPDATE** - Change 1 line in config

**DO IT NOW! →** Copy key → Paste → Restart → Done! 🎉
