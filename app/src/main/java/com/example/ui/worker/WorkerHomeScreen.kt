package com.example.ui.worker

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.JobEntity
import com.example.data.model.UserEntity
import com.example.ui.common.AppLanguage
import com.example.ui.common.StatusBadge
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary

@Composable
fun WorkerHomeScreen(
    worker: UserEntity?,
    jobs: List<JobEntity>,
    workerApplications: List<ApplicationEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    sortBy: String,
    onSortByChange: (String) -> Unit,
    lang: AppLanguage,
    onApplyClick: (JobEntity) -> Unit,
    onJobDetailsClick: (JobEntity) -> Unit,
    onToggleAvailability: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All",
        "Harvesting",
        "Sowing",
        "Weeding",
        "Irrigation",
        "Planting",
        "Spraying",
        "Tractor Work",
        "Picking"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("worker_home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Worker Header & Availability Status
        item {
            WorkerHeaderSection(
                worker = worker,
                lang = lang,
                onToggleAvailability = onToggleAvailability
            )
        }

        // 2. Search Box
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("worker_search_bar"),
                placeholder = {
                    Text(
                        if (lang == AppLanguage.HINDI) "काम या गाँव खोजें (उदा: गेहूँ कटाई, नीलोखेड़ी)"
                        else "Search jobs or village (e.g. Harvesting, Taraori)"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // 3. Category Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                    val label = when (cat) {
                        "All" -> if (lang == AppLanguage.HINDI) "सभी (All)" else "All"
                        "Harvesting" -> if (lang == AppLanguage.HINDI) "कटाई" else "Harvesting"
                        "Sowing" -> if (lang == AppLanguage.HINDI) "बुआई" else "Sowing"
                        "Weeding" -> if (lang == AppLanguage.HINDI) "निराई" else "Weeding"
                        "Irrigation" -> if (lang == AppLanguage.HINDI) "सिंचाई" else "Irrigation"
                        "Planting" -> if (lang == AppLanguage.HINDI) "रोपाई" else "Planting"
                        "Spraying" -> if (lang == AppLanguage.HINDI) "छिड़काव" else "Spraying"
                        "Tractor Work" -> if (lang == AppLanguage.HINDI) "ट्रैक्टर" else "Tractor"
                        "Picking" -> if (lang == AppLanguage.HINDI) "तुड़ाई" else "Picking"
                        else -> cat
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectCategory(cat) },
                        label = { Text(text = label, fontSize = 12.sp) },
                        modifier = Modifier.testTag("category_chip_$cat")
                    )
                }
            }
        }

        // 4. Sort selection row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == AppLanguage.HINDI) "आसपास उपलब्ध काम (${jobs.size})" else "Jobs Near You (${jobs.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SortChip(title = if (lang == AppLanguage.HINDI) "नजदीक" else "Nearest", isSelected = sortBy == "Nearest", onClick = { onSortByChange("Nearest") })
                    SortChip(title = if (lang == AppLanguage.HINDI) "अधिक मजदूरी" else "Highest Wage", isSelected = sortBy == "Highest Wage", onClick = { onSortByChange("Highest Wage") })
                    SortChip(title = if (lang == AppLanguage.HINDI) "नवीनतम" else "Latest", isSelected = sortBy == "Latest", onClick = { onSortByChange("Latest") })
                }
            }
        }

        // 5. Jobs List
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
                            text = if (lang == AppLanguage.HINDI) "इस श्रेणी में कोई काम उपलब्ध नहीं मिला" else "No matching jobs found nearby",
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.HINDI) "फ़िल्टर बदलें या थोड़ी देर में पुनः प्रयास करें" else "Try clearing filters or search query",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(jobs) { job ->
                val userApp = workerApplications.find { it.jobId == job.id }
                WorkerJobCard(
                    job = job,
                    existingApplication = userApp,
                    lang = lang,
                    onApply = { onApplyClick(job) },
                    onDetails = { onJobDetailsClick(job) }
                )
            }
        }
    }
}

@Composable
private fun SortChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun WorkerHeaderSection(
    worker: UserEntity?,
    lang: AppLanguage,
    onToggleAvailability: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(ForestGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👷", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "राम-राम, ${worker?.name ?: "रमेश जी"}" else "Good Morning, ${worker?.name ?: "Ramesh"}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "📍 ${worker?.village ?: "नीलोखेड़ी"}, ${worker?.district ?: "करनाल"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ForestGreenPrimary
                ) {
                    Text(
                        text = "${worker?.rating ?: 4.8}★",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Availability toggle chip (from PRD Missing Feature 40)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lang == AppLanguage.HINDI) "काम के लिए उपलब्धता:" else "Availability Status:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = when (worker?.availability) {
                        "AVAILABLE_TODAY" -> Color(0xFF16A34A)
                        "AVAILABLE_TOMORROW" -> GoldenWheatSecondary
                        else -> Color(0xFF6B7280)
                    },
                    modifier = Modifier.clickable(onClick = onToggleAvailability)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (worker?.availability) {
                                "AVAILABLE_TODAY" -> if (lang == AppLanguage.HINDI) "🟢 आज उपलब्ध (Available Today)" else "🟢 Available Today"
                                "AVAILABLE_TOMORROW" -> if (lang == AppLanguage.HINDI) "🟡 कल उपलब्ध" else "🟡 Available Tomorrow"
                                else -> if (lang == AppLanguage.HINDI) "⚪ अभी व्यस्त (Busy)" else "⚪ Busy"
                            },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkerJobCard(
    job: JobEntity,
    existingApplication: ApplicationEntity?,
    lang: AppLanguage,
    onApply: () -> Unit,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onDetails)
            .testTag("worker_job_card_${job.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Category & Distance Row
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

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Distance",
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${job.distanceKm} km away",
                            color = ForestGreenPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Job Title
            Text(
                text = job.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "खेत मालिक: ${job.farmerName} • 📍 ${job.village}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Key info chips: Wage & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${job.wage}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = if (lang == AppLanguage.HINDI) "प्रति दिन मजदूरी" else "per day wage",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = GoldenWheatSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${job.workDate} • ${job.startTime}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Food & Transport tags
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (job.foodProvided) {
                    PerkBadge(label = if (lang == AppLanguage.HINDI) "🍲 भोजन उपलब्ध" else "🍲 Food Provided")
                }
                if (job.transportProvided) {
                    PerkBadge(label = if (lang == AppLanguage.HINDI) "🚜 वाहन उपलब्ध" else "🚜 Transport Provided")
                }
                PerkBadge(label = "⏱ ${job.duration}")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Apply CTA Button
            if (existingApplication != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = if (existingApplication.status == "ACCEPTED") Color(0xFFE8F5E9) else Color(0xFFFEF3C7)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Status",
                            tint = if (existingApplication.status == "ACCEPTED") ForestGreenPrimary else GoldenWheatSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (existingApplication.status == "ACCEPTED")
                                (if (lang == AppLanguage.HINDI) "स्वीकृत! खेत पर समय से पहुँचें" else "Accepted! Please arrive on time")
                            else (if (lang == AppLanguage.HINDI) "आवेदन भेजा जा चुका है (Pending)" else "Application Submitted (Pending)"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (existingApplication.status == "ACCEPTED") ForestGreenPrimary else GoldenWheatSecondary
                        )
                    }
                }
            } else {
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("apply_job_btn_${job.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreenPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "आवेदन करें (Apply Now)" else "Apply Now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PerkBadge(label: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
