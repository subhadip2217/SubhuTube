# Build SubhuTube APK (Android)

This project is ready for **Capacitor** so you can generate a real Android APK.

## Requirements

- Node.js 18+
- Android Studio (latest)
- Java 17 (recommended)

## Steps to generate APK

### 1. Install dependencies
```bash
npm install
```

### 2. Build the web app + sync to Android
```bash
npm run android
```

This runs `vite build` and then `npx cap sync android`.

### 3. Open in Android Studio
```bash
npm run android:open
```
or
```bash
npx cap open android
```

### 4. Generate the APK inside Android Studio

1. Wait for Gradle sync to finish.
2. Go to **Build → Build Bundle(s) / APK(s) → Build APK(s)**
3. When finished, click **locate** to find the APK.

The debug APK will be at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

### 5. (Optional) Generate signed release APK

1. **Build → Generate Signed Bundle / APK**
2. Choose **APK**
3. Create a new keystore (or use existing)
4. Select release build
5. Finish

## Quick commands summary

| Command                  | What it does                          |
|--------------------------|---------------------------------------|
| `npm run android`        | Build web + sync Capacitor Android    |
| `npm run android:open`   | Open project in Android Studio        |
| `npx cap run android`    | Build & run on connected device/emulator |

## Notes

- App ID: `com.subhadip.subhutube`
- App Name: **SubhuTube**
- Dark status bar matching the app theme
- Uses Invidious frontend → **no YouTube ads**

If you face any Gradle or SDK error, open Android Studio and let it download missing components automatically.
