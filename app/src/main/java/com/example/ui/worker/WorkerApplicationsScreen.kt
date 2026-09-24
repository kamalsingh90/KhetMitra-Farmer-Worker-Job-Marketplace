package com.example.ui.worker

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationEntity
import com.example.data.model.AttendanceEntity
import com.example.data.model.JobEntity
import com.example.data.model.PaymentEntity
import com.example.ui.common.AppLanguage
import com.example.ui.common.StatusBadge
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary

@Composable
fun WorkerApplicationsScreen(
    applications: List<ApplicationEntity>,
    allJobs: List<JobEntity>,
    attendanceList: List<AttendanceEntity>,
    payments: List<PaymentEntity>,
    lang: AppLanguage,
    onCheckInClick: (Long) -> Unit,
    onCheckOutClick: (AttendanceEntity) -> Unit,
    onConfirmPaymentClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        if (lang == AppLanguage.HINDI) "स्वीकृत काम & हाजिरी" else "Accepted & Check-In",
        if (lang == AppLanguage.HINDI) "सभी आवेदन" else "All Applications",
        if (lang == AppLanguage.HINDI) "भुगतान रिकॉर्ड" else "Payments"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("worker_applications_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> AcceptedAndAttendanceTab(
                applications = applications.filter { it.status == "ACCEPTED" },
                allJobs = allJobs,
                attendanceList = attendanceList,
                lang = lang,
                onCheckIn = onCheckInClick,
                onCheckOut = onCheckOutClick
            )
            1 -> AllApplicationsTab(
                applications = applications,
                allJobs = allJobs,
                lang = lang
            )
            2 -> PaymentsTab(
                payments = payments,
                lang = lang,
                onConfirmPayment = onConfirmPaymentClick
            )
        }
    }
}

@Composable
private fun AcceptedAndAttendanceTab(
    applications: List<ApplicationEntity>,
    allJobs: List<JobEntity>,
    attendanceList: List<AttendanceEntity>,
    lang: AppLanguage,
    onCheckIn: (Long) -> Unit,
    onCheckOut: (AttendanceEntity) -> Unit
) {
    if (applications.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (lang == AppLanguage.HINDI) "अभी कोई स्वीकृत काम नहीं है। नए काम पर आवेदन करें!" else "No accepted jobs yet. Apply for jobs near you!",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(applications) { app ->
                val job = allJobs.find { it.id == app.jobId }
                val attendance = attendanceList.find { it.jobId == app.jobId }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = job?.title ?: "कृषि कार्य (Farm Work)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            StatusBadge(status = "ACCEPTED")
                        }

                        Text(
                            text = "किसान: ${job?.farmerName ?: "Kishan"} • 📞 ${job?.farmerPhone ?: ""}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "📍 ${job?.village ?: ""}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Attendance section
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (lang == AppLanguage.HINDI) "खेत पर कार्य हाजिरी (Attendance Status)" else "Farm Attendance Status",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                if (attendance != null) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "चेक-इन समय: ${attendance.checkInTime ?: "--"}",
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (attendance.farmerConfirmed) "✅ किसान द्वारा सत्यापित" else "⏳ सत्यापन लंबित",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (attendance.farmerConfirmed) ForestGreenPrimary else GoldenWheatSecondary
                                        )
                                    }

                                    if (attendance.checkOutTime != null) {
                                        Text(
                                            text = "चेक-आउट समय: ${attendance.checkOutTime}",
                                            fontSize = 12.sp
                                        )
                                    }
                                } else {
                                    Text(
                                        text = if (lang == AppLanguage.HINDI) "खेत पर पहुँचने के बाद 'चेक-इन' बटन दबाएं" else "Tap Check In upon reaching the farm",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action button
                        if (attendance == null) {
                            Button(
                                onClick = { onCheckIn(app.jobId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("check_in_btn_${app.jobId}"),
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(if (lang == AppLanguage.HINDI) "📍 खेत पर पहुँच गए (Check In)" else "📍 Farm Check In")
                            }
                        } else if (attendance.checkOutTime == null) {
                            Button(
                                onClick = { onCheckOut(attendance) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("check_out_btn_${app.jobId}"),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenWheatSecondary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(if (lang == AppLanguage.HINDI) "🏁 काम पूरा हुआ (Check Out)" else "🏁 Complete Work (Check Out)")
                            }
                        } else {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.HINDI) "✅ आज का कार्य पूर्ण हो चुका है" else "✅ Work completed today",
                                    color = ForestGreenPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AllApplicationsTab(
    applications: List<ApplicationEntity>,
    allJobs: List<JobEntity>,
    lang: AppLanguage
) {
    if (applications.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (lang == AppLanguage.HINDI) "कोई आवेदन उपलब्ध नहीं" else "No applications recorded",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(applications) { app ->
                val job = allJobs.find { it.id == app.jobId }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = job?.title ?: "Farm Job",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            StatusBadge(status = app.status)
                        }
                        Text(
                            text = "गाँव: ${job?.village ?: ""} • मजदूरी: ₹${job?.wage ?: 0}/दिन",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentsTab(
    payments: List<PaymentEntity>,
    lang: AppLanguage,
    onConfirmPayment: (Long) -> Unit
) {
    if (payments.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (lang == AppLanguage.HINDI) "अभी कोई भुगतान रिकॉर्ड नहीं है" else "No payment records found",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(payments) { payment ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = payment.jobTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            StatusBadge(status = payment.status)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "भुगतान राशि: ₹${payment.amount} (${payment.paymentMethod})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                        if (payment.transactionRef.isNotEmpty()) {
                            Text(
                                text = "रसीद/रेफरेंस: ${payment.transactionRef}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (payment.status == "PAID") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onConfirmPayment(payment.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("confirm_payment_${payment.id}"),
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.HINDI) "भुगतान प्राप्त हुआ (Confirm Received)" else "Confirm Payment Received",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
