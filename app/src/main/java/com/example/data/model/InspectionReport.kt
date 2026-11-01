package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VerificationStatus {
    GREEN,   // Markings visually consistent with claimed karat
    YELLOW,  // Inconclusive; additional inspection recommended
    RED      // Visible inconsistencies or possible imitation indicators
}

@Entity(tableName = "inspections")
data class InspectionReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val jewelryTitle: String,
    val jewelryType: String, // خاتم، سوار، قلادة، سبيكة، عملة، أخرى
    val imagePath: String,
    val stampImagePath: String? = null,
    val detailImagePath: String? = null,
    val recognizedHallmark: String,
    val possibleKarat: String,
    val purityPercentage: Double,
    val verificationStatus: VerificationStatus,
    val statusSummary: String,
    val confidenceScore: Int, // 0 - 100
    val visualObservations: String,
    val platingIndicators: String,
    val imitationSigns: String,
    val scientificDisclaimer: String = "الفحص البصري بالذكاء الاصطناعي لا يثبت نقاوة الذهب بصورة قاطعة، ويجب تأكيد النتيجة باختبارات مخبرية مثل جهاز XRF أو فحص المحك والأحماض.",
    val userNotes: String = ""
)
