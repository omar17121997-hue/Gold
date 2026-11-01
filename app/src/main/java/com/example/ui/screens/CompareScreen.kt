package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InspectionReport
import com.example.ui.components.LuxuryTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen
import java.io.File

@Composable
fun CompareScreen(
    report1: InspectionReport,
    report2: InspectionReport,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LuxuryTopBar(
                title = "مقارنة تقريرين",
                subtitle = "مقارنة تفصيلية بين قطعتين تم فحصهما",
                onBackClick = { viewModel.navigateTo(Screen.History) }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Side-by-side Images and Titles Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CompareItemHeader(
                        report = report1,
                        badgeLabel = "القطعة الأولى",
                        modifier = Modifier.weight(1f)
                    )
                    CompareItemHeader(
                        report = report2,
                        badgeLabel = "القطعة الثانية",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Comparison Attribute Cards
                CompareRowCard(
                    title = "العيار المحتمل ونسبة النقاوة",
                    val1 = "${report1.possibleKarat} (${report1.purityPercentage}%)",
                    val2 = "${report2.possibleKarat} (${report2.purityPercentage}%)",
                    highlight = true
                )

                CompareRowCard(
                    title = "الختم والدمغة المرصودة",
                    val1 = report1.recognizedHallmark,
                    val2 = report2.recognizedHallmark
                )

                // Status comparison
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "حالة الفحص والمطابقة",
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                StatusBadge(status = report1.verificationStatus)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                StatusBadge(status = report2.verificationStatus)
                            }
                        }
                    }
                }

                CompareRowCard(
                    title = "نسبة الثقة في التحليل البصري",
                    val1 = "${report1.confidenceScore}%",
                    val2 = "${report2.confidenceScore}%"
                )

                CompareRowCard(
                    title = "الملاحظات البصرية والانعكاس",
                    val1 = report1.visualObservations,
                    val2 = report2.visualObservations
                )

                CompareRowCard(
                    title = "مؤشرات الطلاء والتغطية",
                    val1 = report1.platingIndicators,
                    val2 = report2.platingIndicators
                )

                CompareRowCard(
                    title = "علامات التقليد أو الصب",
                    val1 = report1.imitationSigns,
                    val2 = report2.imitationSigns
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CompareItemHeader(
    report: InspectionReport,
    badgeLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF282210))
                    .border(1.dp, GoldDark, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeLabel,
                    color = GoldBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
            ) {
                val file = File(report.imagePath)
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.jewelryTitle,
                color = TextPearlWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CompareRowCard(
    title: String,
    val1: String,
    val2: String,
    highlight: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (highlight) GoldPrimary else ObsidianCardBorder,
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (highlight) GoldBright else GoldLight,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = val1,
                    color = TextPearlWhite,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = val2,
                    color = TextPearlWhite,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
