package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.CameraInspectionScreen
import com.example.ui.screens.CompareScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GoldKaratGuideScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HowToUseScreen
import com.example.ui.screens.InspectionReportScreen
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldExpertTheme
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMutedGold
import com.example.ui.theme.TextPearlWhite
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GoldExpertTheme {
                // Mandatory Full RTL Layout for Arabic
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    GoldExpertApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GoldExpertApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle Back Navigation
    BackHandler(enabled = currentScreen !is Screen.Dashboard) {
        when (currentScreen) {
            is Screen.Dashboard -> { /* Exit */ }
            is Screen.CameraInspection -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.ReportDetail -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.History -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.Compare -> viewModel.navigateTo(Screen.History)
            is Screen.KaratGuide -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.HowToUse -> viewModel.navigateTo(Screen.Dashboard)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = ObsidianBlack,
        bottomBar = {
            // Show bottom navigation on main screens
            val isMainLevel = currentScreen is Screen.Dashboard ||
                    currentScreen is Screen.History ||
                    currentScreen is Screen.KaratGuide ||
                    currentScreen is Screen.HowToUse

            if (isMainLevel) {
                LuxuryBottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onStartCamera = { viewModel.startNewInspection(isStampOnly = false) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    is Screen.Dashboard -> DashboardScreen(viewModel = viewModel)
                    is Screen.CameraInspection -> CameraInspectionScreen(
                        viewModel = viewModel,
                        isStampOnlyMode = screen.isStampOnlyMode
                    )
                    is Screen.ReportDetail -> InspectionReportScreen(
                        report = screen.report,
                        viewModel = viewModel
                    )
                    is Screen.History -> HistoryScreen(viewModel = viewModel)
                    is Screen.Compare -> CompareScreen(
                        report1 = screen.report1,
                        report2 = screen.report2,
                        viewModel = viewModel
                    )
                    is Screen.KaratGuide -> GoldKaratGuideScreen(viewModel = viewModel)
                    is Screen.HowToUse -> HowToUseScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LuxuryBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onStartCamera: () -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, GoldDark.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
            ),
        containerColor = Color(0xFF141312),
        contentColor = TextPearlWhite
    ) {
        NavigationBarItem(
            selected = currentScreen is Screen.Dashboard,
            onClick = { onNavigate(Screen.Dashboard) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "الرئيسية",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("الرئيسية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ObsidianBlack,
                selectedTextColor = GoldBright,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.CameraInspection,
            onClick = onStartCamera,
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "فحص الذهب",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("فحص الذهب", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ObsidianBlack,
                selectedTextColor = GoldBright,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            ),
            modifier = Modifier.testTag("nav_camera")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.History || currentScreen is Screen.Compare,
            onClick = { onNavigate(Screen.History) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "السجل",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("السجل", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ObsidianBlack,
                selectedTextColor = GoldBright,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            ),
            modifier = Modifier.testTag("nav_history")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.KaratGuide,
            onClick = { onNavigate(Screen.KaratGuide) },
            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "العيارات",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("العيارات", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ObsidianBlack,
                selectedTextColor = GoldBright,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            ),
            modifier = Modifier.testTag("nav_karats")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.HowToUse,
            onClick = { onNavigate(Screen.HowToUse) },
            icon = {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "المساعدة",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("المساعدة", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ObsidianBlack,
                selectedTextColor = GoldBright,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            ),
            modifier = Modifier.testTag("nav_help")
        )
    }
}
