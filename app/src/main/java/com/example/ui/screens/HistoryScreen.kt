package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InspectionReport
import com.example.data.model.VerificationStatus
import com.example.ui.components.LuxuryTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
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
fun HistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val inspections by viewModel.inspections.collectAsState()
    val selectedForComparison by viewModel.selectedForComparison.collectAsState()

    var statusFilter by remember { mutableStateOf<VerificationStatus?>(null) }
    var itemToDelete by remember { mutableStateOf<InspectionReport?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val filteredList = remember(inspections, statusFilter) {
        if (statusFilter == null) inspections
        else inspections.filter { it.verificationStatus == statusFilter }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LuxuryTopBar(
                title = "سجل الفحوصات السابقة",
                subtitle = "${inspections.size} فحص محفوظ",
                onBackClick = { viewModel.navigateTo(Screen.Dashboard) },
                actions = {
                    if (inspections.isNotEmpty()) {
                        IconButton(
                            onClick = { showClearAllDialog = true },
                            modifier = Modifier.testTag("history_clear_all_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "مسح الكل",
                                tint = TextGray
                            )
                        }
                    }
                }
            )

            // Status Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = statusFilter == null,
                    onClick = { statusFilter = null },
                    label = { Text("الكل (${inspections.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = ObsidianBlack,
                        containerColor = ObsidianCard,
                        labelColor = TextPearlWhite
                    )
                )

                FilterChip(
                    selected = statusFilter == VerificationStatus.GREEN,
                    onClick = {
                        statusFilter = if (statusFilter == VerificationStatus.GREEN) null else VerificationStatus.GREEN
                    },
                    label = { Text("متطابق", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF10B981),
                        selectedLabelColor = Color.Black,
                        containerColor = ObsidianCard,
                        labelColor = TextPearlWhite
                    )
                )

                FilterChip(
                    selected = statusFilter == VerificationStatus.YELLOW,
                    onClick = {
                        statusFilter = if (statusFilter == VerificationStatus.YELLOW) null else VerificationStatus.YELLOW
                    },
                    label = { Text("غير حاسم", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFF59E0B),
                        selectedLabelColor = Color.Black,
                        containerColor = ObsidianCard,
                        labelColor = TextPearlWhite
                    )
                )

                FilterChip(
                    selected = statusFilter == VerificationStatus.RED,
                    onClick = {
                        statusFilter = if (statusFilter == VerificationStatus.RED) null else VerificationStatus.RED
                    },
                    label = { Text("تحذير تقليد", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEF4444),
                        selectedLabelColor = Color.White,
                        containerColor = ObsidianCard,
                        labelColor = TextPearlWhite
                    )
                )
            }

            if (filteredList.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF231E12))
                                .border(1.dp, GoldDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "لا توجد تقارير في هذا القسم",
                            color = TextPearlWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "قم بفحص قطعة جديدة وحفظ التقرير لمراجعته ومقارنته في أي وقت.",
                            color = TextGray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.startNewInspection(isStampOnly = false) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = ObsidianBlack,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("بدء فحص جديد", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { report ->
                        val isSelectedForComp = selectedForComparison.any { it.id == report.id }

                        HistoryReportCard(
                            report = report,
                            isSelectedForComparison = isSelectedForComp,
                            onToggleComparison = { viewModel.toggleComparisonSelection(report) },
                            onCardClick = { viewModel.navigateTo(Screen.ReportDetail(report)) },
                            onDeleteClick = { itemToDelete = report }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Bottom Comparison Bar when items are selected
        AnimatedVisibility(
            visible = selectedForComparison.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldBright, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A10))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "مقارنة الفحوصات",
                            color = GoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "تم تحديد (${selectedForComparison.size}/2)",
                            color = TextPearlWhite,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.startComparison() },
                        enabled = selectedForComparison.size == 2,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = ObsidianBlack,
                            disabledContainerColor = Color(0xFF332D19),
                            disabledContentColor = Color(0xFF7A6F4E)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("start_comparison_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedForComparison.size == 2) "مقارنة الفحصين الآن" else "اختر فحصين",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Delete confirmation dialog
        itemToDelete?.let { rep ->
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                title = { Text("حذف تقرير الفحص؟", color = TextPearlWhite, fontWeight = FontWeight.Bold) },
                text = { Text("هل أنت متأكد من رغبتك في حذف تقرير \"${rep.jewelryTitle}\"؟ لن يمكن استرجاعه.", color = TextMutedGold) },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteReport(rep.id)
                            itemToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("حذف", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { itemToDelete = null }) {
                        Text("إلغاء", color = TextGray)
                    }
                },
                containerColor = ObsidianCard
            )
        }

        // Clear all dialog
        if (showClearAllDialog) {
            AlertDialog(
                onDismissRequest = { showClearAllDialog = false },
                title = { Text("مسح كافة السجلات؟", color = TextPearlWhite, fontWeight = FontWeight.Bold) },
                text = { Text("سيتم مسح جميع تقارير الفحص السابقة المحفوظة على هذا الجهاز.", color = TextMutedGold) },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearAllReports()
                            showClearAllDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("مسح الكل", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearAllDialog = false }) {
                        Text("إلغاء", color = TextGray)
                    }
                },
                containerColor = ObsidianCard
            )
        }
    }
}

@Composable
fun HistoryReportCard(
    report: InspectionReport,
    isSelectedForComparison: Boolean,
    onToggleComparison: () -> Unit,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateStr = remember(report.timestamp) {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale("ar"))
        sdf.format(Date(report.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelectedForComparison) 2.dp else 1.dp,
                color = if (isSelectedForComparison) GoldBright else ObsidianCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onCardClick)
            .testTag("history_item_${report.id}"),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail image
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
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

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = report.jewelryTitle,
                        fontWeight = FontWeight.Bold,
                        color = TextPearlWhite,
                        fontSize = 14.sp
                    )
                    Text(
                        text = dateStr,
                        color = TextGray,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "العيار: ${report.possibleKarat}",
                        color = GoldLight,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "الدمغة: ${report.recognizedHallmark}",
                        color = TextMutedGold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                StatusBadge(status = report.verificationStatus)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Actions: Compare toggle + Delete
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onToggleComparison,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isSelectedForComparison) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "تحديد للمقارنة",
                        tint = if (isSelectedForComparison) GoldBright else TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف",
                        tint = TextGray.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
