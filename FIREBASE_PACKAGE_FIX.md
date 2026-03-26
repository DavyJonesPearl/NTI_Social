# Phase F8 — Fix Firebase Package Name Mismatch

**Error:** `No matching client found for package name 'com.afterlight.app.debug'`

**Current Issue:** Your `google-services.json` has incorrect package name: `com.afterlight.app.AfterlightApplication`

---

## THE PROBLEM

Your app has **two package names**:
- Debug build: `com.afterlight.app.debug`
- Release build: `com.afterlight.app`

But your `google-services.json` has an invalid package name: `com.afterlight.app.AfterlightApplication`

---

## SOLUTION: Add Both Apps to Firebase Console

### Step 1: Add Debug App (com.afterlight.app.debug)

Firebase Console is now open. Follow these steps:

1. **Scroll down to "Your apps" section**

2. **Click "Add app" button** (or the Android icon if it's your first app)

3. **Enter Debug Package Name:**
   ```
   com.afterlight.app.debug
   ```

4. **Enter App Nickname (optional):**
   ```
   Afterlight Debug
   ```

5. **Add Debug SHA-256 Certificate:**
   ```
   5A:4C:3B:53:C6:A8:B7:94:5C:99:D2:62:15:54:1F:12:35:B9:26:13:40:F4:A8:AB:14:F1:4B:79:F9:4B:6C:D9
   ```

6. **Click "Register app"**

7. **Download the new `google-services.json`**

8. **Replace the file in your project:**
   ```bash
   # Backup old file
   mv app/google-services.json app/google-services.json.backup
   
   # Move downloaded file to app directory
   mv ~/Downloads/google-services.json app/
   ```

---

### Step 2: Add Release App (com.afterlight.app)

Repeat the process for release:

1. **Click "Add app" again**

2. **Enter Release Package Name:**
   ```
   com.afterlight.app
   ```

3. **Enter App Nickname:**
   ```
   Afterlight Release
   ```

4. **Add Release SHA-256 Certificate** (when you have a release keystore)

5. **Click "Register app"**

6. **Download and replace `google-services.json`** (it will now contain BOTH apps)

---

## ALTERNATIVE: Quick Fix (Debug Only)

If you only want to test debug builds for now:

### Option A: Modify Existing google-services.json

Replace the incorrect package name in `app/google-services.json`:

**Change this:**
```json
"package_name": "com.afterlight.app.AfterlightApplication"
```

**To this:**
```json
"package_name": "com.afterlight.app.debug"
```

**BUT:** This is NOT recommended - you should properly register the app in Firebase Console.

---

## VERIFICATION

After updating `google-services.json`:

### 1. Clean and Rebuild
```bash
./gradlew clean
./gradlew assembleDebug
```

### 2. Check the logs
```bash
adb logcat | grep -E "FirebaseApp|google-services"
```

You should see:
```
✅ Successfully initialized Firebase
✅ google-services.json matched package: com.afterlight.app.debug
```

### 3. Test Registration
Run the test script:
```bash
./test-firebase-auth.sh
```

---

## WHAT YOU NEED TO DO NOW

1. ✅ Firebase Console is open in your browser
2. ⏳ Add app with package name: `com.afterlight.app.debug`
3. ⏳ Add SHA-256: `5A:4C:3B:53:C6:A8:B7:94:5C:99:D2:62:15:54:1F:12:35:B9:26:13:40:F4:A8:AB:14:F1:4B:79:F9:4B:6C:D9`
4. ⏳ Download new `google-services.json`
5. ⏳ Replace file in `app/google-services.json`
6. ⏳ Rebuild app

**Once complete, the package mismatch error will be resolved!**

---

## Quick Commands

```bash
# After downloading google-services.json:
cd /Users/developer/AndroidStudioProjects/NTISocial
mv app/google-services.json app/google-services.json.backup
mv ~/Downloads/google-services.json app/
./gradlew clean assembleDebug
```
