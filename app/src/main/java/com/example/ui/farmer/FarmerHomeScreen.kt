package com.example.ui.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationEntity
import com.example.data.model.AttendanceEntity
import com.example.data.model.JobEntity
import com.example.data.model.UserEntity
import com.example.ui.common.AppLanguage
import com.example.ui.common.StatusBadge
import com.example.ui.theme.ForestGreenLight
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary

@Composable
fun FarmerHomeScreen(
    farmer: UserEntity?,
    jobs: List<JobEntity>,
    allApplications: List<ApplicationEntity>,
    allAttendance: List<AttendanceEntity>,
    nearbyWorkers: List<UserEntity>,
    lang: AppLanguage,
    onPostJobClick: () -> Unit,
    onViewApplicantsClick: (JobEntity) -> Unit,
    onJobDetailsClick: (JobEntity) -> Unit,
    onRecordPaymentClick: (JobEntity) -> Unit,
    onRateJobClick: (JobEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("farmer_home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Farmer Header Greeting
        item {
            FarmerHeaderSection(farmer = farmer, lang = lang)
        }

        // 2. Main Primary Hero CTA: "+ मुझे मजदूर चाहिए (नया काम पोस्ट करें)"
        item {
            FarmerHeroCta(
                lang = lang,
                onClick = onPostJobClick
            )
        }

        // 3. Quick Stats summary cards
        item {
            val activeJobsCount = jobs.count { it.status == "PUBLISHED" || it.status == "PARTIALLY_FILLED" }
            val pendingAppsCount = allApplications.count { it.status == "APPLIED" }
            val checkedInTodayCount = allAttendance.count { it.status == "CHECKED_IN" }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickStatCard(
                    title = if (lang == AppLanguage.HINDI) "सक्रिय काम" else "Active Jobs",
                    value = "$activeJobsCount",
                    subtext = if (lang == AppLanguage.HINDI) "खेत में" else "In field",
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = if (lang == AppLanguage.HINDI) "नए आवेदन" else "New Applicants",
                    value = "$pendingAppsCount",
                    subtext = if (lang == AppLanguage.HINDI) "मजदूर इच्छुक" else "Waiting",
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = if (lang == AppLanguage.HINDI) "खेत पर हाजिरी" else "Present Today",
                    value = "$checkedInTodayCount",
                    subtext = if (lang == AppLanguage.HINDI) "मजदूर पहुंचे" else "Checked In",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. Section Title: Active Jobs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == AppLanguage.HINDI) "आपके पोस्ट किए गए काम (My Jobs)" else "Your Posted Jobs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${jobs.size} ${if (lang == AppLanguage.HINDI) "काम" else "Total"}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 5. Jobs list
        if (jobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (lang == AppLanguage.HINDI) "अभी कोई सक्रिय काम नहीं है" else "No active jobs posted yet",
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onPostJobClick) {
                            Text(if (lang == AppLanguage.HINDI) "+ नया काम डालें" else "+ Post a Job")
                        }
                    }
                }
            }
        } else {
            items(jobs) { job ->
                val jobApps = allApplications.filter { it.jobId == job.id }
                FarmerJobCard(
                    job = job,
                    applicantsCount = jobApps.size,
                    pendingCount = jobApps.count { it.status == "APPLIED" },
                    lang = lang,
                    onViewApplicants = { onViewApplicantsClick(job) },
                    onDetailsClick = { onJobDetailsClick(job) },
                    onRecordPayment = { onRecordPaymentClick(job) },
                    onRateJob = { onRateJobClick(job) }
                )
            }
        }

        // 6. Nearby Recommended Workers
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "आसपास उपलब्ध कुशल मजदूर" else "Available Nearby Workers",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(nearbyWorkers) { worker ->
                        RecommendedWorkerCard(worker = worker, lang = lang)
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerHeaderSection(
    farmer: UserEntity?,
    lang: AppLanguage
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ForestGreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌾",
                    fontSize = 26.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (lang == AppLanguage.HINDI) "नमस्ते, ${farmer?.name ?: "किशन जी"}" else "Good Morning, ${farmer?.name ?: "Kishan"}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${farmer?.village ?: "तरावड़ी"}, ${farmer?.district ?: "करनाल"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ForestGreenPrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${farmer?.rating ?: 4.9}★",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FarmerHeroCta(
    lang: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("post_job_hero_cta"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForestGreenPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (lang == AppLanguage.HINDI) "+ मुझे मजदूर चाहिए" else "+ Need Farm Workers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lang == AppLanguage.HINDI) "नया काम पोस्ट करें • आसपास के मजदूरों तक पहुंचेगा" else "Post a new job • Reach nearby verified workers instantly",
                    fontSize = 13.sp,
                    color = Color(0xFFE8F5E9)
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Post Job",
                    tint = ForestGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    subtext: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtext,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerJobCard(
    job: JobEntity,
    applicantsCount: Int,
    pendingCount: Int,
    lang: AppLanguage,
    onViewApplicants: () -> Unit,
    onDetailsClick: () -> Unit,
    onRecordPayment: () -> Unit,
    onRateJob: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onDetailsClick)
            .testTag("job_card_${job.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Category chip & Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = job.category,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                StatusBadge(status = job.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = job.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Key details grid: Date, Time, Wage, Workers Required
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = ForestGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${job.workDate} • ${job.startTime}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Workers",
                        modifier = Modifier.size(14.dp),
                        tint = GoldenWheatSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${job.workersSelected}/${job.workersRequired} ${if (lang == AppLanguage.HINDI) "मजदूर" else "workers"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (job.workersSelected >= job.workersRequired) ForestGreenPrimary else GoldenWheatSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Wage & Village location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${job.wage}${if (lang == AppLanguage.HINDI) "/दिन प्रति मजदूर" else "/day per worker"}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ForestGreenPrimary
                )
                Text(
                    text = "📍 ${job.village}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewApplicants,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("view_applicants_${job.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pendingCount > 0) ForestGreenPrimary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (pendingCount > 0) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (pendingCount > 0) "${if (lang == AppLanguage.HINDI) "आवेदक देखें" else "Applicants"} ($pendingCount)"
                               else if (lang == AppLanguage.HINDI) "आवेदक ($applicantsCount)" else "Applicants ($applicantsCount)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onRecordPayment,
                    modifier = Modifier.weight(0.9f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Payment",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (lang == AppLanguage.HINDI) "भुगतान" else "Pay",
                        fontSize = 12.sp
                    )
                }

                if (job.status == "COMPLETED") {
                    OutlinedButton(
                        onClick = onRateJob,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "★ ${if (lang == AppLanguage.HINDI) "रेटिंग" else "Rate"}",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedWorkerCard(
    worker: UserEntity,
    lang: AppLanguage
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .testTag("worker_card_${worker.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👷", fontSize = 16.sp)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = "${worker.rating}★",
                        color = Color(0xFFB45309),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = worker.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1
            )
            Text(
                text = "📍 ${worker.village}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = worker.skills,
                fontSize = 10.sp,
                color = ForestGreenPrimary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = if (worker.availability == "AVAILABLE_TODAY") Color(0xFFE8F5E9) else Color(0xFFF3F4F6)
            ) {
                Text(
                    text = if (worker.availability == "AVAILABLE_TODAY")
                        (if (lang == AppLanguage.HINDI) "आज उपलब्ध है" else "Available Today")
                    else (if (lang == AppLanguage.HINDI) "कल उपलब्ध" else "Available Tomorrow"),
                    fontSize = 10.sp,
                    color = if (worker.availability == "AVAILABLE_TODAY") ForestGreenPrimary else Color.DarkGray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                )
            }
        }
    }
}
