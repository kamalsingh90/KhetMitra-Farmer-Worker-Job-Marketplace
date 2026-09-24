package com.example.ui.common

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.GoldenWheatSecondary
import com.example.ui.theme.StatusActive
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusPending

@Composable
fun RoleSwitcherBar(
    currentRole: String,
    onRoleSelected: (String) -> Unit,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoleTab(
                title = Strings.farmerRole(lang),
                roleKey = "FARMER",
                isSelected = currentRole == "FARMER",
                icon = Icons.Default.Agriculture,
                onClick = { onRoleSelected("FARMER") },
                modifier = Modifier.weight(1f)
            )
            RoleTab(
                title = Strings.workerRole(lang),
                roleKey = "WORKER",
                isSelected = currentRole == "WORKER",
                icon = Icons.Default.Engineering,
                onClick = { onRoleSelected("WORKER") },
                modifier = Modifier.weight(1f)
            )
            RoleTab(
                title = Strings.adminRole(lang),
                roleKey = "ADMIN",
                isSelected = currentRole == "ADMIN",
                icon = Icons.Default.Info,
                onClick = { onRoleSelected("ADMIN") },
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}

@Composable
private fun RoleTab(
    title: String,
    roleKey: String,
    isSelected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .testTag("role_tab_$roleKey"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "PUBLISHED" -> Triple(Color(0xFFE8F5E9), StatusActive, "खुला है (Open)")
        "PARTIALLY_FILLED" -> Triple(Color(0xFFFEF3C7), GoldenWheatSecondary, "आंशिक भरा")
        "FILLED" -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), "पूर्ण (Filled)")
        "IN_PROGRESS" -> Triple(Color(0xFFFEF3C7), StatusPending, "काम जारी है")
        "COMPLETED" -> Triple(Color(0xFFDBEAFE), StatusCompleted, "पूर्ण (Completed)")
        "ACCEPTED" -> Triple(Color(0xFFE8F5E9), StatusActive, "स्वीकृत (Accepted)")
        "APPLIED" -> Triple(Color(0xFFFEF3C7), GoldenWheatSecondary, "आवेदन भेजा")
        "CHECKED_IN" -> Triple(Color(0xFFE8F5E9), StatusActive, "खेत पर मौजूद (In)")
        "CONFIRMED" -> Triple(Color(0xFFDBEAFE), StatusCompleted, "पुष्टि हो चुकी")
        "PAID" -> Triple(Color(0xFFE8F5E9), StatusActive, "भुगतान हो गया")
        else -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), status)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StarRatingBar(
    rating: Float,
    maxStars: Int = 5,
    onRatingChanged: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            Icon(
                imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = "Star $i",
                tint = if (isFilled) GoldenWheatSecondary else Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .then(
                        if (onRatingChanged != null) Modifier.clickable { onRatingChanged(i.toFloat()) }
                        else Modifier
                    )
            )
        }
    }
}

@Composable
fun AdBanner(
    isEnabled: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = isEnabled) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("ad_banner_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFBEB)
            ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = GoldenWheatSecondary,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "AD",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "किसान क्रेडिट कार्ड और कृषि यंत्र सब्सिडी योजना 2026",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F)
                    )
                    Text(
                        text = "नजदीकी केंद्र पर न्यूनतम ब्याज दर पर खाद एवं बीज लोन उपलब्ध",
                        fontSize = 10.sp,
                        color = Color(0xFF92400E)
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss Ad",
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
