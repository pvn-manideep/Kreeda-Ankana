# Kreeda-Ankana v5 — Setup Guide

## What's New in v5
- ✅ **Google Sign-In** as the primary auth option
- ✅ **Zero API key AI** — KreedaBot works locally, no Anthropic key needed
- ✅ **Dark / Light theme toggle** — button on Home & Auth screen
- ✅ **Modern design** — violet/magenta gradient brand, glassmorphism cards
- ✅ **Challenge validation** — you cannot accept your own challenge (enforced)
- ✅ **Open challenges** — post from your slot; any OTHER team can accept
- ✅ **Reschedule** — with date + time picker from Booking or My Slots tab
- ✅ **Admin panel** — 6manideep@gmail.com gets auto admin on sign-in
- ✅ **Notifications inbox** with unread badge on nav bar

---

## Step 1: Firebase Setup (Required)

1. Go to https://console.firebase.google.com → **Add Project** → `KreedaAnkana`
2. Click Android icon → Package name: `com.kreeda.ankana`
3. Download **google-services.json** → replace `app/google-services.json`

### Enable Authentication
- Firebase Console → **Authentication** → Get Started
- **Email/Password** → Enable
- **Google** → Enable → copy **Web Client ID**

### Add Web Client ID
- Open `app/src/main/res/values/strings.xml`
- Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual Web Client ID

---

## Step 2: Build

```bash
cd kreeda-v5
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## Step 3: Admin Access

Sign up / sign in with **6manideep@gmail.com** — admin role is granted automatically.

Admin unlocks:
- 👑 Post Score tab in Scoreboard
- 👑 Admin tab in Profile (manage all bookings, grant admin to others)
- Cancel any team's booking from Booking screen

---

## KreedaBot — No API Key

KreedaBot is fully local. It understands:
- "Book 4 PM today for Volleyball"
- "What slots are free tomorrow?"
- "Cancel my booking"
- "Reschedule to 6 PM Saturday"
- "Accept the Cricket challenge"
- "Who's playing now?"

When it detects an intent, it shows a **Confirm** card — one tap executes the action.

---

## Challenge Rules
- Only YOU can post a challenge from YOUR booked slot
- Only a DIFFERENT team can accept it
- The app enforces this — no self-acceptance possible
