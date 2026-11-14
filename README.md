# SubhuPlay

**Discover videos. Keep what you learn.**

SubhuPlay is an independent Android video-discovery and study companion. It is designed to add a personal learning workspace around public videos: topic discovery, private study notes, and a focused place to collect what you learn.

The Android app's working product name is **SubhuPlay**. The repository slug is being reviewed separately because third-party video-platform branding rules restrict names that imitate or derive from a platform's brand.

## Current foundation

- Native Android app built with Kotlin and Jetpack Compose.
- Google sign-in using Android Credential Manager (no password collection and no scraping Google or YouTube WebView cookies).
- Public video search through the official YouTube Data API.
- Official embedded YouTube player with platform controls and monetization behaviour intact.
- Local study notes stored on the device.
- Custom SubhuPlay launcher mark and dark visual identity.
- GitHub Actions workflow for debug APK builds.

## Platform and policy boundaries

SubhuPlay is not an official YouTube app and must not imitate YouTube's product name, logo, or overall look and feel.

- YouTube videos play through the official embedded player. The app does not suppress ads, remove player controls, alter thumbnails, or interfere with platform playback.
- The YouTube Data API does not grant permission to download or cache copies of YouTube audiovisual content or enable offline playback. Offline files, when added, will be restricted to content the user owns or has permission to download.
- The project will focus on independent value such as study notes, timestamp-based learning, topic collections, and cross-source discovery rather than being a one-to-one YouTube clone.
- Search results are subject to API availability and quota. The default YouTube Data API allocation currently lists 100 search.list calls per day; additional page requests also use quota.

## Configure local credentials

Do not commit API keys, OAuth client IDs, signing keys, or Google service configuration files.

Add these values to your user Gradle properties file.

Windows location: %USERPROFILE%\.gradle\gradle.properties

    YOUTUBE_DATA_API_KEY=YOUR_RESTRICTED_YOUTUBE_DATA_API_KEY
    GOOGLE_WEB_CLIENT_ID=YOUR_WEB_APPLICATION_OAUTH_CLIENT_ID

Enable the YouTube Data API v3 in Google Cloud Console and create an API key restricted to the Android application ID:

    com.subhadip.subhuplay

Restrict the key using the app's signing certificate SHA-1 fingerprint before distributing builds. For Google sign-in, configure the OAuth consent screen and create the required Android and Web application OAuth clients. The Web client ID is used by Credential Manager to request the Google ID token.

## Build

The repository CI builds a debug APK using Gradle 9.3.1 and JDK 21. For a local build, install Android SDK platform 37 and Gradle 9.3.1, configure the properties above, then run:

    gradle assembleDebug

The debug APK is produced at:

    app/build/outputs/apk/debug/app-debug.apk

## Planned work

1. Complete the account and Google OAuth setup, including consent for any YouTube account data beyond public search.
2. Add a proper library for user-curated collections and timestamped notes.
3. Add cross-source discovery so SubhuPlay has value beyond YouTube content alone.
4. Add offline downloads only for creator-owned or explicitly licensed media.
5. Add playback, accessibility, privacy, and device compatibility tests before releasing an APK.

## Privacy

The initial version stores the signed-in display name, email, and study note locally on the device. It does not upload those values to a project server. A public privacy policy and deletion flow are required before a public release with cloud accounts or synced personal data.

## References

- YouTube API Services Developer Policies: https://developers.google.com/youtube/terms/developer-policies/
- YouTube API Services Branding Guidelines: https://developers.google.com/youtube/terms/branding-guidelines
- YouTube Data API search.list: https://developers.google.com/youtube/v3/docs/search/list
- Google Credential Manager sign-in: https://developer.android.com/identity/sign-in/credential-manager-siwg-implementation
