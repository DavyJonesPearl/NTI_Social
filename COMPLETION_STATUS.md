# NTI_Social current status

## What this app is

AfterLight / NTISocial is an Android ephemeral party-photo app. The live path is Firebase Auth, Firestore, Storage, and callable Cloud Functions. Retrofit (`https://api.afterlight.app/`) and WebSocket scaffolding exist but are unused.

There is **no AI / LLM request path**. Headroom is not appropriate for this architecture:

```text
NTI_Social → Firebase Auth / Firestore / Storage / Functions
```

## Implemented in this pass

- Shared per-party AES-256 media key so remote members can decrypt photos
- Firestore media listeners that sync metadata into Room
- JPEG capture via CameraX `OutputFileOptions`
- Party detail loading and expiration countdown
- Hilt WorkManager (`Configuration.Provider` + `HiltWorkerFactory`)
- `FLAG_SECURE` on camera, gallery, and full-screen media
- SQLCipher passphrase stored in EncryptedSharedPreferences (legacy hardcoded key is only used for one-time rekey)
- Release HTTP logging disabled; debug logging is BASIC only
- Backup disabled / sensitive paths excluded
- `network_security_config` wired in the manifest
- `storage.rules` added and referenced from `firebase.json`
- Firestore create/update rules tightened (host identity + immutable `mediaKey`)

## Intentionally not changed

- Retrofit / `AuthApi` / `PartyApi` / `MediaApi` / `WebSocketManager` — quarantined, unused
- `CameraController` — unused duplicate of inline CameraX setup
- Face / Sync Room entities — schema only
- `feature-voting`, `feature-voice`, `feature-recap` — commented out and absent
- Cloud Functions were **not** deployed from this machine
- Release signing remains the debug keystore (no production keystore was created)

## Verification

Static / local:

- Source audit of auth, party, camera, gallery, security, workers, and Firebase files
- Gradle debug assemble, unit tests, and lint after the changes

Requires a configured Firebase project + physical/emulator device:

- Email/password register and login
- Create / join / list / detail parties
- Capture, encrypt, upload
- Second-device gallery decryption
- Expiration worker and Storage rule enforcement
- Deployed `storage.rules` and updated `createParty` (mediaKey)

## Remaining work

- Deploy Firestore rules, Storage rules, and Functions (`createParty` now writes `mediaKey`)
- Register the release package `com.afterlight.app` in Firebase (`google-services.json` currently has the debug package)
- Production signing keystore
- App Check
- Upload WorkManager (current retry is in-process only)
- Optional Stage 14+ modules: voting, voice, recap
