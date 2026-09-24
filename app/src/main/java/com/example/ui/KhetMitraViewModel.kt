package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.KhetMitraRepository
import com.example.data.model.ApplicationEntity
import com.example.data.model.AttendanceEntity
import com.example.data.model.JobEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.RatingEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.UserEntity
import com.example.ui.common.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class JobFilterCriteria(
    val query: String,
    val category: String,
    val distance: Float,
    val wage: Int,
    val sort: String
)

class KhetMitraViewModel(
    private val repository: KhetMitraRepository
) : ViewModel() {

    // Current Active Role: "FARMER", "WORKER", "ADMIN"
    private val _currentRole = MutableStateFlow("FARMER")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    // Current App Language
    private val _language = MutableStateFlow(AppLanguage.HINDI)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Ads toggle (From PRD section 28 Ad Management)
    private val _adsEnabled = MutableStateFlow(true)
    val adsEnabled: StateFlow<Boolean> = _adsEnabled.asStateFlow()

    // Filter states for worker discovery
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val maxDistanceKm = MutableStateFlow(10f)
    val minWage = MutableStateFlow(400)
    val sortBy = MutableStateFlow("Latest") // "Latest", "Nearest", "Highest Wage"

    // Active User
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser: StateFlow<UserEntity?> = combine(allUsers, currentRole) { users, role ->
        when (role) {
            "FARMER" -> users.find { it.role == "FARMER" }
            "WORKER" -> users.find { it.role == "WORKER" && it.id == 2L }
            else -> users.find { it.role == "ADMIN" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Jobs
    val allJobs: StateFlow<List<JobEntity>> = repository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val filterCriteria = combine(
        searchQuery,
        selectedCategory,
        maxDistanceKm,
        minWage,
        sortBy
    ) { q, cat, dist, w, s ->
        JobFilterCriteria(q, cat, dist, w, s)
    }

    // Filtered jobs for worker
    val filteredJobs: StateFlow<List<JobEntity>> = combine(allJobs, filterCriteria) { jobs, filters ->
        var list = jobs.filter { job ->
            job.status != "CANCELLED" &&
            (filters.category == "All" || job.category.equals(filters.category, ignoreCase = true)) &&
            job.distanceKm <= filters.distance &&
            job.wage >= filters.wage &&
            (filters.query.isBlank() || job.title.contains(filters.query, ignoreCase = true) || job.village.contains(filters.query, ignoreCase = true))
        }

        list = when (filters.sort) {
            "Nearest" -> list.sortedBy { it.distanceKm }
            "Highest Wage" -> list.sortedByDescending { it.wage }
            else -> list.sortedByDescending { it.createdAt }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Farmer's jobs
    val farmerJobs: StateFlow<List<JobEntity>> = combine(allJobs, currentUser) { jobs, user ->
        if (user != null && user.role == "FARMER") {
            jobs.filter { it.farmerId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Applications
    val allApplications: StateFlow<List<ApplicationEntity>> = repository.allApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workerApplications: StateFlow<List<ApplicationEntity>> = combine(allApplications, currentUser) { apps, user ->
        if (user != null && user.role == "WORKER") {
            apps.filter { it.workerId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Attendance
    val allAttendance: StateFlow<List<AttendanceEntity>> = repository.allAttendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Payments
    val allPayments: StateFlow<List<PaymentEntity>> = repository.allPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ratings
    val allRatings: StateFlow<List<RatingEntity>> = repository.allRatings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications
    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Referral
    val referral: StateFlow<ReferralEntity?> = combine(currentUser, allUsers) { user, _ ->
        ReferralEntity(
            userId = user?.id ?: 1L,
            referralCode = user?.referralCode ?: "KHET100",
            totalEarnings = 300,
            pendingEarnings = 100,
            completedReferrals = 3
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI Dialog State Management
    private val _selectedJobForDetails = MutableStateFlow<JobEntity?>(null)
    val selectedJobForDetails: StateFlow<JobEntity?> = _selectedJobForDetails.asStateFlow()

    private val _selectedJobForApplicants = MutableStateFlow<JobEntity?>(null)
    val selectedJobForApplicants: StateFlow<JobEntity?> = _selectedJobForApplicants.asStateFlow()

    private val _showPostJobDialog = MutableStateFlow(false)
    val showPostJobDialog: StateFlow<Boolean> = _showPostJobDialog.asStateFlow()

    private val _showReferralSheet = MutableStateFlow(false)
    val showReferralSheet: StateFlow<Boolean> = _showReferralSheet.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _selectedJobForPayment = MutableStateFlow<JobEntity?>(null)
    val selectedJobForPayment: StateFlow<JobEntity?> = _selectedJobForPayment.asStateFlow()

    private val _selectedJobForRating = MutableStateFlow<JobEntity?>(null)
    val selectedJobForRating: StateFlow<JobEntity?> = _selectedJobForRating.asStateFlow()

    // Role switching
    fun switchRole(role: String) {
        _currentRole.value = role
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.HINDI) AppLanguage.ENGLISH else AppLanguage.HINDI
    }

    fun toggleAds() {
        _adsEnabled.value = !_adsEnabled.value
    }

    // Dialog controls
    fun openPostJob() { _showPostJobDialog.value = true }
    fun closePostJob() { _showPostJobDialog.value = false }

    fun openJobDetails(job: JobEntity) { _selectedJobForDetails.value = job }
    fun closeJobDetails() { _selectedJobForDetails.value = null }

    fun openApplicants(job: JobEntity) { _selectedJobForApplicants.value = job }
    fun closeApplicants() { _selectedJobForApplicants.value = null }

    fun openReferral() { _showReferralSheet.value = true }
    fun closeReferral() { _showReferralSheet.value = false }

    fun openNotifications() { _showNotificationsSheet.value = true }
    fun closeNotifications() { _showNotificationsSheet.value = false }

    fun openPayment(job: JobEntity) { _selectedJobForPayment.value = job }
    fun closePayment() { _selectedJobForPayment.value = null }

    fun openRating(job: JobEntity) { _selectedJobForRating.value = job }
    fun closeRating() { _selectedJobForRating.value = null }

    // Business actions
    fun postJob(
        title: String,
        description: String,
        category: String,
        workersRequired: Int,
        wage: Int,
        workDate: String,
        startTime: String,
        duration: String,
        village: String,
        paymentType: String,
        foodProvided: Boolean,
        transportProvided: Boolean
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val newJob = JobEntity(
                farmerId = user.id,
                farmerName = user.name,
                farmerPhone = user.phone,
                title = title,
                description = description,
                category = category,
                workersRequired = workersRequired,
                workersSelected = 0,
                wage = wage,
                paymentType = paymentType,
                workDate = workDate,
                startTime = startTime,
                duration = duration,
                village = village,
                district = user.district,
                distanceKm = 1.5f,
                foodProvided = foodProvided,
                transportProvided = transportProvided,
                status = "PUBLISHED"
            )
            repository.postJob(newJob)
            closePostJob()
        }
    }

    fun applyForJob(job: JobEntity) {
        viewModelScope.launch {
            val worker = currentUser.value ?: return@launch
            repository.applyForJob(job, worker)
        }
    }

    fun acceptApplication(app: ApplicationEntity, jobTitle: String) {
        viewModelScope.launch {
            repository.acceptApplication(app, jobTitle)
        }
    }

    fun rejectApplication(appId: Long) {
        viewModelScope.launch {
            repository.rejectApplication(appId)
        }
    }

    fun checkInWorker(jobId: Long) {
        viewModelScope.launch {
            val worker = currentUser.value ?: return@launch
            repository.checkInWorker(jobId, worker)
        }
    }

    fun checkOutWorker(attendance: AttendanceEntity) {
        viewModelScope.launch {
            repository.checkOutWorker(attendance)
        }
    }

    fun confirmAttendance(attendance: AttendanceEntity) {
        viewModelScope.launch {
            repository.confirmAttendanceByFarmer(attendance)
        }
    }

    fun recordOfflinePayment(
        job: JobEntity,
        workerId: Long,
        workerName: String,
        amount: Int,
        method: String,
        reference: String
    ) {
        viewModelScope.launch {
            repository.recordOfflinePayment(
                jobId = job.id,
                jobTitle = job.title,
                farmerId = job.farmerId,
                workerId = workerId,
                workerName = workerName,
                amount = amount,
                method = method,
                reference = reference
            )
            closePayment()
        }
    }

    fun confirmPaymentReceived(paymentId: Long) {
        viewModelScope.launch {
            val worker = currentUser.value ?: return@launch
            repository.confirmPaymentReceived(paymentId, worker.name)
        }
    }

    fun submitRating(
        jobId: Long,
        toUserId: Long,
        toUserName: String,
        rating: Float,
        workQuality: Float,
        punctuality: Float,
        behaviour: Float,
        review: String
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.submitRating(
                jobId = jobId,
                fromUserId = user.id,
                fromUserName = user.name,
                toUserId = toUserId,
                toUserName = toUserName,
                rating = rating,
                workQuality = workQuality,
                punctuality = punctuality,
                behaviour = behaviour,
                review = review
            )
            closeRating()
        }
    }

    fun toggleAvailability() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val nextAvailability = when (user.availability) {
                "AVAILABLE_TODAY" -> "AVAILABLE_TOMORROW"
                "AVAILABLE_TOMORROW" -> "BUSY"
                else -> "AVAILABLE_TODAY"
            }
            repository.updateUser(user.copy(availability = nextAvailability))
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }
}

class KhetMitraViewModelFactory(private val repository: KhetMitraRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KhetMitraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KhetMitraViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
