package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.KhetMitraDao
import com.example.data.model.ApplicationEntity
import com.example.data.model.AttendanceEntity
import com.example.data.model.JobEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.RatingEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        JobEntity::class,
        ApplicationEntity::class,
        AttendanceEntity::class,
        PaymentEntity::class,
        RatingEntity::class,
        NotificationEntity::class,
        ReferralEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): KhetMitraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "khetmitra_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.dao())
                    }
                }
            }

            suspend fun populateDatabase(dao: KhetMitraDao) {
                // Initial Users
                val users = listOf(
                    UserEntity(
                        id = 1L,
                        role = "FARMER",
                        name = "Kishan Kumar (किसान किशन)",
                        phone = "+91 98765 43210",
                        village = "Taraori (तरावड़ी)",
                        district = "Karnal",
                        state = "Haryana",
                        preferredLanguage = "hi",
                        skills = "Wheat, Paddy, Organic Farming",
                        experienceYears = 15,
                        rating = 4.9f,
                        totalJobs = 18,
                        referralCode = "KISHAN101",
                        kycVerified = true
                    ),
                    UserEntity(
                        id = 2L,
                        role = "WORKER",
                        name = "Ramesh Kumar (रमेश)",
                        phone = "+91 98123 45678",
                        village = "Nilokheri (नीलोखेड़ी)",
                        district = "Karnal",
                        state = "Haryana",
                        preferredLanguage = "hi",
                        skills = "Harvesting (कटाई), Sowing (बुआई), Tractor Work",
                        experienceYears = 6,
                        rating = 4.8f,
                        totalJobs = 24,
                        availability = "AVAILABLE_TODAY",
                        referralCode = "RAMESH50",
                        kycVerified = true
                    ),
                    UserEntity(
                        id = 3L,
                        role = "WORKER",
                        name = "Suresh Pal (सुरेश पाल)",
                        phone = "+91 97234 56789",
                        village = "Indri (इन्द्री)",
                        district = "Karnal",
                        state = "Haryana",
                        preferredLanguage = "hi",
                        skills = "Weeding (निराई), Irrigation (सिंचाई), Spraying",
                        experienceYears = 4,
                        rating = 4.6f,
                        totalJobs = 16,
                        availability = "AVAILABLE_TODAY",
                        referralCode = "SURESH20",
                        kycVerified = true
                    ),
                    UserEntity(
                        id = 4L,
                        role = "WORKER",
                        name = "Mohan Lal (मोहन लाल)",
                        phone = "+91 98980 12345",
                        village = "Gharaunda (घरौंडा)",
                        district = "Karnal",
                        state = "Haryana",
                        preferredLanguage = "hi",
                        skills = "Harvesting (कटाई), Vegetable picking",
                        experienceYears = 5,
                        rating = 4.7f,
                        totalJobs = 19,
                        availability = "AVAILABLE_TOMORROW",
                        referralCode = "MOHAN77",
                        kycVerified = true
                    ),
                    UserEntity(
                        id = 99L,
                        role = "ADMIN",
                        name = "KhetMitra Admin Panel",
                        phone = "+91 80000 00000",
                        village = "HQ",
                        district = "Karnal",
                        state = "Haryana",
                        preferredLanguage = "en",
                        skills = "Operations & Dispute Resolution",
                        experienceYears = 10,
                        rating = 5.0f,
                        totalJobs = 100,
                        referralCode = "ADMIN_SECURE",
                        kycVerified = true
                    )
                )
                dao.insertUsers(users)

                // Initial Jobs
                val jobs = listOf(
                    JobEntity(
                        id = 101L,
                        farmerId = 1L,
                        farmerName = "Kishan Kumar",
                        farmerPhone = "+91 98765 43210",
                        title = "Wheat Harvesting (गेहूँ की कटाई)",
                        description = "खेत में पके गेहूँ की कटाई और बंडल बनाने का कार्य। दोपहर का भोजन एवं चाय उपलब्ध है।",
                        category = "Harvesting",
                        workersRequired = 5,
                        workersSelected = 2,
                        wage = 550,
                        paymentType = "Cash / UPI at field",
                        workDate = "Tomorrow (कल)",
                        startTime = "08:00 AM",
                        duration = "Full day (8 hrs)",
                        village = "Taraori Farm #4",
                        district = "Karnal",
                        distanceKm = 2.8f,
                        foodProvided = true,
                        transportProvided = true,
                        status = "PUBLISHED"
                    ),
                    JobEntity(
                        id = 102L,
                        farmerId = 1L,
                        farmerName = "Kishan Kumar",
                        farmerPhone = "+91 98765 43210",
                        title = "Irrigation & Channel Maintenance (सिंचाई कार्य)",
                        description = "खेत में ट्यूबवेल से पानी का बहाव और नालियों की सफाई का काम।",
                        category = "Irrigation",
                        workersRequired = 2,
                        workersSelected = 1,
                        wage = 500,
                        paymentType = "Cash",
                        workDate = "26 Sep",
                        startTime = "07:30 AM",
                        duration = "Half day (5 hrs)",
                        village = "Taraori Farm #1",
                        district = "Karnal",
                        distanceKm = 3.5f,
                        foodProvided = true,
                        transportProvided = false,
                        status = "PUBLISHED"
                    ),
                    JobEntity(
                        id = 103L,
                        farmerId = 1L,
                        farmerName = "Kishan Kumar",
                        farmerPhone = "+91 98765 43210",
                        title = "Paddy Transplantation (धान रोपाई)",
                        description = "2 एकड़ खेत में धान की पौध लगाना। अनुभवी मजदूरों को प्राथमिकता।",
                        category = "Planting",
                        workersRequired = 8,
                        workersSelected = 8,
                        wage = 600,
                        paymentType = "Cash / UPI",
                        workDate = "20 Sep",
                        startTime = "08:00 AM",
                        duration = "Full day (8 hrs)",
                        village = "Taraori North",
                        district = "Karnal",
                        distanceKm = 4.2f,
                        foodProvided = true,
                        transportProvided = true,
                        status = "COMPLETED"
                    ),
                    JobEntity(
                        id = 104L,
                        farmerId = 5L,
                        farmerName = "Balwant Singh (बलवंत सिंह)",
                        farmerPhone = "+91 94160 11223",
                        title = "Vegetable & Tomato Picking (टमाटर तोड़ाई)",
                        description = "सब्जी के खेत से पके टमाटर तोड़ना और क्रेट में भरना। हल्का काम।",
                        category = "Picking",
                        workersRequired = 3,
                        workersSelected = 1,
                        wage = 480,
                        paymentType = "Cash",
                        workDate = "Today (आज)",
                        startTime = "09:00 AM",
                        duration = "Full day (8 hrs)",
                        village = "Nilokheri By-pass",
                        district = "Karnal",
                        distanceKm = 1.9f,
                        foodProvided = true,
                        transportProvided = false,
                        status = "PUBLISHED"
                    ),
                    JobEntity(
                        id = 105L,
                        farmerId = 6L,
                        farmerName = "Rajinder Mann (राजिंदर मान)",
                        farmerPhone = "+91 98960 99887",
                        title = "Tractor Ploughing & Harrowing (जुताई)",
                        description = "खेत की गहरी जुताई के लिए कुशल ट्रैक्टर चालक चाहिए।",
                        category = "Tractor Work",
                        workersRequired = 1,
                        workersSelected = 0,
                        wage = 750,
                        paymentType = "UPI",
                        workDate = "27 Sep",
                        startTime = "06:30 AM",
                        duration = "Full day",
                        village = "Gharaunda Canal",
                        district = "Karnal",
                        distanceKm = 6.4f,
                        foodProvided = true,
                        transportProvided = false,
                        status = "PUBLISHED"
                    )
                )
                dao.insertJobs(jobs)

                // Initial Applications
                val applications = listOf(
                    ApplicationEntity(
                        id = 201L,
                        jobId = 101L,
                        workerId = 2L,
                        workerName = "Ramesh Kumar (रमेश)",
                        workerPhone = "+91 98123 45678",
                        workerSkills = "Harvesting (कटाई), Sowing (बुआई)",
                        workerRating = 4.8f,
                        workerVillage = "Nilokheri (2.8 km)",
                        status = "ACCEPTED"
                    ),
                    ApplicationEntity(
                        id = 202L,
                        jobId = 101L,
                        workerId = 3L,
                        workerName = "Suresh Pal (सुरेश पाल)",
                        workerPhone = "+91 97234 56789",
                        workerSkills = "Weeding (निराई), Irrigation (सिंचाई)",
                        workerRating = 4.6f,
                        workerVillage = "Indri (3.5 km)",
                        status = "ACCEPTED"
                    ),
                    ApplicationEntity(
                        id = 203L,
                        jobId = 101L,
                        workerId = 4L,
                        workerName = "Mohan Lal (मोहन लाल)",
                        workerPhone = "+91 98980 12345",
                        workerSkills = "Harvesting (कटाई), Vegetable picking",
                        workerRating = 4.7f,
                        workerVillage = "Gharaunda (5.0 km)",
                        status = "APPLIED"
                    ),
                    ApplicationEntity(
                        id = 204L,
                        jobId = 102L,
                        workerId = 3L,
                        workerName = "Suresh Pal (सुरेश पाल)",
                        workerPhone = "+91 97234 56789",
                        workerSkills = "Irrigation (सिंचाई), Spraying",
                        workerRating = 4.6f,
                        workerVillage = "Indri",
                        status = "ACCEPTED"
                    )
                )
                dao.insertApplications(applications)

                // Initial Attendance
                val attendances = listOf(
                    AttendanceEntity(
                        id = 301L,
                        jobId = 101L,
                        workerId = 2L,
                        workerName = "Ramesh Kumar",
                        date = "Today",
                        checkInTime = "08:05 AM",
                        checkOutTime = null,
                        farmerConfirmed = true,
                        workerConfirmed = true,
                        status = "CHECKED_IN"
                    ),
                    AttendanceEntity(
                        id = 302L,
                        jobId = 101L,
                        workerId = 3L,
                        workerName = "Suresh Pal",
                        date = "Today",
                        checkInTime = "08:12 AM",
                        checkOutTime = null,
                        farmerConfirmed = false,
                        workerConfirmed = true,
                        status = "CHECKED_IN"
                    )
                )
                dao.insertAttendanceList(attendances)

                // Initial Payments
                val payments = listOf(
                    PaymentEntity(
                        id = 401L,
                        jobId = 103L,
                        jobTitle = "Paddy Transplantation (धान रोपाई)",
                        farmerId = 1L,
                        workerId = 2L,
                        workerName = "Ramesh Kumar",
                        amount = 600,
                        paymentMethod = "Cash",
                        transactionRef = "CASH-REC-103",
                        status = "CONFIRMED"
                    ),
                    PaymentEntity(
                        id = 402L,
                        jobId = 101L,
                        jobTitle = "Wheat Harvesting (गेहूँ की कटाई)",
                        farmerId = 1L,
                        workerId = 2L,
                        workerName = "Ramesh Kumar",
                        amount = 550,
                        paymentMethod = "Cash / UPI",
                        transactionRef = "",
                        status = "PENDING"
                    )
                )
                dao.insertPayments(payments)

                // Initial Ratings
                val ratings = listOf(
                    RatingEntity(
                        id = 501L,
                        jobId = 103L,
                        fromUserId = 1L,
                        fromUserName = "Kishan Kumar (किसान)",
                        toUserId = 2L,
                        toUserName = "Ramesh Kumar (मजदूर)",
                        rating = 5.0f,
                        workQuality = 5.0f,
                        punctuality = 5.0f,
                        behaviour = 5.0f,
                        review = "बहुत मेहनती और समय के पाबंद मजदूर हैं। धान रोपाई का काम बहुत सफाई से किया।"
                    ),
                    RatingEntity(
                        id = 502L,
                        jobId = 103L,
                        fromUserId = 2L,
                        fromUserName = "Ramesh Kumar (मजदूर)",
                        toUserId = 1L,
                        toUserName = "Kishan Kumar (किसान)",
                        rating = 5.0f,
                        workQuality = 5.0f,
                        punctuality = 5.0f,
                        behaviour = 5.0f,
                        review = "किशन जी ने समय पर पूरा कैश भुगतान किया और दोपहर का भोजन भी बहुत अच्छा दिया।"
                    )
                )
                dao.insertRatings(ratings)

                // Initial Notifications
                val notifications = listOf(
                    NotificationEntity(
                        id = 601L,
                        userId = 1L,
                        targetRole = "FARMER",
                        title = "New Worker Application (नया आवेदन)",
                        body = "Mohan Lal has applied for Wheat Harvesting (गेहूँ कटाई) job.",
                        isRead = false
                    ),
                    NotificationEntity(
                        id = 602L,
                        userId = 2L,
                        targetRole = "WORKER",
                        title = "Application Accepted! (आवेदन स्वीकृत)",
                        body = "Kishan Kumar accepted your application for Wheat Harvesting.",
                        isRead = false
                    ),
                    NotificationEntity(
                        id = 603L,
                        userId = 2L,
                        targetRole = "WORKER",
                        title = "New Job Near You (नया काम)",
                        body = "Tomato picking available 1.9 km away at Nilokheri (₹480/day).",
                        isRead = false
                    )
                )
                dao.insertNotifications(notifications)

                // Referrals
                dao.insertReferral(
                    ReferralEntity(
                        userId = 1L,
                        referralCode = "KISHAN101",
                        totalEarnings = 300,
                        pendingEarnings = 100,
                        completedReferrals = 3
                    )
                )
                dao.insertReferral(
                    ReferralEntity(
                        userId = 2L,
                        referralCode = "RAMESH50",
                        totalEarnings = 200,
                        pendingEarnings = 50,
                        completedReferrals = 2
                    )
                )
            }
        }
    }
}
