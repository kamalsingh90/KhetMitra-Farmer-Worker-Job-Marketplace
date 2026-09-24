# KhetMitra – Farmer & Worker Job Marketplace

**KhetMitra** is a modern agricultural job marketplace designed to connect **farmers (Kisan)** with **agricultural workers (Majdoor)**.

The application helps farmers find suitable workers for farm-related activities while allowing workers to discover and apply for nearby agricultural jobs.

> **KhetMitra — Kisan Ko Majdoor, Majdoor Ko Kaam.**

---

## 📱 Project Overview

KhetMitra provides a simple digital platform for agricultural employment.

###  Farmers can

* Register using their mobile number
* Create and manage their profile
* Post agricultural jobs
* Set wages and worker requirements
* Select work date and time
* Add farm/work location
* Receive worker applications
* Accept or reject workers
* Manage attendance
* Track payments
* Rate workers
* View job history

###  Workers can

* Register using their mobile number
* Create a worker profile
* Add skills and experience
* Discover nearby agricultural jobs
* Search and filter jobs
* View wages and work details
* Apply for jobs
* Receive application notifications
* Navigate to the work location
* Check in and check out
* Track payment status
* Rate farmers
* View job history

---

#  Problem Statement

Farmers often face difficulties finding reliable workers during important farming seasons such as:

* 🌱 Sowing
* 🌾 Harvesting
* 🥕 Vegetable picking
* 🌿 Weeding
* 💧 Irrigation
* 🌳 Plantation
* 🚜 Tractor-related work
* 🧴 Crop spraying

At the same time, agricultural workers may not know where suitable jobs are available.

**KhetMitra solves this problem by digitally connecting farmers and agricultural workers.**

---

# Main Features

## Farmer Features

* Farmer registration
* Farmer profile
* Create agricultural jobs
* Set wages
* Set required workers
* Select work date and time
* Add farm location
* Manage applications
* Accept/reject workers
* View worker profiles
* Worker ratings and reviews
* Attendance management
* Payment tracking
* Notifications
* Job history
* Ratings and reviews
* Referral program

---

## Worker Features

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
* Ratings and reviews
* Referral program

---

# 📍 Nearby Jobs

Workers can discover agricultural jobs based on their location.

The application can display:

* Distance from the job
* Farm/work location
* Work area
* Work date
* Work timing
* Wage
* Number of workers required
* Navigation to the farm

Farmers can select or add their farm location while creating a job.

---

# Notifications

KhetMitra uses push notifications for important events.

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

# Payment System

KhetMitra is designed to support both offline and online payment workflows.

### Offline Payment

Farmers can pay workers using:

* Cash
* UPI
* Other agreed payment methods

The payment can then be recorded and confirmed in the application.

### Payment Status

```text
PENDING
PAYMENT_INITIATED
PAID
PAYMENT_CONFIRMED
DISPUTED
REFUNDED
```

Online payment gateway integration can be added in a future version.

---

# Ratings & Reviews

After completing a job, both farmers and workers can provide ratings and reviews.

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

The rating system helps improve transparency and trust between users.

---

# Referral Program

KhetMitra can provide a referral system for farmers and workers.

Each user can receive a unique referral code.

Example:

```text
KISHAN123
```

Possible rewards include:

* Wallet rewards
* Bonus points
* Coupons
* Promotional rewards

Referral rewards can be managed through the admin panel.

---

#  Advertisement & Monetization

KhetMitra can support multiple monetization models.

### Advertisement

Possible ad formats:

* Banner Ads
* Native Ads
* Interstitial Ads
* Rewarded Ads

Ads should not interrupt important actions such as:

* Job acceptance
* Check-in
* Check-out
* Payment confirmation

### Featured Jobs

Farmers can optionally promote their jobs using:

```text
Featured / Sponsored Jobs
```

This can provide an additional revenue source for the platform.

---

# 🛡️ Safety & Trust

KhetMitra includes features designed to improve user safety and trust.

* Mobile number verification
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

The application is designed with localization support.

### Initial Languages

* 🇮🇳 Hindi
* 🇬🇧 English

Future versions can support additional Indian regional languages.

---

# User Roles

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

Manages users, jobs, reports, payments, referrals and platform settings.

---

# Application Architecture

```text
                    KhetMitra App
                          │
                          ▼
             Kotlin + Jetpack Compose
                          │
                          ▼
                    REST API
                          │
                          ▼
                NestJS Backend
                          │
          ┌───────────────┼───────────────┐
          │               │               │
          ▼               ▼               ▼
     PostgreSQL         Redis       Object Storage
          │               │               │
          └───────────────┼───────────────┘
                          │
                          ▼
                 External Services
                          │
              ┌───────────┼───────────┐
              │           │           │
              ▼           ▼           ▼
             FCM        Maps       Payments
```

---

# Project Architecture

The Android application follows a modular Clean Architecture approach.

```text
KhetMitra
│
├── app
│
├── core
│   ├── common
│   ├── model
│   ├── network
│   ├── database
│   ├── datastore
│   ├── navigation
│   └── designsystem
│
├── feature
│   ├── auth
│   ├── onboarding
│   ├── profile
│   ├── farmer
│   ├── worker
│   ├── jobs
│   ├── applications
│   ├── attendance
│   ├── payments
│   ├── notifications
│   ├── ratings
│   ├── referrals
│   └── settings
│
└── backend
    └── NestJS API
```

---

# 🛠️ Technology Stack

## Mobile Application

* Kotlin
* Jetpack Compose
* Material 3
* Navigation Compose
* MVVM
* Clean Architecture
* Kotlin Coroutines
* Kotlin Flow
* Hilt
* Retrofit
* OkHttp
* Room
* DataStore
* WorkManager
* Firebase Cloud Messaging
* Firebase Crashlytics
* Maps / Location API
* Coil

## Backend

* Node.js
* NestJS
* TypeScript
* REST API
* WebSocket support

## Database

* PostgreSQL
* Redis

## Services

* Firebase Cloud Messaging
* Maps / Location API
* Object Storage
* Payment Gateway
* Advertisement SDK

## Admin Panel

* React
* Next.js
* REST API

---

# 📱 Main Screens

## Common Screens

```text
Splash
Onboarding
Language Selection
Login
OTP Verification
Role Selection
Profile Setup
Notifications
Settings
Help & Support
```

## Farmer Screens

```text
Farmer Home
Create Job
Job Preview
My Jobs
Job Details
Applications
Worker Profile
Selected Workers
Attendance
Payment
Job History
```

## Worker Screens

```text
Worker Home
Find Jobs
Search & Filters
Job Details
Apply for Job
My Applications
Accepted Jobs
Check-In
Check-Out
Payment
Job History
```

---

# 🔄 Farmer Workflow

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

---

# 🔄 Worker Workflow

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

# 🗄️ Core Data Models

### User

```text
User
├── id
├── mobile
├── name
├── role
├── profileImage
├── language
├── latitude
├── longitude
├── rating
├── isVerified
└── createdAt
```

### Job

```text
Job
├── id
├── farmerId
├── title
├── description
├── workType
├── wage
├── wageType
├── workersRequired
├── workersSelected
├── date
├── startTime
├── endTime
├── latitude
├── longitude
├── address
├── status
└── createdAt
```

### Job Application

```text
JobApplication
├── id
├── jobId
├── workerId
├── status
└── appliedAt
```

Application states:

```text
PENDING
ACCEPTED
REJECTED
CANCELLED
COMPLETED
```

---

# Authentication

The application uses mobile-number-based authentication.

```text
Enter Mobile Number
        ↓
    Send OTP
        ↓
  Verify OTP
        ↓
 Select Role
        ↓
 Complete Profile
        ↓
      Home
```

Authentication and authorization should be handled securely through the backend API.

---

# 🧪 Development Roadmap

## Phase 1 — MVP

* [x] Project setup
* [ ] Authentication
* [ ] OTP verification
* [ ] Role selection
* [ ] Farmer profile
* [ ] Worker profile
* [ ] Create job
* [ ] Find jobs
* [ ] Nearby jobs
* [ ] Job details
* [ ] Apply for job
* [ ] Accept/reject application
* [ ] Push notifications

## Phase 2 — Work Management

* [ ] Accepted jobs
* [ ] Check-in
* [ ] Check-out
* [ ] Attendance
* [ ] Job completion
* [ ] Payment status
* [ ] Job history

## Phase 3 — Trust & Safety

* [ ] Ratings
* [ ] Reviews
* [ ] User reporting
* [ ] User blocking
* [ ] KYC verification
* [ ] Payment disputes

## Phase 4 — Monetization

* [ ] Featured jobs
* [ ] Advertisements
* [ ] Referral system
* [ ] Sponsored jobs

## Phase 5 — Advanced Features

* [ ] AI-powered job matching
* [ ] Voice-based job posting
* [ ] Real-time chat
* [ ] Online payments
* [ ] Recurring jobs
* [ ] Worker groups
* [ ] Regional languages
* [ ] Agricultural equipment marketplace
* [ ] Seeds and farming services marketplace

---

#  Project Status

**Status:** In Development

**Version:** 1.0.0

**Platform:** Android

**Framework:** Jetpack Compose

**Language:** Kotlin

**Architecture:** Clean Architecture + MVVM

---

# 🎯 Goal

The goal of KhetMitra is to make agricultural worker hiring **simple, accessible and efficient** by connecting farmers and workers through an easy-to-use digital platform.

> **KhetMitra — Kisan Ko Majdoor, Majdoor Ko Kaam.**

---

# Development

KhetMitra is being developed using modern Android development practices with Kotlin and Jetpack Compose.

The project is designed with scalability, maintainability and future platform expansion in mind.

---

## 📄 License

This project is currently under development.

License information will be added in a future release.
