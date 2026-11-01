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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GoldKaratInfo
import com.example.data.model.GoldKaratRegistry
import com.example.ui.components.LuxuryTopBar
import com.example.ui.components.ScientificDisclaimerCard
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.StatusWarningRed
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

@Composable
fun GoldKaratGuideScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("10") }
    var selectedKaratForCalc by remember { mutableStateOf("21K") }

    val filteredList = remember(searchQuery) {
        if (searchQuery.isBlank()) GoldKaratRegistry.KARAT_LIST
        else {
            val q = searchQuery.trim()
            GoldKaratRegistry.KARAT_LIST.filter {
                it.karat.contains(q, ignoreCase = true) ||
                it.hallmarkNumber.contains(q) ||
                it.arabicName.contains(q)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LuxuryTopBar(
                title = "دليل عيارات وأختام الذهب",
                subtitle = "المرجع الشامل للدمغات ونسب النقاوة ومؤشرات التقليد",
                onBackClick = { viewModel.navigateTo(Screen.Dashboard) }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    // Search & Lookup Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("karat_search_input"),
                        placeholder = { Text("ابحث برقم الختم (875, 750, 999) أو العيار...", color = TextGray, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = GoldPrimary)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = ObsidianCardBorder,
                            focusedTextColor = TextPearlWhite,
                            unfocusedTextColor = TextPearlWhite,
                            focusedContainerColor = ObsidianCard,
                            unfocusedContainerColor = ObsidianCard
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    // Interactive Pure Gold Calculator
                    InteractivePureGoldCalculator(
                        weightInput = weightInput,
                        onWeightChange = { weightInput = it },
                        selectedKarat = selectedKaratForCalc,
                        onSelectKarat = { selectedKaratForCalc = it }
                    )
                }

                item {
                    // Section Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = GoldBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "جدول العيارات والدمغات الرسمية (Hallmarks)",
                            fontWeight = FontWeight.Bold,
                            color = TextPearlWhite,
                            fontSize = 14.sp
                        )
                    }
                }

                // List of Karat Cards
                items(filteredList) { info ->
                    KaratDetailCard(info)
                }

                item {
                    // Educational Section on Counterfeit & Hallmarks
                    GoldEducationOverviewCard()
                }

                item {
                    ScientificDisclaimerCard()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun InteractivePureGoldCalculator(
    weightInput: String,
    onWeightChange: (String) -> Unit,
    selectedKarat: String,
    onSelectKarat: (String) -> Unit
) {
    val weightNum = weightInput.toDoubleOrNull() ?: 0.0
    val matchedInfo = GoldKaratRegistry.findByNumber(selectedKarat)
    val purity = matchedInfo?.purityPercentage ?: 87.5
    val pureGoldWeight = (weightNum * (purity / 100.0))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldDark, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A11))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "حاسبة وزن الذهب الصافي (Fine Gold)",
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { onWeightChange(it) },
                    label = { Text("الوزن بالجرام", fontSize = 11.sp, color = TextMutedGold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("calc_weight_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = ObsidianCardBorder,
                        focusedTextColor = TextPearlWhite,
                        unfocusedTextColor = TextPearlWhite
                    ),
                    singleLine = true
                )

                // Karat Selector chips
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "اختر العيار:", color = TextMutedGold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("24K", "21K", "18K").forEach { k ->
                            val isSel = selectedKarat == k
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) GoldPrimary else ObsidianCard)
                                    .border(1.dp, if (isSel) GoldBright else ObsidianCardBorder, RoundedCornerShape(6.dp))
                                    .clickable { onSelectKarat(k) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = k,
                                    color = if (isSel) ObsidianBlack else TextPearlWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calculated Output Result
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF2B2412))
                    .border(1.dp, GoldBright.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "صافي الذهب النقي (24K معادل):",
                            color = TextMutedGold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${String.format("%.2f", pureGoldWeight)} جرام",
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldBright,
                            fontSize = 16.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "نسبة النقاوة:",
                            color = TextMutedGold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "$purity% (ختم ${matchedInfo?.hallmarkNumber ?: "875"})",
                            fontWeight = FontWeight.Bold,
                            color = TextPearlWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KaratDetailCard(info: GoldKaratInfo) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Hallmark Number Badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF262010))
                            .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = info.hallmarkNumber,
                            color = GoldBright,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = info.arabicName,
                            fontWeight = FontWeight.Bold,
                            color = TextPearlWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${info.karat} - نقاوة ${info.purityPercentage}%",
                            color = GoldLight,
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextMutedGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = info.description,
                color = TextMutedGold,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailRow("الاستخدام الشائع:", info.commonUsage)
                    DetailRow("درجة اللون واللمعان:", info.colorTone)
                    DetailRow("درجة الصلابة:", info.hardness)

                    // Counterfeit warning
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF381414))
                            .border(1.dp, StatusWarningRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = StatusWarningRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تحذير التزييف: ${info.counterfeitWarning}",
                                color = Color(0xFFFCA5A5),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = GoldLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            color = TextPearlWhite,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun GoldEducationOverviewCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "حقائق علمية وقانونية عن أختام الذهب",
                    fontWeight = FontWeight.Bold,
                    color = TextPearlWhite,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val points = listOf(
                "• الختم لا يعني الأصالة بمفرده: يمكن تزوير الأختام والأرقام (مثل 875 أو 750) بآلات الليزر أو القوالب اليدوية على قطع نحاسية أو فضية مطلية.",
                "• دمغات مصلحة الموازين: في الدول العربية (مثل مصر، السعودية، الإمارات، البحرين)، يتكون الختم الأصلي من ثلاثة عناصر: رقم العيار، رمز الدولة أو مصلحة الدمغة (مثل النسر أو الصقر أو النخلة)، وحرف يدل على سنة الصنع.",
                "• الذهب المطلي (Gold Plated): طبقة خارجية مجهرية من الذهب (0.5 إلى 2 ميكرون) فوق النحاس أو الفولاذ؛ لا تمتلك قيمة استثمارية وتتلاشى بالاحتكاك."
            )

            points.forEach { p ->
                Text(
                    text = p,
                    color = TextMutedGold,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
