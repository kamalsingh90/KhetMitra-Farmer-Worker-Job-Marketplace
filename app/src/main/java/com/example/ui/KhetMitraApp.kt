package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.common.AdBanner
import com.example.ui.common.AppLanguage
import com.example.ui.common.RoleSwitcherBar
import com.example.ui.common.Strings
import com.example.ui.dialogs.JobApplicantsDialog
import com.example.ui.dialogs.JobDetailsDialog
import com.example.ui.dialogs.NotificationsSheet
import com.example.ui.dialogs.PaymentDialog
import com.example.ui.dialogs.PostJobDialog
import com.example.ui.dialogs.RatingDialog
import com.example.ui.dialogs.ReferralSheet
import com.example.ui.farmer.FarmerHomeScreen
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary
import com.example.ui.worker.WorkerApplicationsScreen
import com.example.ui.worker.WorkerHomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhetMitraApp(
    viewModel: KhetMitraViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val adsEnabled by viewModel.adsEnabled.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val allJobs by viewModel.allJobs.collectAsStateWithLifecycle()
    val farmerJobs by viewModel.farmerJobs.collectAsStateWithLifecycle()
    val filteredJobs by viewModel.filteredJobs.collectAsStateWithLifecycle()
    val allApplications by viewModel.allApplications.collectAsStateWithLifecycle()
    val workerApplications by viewModel.workerApplications.collectAsStateWithLifecycle()
    val allAttendance by viewModel.allAttendance.collectAsStateWithLifecycle()
    val allPayments by viewModel.allPayments.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val referral by viewModel.referral.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

    // Dialog states
    val showPostJobDialog by viewModel.showPostJobDialog.collectAsStateWithLifecycle()
    val selectedJobForDetails by viewModel.selectedJobForDetails.collectAsStateWithLifecycle()
    val selectedJobForApplicants by viewModel.selectedJobForApplicants.collectAsStateWithLifecycle()
    val selectedJobForPayment by viewModel.selectedJobForPayment.collectAsStateWithLifecycle()
    val selectedJobForRating by viewModel.selectedJobForRating.collectAsStateWithLifecycle()
    val showReferralSheet by viewModel.showReferralSheet.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()

    var farmerBottomNavIndex by remember { mutableIntStateOf(0) }
    var workerBottomNavIndex by remember { mutableIntStateOf(0) }

    val unreadNotificationsCount = notifications.count { !it.isRead }
    val nearbyWorkers = allUsers.filter { it.role == "WORKER" }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ForestGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🌾", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = Strings.appName(language),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "खेती मजदूर & किसान मंच" else "Farmer & Labor Marketplace",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Referral Rewards button
                    IconButton(
                        onClick = { viewModel.openReferral() },
                        modifier = Modifier.testTag("referral_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = "Referral",
                            tint = GoldenWheatSecondary
                        )
                    }

                    // Notifications button
                    IconButton(
                        onClick = { viewModel.openNotifications() },
                        modifier = Modifier.testTag("notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationsCount > 0) {
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text("$unreadNotificationsCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }

                    // Language Toggle
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("language_toggle_btn")
                        ) {
                            IconButton(
                                onClick = { viewModel.toggleLanguage() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Language",
                                    modifier = Modifier.size(16.dp),
                                    tint = ForestGreenPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "English" else "हिंदी",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (currentRole == "FARMER") {
                NavigationBar {
                    NavigationBarItem(
                        selected = farmerBottomNavIndex == 0,
                        onClick = { farmerBottomNavIndex = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (language == AppLanguage.HINDI) "होम" else "Home", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = farmerBottomNavIndex == 1,
                        onClick = { viewModel.openPostJob() },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = "Post Job", tint = ForestGreenPrimary) },
                        label = { Text(if (language == AppLanguage.HINDI) "+ नया काम" else "+ Post Job", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    NavigationBarItem(
                        selected = farmerBottomNavIndex == 2,
                        onClick = { farmerBottomNavIndex = 2 },
                        icon = { Icon(Icons.Default.Group, contentDescription = "Applicants") },
                        label = { Text(if (language == AppLanguage.HINDI) "आवेदक" else "Applicants", fontSize = 11.sp) }
                    )
                }
            } else if (currentRole == "WORKER") {
                NavigationBar {
                    NavigationBarItem(
                        selected = workerBottomNavIndex == 0,
                        onClick = { workerBottomNavIndex = 0 },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Find Jobs") },
                        label = { Text(if (language == AppLanguage.HINDI) "काम खोजें" else "Find Jobs", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = workerBottomNavIndex == 1,
                        onClick = { workerBottomNavIndex = 1 },
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = "My Work") },
                        label = { Text(if (language == AppLanguage.HINDI) "हाजिरी & कार्य" else "My Work", fontSize = 11.sp) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Role switcher (Farmer, Worker, Admin)
            RoleSwitcherBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.switchRole(it) },
                lang = language
            )

            // 2. Dismissable Ad Banner (PRD Section 28)
            AdBanner(
                isEnabled = adsEnabled,
                onDismiss = { viewModel.toggleAds() }
            )

            // 3. Screen content depending on active role & tab
            when (currentRole) {
                "FARMER" -> {
                    when (farmerBottomNavIndex) {
                        0 -> {
                            FarmerHomeScreen(
                                farmer = currentUser,
                                jobs = farmerJobs,
                                allApplications = allApplications,
                                allAttendance = allAttendance,
                                nearbyWorkers = nearbyWorkers,
                                lang = language,
                                onPostJobClick = { viewModel.openPostJob() },
                                onViewApplicantsClick = { viewModel.openApplicants(it) },
                                onJobDetailsClick = { viewModel.openJobDetails(it) },
                                onRecordPaymentClick = { viewModel.openPayment(it) },
                                onRateJobClick = { viewModel.openRating(it) }
                            )
                        }
                        2 -> {
                            // View all applicants across all jobs
                            val firstJob = farmerJobs.firstOrNull()
                            if (firstJob != null) {
                                val applicants = allApplications.filter { it.jobId == firstJob.id }
                                JobApplicantsDialog(
                                    job = firstJob,
                                    applicants = applicants,
                                    lang = language,
                                    onDismiss = { farmerBottomNavIndex = 0 },
                                    onAccept = { viewModel.acceptApplication(it, firstJob.title) },
                                    onReject = { viewModel.rejectApplication(it) }
                                )
                            }
                            FarmerHomeScreen(
                                farmer = currentUser,
                                jobs = farmerJobs,
                                allApplications = allApplications,
                                allAttendance = allAttendance,
                                nearbyWorkers = nearbyWorkers,
                                lang = language,
                                onPostJobClick = { viewModel.openPostJob() },
                                onViewApplicantsClick = { viewModel.openApplicants(it) },
                                onJobDetailsClick = { viewModel.openJobDetails(it) },
                                onRecordPaymentClick = { viewModel.openPayment(it) },
                                onRateJobClick = { viewModel.openRating(it) }
                            )
                        }
                    }
                }
                "WORKER" -> {
                    when (workerBottomNavIndex) {
                        0 -> {
                            WorkerHomeScreen(
                                worker = currentUser,
                                jobs = filteredJobs,
                                workerApplications = workerApplications,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.searchQuery.value = it },
                                selectedCategory = selectedCategory,
                                onSelectCategory = { viewModel.selectedCategory.value = it },
                                sortBy = sortBy,
                                onSortByChange = { viewModel.sortBy.value = it },
                                lang = language,
                                onApplyClick = { viewModel.applyForJob(it) },
                                onJobDetailsClick = { viewModel.openJobDetails(it) },
                                onToggleAvailability = { viewModel.toggleAvailability() }
                            )
                        }
                        1 -> {
                            WorkerApplicationsScreen(
                                applications = workerApplications,
                                allJobs = allJobs,
                                attendanceList = allAttendance,
                                payments = allPayments.filter { it.workerId == (currentUser?.id ?: 2L) },
                                lang = language,
                                onCheckInClick = { viewModel.checkInWorker(it) },
                                onCheckOutClick = { viewModel.checkOutWorker(it) },
                                onConfirmPaymentClick = { viewModel.confirmPaymentReceived(it) }
                            )
                        }
                    }
                }
                "ADMIN" -> {
                    AdminDashboardScreen(
                        users = allUsers,
                        jobs = allJobs,
                        payments = allPayments,
                        adsEnabled = adsEnabled,
                        onToggleAds = { viewModel.toggleAds() },
                        lang = language
                    )
                }
            }
        }
    }

    // Dialogs
    if (showPostJobDialog) {
        PostJobDialog(
            lang = language,
            onDismiss = { viewModel.closePostJob() },
            onSubmit = { title, desc, cat, req, wage, date, time, duration, village, pType, food, trans ->
                viewModel.postJob(title, desc, cat, req, wage, date, time, duration, village, pType, food, trans)
            }
        )
    }

    selectedJobForDetails?.let { job ->
        JobDetailsDialog(
            job = job,
            lang = language,
            onDismiss = { viewModel.closeJobDetails() },
            onApply = if (currentRole == "WORKER") { { viewModel.applyForJob(job) } } else null
        )
    }

    selectedJobForApplicants?.let { job ->
        val applicants = allApplications.filter { it.jobId == job.id }
        JobApplicantsDialog(
            job = job,
            applicants = applicants,
            lang = language,
            onDismiss = { viewModel.closeApplicants() },
            onAccept = { viewModel.acceptApplication(it, job.title) },
            onReject = { viewModel.rejectApplication(it) }
        )
    }

    selectedJobForPayment?.let { job ->
        PaymentDialog(
            job = job,
            lang = language,
            onDismiss = { viewModel.closePayment() },
            onSubmitPayment = { wId, wName, amt, method, ref ->
                viewModel.recordOfflinePayment(job, wId, wName, amt, method, ref)
            }
        )
    }

    selectedJobForRating?.let { job ->
        RatingDialog(
            job = job,
            lang = language,
            onDismiss = { viewModel.closeRating() },
            onSubmitRating = { r, q, p, b, rev ->
                viewModel.submitRating(job.id, 2L, "Ramesh Kumar", r, q, p, b, rev)
            }
        )
    }

    if (showReferralSheet) {
        ReferralSheet(
            referral = referral,
            user = currentUser,
            lang = language,
            onDismiss = { viewModel.closeReferral() }
        )
    }

    if (showNotificationsSheet) {
        NotificationsSheet(
            notifications = notifications,
            lang = language,
            onDismiss = { viewModel.closeNotifications() },
            onMarkRead = { viewModel.markNotificationRead(it) }
        )
    }
}
