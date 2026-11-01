package com.example.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.InspectionReport
import com.example.data.model.VerificationStatus
import com.example.ui.components.LuxuryTopBar
import com.example.ui.components.ScientificDisclaimerCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.StatusCautionYellow
import com.example.ui.theme.StatusConsistentGreen
import com.example.ui.theme.StatusWarningRed
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InspectionReportScreen(
    report: InspectionReport,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSaved by viewModel.isSavedInHistory.collectAsState()
    var selectedPreviewPath by remember { mutableStateOf(report.imagePath) }

    val dateFormatted = remember(report.timestamp) {
        val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
        sdf.format(Date(report.timestamp))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LuxuryTopBar(
                title = "تقرير فحص الذهب",
                subtitle = dateFormatted,
                onBackClick = { viewModel.navigateTo(Screen.Dashboard) },
                actions = {
                    Button(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
تقرير فحص الذهب - خبير الذهب الذكي (Gold Expert AI):
القطعة: ${report.jewelryTitle}
العيار المحتمل: ${report.possibleKarat} (${report.purityPercentage}%)
الختم المكتشف: ${report.recognizedHallmark}
حالة الفحص: ${report.verificationStatus}
التقييم: ${report.statusSummary}
نسبة الثقة في القراءة: ${report.confidenceScore}%

ملاحظة علمية: هذا الفحص بصري استرشادي، ويلزم فحص XRF أو الأحماض لإثبات النقاوة القانونية.
                                    """.trimIndent()
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "مشاركة تقرير الفحص"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianCard),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("report_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة",
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("مشاركة", color = GoldLight, fontSize = 12.sp)
                    }
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Verification Status Banner Card
                VerificationStatusCard(report)

                // Images Gallery Preview
                InspectionImagesGallery(
                    report = report,
                    selectedPath = selectedPreviewPath,
                    onSelectImage = { selectedPreviewPath = it }
                )

                // Hallmark & Karat Metrics Summary Card
                HallmarkAndKaratMetricsCard(report)

                // Observations Section: Visual Luster & Surface
                ReportDetailSection(
                    title = "الملاحظات البصرية والانعكاس السطحي",
                    content = report.visualObservations,
                    icon = Icons.Default.Visibility,
                    tint = GoldBright
                )

                // Plating & Coating Indicators Section
                ReportDetailSection(
                    title = "مؤشرات الطلاء والتغطية (Plating Indicators)",
                    content = report.platingIndicators,
                    icon = Icons.Default.Layers,
                    tint = if (report.verificationStatus == VerificationStatus.RED) StatusWarningRed else GoldLight
                )

                // Signs Suggesting Imitation Section
                ReportDetailSection(
                    title = "علامات تشير للتقليد أو عيوب الصب",
                    content = report.imitationSigns,
                    icon = Icons.Default.WarningAmber,
                    tint = if (report.verificationStatus == VerificationStatus.RED) StatusWarningRed else StatusCautionYellow
                )

                // Scientific Limitations Disclaimer Card
                ScientificDisclaimerCard(customText = report.scientificDisclaimer)

                // Action Buttons Row
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveCurrentReport() },
                        enabled = !isSaved,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("report_save_history_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSaved) Color(0xFF1B3322) else GoldPrimary,
                            contentColor = if (isSaved) StatusConsistentGreen else ObsidianBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSaved) "تم حفظ التقرير في سجلك" else "حفظ التقرير في سجلي",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.startNewInspection(isStampOnly = false) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("report_new_inspection_button"),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("فحص جديد", color = GoldLight, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(Screen.KaratGuide) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("report_karat_guide_button"),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = TextPearlWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("دليل العيارات", color = TextPearlWhite, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun VerificationStatusCard(report: InspectionReport) {
    val (borderColor, bgGradient, title, description) = when (report.verificationStatus) {
        VerificationStatus.GREEN -> Quadruple(
            StatusConsistentGreen,
            Brush.verticalGradient(listOf(Color(0xFF0F3820), Color(0xFF081C10))),
            "الحالة: متطابق ظاهرياً مع معايير العيار",
            report.statusSummary
        )
        VerificationStatus.YELLOW -> Quadruple(
            StatusCautionYellow,
            Brush.verticalGradient(listOf(Color(0xFF382708), Color(0xFF1F1504))),
            "الحالة: غير حاسم - يُنصح بفحص إضافي",
            report.statusSummary
        )
        VerificationStatus.RED -> Quadruple(
            StatusWarningRed,
            Brush.verticalGradient(listOf(Color(0xFF421010), Color(0xFF240808))),
            "الحالة: مؤشرات عدم تطابق أو شبهة تقليد",
            report.statusSummary
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgGradient)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.ExtraBold,
                        color = borderColor,
                        fontSize = 15.sp
                    )

                    // Confidence Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, borderColor.copy(alpha = 0.6f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ثقة ${report.confidenceScore}%",
                            color = borderColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    color = TextPearlWhite,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun InspectionImagesGallery(
    report: InspectionReport,
    selectedPath: String,
    onSelectImage: (String) -> Unit
) {
    val availableImages = remember(report) {
        listOfNotNull(
            report.imagePath.takeIf { it.isNotBlank() },
            report.stampImagePath?.takeIf { it.isNotBlank() },
            report.detailImagePath?.takeIf { it.isNotBlank() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianCard)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // Main enlarged view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.25f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            val file = File(selectedPath)
            if (file.exists()) {
                val bmp = BitmapFactory.decodeFile(file.absolutePath)
                if (bmp != null) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "صورة الفحص",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Text(
                    text = "صورة الفحص",
                    color = TextGray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        if (availableImages.size > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableImages) { path ->
                    val isSelected = path == selectedPath
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) GoldBright else ObsidianCardBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectImage(path) }
                    ) {
                        val file = File(path)
                        if (file.exists()) {
                            val bmp = BitmapFactory.decodeFile(file.absolutePath)
                            if (bmp != null) {
                                Image(
                                    bitmap = bmp.asImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
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
fun HallmarkAndKaratMetricsCard(report: InspectionReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MetricItem(
                label = "العيار المحتمل",
                value = report.possibleKarat,
                valueColor = GoldBright
            )

            MetricItem(
                label = "الختم المكتشف",
                value = report.recognizedHallmark,
                valueColor = TextPearlWhite
            )

            MetricItem(
                label = "نسبة النقاوة",
                value = "${report.purityPercentage}%",
                valueColor = GoldLight
            )
        }
    }
}

@Composable
fun MetricItem(
    label: String,
    value: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = TextMutedGold,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun ReportDetailSection(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = TextPearlWhite,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                color = TextMutedGold,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}
