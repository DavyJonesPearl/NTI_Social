# Firebase Console Configuration

**Project ID:** afterlight-d8729  
**Package Name:** com.afterlight.app.debug  

---

## 🚨 IMMEDIATE FIX REQUIRED

### Issue
Registration fails with: `CONFIGURATION_NOT_FOUND` - reCAPTCHA error

### Root Cause
Email/Password authentication not enabled in Firebase Console

---

## ✅ Step 1: Enable Email/Password Sign-In

**URL:** https://console.firebase.google.com/project/afterlight-d8729/authentication/providers

**Actions:**
1. Click "Email/Password" in the list
2. Toggle **Enable** switch ON
3. Click **Save**

---

## ✅ Step 2: Add Debug SHA-256 (Bypass reCAPTCHA for Dev)

**URL:** https://console.firebase.google.com/project/afterlight-d8729/settings/general

**Your Debug Certificate:**
```
SHA-1: 5D:C7:92:46:BA:58:EC:68:FB:64:0A:48:73:C4:E2:F0:46:A8:43:55

SHA-256: 5A:4C:3B:53:C6:A8:B7:94:5C:99:D2:62:15:54:1F:12:35:B9:26:13:40:F4:A8:AB:14:F1:4B:79:F9:4B:6C:D9
```

**Actions:**
1. Scroll to "Your apps" section
2. Find your Android app (com.afterlight.app.debug)
3. Click "Add fingerprint"
4. Paste the SHA-256 value above
5. Click "Save"

---

## ✅ Step 3: Test Registration

After completing Steps 1 & 2:

1. **Force close the app** (swipe away from recents)
2. **Relaunch the app**
3. **Register a test account:**
   - Email: `test@afterlight.app`
   - Password: `Test123!@#`
   - DOB: Any date making you 13+

4. **Verify in Console:**
   - Go to: https://console.firebase.google.com/project/afterlight-d8729/authentication/users
   - You should see the new user listed

---

## 🔍 Verify Fix

Check logcat after registration attempt:

```bash
adb logcat -c  # Clear logs
# Register in app
adb logcat -d | grep -E "RecaptchaCallWrapper|CONFIGURATION_NOT_FOUND|FirebaseAuth"
```

**Success:** No reCAPTCHA errors  
**Failure:** Still seeing `CONFIGURATION_NOT_FOUND` → Double-check Console settings saved

---

## Alternative: Use Firebase Emulator (No Console Setup)

If you want to develop offline without Console configuration:

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Start emulators
cd /Users/developer/AndroidStudioProjects/NTISocial
firebase emulators:start
```

Then enable emulators in code:
```kotlin
// File: app/src/debug/java/com/afterlight/app/FirebaseDebugConfig.kt
private const val USE_EMULATORS = true  // Change to true
```

Emulator UI: http://localhost:4000

---

## Summary

✅ **Completed:**
- Firebase SDK integrated
- FirebaseAuthService implemented
- Error handling added
- Debug SHA-256 certificate identified

⏳ **Requires Firebase Console:**
- Enable Email/Password authentication
- Add SHA-256 fingerprint to bypass reCAPTCHA

⏳ **OR Use Emulator:**
- No Console configuration needed
- Works offline
- Perfect for development

---

**Quick Links:**
- [Enable Auth](https://console.firebase.google.com/project/afterlight-d8729/authentication/providers)
- [Add SHA Fingerprint](https://console.firebase.google.com/project/afterlight-d8729/settings/general)
- [View Users](https://console.firebase.google.com/project/afterlight-d8729/authentication/users)
