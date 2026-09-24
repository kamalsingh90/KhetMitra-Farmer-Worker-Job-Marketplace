# KhetMitra-Farmer-Worker-Job-Marketplace
KhetMitra is a Jetpack compose-based farmer-worker marketplace app that helps farmers find agricultural workers and helps workers discover nearby farm jobs with easy job posting, applications, notifications, payments, referrals, and ratings.


# 🌾 KhetMitra – Farmer & Worker Job Marketplace

**KhetMitra** is a Flutter-based mobile application designed to connect **farmers (Kishan)** with **agricultural workers (Majdoor)**. The platform makes it easier for farmers to find workers for farm-related activities and helps workers discover and apply for nearby agricultural jobs.

The application is designed for **Android and iOS** using a single Flutter codebase.

---

## 🚜 Problem Statement

Farmers often face difficulties finding reliable workers during important farming seasons such as:

* 🌱 Sowing
* 🌾 Harvesting
* 🥕 Vegetable picking
* 🌿 Weeding
* 💧 Irrigation
* 🌳 Plantation
* 🚜 Farm and tractor-related work

At the same time, workers may not know where suitable jobs are available.

**KhetMitra solves this problem by creating a simple digital connection between farmers and workers.**

---

## 💡 How KhetMitra Works

### 👨‍🌾 For Farmers

Farmers can:

1. Register using their mobile number.
2. Create their farmer profile.
3. Post a farm job.
4. Add work details, wages, date and time.
5. Specify the number of workers required.
6. Add the farm/work location.
7. Receive worker applications.
8. View worker profiles and ratings.
9. Accept suitable workers.
10. Receive notifications.
11. Track work attendance.
12. Record online or offline payments.
13. Rate workers after job completion.

---

### 👷 For Workers / Majdoor

Workers can:

1. Register using their mobile number.
2. Create their worker profile.
3. Add their skills and experience.
4. Discover nearby agricultural jobs.
5. Search and filter available jobs.
6. View wages, date, time and location.
7. Apply for suitable jobs.
8. Receive acceptance notifications.
9. Navigate to the work location.
10. Check in and check out.
11. View payment status.
12. Rate farmers after completing the job.

---

# ⭐ Main Features

## 👨‍🌾 Farmer Features

* Farmer registration
* Farmer profile
* Create farm jobs
* Set wages
* Set required workers
* Select work date and time
* Add farm location
* Manage job applications
* Accept/reject workers
* Worker profile and ratings
* Attendance management
* Payment tracking
* Notifications
* Job history
* Ratings & reviews
* Referral rewards

---

## 👷 Worker Features

* Worker registration
* Worker profile
* Skills and experience
* Nearby job discovery
* Job search
* Job filters
* Wage information
* Apply for jobs
* Application tracking
* Accepted jobs
* Check-in/check-out
* Payment status
* Job history
* Ratings & reviews
* Referral program

---

# 🔔 Notifications

KhetMitra provides notifications for important events.

### Farmer Notifications

* New worker application
* Worker accepted
* Worker cancelled
* Work reminder
* Worker check-in
* Work completed
* Payment confirmation

### Worker Notifications

* New nearby job
* Application accepted
* Application rejected
* Job cancelled
* Work reminder
* Payment confirmation

---

# 💰 Payment System

KhetMitra supports both **online and offline payment concepts**.

### Offline Payment

Farmers can pay workers through:

* Cash
* UPI
* Other agreed payment methods

The payment can then be marked and confirmed inside the application.

### Online Payment

The architecture can support future integration with an online payment gateway.

Payment statuses can include:

```text
Pending
Payment Initiated
Paid
Payment Confirmed
Disputed
Refunded
```

---

# 📍 Location & Nearby Jobs

Location-based functionality helps workers discover jobs around them.

Workers can see:

* Distance from job
* Farm/work location
* Job area
* Work timing
* Navigation

Farmers can add their farm location while creating a job.

---

# ⭐ Ratings & Reviews

After completing a job, both users can provide ratings.

### Farmer → Worker

* Work quality
* Punctuality
* Behaviour
* Overall rating

### Worker → Farmer

* Work description
* Payment behaviour
* Behaviour
* Overall rating

This helps build trust within the platform.

---

# 🎁 Referral Program

KhetMitra includes a referral system where users can invite other farmers and workers.

Each user can receive a unique referral code.

Example:

```text
KISHAN123
```

Referral rewards can be managed through the admin panel.

Possible rewards:

* Wallet rewards
* Bonus points
* Coupons
* Promotional rewards

---

# 📢 Advertisement & Monetization

The application can support advertising to generate platform revenue.

Potential ad formats:

* Banner Ads
* Native Ads
* Interstitial Ads
* Rewarded Ads

The advertising system should be designed so that ads do not interrupt important actions such as:

* Job acceptance
* Check-in
* Check-out
* Payment confirmation

The platform can also support **Featured/Sponsored Jobs** as an additional revenue model.

---

# 🛡️ Safety & Trust

KhetMitra includes features designed to improve user safety and trust:

* Mobile verification
* Optional KYC verification
* User ratings
* Reviews
* Report user
* Block user
* Job reporting
* Payment dispute system
* Admin moderation

---

# 🌐 Multi-Language Support

The application is designed with localization support so it can be used by users from different regions.

Initial languages:

* 🇮🇳 Hindi
* 🇬🇧 English

Future support can include additional Indian regional languages.

---

# 📱 Technology Stack

## Mobile Application

* Flutter
* Dart
* Riverpod
* GoRouter
* Dio
* Secure Storage
* Firebase Cloud Messaging
* Firebase Crashlytics

## Backend

* Node.js
* NestJS
* TypeScript
* REST API
* WebSocket support for future real-time communication

## Database

* PostgreSQL
* Redis

## Services

* Firebase Cloud Messaging
* Maps/Location API
* Object Storage
* Payment Gateway
* Advertisement SDK

## Admin Panel

* React / Next.js
* REST API

---

# 🏗️ Application Architecture

```text
                 KhetMitra App
                       |
              Flutter Android/iOS
                       |
                    REST API
                       |
                Backend Server
                       |
        ┌──────────────┼──────────────┐
        │              │              │
   PostgreSQL       Redis       Object Storage
        │              │              │
        └──────────────┼──────────────┘
                       |
              External Services
                       |
       ┌───────────────┼───────────────┐
       │               │               │
   Notifications      Maps          Payments
       │
      FCM
```

---

# 👥 User Roles

KhetMitra supports three primary roles:

```text
ADMIN
FARMER
WORKER
```

### Farmer

Creates and manages agricultural jobs.

### Worker

Discovers and applies for agricultural jobs.

### Admin

Manages the complete platform.

---

# 🖥️ Main App Screens

## Common Screens

* Splash Screen
* Onboarding
* Language Selection
* Login
* OTP Verification
* Role Selection
* Profile Setup
* Notifications
* Settings
* Help & Support

## Farmer Screens

* Farmer Home
* Create Job
* Job Preview
* My Jobs
* Job Details
* Applications
* Worker Profile
* Selected Workers
* Attendance
* Payment
* Job History

## Worker Screens

* Worker Home
* Find Jobs
* Search & Filters
* Job Details
* Apply for Job
* My Applications
* Accepted Jobs
* Check-in
* Check-out
* Payment
* Job History

---

# 🔄 Basic Workflow

## Farmer

```text
Register
   ↓
Create Profile
   ↓
Post Job
   ↓
Receive Applications
   ↓
Select Worker
   ↓
Worker Notification
   ↓
Work Starts
   ↓
Check-In
   ↓
Work Completed
   ↓
Payment
   ↓
Rating
```

## Worker

```text
Register
   ↓
Create Profile
   ↓
Find Nearby Jobs
   ↓
Apply
   ↓
Farmer Accepts
   ↓
Receive Notification
   ↓
Go To Farm
   ↓
Check-In
   ↓
Complete Work
   ↓
Check-Out
   ↓
Receive Payment
   ↓
Rate Farmer
```

---

# 📂 Flutter Project Structure

```text
lib/
│
├── core/
│   ├── network/
│   ├── storage/
│   ├── theme/
│   ├── routing/
│   └── utils/
│
├── features/
│   ├── auth/
│   ├── farmer/
│   ├── worker/
│   ├── jobs/
│   ├── applications/
│   ├── attendance/
│   ├── payments/
│   ├── notifications/
│   ├── referral/
│   ├── ratings/
│   └── profile/
│
└── main.dart
```

---

# 🚀 Future Roadmap

Future versions of KhetMitra can include:

* 🤖 AI-powered job matching
* 🎙️ Voice-based job posting
* 🗺️ Advanced location matching
* 💬 Real-time chat
* 💳 Online payments
* 🔁 Recurring jobs
* 👥 Worker groups
* ⭐ Featured jobs
* 💎 Premium farmer plans
* 📊 Advanced analytics
* 🌐 More regional languages
* 🚜 Agricultural equipment marketplace
* 🌱 Seeds and farming services marketplace

---

# 🎯 Goal

The main goal of KhetMitra is to make agricultural worker hiring **simple, accessible and efficient** by connecting farmers with workers through an easy-to-use mobile platform.

> **"KhetMitra — Kisan Ko Majdoor, Majdoor Ko Kaam."** 🌾🤝

---

# 📌 Project Status

**Status:** In Development 🚧

**Platform:** Android & iOS

**Framework:** Flutter

**Version:** 1.0.0

