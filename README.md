# NonFollowers Finder

Find Instagram users who don't follow you back by comparing your Instagram data export HTML files. **No internet required — works entirely offline.**

<p align="center">
  <img src="screenshots/screenshot1.png" width="200" alt="Splash Screen" />
  <img src="screenshots/screenshot2.png" width="200" alt="Main Screen" />
  <img src="screenshots/screenshot3.png" width="200" alt="Results Screen" />
  <img src="screenshots/screenshot4.png" width="200" alt="About Dialog" />
</p>

## Features

- **Import Instagram Data** — Select `followers_1.html` and `following.html` from your Instagram data download
- **Compare & Find** — Automatically detects who you follow that doesn't follow you back
- **One Tap to Unfollow** — Click any username to open their Instagram profile directly in the Instagram app
- **Samsung One UI Design** — Clean, modern interface inspired by Samsung's One UI
- **No Internet Required** — Works 100% offline. No permissions, no tracking, no servers
- **Your Data Stays Yours** — Everything is processed on-device, nothing is uploaded
- **Works on Android 5.0+** — Compatible with all Android versions from Lollipop onward

## How to Use

1. **Request your Instagram data** — Go to Instagram Settings → Privacy and Security → Download Data. Request your information and wait for the email.
2. **Extract the ZIP** — Download the ZIP file from Instagram and extract `followers_1.html` and `following.html`
3. **Open the app** — Tap the file selection cards and choose the corresponding HTML files
4. **Tap "Find Non-Followers"** — The app compares both files and shows the results
5. **Tap any username** — Opens their Instagram profile so you can unfollow

## How It Works

Instagram data export provides HTML files containing your followers and following lists. This app extracts all usernames from both files using pattern matching, then computes the set difference (following − followers) to identify accounts that don't follow you back.


## Building the Project

### Prerequisites
- Android SDK (API 34)
- Gradle (or use the included wrapper)

### Build with Gradle
```bash
./gradlew assembleRelease
```

The signed APK will be at `app/build/outputs/apk/release/app-release.apk`.

### Build on CodeAssist
1. Open the project folder in CodeAssist
2. Tap "Parse libraries from build.gradle"
3. Build and install

## Download

[Download latest APK](https://github.com/Did-Dog/NonFollowers-Finder/releases/latest)

## Tech Stack

- **Language:** Java 8
- **Min SDK:** 21 (Android 5.0)
- **Target SDK:** 34 (Android 14)
- **UI:** AppCompat, CardView, RecyclerView
- **Build System:** Gradle + Android Gradle Plugin 7.4.2

## License

```
Copyright 2026 NonFollowers Finder

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
