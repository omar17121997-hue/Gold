package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VerificationStatus
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.StatusCautionYellow
import com.example.ui.theme.StatusCautionYellowBg
import com.example.ui.theme.StatusConsistentGreen
import com.example.ui.theme.StatusConsistentGreenBg
import com.example.ui.theme.StatusWarningRed
import com.example.ui.theme.StatusWarningRedBg
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite

@Composable
fun LuxuryTopBar(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1F1D17),
                        ObsidianBlack
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        GoldDark.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(ObsidianCard)
                            .border(1.dp, ObsidianCardBorder, CircleShape)
                            .testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "الرجوع للخلف",
                            tint = GoldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPearlWhite,
                            letterSpacing = 0.3.sp
                        )
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMutedGold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            if (actions != null) {
                actions()
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: VerificationStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, fgColor, icon, text) = when (status) {
        VerificationStatus.GREEN -> Quadruple(
            StatusConsistentGreenBg,
            StatusConsistentGreen,
            Icons.Default.Shield,
            "متطابق ظاهرياً مع المعايير"
        )
        VerificationStatus.YELLOW -> Quadruple(
            StatusCautionYellowBg,
            StatusCautionYellow,
            Icons.Default.Info,
            "فحص غير حاسم (يُنصح بمزيد من التحقق)"
        )
        VerificationStatus.RED -> Quadruple(
            StatusWarningRedBg,
            StatusWarningRed,
            Icons.Default.Warning,
            "مؤشرات عدم تطابق أو شبهة تقليد"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, fgColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fgColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = fgColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun ScientificDisclaimerCard(
    modifier: Modifier = Modifier,
    customText: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GoldDark.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF191712)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "التنويه العلمي والقانوني الإلزامي",
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = customText ?: "الفحص البصري بالذكاء الاصطناعي لا يثبت نقاوة الذهب بصورة قاطعة. لا يمكن لأي كاميرا تحديد نسبة الذهب الصافي بنسبة 100% دون فحص مخبري حاسم مثل جهاز مطياف الأشعة السينية (XRF) أو حجر المحك والأحماض المعتمدة.",
                color = TextMutedGold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}
