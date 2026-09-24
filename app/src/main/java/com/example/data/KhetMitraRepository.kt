package com.example.data

import com.example.data.dao.KhetMitraDao
import com.example.data.model.ApplicationEntity
import com.example.data.model.AttendanceEntity
import com.example.data.model.JobEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.RatingEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KhetMitraRepository(private val dao: KhetMitraDao) {

    // Users
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    fun getUserById(id: Long): Flow<UserEntity?> = dao.getUserById(id)
    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

    // Jobs
    val allJobs: Flow<List<JobEntity>> = dao.getAllJobs()
    fun getJobsByFarmer(farmerId: Long): Flow<List<JobEntity>> = dao.getJobsByFarmer(farmerId)
    fun getJobById(id: Long): Flow<JobEntity?> = dao.getJobById(id)

    suspend fun postJob(job: JobEntity): Long {
        val id = dao.insertJob(job)
        // Notify nearby workers
        dao.insertNotification(
            NotificationEntity(
                userId = 2L,
                targetRole = "WORKER",
                title = "New Job Near You (नया काम)",
                body = "${job.title} posted in ${job.village} (₹${job.wage}/day). Apply now!"
            )
        )
        return id
    }

    suspend fun updateJobStatus(jobId: Long, status: String) = dao.updateJobStatus(jobId, status)
    suspend fun deleteJob(jobId: Long) = dao.deleteJobById(jobId)

    // Applications
    val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()
    fun getApplicationsForJob(jobId: Long): Flow<List<ApplicationEntity>> = dao.getApplicationsForJob(jobId)
    fun getApplicationsForWorker(workerId: Long): Flow<List<ApplicationEntity>> = dao.getApplicationsForWorker(workerId)

    suspend fun applyForJob(job: JobEntity, worker: UserEntity): Long {
        val app = ApplicationEntity(
            jobId = job.id,
            workerId = worker.id,
            workerName = worker.name,
            workerPhone = worker.phone,
            workerSkills = worker.skills,
            workerRating = worker.rating,
            workerVillage = "${worker.village} (Near)",
            status = "APPLIED"
        )
        val appId = dao.insertApplication(app)
        // Notify farmer
        dao.insertNotification(
            NotificationEntity(
                userId = job.farmerId,
                targetRole = "FARMER",
                title = "New Applicant (नया आवेदन)",
                body = "${worker.name} applied for ${job.title}."
            )
        )
        return appId
    }

    suspend fun acceptApplication(app: ApplicationEntity, jobTitle: String) {
        dao.updateApplicationStatus(app.id, "ACCEPTED")
        dao.incrementWorkersSelected(app.jobId)

        // Initialize attendance record for this worker
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        dao.insertAttendance(
            AttendanceEntity(
                jobId = app.jobId,
                workerId = app.workerId,
                workerName = app.workerName,
                date = "Today",
                checkInTime = currentTime,
                checkOutTime = null,
                farmerConfirmed = false,
                workerConfirmed = true,
                status = "CHECKED_IN"
            )
        )

        // Notify worker
        dao.insertNotification(
            NotificationEntity(
                userId = app.workerId,
                targetRole = "WORKER",
                title = "Application Accepted! (स्वीकृत)",
                body = "Your application for $jobTitle was accepted by the farmer."
            )
        )
    }

    suspend fun rejectApplication(appId: Long) {
        dao.updateApplicationStatus(appId, "REJECTED")
    }

    // Attendance
    val allAttendance: Flow<List<AttendanceEntity>> = dao.getAllAttendance()
    fun getAttendanceForJob(jobId: Long): Flow<List<AttendanceEntity>> = dao.getAttendanceForJob(jobId)
    fun getAttendanceForWorker(workerId: Long): Flow<List<AttendanceEntity>> = dao.getAttendanceForWorker(workerId)

    suspend fun checkInWorker(jobId: Long, worker: UserEntity) {
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        dao.insertAttendance(
            AttendanceEntity(
                jobId = jobId,
                workerId = worker.id,
                workerName = worker.name,
                date = "Today",
                checkInTime = time,
                checkOutTime = null,
                farmerConfirmed = false,
                workerConfirmed = true,
                status = "CHECKED_IN"
            )
        )
    }

    suspend fun checkOutWorker(attendance: AttendanceEntity) {
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        dao.updateAttendance(
            attendance.copy(
                checkOutTime = time,
                status = "CHECKED_OUT"
            )
        )
    }

    suspend fun confirmAttendanceByFarmer(attendance: AttendanceEntity) {
        dao.updateAttendance(
            attendance.copy(
                farmerConfirmed = true,
                status = "CONFIRMED"
            )
        )
    }

    // Payments
    val allPayments: Flow<List<PaymentEntity>> = dao.getAllPayments()
    fun getPaymentsForFarmer(farmerId: Long): Flow<List<PaymentEntity>> = dao.getPaymentsForFarmer(farmerId)
    fun getPaymentsForWorker(workerId: Long): Flow<List<PaymentEntity>> = dao.getPaymentsForWorker(workerId)

    suspend fun recordOfflinePayment(
        jobId: Long,
        jobTitle: String,
        farmerId: Long,
        workerId: Long,
        workerName: String,
        amount: Int,
        method: String,
        reference: String
    ): Long {
        val payment = PaymentEntity(
            jobId = jobId,
            jobTitle = jobTitle,
            farmerId = farmerId,
            workerId = workerId,
            workerName = workerName,
            amount = amount,
            paymentMethod = method,
            transactionRef = reference,
            status = "PAID"
        )
        val id = dao.insertPayment(payment)
        // Notify worker
        dao.insertNotification(
            NotificationEntity(
                userId = workerId,
                targetRole = "WORKER",
                title = "Payment Recorded (भुगतान दर्ज)",
                body = "Farmer recorded payment of ₹$amount via $method. Please confirm."
            )
        )
        return id
    }

    suspend fun confirmPaymentReceived(paymentId: Long, workerName: String) {
        dao.updatePaymentStatus(paymentId, "CONFIRMED")
    }

    // Ratings
    val allRatings: Flow<List<RatingEntity>> = dao.getAllRatings()
    fun getRatingsForUser(userId: Long): Flow<List<RatingEntity>> = dao.getRatingsForUser(userId)

    suspend fun submitRating(
        jobId: Long,
        fromUserId: Long,
        fromUserName: String,
        toUserId: Long,
        toUserName: String,
        rating: Float,
        workQuality: Float,
        punctuality: Float,
        behaviour: Float,
        review: String
    ): Long {
        val ratingObj = RatingEntity(
            jobId = jobId,
            fromUserId = fromUserId,
            fromUserName = fromUserName,
            toUserId = toUserId,
            toUserName = toUserName,
            rating = rating,
            workQuality = workQuality,
            punctuality = punctuality,
            behaviour = behaviour,
            review = review
        )
        return dao.insertRating(ratingObj)
    }

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    fun getNotificationsForRole(role: String): Flow<List<NotificationEntity>> = dao.getNotificationsForRole(role)
    suspend fun markNotificationAsRead(id: Long) = dao.markNotificationAsRead(id)

    // Referrals
    fun getReferralForUser(userId: Long): Flow<ReferralEntity?> = dao.getReferralForUser(userId)
}
