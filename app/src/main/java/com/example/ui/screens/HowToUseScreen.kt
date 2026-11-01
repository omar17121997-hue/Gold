package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.components.LuxuryTopBar
import com.example.ui.components.ScientificDisclaimerCard
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

@Composable
fun HowToUseScreen(
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
                title = "طريقة استخدام التطبيق",
                subtitle = "دليل التصوير الاحترافي والاختبارات العلمية",
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
                    Text(
                        text = "خطوات الفحص البصري بالذكاء الاصطناعي",
                        fontWeight = FontWeight.Bold,
                        color = TextPearlWhite,
                        fontSize = 15.sp
                    )
                }

                item {
                    StepCard(
                        stepNumber = "1",
                        title = "تنظيف القطعة",
                        desc = "امسح القطعة الذهبية بقطعة قماش ناعمة لإزالة بصمات الأصابع أو الدهون التي قد تغطي تفاصيل الدمغة وتشتت ضوء الكاميرا.",
                        icon = Icons.Default.CleaningServices
                    )
                }

                item {
                    StepCard(
                        stepNumber = "2",
                        title = "إضاءة ناعمة ومحايدة",
                        desc = "استخدم ضوءاً نهارياً غير مباشر أو مصباح مكتب مائل. تجنب فلاش الهاتف المباشر لأنه يحدث انعكاساً شديداً يخفي الأرقام المحفورة.",
                        icon = Icons.Default.Lightbulb
                    )
                }

                item {
                    StepCard(
                        stepNumber = "3",
                        title = "استخدام التكبير الماكرو (Macro Zoom)",
                        desc = "اسحب شريط التكبير في الكاميرا إلى (3x - 6x) وركّز الإطار على رقم الدمغة (مثل 875 أو 750) مع تثبيت يدك تماماً.",
                        icon = Icons.Default.CenterFocusStrong
                    )
                }

                item {
                    StepCard(
                        stepNumber = "4",
                        title = "تصوير زوايا متعددة",
                        desc = "التقط الزاوية الأولى لكامل القطعة لتقييم تناسق اللون، ثم الزاوية الثانية لختم العيار، والزاوية الثالثة للحواف ومناطق الاحتكاك.",
                        icon = Icons.Default.CameraAlt
                    )
                }

                item {
                    // Where to find hallmarks card
                    HallmarkLocationsCard()
                }

                item {
                    // Professional Testing Methods Explanation
                    ProfessionalLabTestsCard()
                }

                item {
                    ScientificDisclaimerCard()
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Button(
                        onClick = { viewModel.startNewInspection(isStampOnly = false) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("how_to_use_start_inspection"),
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
                            text = "جاهز؟ ابدأ فحص قطعة الآن",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun StepCard(
    stepNumber: String,
    title: String,
    desc: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF282210))
                    .border(1.dp, GoldPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    color = GoldBright,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = TextPearlWhite,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    color = TextMutedGold,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun HallmarkLocationsCard() {
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
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "أين تجد الختم (الدمغة) على مجوهراتك؟",
                    fontWeight = FontWeight.Bold,
                    color = TextPearlWhite,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val places = listOf(
                "• الخواتم: على الجدار الداخلي للحلقة الدائرية (Band).",
                "• السلاسل والقلائد: على لسان القفل أو الصفيحة المعدنية المسطحة الصغيرة المجاورة للقفل.",
                "• الأساور والغوايش: على الحافة الداخلية أو على المفصل والقفل.",
                "• الأقراط (الحلق): على السلك أو الدبوس الخلفي الذي يدخل الأذن أو على قفل الفراشة.",
                "• السبائك والعملات: مطبوع بحجم كبير وواضح على الواجهة مع الوزن الصافي ورقم الإصدار."
            )

            places.forEach { p ->
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

@Composable
fun ProfessionalLabTestsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GoldDark, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D1810))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "طرق الفحص المخبرية القاطعة للذهب",
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val tests = listOf(
                Pair(
                    "1. جهاز مطياف الأشعة السينية (XRF Spectrometry):",
                    "أحدث وأدق فحص علمي غير متلف؛ يطلق أشعة سينية تقرأ التوزيع الذري للذهب والمعادن بدقة 99.9% خلال ثوانٍ معدودة دون خدش القطعة."
                ),
                Pair(
                    "2. فحص حجر المحك وحمض النيتريك (Touchstone Acid Test):",
                    "الطريقة التقليدية لدى الصاغة؛ تُحك القطعة برفق على حجر بازلتي ويوضع حمض النيتريك المخصص لكل عيار. الذهب الأصلي لا يتأثر بالحمض."
                ),
                Pair(
                    "3. اختبار الكثافة النوعية والوزن المائي (Specific Gravity):",
                    "يعتمد على قاعدة أرخميدس لقياس كثافة الذهب (كثافة عيار 24 = 19.32 جم/سم³، وعيار 21 = 17.5 جم/سم³)، وهو فحص حاسم للسبائك."
                ),
                Pair(
                    "4. اختبار المغناطيس القوي (Neodymium Magnet):",
                    "الذهب معدن غير مغناطيسي تماماً؛ إذا انجذبت القطعة للمغناطيس فهذا دليل قاطع على وجود حديد أو نيكل تحته."
                )
            )

            tests.forEach { (t, d) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = t,
                        color = GoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = d,
                        color = TextMutedGold,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
