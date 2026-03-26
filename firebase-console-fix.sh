#!/bin/bash

# Quick Firebase Console Setup Guide
# Fix reCAPTCHA and Email/Password authentication issues

echo "🔥 Firebase Console Setup Instructions"
echo "======================================"
echo ""
echo "Project: afterlight-d8729"
echo ""

cat << 'EOF'

## ISSUE: reCAPTCHA CONFIGURATION_NOT_FOUND Error

This error occurs because Email/Password authentication or reCAPTCHA
enforcement is not properly configured in Firebase Console.

## FIX 1: Enable Email/Password Authentication

1. Open Firebase Console:
   https://console.firebase.google.com/project/afterlight-d8729/authentication/providers

2. Click on "Email/Password" provider

3. Enable the toggle switch for "Email/Password"

4. (Optional) Enable "Email link (passwordless sign-in)"

5. Click "Save"


## FIX 2: Disable reCAPTCHA Enforcement (Development)

Firebase now enforces reCAPTCHA by default for security. For development,
you can disable it:

1. Open Authentication Settings:
   https://console.firebase.google.com/project/afterlight-d8729/authentication/settings

2. Scroll to "App Verification" section

3. Under "Play Integrity", click "Manage"

4. For Development:
   - Option A: Add your debug signing certificate SHA-256
   - Option B: Disable enforcement temporarily

To get your debug SHA-256:
   cd ~/.android/
   keytool -list -v -keystore debug.keystore -alias androiddebugkey -storepass android -keypass android


## FIX 3: Enable App Check (Production - Optional)

For production security, enable App Check instead:

1. Go to App Check:
   https://console.firebase.google.com/project/afterlight-d8729/appcheck

2. Click "Register" for your Android app

3. Choose "Play Integrity" as provider

4. Enable enforcement for:
   - Authentication
   - Firestore
   - Cloud Storage

5. Add debug token for testing:
   adb logcat | grep DebugAppCheckProvider


## ALTERNATIVE: Use Firebase Emulator (No Console Setup)

For offline development without Console configuration:

1. Install Firebase CLI:
   npm install -g firebase-tools

2. Start emulators:
   firebase emulators:start

3. Update app code:
   Edit: app/src/debug/java/com/afterlight/app/FirebaseDebugConfig.kt
   Set: USE_EMULATORS = true

4. Restart app

Emulator bypasses all reCAPTCHA and App Check requirements!


## Quick Test After Fixing

1. Restart the app (kill and relaunch)

2. Try registering with:
   Email: test@example.com
   Password: Test123!@#

3. Check Firebase Console → Authentication → Users
   Should see the new user listed

4. Check logcat - should NOT see reCAPTCHA errors:
   adb logcat | grep -E "RecaptchaCallWrapper|CONFIGURATION_NOT_FOUND"


## Current Status

✅ Firebase SDK integrated in app
✅ FirebaseAuthService implemented
✅ Error handling improved
⚠️ Email/Password provider needs to be enabled in Console
⚠️ reCAPTCHA causing registration failures

EOF

echo ""
echo "📋 Next Steps:"
echo "1. Open Firebase Console Authentication"
echo "2. Enable Email/Password sign-in method"
echo "3. Restart the app and test registration"
echo ""
echo "🔗 Quick Link:"
echo "https://console.firebase.google.com/project/afterlight-d8729/authentication/providers"
echo ""
