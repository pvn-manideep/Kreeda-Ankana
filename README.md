# 🏏 KreedaAnkana

A native Android application for sports team management, ground slot booking, match challenges, live scoreboards, and an AI sports assistant — built for rural and semi-urban sports communities in India.

---

## 📱 Features

- **Team Registration** — Register your team with sport, village, player count, and contact details
- **Ground Slot Booking** — Browse and book ground slots by date and time with conflict prevention
- **Match Challenges** — Post and accept match challenges against other booked teams
- **Live Scoreboard** — Real-time team rankings based on wins and losses
- **AI Sports Assistant** — Context-aware in-app AI assistant for booking and match guidance
- **Admin Controls** — Score entry, slot management, and team administration for admin users
- **Notifications** — In-app notifications for challenges, reschedules, and match results

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| Local Database | Room Database (5 entities) |
| Auth | Firebase Authentication |
| Cloud Sync | Firebase Firestore |
| Async | Kotlin Coroutines + Flow |
| Navigation | Navigation Compose |
| Build | Gradle (Kotlin DSL) |

---

## ⚙️ Prerequisites & Setup

### 1. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com) and create a new project
2. Add an **Android app** with package name `com.kreeda.ankana`
3. Enable **Email/Password** authentication:
   - Firebase Console → Authentication → Sign-in method → Email/Password → Enable
4. Enable **Firestore Database**:
   - Firebase Console → Firestore Database → Create Database → Start in test mode
5. Download `google-services.json` from Project Settings → Your Apps
6. Paste `google-services.json` inside the **`app/`** folder:

```
KreedaAnkana/
└── app/
    └── google-services.json   ← place it here
```

---

### 2. Set Admin Email

Open this file:

```
app/src/main/kotlin/com/kreeda/ankana/auth/FirebaseAuthManager.kt
```

Find this line and replace with your admin email:

```kotlin
const val ADMIN_EMAIL = "Enter your admin email here"
```

Example:

```kotlin
const val ADMIN_EMAIL = "youradmin@gmail.com"
```

> The account that signs in with this email will automatically get admin access — including score entry, slot management, and user role controls.

---

### 3. Android Studio JDK Fix (if you get a Gradle JDK error)

If you see:
> *"Invalid Gradle JDK configuration found. Undefined java.home"*

**Quick fix:** Click **"Use Embedded JDK"** from the warning banner.

**Manual fix:**
1. Go to `File` → `Settings` (or `Ctrl + Alt + S`)
2. Navigate to `Build, Execution, Deployment` → `Build Tools` → `Gradle`
3. Under **Gradle JDK**, select:
   ```
   jbr-21  (C:\Program Files\Android\Android Studio\jbr)
   ```
4. Click **OK** and sync the project

---

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/KreedaAnkana.git
   ```
2. Open the project in **Android Studio**
3. Complete the Firebase setup and add `google-services.json` (see above)
4. Set your admin email in `FirebaseAuthManager.kt`
5. Click **Run** or press `Shift + F10` to build and launch on emulator or device

---

## 📋 Room Database Entities

| Entity | Description |
|---|---|
| `Team` | Team profile with sport, village, wins, losses, role |
| `Slot` | Ground booking slots with status lifecycle |
| `Challenge` | Match challenges between teams |
| `MatchScore` | Recorded match results and winners |
| `AppNotification` | Per-user in-app notifications |

---

## 🔐 Admin Access

The email set in `ADMIN_EMAIL` gets admin privileges:
- Enter live match scores
- View and manage all ground slots
- Approve or reject reschedule requests
- Grant admin access to other users

---

## 📄 License

This project was developed as part of an internship at **MindMatrix Learning Solutions Private Limited**, Bengaluru.
