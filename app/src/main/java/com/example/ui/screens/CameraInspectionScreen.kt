package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.components.LuxuryTopBar
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
import com.example.util.ImageUtils
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen
import java.util.concurrent.Executors

@Composable
fun CameraInspectionScreen(
    viewModel: MainViewModel,
    isStampOnlyMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val mainBitmap by viewModel.mainBitmap.collectAsState()
    val stampBitmap by viewModel.stampBitmap.collectAsState()
    val detailBitmap by viewModel.detailBitmap.collectAsState()
    val selectedJewelryType by viewModel.selectedJewelryType.collectAsState()
    val claimedKarat by viewModel.claimedKarat.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val progressText by viewModel.analysisProgressText.collectAsState()

    // 0 = Main, 1 = Stamp, 2 = Detail
    var activeAngleIndex by remember { mutableIntStateOf(if (isStampOnlyMode) 1 else 0) }
    var zoomLevel by remember { mutableFloatStateOf(if (isStampOnlyMode) 3f else 1f) }
    var isFlashOn by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }

    // CameraX references
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var cameraControl: Camera? by remember { mutableStateOf(null) }

    // Gallery picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val bmp = ImageUtils.uriToBitmap(context, uri)
            if (bmp != null) {
                when (activeAngleIndex) {
                    0 -> viewModel.setMainBitmap(bmp)
                    1 -> viewModel.setStampBitmap(bmp)
                    2 -> viewModel.setDetailBitmap(bmp)
                }
            }
        }
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
                title = if (isStampOnlyMode) "مسح ختم الذهب (ماكرو)" else "فحص الذهب الذكي",
                subtitle = "التقط صوراً واضحة للتحليل بالذكاء الاصطناعي",
                onBackClick = { viewModel.navigateTo(Screen.Dashboard) },
                actions = {
                    IconButton(
                        onClick = { showTipsDialog = !showTipsDialog },
                        modifier = Modifier.testTag("camera_tips_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "إرشادات التصوير",
                            tint = GoldBright
                        )
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
                // Angle Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AngleTabChip(
                        title = "1. القطعة كاملة",
                        isCaptured = mainBitmap != null,
                        isSelected = activeAngleIndex == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { activeAngleIndex = 0 }
                    )
                    AngleTabChip(
                        title = "2. ختم العيار (ماكرو)",
                        isCaptured = stampBitmap != null,
                        isSelected = activeAngleIndex == 1,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            activeAngleIndex = 1
                            zoomLevel = 3f
                            cameraControl?.cameraControl?.setZoomRatio(3f)
                        }
                    )
                    AngleTabChip(
                        title = "3. تفاصيل السطح",
                        isCaptured = detailBitmap != null,
                        isSelected = activeAngleIndex == 2,
                        modifier = Modifier.weight(1f),
                        onClick = { activeAngleIndex = 2 }
                    )
                }

                // Camera Viewport Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .border(1.5.dp, GoldDark, RoundedCornerShape(16.dp))
                ) {
                    if (hasCameraPermission) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.surfaceProvider = previewView.surfaceProvider
                                    }
                                    val capture = ImageCapture.Builder()
                                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                        .build()
                                    imageCapture = capture

                                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                    try {
                                        cameraProvider.unbindAll()
                                        val cam = cameraProvider.bindToLifecycle(
                                            ctx as androidx.lifecycle.LifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            capture
                                        )
                                        cameraControl = cam
                                        cam.cameraControl.setZoomRatio(zoomLevel)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }, ContextCompat.getMainExecutor(ctx))
                                previewView
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Permission missing fallback
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "مطلوب إذن الكاميرا للفحص المباشر",
                                color = TextPearlWhite,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                            ) {
                                Text("منح إذن الكاميرا", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Target Reticle Guideline Overlay
                    Box(
                        modifier = Modifier
                            .size(if (activeAngleIndex == 1) 180.dp else 240.dp)
                            .align(Alignment.Center)
                            .border(
                                width = 1.5.dp,
                                color = if (activeAngleIndex == 1) GoldBright else GoldLight.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = if (activeAngleIndex == 1) "وجّه نحو ختم العيار (مثل 875, 750)" else "ضع القطعة داخل الإطار",
                            color = TextPearlWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Top overlay controls inside camera: Flash toggle & Zoom badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                isFlashOn = !isFlashOn
                                cameraControl?.cameraControl?.enableTorch(isFlashOn)
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, GoldDark, CircleShape)
                                .testTag("camera_flash_toggle")
                        ) {
                            Icon(
                                imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "الفلاش",
                                tint = if (isFlashOn) GoldBright else TextGray
                            )
                        }

                        // Zoom Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, GoldDark, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${String.format("%.1f", zoomLevel)}x",
                                color = GoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Macro Zoom Slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مستوى التكبير الماكرو (Macro Zoom)",
                                color = TextPearlWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "${String.format("%.1f", zoomLevel)}x",
                            color = GoldBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Slider(
                        value = zoomLevel,
                        onValueChange = { newZoom ->
                            zoomLevel = newZoom
                            cameraControl?.cameraControl?.setZoomRatio(newZoom)
                        },
                        valueRange = 1f..8f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldBright,
                            activeTrackColor = GoldPrimary,
                            inactiveTrackColor = Color(0xFF333333)
                        ),
                        modifier = Modifier.testTag("camera_zoom_slider")
                    )
                }

                // Capture Controls & Gallery Upload Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Upload from gallery button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            photoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        IconButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ObsidianCard)
                                .border(1.dp, GoldDark, CircleShape)
                                .testTag("camera_gallery_upload_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "اختيار من المعرض",
                                tint = GoldPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "من المعرض",
                            color = TextMutedGold,
                            fontSize = 11.sp
                        )
                    }

                    // Primary Shutter Button
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(GoldBright, GoldDark)
                                )
                            )
                            .border(3.dp, TextPearlWhite, CircleShape)
                            .clickable {
                                val capture = imageCapture
                                if (capture != null) {
                                    capture.takePicture(
                                        Executors.newSingleThreadExecutor(),
                                        object : ImageCapture.OnImageCapturedCallback() {
                                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                                val bmp = imageProxyToBitmap(imageProxy)
                                                imageProxy.close()
                                                if (bmp != null) {
                                                    when (activeAngleIndex) {
                                                        0 -> viewModel.setMainBitmap(bmp)
                                                        1 -> viewModel.setStampBitmap(bmp)
                                                        2 -> viewModel.setDetailBitmap(bmp)
                                                    }
                                                    // Auto advance to next angle if empty
                                                    if (activeAngleIndex == 0 && stampBitmap == null) {
                                                        activeAngleIndex = 1
                                                        zoomLevel = 3f
                                                        cameraControl?.cameraControl?.setZoomRatio(3f)
                                                    } else if (activeAngleIndex == 1 && detailBitmap == null) {
                                                        activeAngleIndex = 2
                                                    }
                                                }
                                            }

                                            override fun onError(exception: ImageCaptureException) {
                                                exception.printStackTrace()
                                            }
                                        }
                                    )
                                }
                            }
                            .testTag("camera_shutter_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "التقاط الصورة",
                            tint = ObsidianBlack,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    // Current captured thumbnail preview
                    val currentAngleBmp = when (activeAngleIndex) {
                        0 -> mainBitmap
                        1 -> stampBitmap
                        else -> detailBitmap
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ObsidianCard)
                                .border(1.dp, if (currentAngleBmp != null) GoldBright else ObsidianCardBorder, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentAngleBmp != null) {
                                Image(
                                    bitmap = currentAngleBmp.asImageBitmap(),
                                    contentDescription = "الصورة الملتقطة",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = "فارغ",
                                    color = TextGray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (currentAngleBmp != null) "تم الالتقاط" else "لم يتم",
                            color = if (currentAngleBmp != null) GoldBright else TextGray,
                            fontSize = 11.sp
                        )
                    }
                }

                // Jewelry Type & Claimed Karat Selector
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "نوع القطعة والعيار المفترض (اختياري)",
                        fontWeight = FontWeight.Bold,
                        color = TextPearlWhite,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Jewelry type chips
                    val types = listOf("خاتم ذهبي", "سوار / غويشة", "قلادة / سلسلة", "سبيكة استثمارية", "عملة ذهبية", "أقراط")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        types.take(3).forEach { t ->
                            FilterChip(
                                selected = selectedJewelryType == t,
                                onClick = { viewModel.setJewelryType(t) },
                                label = { Text(t, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary,
                                    selectedLabelColor = ObsidianBlack,
                                    containerColor = ObsidianBlack,
                                    labelColor = TextPearlWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Claimed Karat chips
                    Text(
                        text = "العيار المزعوم للقطعة:",
                        color = TextMutedGold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val karats = listOf("24K (999)", "22K (916)", "21K (875)", "18K (750)", "14K (585)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        karats.take(4).forEach { k ->
                            val cleanKarat = k.substringBefore(" ")
                            FilterChip(
                                selected = claimedKarat == cleanKarat,
                                onClick = { viewModel.setClaimedKarat(cleanKarat) },
                                label = { Text(cleanKarat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary,
                                    selectedLabelColor = ObsidianBlack,
                                    containerColor = ObsidianBlack,
                                    labelColor = TextPearlWhite
                                )
                            )
                        }
                    }
                }

                // AI Analyze Action Button
                val hasImages = mainBitmap != null || stampBitmap != null
                Button(
                    onClick = {
                        // If only stamp image was captured, set it as main as well so analysis proceeds
                        if (mainBitmap == null && stampBitmap != null) {
                            viewModel.setMainBitmap(stampBitmap!!)
                        }
                        viewModel.runAnalysis()
                    },
                    enabled = hasImages && !isAnalyzing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("run_ai_analysis_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        disabledContainerColor = Color(0xFF332B14),
                        contentColor = ObsidianBlack,
                        disabledContentColor = Color(0xFF887D5D)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasImages) "تحليل القطعة بالذكاء الاصطناعي (Gemini)" else "التقط صورة واحدة على الأقل للبدء",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Photography instructions card (Expandable)
                AnimatedVisibility(visible = showTipsDialog) {
                    PhotographyTipsCard()
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Analysis Loading Progress Overlay
        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    CircularProgressIndicator(
                        color = GoldBright,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "جاري فحص الذهب بالذكاء الاصطناعي",
                        fontWeight = FontWeight.Bold,
                        color = TextPearlWhite,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = progressText,
                        color = GoldLight,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AngleTabChip(
    title: String,
    isCaptured: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF2E2611) else ObsidianCard)
            .border(
                width = 1.dp,
                color = if (isSelected) GoldPrimary else ObsidianCardBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isCaptured) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GoldBright,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = title,
                color = if (isSelected) GoldBright else TextPearlWhite,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PhotographyTipsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1A11))
            .border(1.dp, GoldDark, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Text(
            text = "إرشادات التصوير لتحقيق أعلى دقة فحص:",
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        val tips = listOf(
            "1. إضاءة طبيعية دافئة: تجنب الفلاش المباشر القوي الذي يسبب انعكاسات بيضاء تعمي ختم الدمغة.",
            "2. تكبير ماكرو على الختم: استخدم شريط التكبير (3x إلى 6x) وركّز الكاميرا على الأرقام المحفورة (875، 750، 916).",
            "3. ثبات اليد: ثبّت الهاتف على سطح مستوٍ أو اسند يدك أثناء التقاط الصورة لتجنب الغبش.",
            "4. تصوير الحواف والقفل: الحواف ومناطق الاحتكاك تكشف فورياً ما إذا كانت القطعة مجرد طلاء خارجي."
        )
        tips.forEach { tip ->
            Text(
                text = tip,
                color = TextMutedGold,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    val planeProxy = image.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null

    val rotation = image.imageInfo.rotationDegrees
    return if (rotation != 0) {
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}
