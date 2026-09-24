package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ApplicationEntity
import com.example.data.model.JobEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.UserEntity
import com.example.ui.common.AppLanguage
import com.example.ui.common.StarRatingBar
import com.example.ui.common.StatusBadge
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary

@Composable
fun PostJobDialog(
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    val categories = listOf(
        "Harvesting", "Sowing", "Weeding", "Irrigation", "Planting", "Spraying", "Tractor Work", "Picking"
    )

    var selectedCategory by remember { mutableStateOf("Harvesting") }
    var title by remember { mutableStateOf("गेहूँ कटाई कार्य (Wheat Harvesting)") }
    var description by remember { mutableStateOf("खेत में पके गेहूँ की कटाई और पूले बांधने के लिए मजदूर चाहिए।") }
    var workersRequired by remember { mutableIntStateOf(5) }
    var wage by remember { mutableIntStateOf(550) }
    var workDate by remember { mutableStateOf("Tomorrow (कल)") }
    var startTime by remember { mutableStateOf("08:00 AM") }
    var duration by remember { mutableStateOf("Full day (8 hrs)") }
    var village by remember { mutableStateOf("Taraori Farm (तरावड़ी)") }
    var paymentType by remember { mutableStateOf("Cash / UPI") }
    var foodProvided by remember { mutableStateOf(true) }
    var transportProvided by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .testTag("post_job_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "नया काम पोस्ट करें (+ Post Job)" else "Post a New Farm Job",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Work Category Selection
                Text(
                    text = if (lang == AppLanguage.HINDI) "काम की श्रेणी चुनें (Work Category):" else "Select Category:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = {
                                selectedCategory = cat
                                title = when (cat) {
                                    "Harvesting" -> "गेहूँ/धान कटाई (Harvesting)"
                                    "Sowing" -> "बीज बुआई कार्य (Sowing)"
                                    "Weeding" -> "खरपतवार निराई-गुड़ाई (Weeding)"
                                    "Irrigation" -> "खेत में पानी/सिंचाई (Irrigation)"
                                    "Spraying" -> "कीटनाशक छिड़काव (Spraying)"
                                    "Tractor Work" -> "ट्रैक्टर जुताई कार्य (Tractor)"
                                    else -> "$cat Work"
                                }
                            },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Job Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "काम का नाम (Job Title)" else "Job Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "विवरण (Work Description)" else "Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Workers Count & Daily Wage Steppers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Workers Required
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == AppLanguage.HINDI) "कितने मजदूर चाहिए?" else "Workers Needed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (workersRequired > 1) workersRequired-- },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "$workersRequired",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = { workersRequired++ },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }

                    // Wage
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == AppLanguage.HINDI) "मजदूरी (₹/दिन)" else "Daily Wage (₹)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (wage > 300) wage -= 50 },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "₹$wage",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = { wage += 50 },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Date & Start Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = workDate,
                        onValueChange = { workDate = it },
                        label = { Text(if (lang == AppLanguage.HINDI) "तारीख" else "Date") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text(if (lang == AppLanguage.HINDI) "समय" else "Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Village location
                OutlinedTextField(
                    value = village,
                    onValueChange = { village = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "खेत का गाँव / पता" else "Farm Location / Village") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Perks Checkboxes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = foodProvided,
                        onCheckedChange = { foodProvided = it }
                    )
                    Text(
                        text = if (lang == AppLanguage.HINDI) "दोपहर का भोजन/चाय उपलब्ध है" else "Lunch/Tea provided",
                        fontSize = 12.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = transportProvided,
                        onCheckedChange = { transportProvided = it }
                    )
                    Text(
                        text = if (lang == AppLanguage.HINDI) "गाँव से खेत तक वाहन उपलब्ध है" else "Transport provided from village",
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        onSubmit(
                            title,
                            description,
                            selectedCategory,
                            workersRequired,
                            wage,
                            workDate,
                            startTime,
                            duration,
                            village,
                            paymentType,
                            foodProvided,
                            transportProvided
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_post_job_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "काम प्रकाशित करें (Post Job)" else "Post Job Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun JobDetailsDialog(
    job: JobEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onApply: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = job.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusBadge(status = job.status)
                    Text(
                        text = "₹${job.wage}/दिन",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ForestGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = job.description,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "👨‍🌾 किसान: ${job.farmerName}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "📞 संपर्क: ${job.farmerPhone}", fontSize = 13.sp)
                Text(text = "📍 खेत का पता: ${job.village}, ${job.district}", fontSize = 13.sp)
                Text(text = "📅 तारीख: ${job.workDate} (${job.startTime})", fontSize = 13.sp)
                Text(text = "⏱ अवधि: ${job.duration}", fontSize = 13.sp)
                Text(text = "👷 मजदूर जरूरत: ${job.workersSelected}/${job.workersRequired}", fontSize = 13.sp)
                Text(text = "💰 भुगतान विधि: ${job.paymentType}", fontSize = 13.sp)

                if (job.foodProvided) {
                    Text(text = "🍲 भोजन: किसान द्वारा उपलब्ध", fontSize = 13.sp, color = ForestGreenPrimary)
                }
            }
        },
        confirmButton = {
            if (onApply != null && job.status == "PUBLISHED") {
                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text(if (lang == AppLanguage.HINDI) "आवेदन करें (Apply)" else "Apply Now")
                }
            } else {
                OutlinedButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (lang == AppLanguage.HINDI) "बंद करें" else "Close")
            }
        }
    )
}

@Composable
fun JobApplicantsDialog(
    job: JobEntity,
    applicants: List<ApplicationEntity>,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onAccept: (ApplicationEntity) -> Unit,
    onReject: (Long) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .testTag("job_applicants_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == AppLanguage.HINDI) "मजदूर आवेदन (${applicants.size})" else "Applicants (${applicants.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "${job.title} • ${job.workersSelected}/${job.workersRequired} मजदूर चयनित",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (applicants.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (lang == AppLanguage.HINDI) "अभी कोई नया आवेदन नहीं आया है" else "No worker applications received yet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(applicants) { app ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = app.workerName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "${app.workerRating}★",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = ForestGreenPrimary
                                        )
                                    }

                                    Text(
                                        text = "📞 ${app.workerPhone} • 📍 ${app.workerVillage}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "कौशल: ${app.workerSkills}",
                                        fontSize = 11.sp,
                                        color = ForestGreenPrimary
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (app.status == "APPLIED") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { onAccept(app) },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(if (lang == AppLanguage.HINDI) "स्वीकार करें (Accept)" else "Accept", fontSize = 12.sp)
                                            }
                                            OutlinedButton(
                                                onClick = { onReject(app.id) },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(if (lang == AppLanguage.HINDI) "अस्वीकार" else "Reject", fontSize = 12.sp)
                                            }
                                        }
                                    } else {
                                        StatusBadge(status = app.status)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    job: JobEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitPayment: (workerId: Long, workerName: String, amount: Int, method: String, ref: String) -> Unit
) {
    var amountText by remember { mutableStateOf("${job.wage}") }
    var selectedMethod by remember { mutableStateOf("Cash") }
    var referenceText by remember { mutableStateOf("") }
    var workerName by remember { mutableStateOf("Ramesh Kumar (मजदूर)") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == AppLanguage.HINDI) "मजदूरी भुगतान दर्ज करें" else "Record Wage Payment",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "काम: ${job.title}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = workerName,
                    onValueChange = { workerName = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "मजदूर का नाम" else "Worker Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "भुगतान राशि (₹)" else "Amount (₹)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedMethod == "Cash",
                        onClick = { selectedMethod = "Cash" },
                        label = { Text("नकद (Cash)") }
                    )
                    FilterChip(
                        selected = selectedMethod == "UPI",
                        onClick = { selectedMethod = "UPI" },
                        label = { Text("UPI / Online") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = referenceText,
                    onValueChange = { referenceText = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "रेफरेंस या नोट (वैकल्पिक)" else "Reference / Note (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toIntOrNull() ?: job.wage
                    onSubmitPayment(2L, workerName, amount, selectedMethod, referenceText)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
            ) {
                Text(if (lang == AppLanguage.HINDI) "भुगतान दर्ज करें" else "Record Payment")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (lang == AppLanguage.HINDI) "रद्द करें" else "Cancel")
            }
        }
    )
}

@Composable
fun RatingDialog(
    job: JobEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitRating: (rating: Float, quality: Float, punctuality: Float, behaviour: Float, review: String) -> Unit
) {
    var overallRating by remember { mutableFloatStateOf(5.0f) }
    var reviewText by remember { mutableStateOf("बहुत अच्छा कार्य किया।") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (lang == AppLanguage.HINDI) "रेटिंग और समीक्षा दें" else "Rate & Review",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "काम: ${job.title}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                StarRatingBar(
                    rating = overallRating,
                    onRatingChanged = { overallRating = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text(if (lang == AppLanguage.HINDI) "आपकी समीक्षा / अनुभव" else "Your Review") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitRating(overallRating, overallRating, overallRating, overallRating, reviewText)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
            ) {
                Text(if (lang == AppLanguage.HINDI) "सबमिट करें" else "Submit Rating")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (lang == AppLanguage.HINDI) "रद्द करें" else "Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralSheet(
    referral: ReferralEntity?,
    user: UserEntity?,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val code = referral?.referralCode ?: user?.referralCode ?: "KHET100"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (lang == AppLanguage.HINDI) "मित्रों को जोड़ें और रिवार्ड पाएं!" else "Refer Farmers & Workers, Earn Rewards!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (lang == AppLanguage.HINDI)
                    "जब आपका दोस्त पहली बार काम पूरा करेगा, आपको ₹100 का नकद इनाम मिलेगा!"
                else "Earn ₹100 reward when your referred friend completes their first farm job!",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Code Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (lang == AppLanguage.HINDI) "आपका रेफरल कोड" else "Your Referral Code",
                            fontSize = 11.sp
                        )
                        Text(
                            text = code,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ForestGreenPrimary
                        )
                    }
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Referral Code", code))
                            Toast.makeText(context, "कोड कॉपी हो गया! ($code)", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rewards Wallet Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("₹${referral?.totalEarnings ?: 300}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        Text("कुल कमाई", fontSize = 11.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${referral?.completedReferrals ?: 3}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("सफल रेफरल", fontSize = 11.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("₹${referral?.pendingEarnings ?: 100}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldenWheatSecondary)
                        Text("लंबित बोनस", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    notifications: List<NotificationEntity>,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onMarkRead: (Long) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = if (lang == AppLanguage.HINDI) "सूचनाएं (Notifications)" else "Notifications",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "कोई नई सूचना नहीं है", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMarkRead(notif.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (notif.isRead) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = notif.body,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
