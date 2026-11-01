package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.GoldKaratRegistry
import com.example.ui.components.ScientificDisclaimerCard
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val historyCount by viewModel.inspectionCount.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Brand Header
            LuxuryHeroHeader()
        }

        item {
            // Main Action Hero Card: Start Gold Inspection
            StartInspectionHeroCard(
                onStartClick = { viewModel.startNewInspection(isStampOnly = false) }
            )
        }

        item {
            // Secondary Quick Actions Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Scan Stamp Card
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "مسح ختم الذهب",
                    subtitle = "تكبير ماكرو للدمغة 999, 875, 750",
                    icon = Icons.Default.DocumentScanner,
                    iconBg = Color(0xFF2E2408),
                    iconTint = GoldBright,
                    testTag = "dashboard_scan_stamp_button",
                    onClick = { viewModel.startNewInspection(isStampOnly = true) }
                )

                // Inspection History Card
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "سجل الفحوصات",
                    subtitle = if (historyCount > 0) "$historyCount تقارير محفوظة" else "لا توجد تقارير سابقة",
                    badge = if (historyCount > 0) "$historyCount" else null,
                    icon = Icons.Default.History,
                    iconBg = Color(0xFF1D2230),
                    iconTint = Color(0xFF93C5FD),
                    testTag = "dashboard_history_button",
                    onClick = { viewModel.navigateTo(Screen.History) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Gold Karat Guide Card
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "دليل عيارات الذهب",
                    subtitle = "جدول النقاوة ومؤشرات التقليد",
                    icon = Icons.Default.LocalOffer,
                    iconBg = Color(0xFF261D15),
                    iconTint = GoldLight,
                    testTag = "dashboard_karat_guide_button",
                    onClick = { viewModel.navigateTo(Screen.KaratGuide) }
                )

                // How to Use Card
                DashboardActionCard(
                    modifier = Modifier.weight(1f),
                    title = "طريقة الاستخدام",
                    subtitle = "إرشادات التصوير والفحص العلمي",
                    icon = Icons.Default.HelpOutline,
                    iconBg = Color(0xFF192420),
                    iconTint = Color(0xFF6EE7B7),
                    testTag = "dashboard_how_to_use_button",
                    onClick = { viewModel.navigateTo(Screen.HowToUse) }
                )
            }
        }

        item {
            // Quick Karat Standard Reference Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianCard)
                    .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "أشهر عيارات وأختام الذهب المعتمدة",
                            fontWeight = FontWeight.Bold,
                            color = TextPearlWhite,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "عرض الكل",
                        color = GoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(Screen.KaratGuide) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GoldKaratRegistry.KARAT_LIST.take(5)) { info ->
                        KaratQuickPill(info.karat, info.hallmarkNumber, "${info.purityPercentage}%") {
                            viewModel.navigateTo(Screen.KaratGuide)
                        }
                    }
                }
            }
        }

        item {
            // Scientific limitations disclaimer
            ScientificDisclaimerCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LuxuryHeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF262115),
                        ObsidianSurface
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    colors = listOf(GoldDark, GoldBright, GoldDark)
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Emblem Icon
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(GoldBright, GoldDark)
                        )
                    )
                    .border(2.dp, TextPearlWhite.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = ObsidianBlack,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "خبير الذهب الذكي",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPearlWhite,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "GOLD EXPERT AI",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldBright,
                    letterSpacing = 3.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "منظومة متطورة لفحص المشغولات والسبائك، وتحليل الأختام والدمغات، والكشف المبدئي عن علامات الطلاء والتقليد",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMutedGold,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun StartInspectionHeroCard(
    onStartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, GoldBright, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A10))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = ObsidianBlack,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "بدء فحص الذهب الشامل",
                        fontWeight = FontWeight.Bold,
                        color = TextPearlWhite,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "تصوير متعدد الزوايا + فحص الذكاء الاصطناعي",
                        color = GoldLight,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "التقط صوراً عالية الدقة للقطعة ولختم العيار عن قرب، وسيقوم النظام بفحص الانعكاس اللوني وجودة الصب والدمغة المحفورة ومطابقتها فورياً.",
                color = TextMutedGold,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("dashboard_start_inspection_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "فتح كاميرا الفحص الآن",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(GoldPrimary)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            color = ObsidianBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = TextPearlWhite,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = TextGray,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun KaratQuickPill(
    karat: String,
    hallmark: String,
    purity: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ObsidianElevated)
            .border(1.dp, GoldDark.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = karat,
                color = GoldBright,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "ختم $hallmark",
                color = TextPearlWhite,
                fontSize = 11.sp
            )
            Text(
                text = purity,
                color = TextMutedGold,
                fontSize = 10.sp
            )
        }
    }
}
