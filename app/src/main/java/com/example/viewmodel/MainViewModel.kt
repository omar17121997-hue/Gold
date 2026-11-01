package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.InspectionReport
import com.example.data.repository.InspectionRepository
import com.example.service.GoldAnalysisService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Dashboard : Screen()
    data class CameraInspection(val isStampOnlyMode: Boolean = false) : Screen()
    data class ReportDetail(val report: InspectionReport) : Screen()
    object History : Screen()
    data class Compare(val report1: InspectionReport, val report2: InspectionReport) : Screen()
    object KaratGuide : Screen()
    object HowToUse : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InspectionRepository
    private val analysisService: GoldAnalysisService

    init {
        val db = AppDatabase.getInstance(application)
        repository = InspectionRepository(db.inspectionDao())
        analysisService = GoldAnalysisService(application)
    }

    val inspections: StateFlow<List<InspectionReport>> = repository.allInspections
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val inspectionCount: StateFlow<Int> = repository.count
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Inspection drafting state
    private val _mainBitmap = MutableStateFlow<Bitmap?>(null)
    val mainBitmap: StateFlow<Bitmap?> = _mainBitmap.asStateFlow()

    private val _stampBitmap = MutableStateFlow<Bitmap?>(null)
    val stampBitmap: StateFlow<Bitmap?> = _stampBitmap.asStateFlow()

    private val _detailBitmap = MutableStateFlow<Bitmap?>(null)
    val detailBitmap: StateFlow<Bitmap?> = _detailBitmap.asStateFlow()

    private val _selectedJewelryType = MutableStateFlow("خاتم ذهبي")
    val selectedJewelryType: StateFlow<String> = _selectedJewelryType.asStateFlow()

    private val _claimedKarat = MutableStateFlow("21K")
    val claimedKarat: StateFlow<String> = _claimedKarat.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisProgressText = MutableStateFlow("جاري تهيئة محلل الذكاء الاصطناعي...")
    val analysisProgressText: StateFlow<String> = _analysisProgressText.asStateFlow()

    private val _currentReport = MutableStateFlow<InspectionReport?>(null)
    val currentReport: StateFlow<InspectionReport?> = _currentReport.asStateFlow()

    private val _isSavedInHistory = MutableStateFlow(false)
    val isSavedInHistory: StateFlow<Boolean> = _isSavedInHistory.asStateFlow()

    // History comparison selection
    private val _selectedForComparison = MutableStateFlow<List<InspectionReport>>(emptyList())
    val selectedForComparison: StateFlow<List<InspectionReport>> = _selectedForComparison.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun startNewInspection(isStampOnly: Boolean = false) {
        _mainBitmap.value = null
        _stampBitmap.value = null
        _detailBitmap.value = null
        _currentReport.value = null
        _isSavedInHistory.value = false
        _currentScreen.value = Screen.CameraInspection(isStampOnlyMode = isStampOnly)
    }

    fun setMainBitmap(bitmap: Bitmap) {
        _mainBitmap.value = bitmap
    }

    fun setStampBitmap(bitmap: Bitmap) {
        _stampBitmap.value = bitmap
    }

    fun setDetailBitmap(bitmap: Bitmap) {
        _detailBitmap.value = bitmap
    }

    fun setJewelryType(type: String) {
        _selectedJewelryType.value = type
    }

    fun setClaimedKarat(karat: String) {
        _claimedKarat.value = karat
    }

    fun runAnalysis() {
        val main = _mainBitmap.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisProgressText.value = "جاري قراءة الخصائص البصرية وانعكاسات الذهب..."
            kotlinx.coroutines.delay(600)
            _analysisProgressText.value = "جاري فحص الأختام والدمغات المحفورة ومطابقتها..."
            kotlinx.coroutines.delay(600)
            _analysisProgressText.value = "جاري الكشف عن مؤشرات الطلاء والعيوب السطحية..."

            try {
                val report = analysisService.analyzeJewelry(
                    mainBitmap = main,
                    stampBitmap = _stampBitmap.value,
                    detailBitmap = _detailBitmap.value,
                    jewelryType = _selectedJewelryType.value,
                    claimedKaratInput = _claimedKarat.value
                )
                _currentReport.value = report
                _isSavedInHistory.value = false
                _currentScreen.value = Screen.ReportDetail(report)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun saveCurrentReport() {
        val report = _currentReport.value ?: return
        if (_isSavedInHistory.value) return
        viewModelScope.launch {
            val id = repository.saveInspection(report)
            _currentReport.value = report.copy(id = id)
            _isSavedInHistory.value = true
        }
    }

    fun deleteReport(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearAllReports() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun toggleComparisonSelection(report: InspectionReport) {
        val current = _selectedForComparison.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.id == report.id }
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
        } else {
            if (current.size >= 2) {
                current.removeAt(0)
            }
            current.add(report)
        }
        _selectedForComparison.value = current
    }

    fun startComparison() {
        val list = _selectedForComparison.value
        if (list.size == 2) {
            _currentScreen.value = Screen.Compare(list[0], list[1])
        }
    }
}
