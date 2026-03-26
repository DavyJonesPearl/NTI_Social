#!/bin/bash

# Firebase Configuration Test Script
# Run this after enabling Email/Password auth in Console

set -e

PROJECT_ID="afterlight-d8729"
PACKAGE_NAME="com.afterlight.app.debug"

echo ""
echo "🔥 Firebase Configuration Test"
echo "=============================="
echo ""
echo "Project: $PROJECT_ID"
echo "Package: $PACKAGE_NAME"
echo ""

# Step 1: Check if app is installed
echo "📱 Step 1: Checking if app is installed..."
if adb shell pm list packages | grep -q "$PACKAGE_NAME"; then
    echo "   ✅ App is installed"
else
    echo "   ❌ App not found - installing..."
    ./gradlew installDebug
fi
echo ""

# Step 2: Clear app data for fresh test
echo "🧹 Step 2: Clearing app data..."
adb shell pm clear "$PACKAGE_NAME" 2>/dev/null || echo "   (App data cleared)"
echo "   ✅ App data cleared"
echo ""

# Step 3: Clear logcat
echo "📋 Step 3: Clearing logcat..."
adb logcat -c
echo "   ✅ Logcat cleared"
echo ""

# Step 4: Launch app
echo "🚀 Step 4: Launching app..."
adb shell am start -S -n "$PACKAGE_NAME/com.afterlight.app.MainActivity"
sleep 3
echo "   ✅ App launched"
echo ""

# Step 5: Check Firebase initialization
echo "🔍 Step 5: Checking Firebase initialization..."
FIREBASE_LOGS=$(adb logcat -d | grep -E "(Firebase|🚀|🔧)" | tail -5)
if [ -z "$FIREBASE_LOGS" ]; then
    echo "   ⚠️ No Firebase logs found yet"
else
    echo "   ✅ Firebase initialized:"
    echo "$FIREBASE_LOGS" | sed 's/^/      /'
fi
echo ""

# Step 6: Instructions for manual testing
echo "📝 Step 6: Manual Registration Test"
echo "===================================="
echo ""
echo "Now test registration in the app:"
echo ""
echo "   1. Tap 'Register' button"
echo "   2. Enter test credentials:"
echo "      Email:    test@afterlight.app"
echo "      Password: Test123!@#"
echo "      DOB:      Any date (13+ years ago)"
echo "   3. Tap 'Create Account'"
echo ""
echo "⏳ Waiting 15 seconds for you to register..."
echo ""

# Wait for user to register
for i in {15..1}; do
    echo -ne "   Checking in $i seconds...\r"
    sleep 1
done
echo ""
echo ""

# Step 7: Check for registration errors
echo "🔍 Step 7: Checking registration logs..."
echo ""

RECAPTCHA_ERRORS=$(adb logcat -d | grep -c "CONFIGURATION_NOT_FOUND" || echo "0")
AUTH_ERRORS=$(adb logcat -d | grep -E "FirebaseAuth.*error|Registration failed" || echo "")
SUCCESS_LOGS=$(adb logcat -d | grep -E "Creating user|getIdToken|success" | tail -3)

if [ "$RECAPTCHA_ERRORS" -gt 0 ]; then
    echo "   ❌ CONFIGURATION_NOT_FOUND errors found: $RECAPTCHA_ERRORS"
    echo ""
    echo "   This means Email/Password auth is NOT enabled in Console!"
    echo ""
    echo "   🔗 Fix it here:"
    echo "   https://console.firebase.google.com/project/$PROJECT_ID/authentication/providers"
    echo ""
    echo "   Steps:"
    echo "   1. Click 'Email/Password'"
    echo "   2. Toggle 'Enable' to ON"
    echo "   3. Click 'Save'"
    echo "   4. Run this script again"
    echo ""
elif [ -n "$AUTH_ERRORS" ]; then
    echo "   ⚠️ Authentication errors found:"
    echo "$AUTH_ERRORS" | sed 's/^/      /'
    echo ""
else
    echo "   ✅ No reCAPTCHA errors detected!"
    echo ""
    if [ -n "$SUCCESS_LOGS" ]; then
        echo "   ✅ Success indicators found:"
        echo "$SUCCESS_LOGS" | sed 's/^/      /'
        echo ""
        echo "   🎉 Registration likely succeeded!"
        echo ""
        echo "   Verify in Firebase Console:"
        echo "   https://console.firebase.google.com/project/$PROJECT_ID/authentication/users"
        echo ""
    else
        echo "   ℹ️ Check if registration succeeded in the app UI"
        echo ""
    fi
fi

# Step 8: Show recent logcat for manual inspection
echo "📋 Step 8: Recent app logs (last 20 lines):"
echo "==========================================="
adb logcat -d | grep -E "$PACKAGE_NAME|FirebaseAuth|RecaptchaCallWrapper" | tail -20 | sed 's/^/   /'
echo ""

echo ""
echo "🏁 Test Complete"
echo "================"
echo ""
echo "If you see CONFIGURATION_NOT_FOUND errors above:"
echo "  → Enable Email/Password in Firebase Console"
echo ""
echo "If you see NO errors:"
echo "  → Check Firebase Console Users tab for new account"
echo "  → https://console.firebase.google.com/project/$PROJECT_ID/authentication/users"
echo ""
