# Firebase Backend Configuration for Afterlight

**Project ID:** `afterlight-d8729`  
**Status:** ✅ `google-services.json` configured  
**Date:** 16 February 2026

---

## Phase B1 — Firebase Services Configuration

### Current Status

✅ Firebase project created (`afterlight-d8729`)  
✅ `google-services.json` added to `app/` module  
✅ Firebase dependencies added to Gradle  
✅ `FirebaseAuthService` implemented in `data-remote`  
⏳ Firebase services need to be enabled in console

---

## B1.1 — Enable Firebase Services

### 1. Firebase Authentication

**Console:** https://console.firebase.google.com/project/afterlight-d8729/authentication/providers

**Actions Required:**
```bash
# 1. Navigate to Authentication → Sign-in methods
# 2. Enable Email/Password authentication
#    - Click "Email/Password"
#    - Toggle "Enable" switch
#    - Save

# 3. (Optional) Enable Google Sign-in
#    - Click "Google"
#    - Toggle "Enable"
#    - Add support email
#    - Save
```

**Test Command:**
```bash
# After enabling, test with Firebase Auth Emulator
firebase emulators:start --only auth
```

---

### 2. Cloud Firestore Database

**Console:** https://console.firebase.google.com/project/afterlight-d8729/firestore

**Actions Required:**
```bash
# 1. Go to Firestore Database → Create database
# 2. Select "Start in test mode" (temporary - will secure in B1.3)
# 3. Choose region:
#    - Recommended: us-central1 (Iowa)
#    - Alternative: asia-southeast1 (Singapore) - closer to your location
# 4. Click "Enable"
```

**Initial Security Rules (Test Mode - 30 days):**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // WARNING: These rules allow anyone to read/write
    // Replace with proper rules in B1.3
    match /{document=**} {
      allow read, write: if request.time < timestamp.date(2026, 3, 18);
    }
  }
}
```

**Data Structure:**
```
/users/{userId}
  - email: string
  - displayName: string
  - createdAt: timestamp

/parties/{partyId}
  - name: string
  - createdBy: userId
  - expiresAt: timestamp
  - isDeleted: boolean
  - members: array<userId>
  - createdAt: timestamp

/media/{mediaId}
  - partyId: string
  - userId: string
  - encryptedPath: string (Storage path)
  - createdAt: timestamp
  - isDeleted: boolean
```

---

### 3. Cloud Storage for Firebase

**Console:** https://console.firebase.google.com/project/afterlight-d8729/storage

**Actions Required:**
```bash
# 1. Go to Storage → Get started
# 2. Start in test mode (will secure in B1.3)
# 3. Choose same region as Firestore (us-central1)
# 4. Click "Done"
```

**Bucket Structure:**
```
gs://afterlight-d8729.appspot.com/
  /encrypted-media/
    /{partyId}/
      /{mediaId}.enc  (encrypted image files)
  /thumbnails/
    /{partyId}/
      /{mediaId}_thumb.jpg  (encrypted thumbnails)
```

**Initial Storage Rules (Test Mode):**
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // WARNING: Public read/write for testing only
    // Replace with proper rules in B1.3
    match /{allPaths=**} {
      allow read, write: if request.time < timestamp.date(2026, 3, 18);
    }
  }
}
```

---

### 4. Firebase Cloud Functions (Optional)

**Console:** https://console.firebase.google.com/project/afterlight-d8729/functions

**Actions Required:**
```bash
# 1. Go to Functions → Get started
# 2. Upgrade to Blaze plan (pay-as-you-go)
#    - Required for Cloud Functions
#    - Free tier: 2M invocations/month
# 3. Enable Cloud Build API
# 4. Install Firebase CLI locally:

npm install -g firebase-tools
firebase login
firebase init functions
```

**Planned Functions:**
- `cleanupExpiredParties`: Scheduled function to delete expired parties
- `deleteEncryptedMedia`: Triggered on party deletion
- `generateThumbnails`: On media upload, create thumbnails

---

## B1.2 — Firebase Emulator Setup (Local Development)

### Installation

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase in project
cd /Users/developer/AndroidStudioProjects/NTISocial
firebase init

# Select services:
# ✅ Firestore
# ✅ Functions
# ✅ Storage
# ✅ Emulators

# Select emulators:
# ✅ Authentication Emulator
# ✅ Firestore Emulator
# ✅ Storage Emulator
```

### firebase.json Configuration

```json
{
  "firestore": {
    "rules": "firestore.rules",
    "indexes": "firestore.indexes.json"
  },
  "storage": {
    "rules": "storage.rules"
  },
  "emulators": {
    "auth": {
      "port": 9099,
      "host": "localhost"
    },
    "firestore": {
      "port": 8080,
      "host": "localhost"
    },
    "storage": {
      "port": 9199,
      "host": "localhost"
    },
    "ui": {
      "enabled": true,
      "port": 4000
    }
  }
}
```

### Start Emulators

```bash
# Start all emulators
firebase emulators:start

# Emulator UI will be available at:
# http://localhost:4000

# Use emulators in app by setting:
# FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
# FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080)
# FirebaseStorage.getInstance().useEmulator("10.0.2.2", 9199)
```

---

## B1.3 — Production Security Rules

### Firestore Security Rules (Production)

**File:** `firestore.rules`

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Helper functions
    function isAuthenticated() {
      return request.auth != null;
    }
    
    function isOwner(userId) {
      return isAuthenticated() && request.auth.uid == userId;
    }
    
    function isPartyMember(partyId) {
      return isAuthenticated() && 
        request.auth.uid in get(/databases/$(database)/documents/parties/$(partyId)).data.members;
    }
    
    // Users collection
    match /users/{userId} {
      allow read: if isAuthenticated();
      allow create: if isOwner(userId);
      allow update, delete: if isOwner(userId);
    }
    
    // Parties collection
    match /parties/{partyId} {
      allow read: if isPartyMember(partyId);
      allow create: if isAuthenticated();
      allow update: if isPartyMember(partyId);
      allow delete: if isOwner(resource.data.createdBy);
    }
    
    // Media collection
    match /media/{mediaId} {
      allow read: if isAuthenticated() && 
        isPartyMember(resource.data.partyId);
      allow create: if isAuthenticated();
      allow delete: if isOwner(resource.data.userId);
    }
  }
}
```

### Storage Security Rules (Production)

**File:** `storage.rules`

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    
    function isAuthenticated() {
      return request.auth != null;
    }
    
    function isPartyMember(partyId) {
      return isAuthenticated() && 
        request.auth.uid in firestore.get(/databases/(default)/documents/parties/$(partyId)).data.members;
    }
    
    // Encrypted media files
    match /encrypted-media/{partyId}/{mediaId} {
      allow read: if isPartyMember(partyId);
      allow write: if isAuthenticated() && isPartyMember(partyId);
      allow delete: if isAuthenticated() && isPartyMember(partyId);
    }
    
    // Thumbnails (same rules as media)
    match /thumbnails/{partyId}/{mediaId} {
      allow read: if isPartyMember(partyId);
      allow write: if isAuthenticated() && isPartyMember(partyId);
      allow delete: if isAuthenticated() && isPartyMember(partyId);
    }
  }
}
```

### Deploy Security Rules

```bash
# Deploy Firestore rules
firebase deploy --only firestore:rules

# Deploy Storage rules
firebase deploy --only storage:rules

# Deploy all rules
firebase deploy --only firestore,storage
```

---

## B1.4 — Update Android App for Emulators (Development)

### Enable Firebase Emulators in Debug Build

**File:** `app/src/debug/java/com/afterlight/app/FirebaseConfig.kt`

```kotlin
package com.afterlight.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseConfig {
    
    private const val USE_EMULATORS = true // Toggle for emulator mode
    private const val EMULATOR_HOST = "10.0.2.2" // Android emulator host
    
    fun configureForDebug() {
        if (USE_EMULATORS) {
            FirebaseAuth.getInstance().useEmulator(EMULATOR_HOST, 9099)
            FirebaseFirestore.getInstance().useEmulator(EMULATOR_HOST, 8080)
            FirebaseStorage.getInstance().useEmulator(EMULATOR_HOST, 9199)
            
            // Enable offline persistence for Firestore
            FirebaseFirestore.getInstance().firestoreSettings = 
                firestoreSettings {
                    isPersistenceEnabled = true
                }
        }
    }
}
```

**Call in Application.onCreate():**

```kotlin
class AfterlightApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            FirebaseConfig.configureForDebug()
        }
    }
}
```

---

## B1.5 — Firebase Configuration Summary

### Project Details

| Setting | Value |
|---------|-------|
| Project ID | `afterlight-d8729` |
| Region | `us-central1` (or `asia-southeast1`) |
| Package Name | `com.afterlight.app.debug` |
| Storage Bucket | `afterlight-d8729.appspot.com` |

### Enabled Services

- ✅ **Firebase Authentication** (Email/Password)
- ✅ **Cloud Firestore** (NoSQL database)
- ✅ **Cloud Storage** (Encrypted media files)
- ⏳ **Cloud Functions** (Scheduled cleanup - optional)

### Emulator Ports

| Service | Port | URL |
|---------|------|-----|
| Auth | 9099 | http://localhost:9099 |
| Firestore | 8080 | http://localhost:8080 |
| Storage | 9199 | http://localhost:9199 |
| Emulator UI | 4000 | http://localhost:4000 |

### Security Status

- **Development:** Test mode rules (30-day expiry)
- **Production:** Secure rules (deploy with `firebase deploy --only firestore,storage`)

---

## Next Steps

1. **Enable Services in Console:**
   ```bash
   open https://console.firebase.google.com/project/afterlight-d8729
   ```

2. **Install Firebase CLI:**
   ```bash
   npm install -g firebase-tools
   firebase login
   ```

3. **Initialize Firebase locally:**
   ```bash
   cd /Users/developer/AndroidStudioProjects/NTISocial
   firebase init
   ```

4. **Start Emulators:**
   ```bash
   firebase emulators:start
   ```

5. **Test Registration:**
   - Run app
   - Register with email/password
   - Check Emulator UI (http://localhost:4000)

6. **Deploy Production Rules:**
   ```bash
   firebase deploy --only firestore:rules,storage:rules
   ```

---

## Troubleshooting

### Issue: "Error: Failed to get Firebase project"

**Solution:**
```bash
firebase login --reauth
firebase use afterlight-d8729
```

### Issue: "Connection refused to emulator"

**Solution:**
```kotlin
// Use 10.0.2.2 for Android emulator (not localhost)
FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
```

### Issue: "Permission denied in Firestore"

**Solution:**
```bash
# Check rules in console or deploy test rules:
firebase deploy --only firestore:rules
```

---

## Resources

- **Firebase Console:** https://console.firebase.google.com/project/afterlight-d8729
- **Firebase Docs:** https://firebase.google.com/docs/android/setup
- **Emulator Suite:** https://firebase.google.com/docs/emulator-suite
- **Security Rules:** https://firebase.google.com/docs/rules

---

**Status:** Ready for Firebase service enablement in console  
**Next:** Enable Authentication, Firestore, and Storage services
