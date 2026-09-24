package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val role: String, // "FARMER", "WORKER", "ADMIN"
    val name: String,
    val phone: String,
    val village: String,
    val district: String,
    val state: String = "Haryana",
    val preferredLanguage: String = "hi",
    val skills: String = "Harvesting, Sowing",
    val experienceYears: Int = 3,
    val rating: Float = 4.8f,
    val totalJobs: Int = 12,
    val availability: String = "AVAILABLE_TODAY", // "AVAILABLE_TODAY", "AVAILABLE_TOMORROW", "BUSY"
    val referralCode: String = "KHET101",
    val kycVerified: Boolean = true
)

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmerId: Long,
    val farmerName: String,
    val farmerPhone: String,
    val title: String,
    val description: String,
    val category: String, // "Harvesting", "Sowing", "Weeding", "Irrigation", "Spraying", "Tractor Work", "Other"
    val workersRequired: Int,
    val workersSelected: Int = 0,
    val wage: Int, // e.g. 500
    val paymentType: String = "Cash / UPI",
    val workDate: String, // e.g. "25 Sep"
    val startTime: String = "08:00 AM",
    val duration: String = "Full day (8 hrs)",
    val village: String,
    val district: String,
    val distanceKm: Float = 3.2f,
    val foodProvided: Boolean = true,
    val transportProvided: Boolean = false,
    val status: String = "PUBLISHED", // "PUBLISHED", "PARTIALLY_FILLED", "FILLED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val workerId: Long,
    val workerName: String,
    val workerPhone: String,
    val workerSkills: String,
    val workerRating: Float = 4.7f,
    val workerVillage: String,
    val status: String = "APPLIED", // "APPLIED", "ACCEPTED", "REJECTED", "COMPLETED", "CANCELLED"
    val appliedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val workerId: Long,
    val workerName: String,
    val date: String,
    val checkInTime: String? = null,
    val checkOutTime: String? = null,
    val farmerConfirmed: Boolean = false,
    val workerConfirmed: Boolean = false,
    val status: String = "CHECKED_IN" // "CHECKED_IN", "CHECKED_OUT", "CONFIRMED"
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val jobTitle: String,
    val farmerId: Long,
    val workerId: Long,
    val workerName: String,
    val amount: Int,
    val paymentMethod: String = "Cash", // "Cash", "UPI"
    val transactionRef: String = "",
    val status: String = "PAID", // "PENDING", "PAID", "CONFIRMED"
    val paidAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ratings")
data class RatingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val fromUserId: Long,
    val fromUserName: String,
    val toUserId: Long,
    val toUserName: String,
    val rating: Float,
    val workQuality: Float = 5.0f,
    val punctuality: Float = 5.0f,
    val behaviour: Float = 5.0f,
    val review: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val targetRole: String, // "FARMER", "WORKER", "ALL"
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "referrals")
data class ReferralEntity(
    @PrimaryKey val userId: Long,
    val referralCode: String,
    val totalEarnings: Int = 300,
    val pendingEarnings: Int = 100,
    val completedReferrals: Int = 3
)
