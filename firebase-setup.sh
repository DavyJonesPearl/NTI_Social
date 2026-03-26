#!/bin/bash

# Firebase Setup Script for Afterlight
# This script helps configure Firebase services for the Afterlight app

set -e

PROJECT_ID="afterlight-d8729"
REGION="us-central1"

echo "🔥 Firebase Setup for Afterlight"
echo "=================================="
echo ""
echo "Project ID: $PROJECT_ID"
echo "Region: $REGION"
echo ""

# Check if Firebase CLI is installed
if ! command -v firebase &> /dev/null; then
    echo "❌ Firebase CLI not found"
    echo ""
    echo "Install Firebase CLI:"
    echo "  npm install -g firebase-tools"
    echo ""
    exit 1
fi

echo "✅ Firebase CLI installed: $(firebase --version)"
echo ""

# Check if user is logged in
if ! firebase projects:list &> /dev/null; then
    echo "🔐 Please login to Firebase:"
    firebase login
    echo ""
fi

# Select project
echo "📋 Selecting Firebase project..."
firebase use "$PROJECT_ID" || {
    echo "❌ Failed to select project $PROJECT_ID"
    echo ""
    echo "Make sure the project exists in Firebase Console:"
    echo "  https://console.firebase.google.com/"
    exit 1
}

echo "✅ Project selected: $PROJECT_ID"
echo ""

# Display next steps
echo "📝 Next Steps:"
echo ""
echo "1. Enable Firebase Services in Console:"
echo "   https://console.firebase.google.com/project/$PROJECT_ID"
echo ""
echo "   Required services:"
echo "   ✓ Authentication (Email/Password)"
echo "   ✓ Cloud Firestore"
echo "   ✓ Cloud Storage"
echo ""
echo "2. Start Firebase Emulators for local testing:"
echo "   firebase emulators:start"
echo ""
echo "   Emulator UI: http://localhost:4000"
echo ""
echo "3. Deploy Security Rules to production:"
echo "   firebase deploy --only firestore:rules,storage:rules"
echo ""
echo "4. Update app to use emulators:"
echo "   Edit: app/src/debug/java/com/afterlight/app/FirebaseDebugConfig.kt"
echo "   Set: USE_EMULATORS = true"
echo ""

# Offer to start emulators
read -p "Do you want to start Firebase Emulators now? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "🚀 Starting Firebase Emulators..."
    echo ""
    firebase emulators:start
fi
