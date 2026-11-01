package com.example.data.model

data class GoldKaratInfo(
    val karat: String,
    val hallmarkNumber: String,
    val purityPercentage: Double,
    val englishName: String,
    val arabicName: String,
    val description: String,
    val commonUsage: String,
    val colorTone: String,
    val hardness: String,
    val counterfeitWarning: String
)

object GoldKaratRegistry {
    val KARAT_LIST = listOf(
        GoldKaratInfo(
            karat = "24K",
            hallmarkNumber = "999",
            purityPercentage = 99.9,
            englishName = "24 Karat Pure Gold",
            arabicName = "عيار 24 (ذهب نقي)",
            description = "أنقى أشكال الذهب بنسبة نقاوة 99.9%. يتميز بلون أصفر غني ودافئ ولمعان ساطع ولكنه لين نسبياً وعرضة للخدش والانثناء.",
            commonUsage = "السبائك الاستثمارية، والعملات الذهبية، وبعض الحلي التقليدية الثقيلة.",
            colorTone = "أصفر ذهبي فاقع وعميق جداً",
            hardness = "طري / لين (صلابة منخفضة 2.5 على مقياس موس)",
            counterfeitWarning = "نادراً ما يُصنع منه مجوهرات رفيعة؛ إذا رأيت قطعة رقيقة جداً ومحفور عليها 999 فقد تكون مطلية فقط."
        ),
        GoldKaratInfo(
            karat = "22K",
            hallmarkNumber = "916",
            purityPercentage = 91.6,
            englishName = "22 Karat Gold",
            arabicName = "عيار 22",
            description = "يحتوي على 91.6% ذهب خالص مع 8.4% معادن أخرى تمنحه متانة إضافية مع الحفاظ على اللون الذهبي الفاخر.",
            commonUsage = "مجوهرات المناسبات الفاخرة، خاصة في الهند والخليج وجنوب آسيا.",
            colorTone = "أصفر ذهبي ساطع وغني",
            hardness = "متوسط الطراوة (أقوى قليلاً من 24K)",
            counterfeitWarning = "يُقلد كثيراً بالنحاس المطلي بماء الذهب عيار 24؛ يجب الانتباه لعدم وجود لمعان نحاسي أحمر خشن."
        ),
        GoldKaratInfo(
            karat = "21K",
            hallmarkNumber = "875",
            purityPercentage = 87.5,
            englishName = "21 Karat Gold",
            arabicName = "عيار 21 (الأكثر انتشاراً في العالم العربي)",
            description = "المعيار الذهبي والعيار الأكثر شعبية في الشرق الأوسط والخليج العربي ومصر. يوازن بشكل ممتاز بين نقاوة اللون والصلابة.",
            commonUsage = "أطقم الأعراس، الأساور (الغوايش)، الخواتم، والسلاسل اليومية.",
            colorTone = "أصفر كلاسيكي متوازن وجذاب",
            hardness = "صلابة جيدة مناسبة للاستخدام اليومي",
            counterfeitWarning = "الدمغة 875 قد تُزور على معادن رخيصة، ويجب التأكد من عمق ودقة ختم مصلحة الدمغة والموازين."
        ),
        GoldKaratInfo(
            karat = "18K",
            hallmarkNumber = "750",
            purityPercentage = 75.0,
            englishName = "18 Karat Gold",
            arabicName = "عيار 18 (المجوهرات الإيطالية والألماس)",
            description = "يحتوي على 75% ذهب نقي و25% سبائك مثل الفضة والنحاس أو البلاديوم. عيار قوي ومتين ومثالي لتثبيت الأحجار الكريمة والألماس.",
            commonUsage = "مجوهرات الماركات العالمية، الذهب الأبيض، الذهب الوردي، وقطع الألماس الفاخرة.",
            colorTone = "أصفر هادئ، أو أبيض ناصع، أو وردي أنيق",
            hardness = "صلابة ممتازة ومقاومة عالية للخدوش",
            counterfeitWarning = "شائع جداً في التزوير واستخدام أختام 750 وهمية على الفضة المطلية (Vermeil) أو النيكل."
        ),
        GoldKaratInfo(
            karat = "14K",
            hallmarkNumber = "585",
            purityPercentage = 58.5,
            englishName = "14 Karat Gold",
            arabicName = "عيار 14",
            description = "يحتوي على 58.5% ذهب و41.5% معادن مضافة. يتميز بصلابة فائقة ومقاومة عالية للاحتكاك، وشائع في أمريكا الشمالية وأوروبا.",
            commonUsage = "الخواتم اليومية، السلاسل العملية، والمجوهرات الرياضية المقاومة للجهد.",
            colorTone = "أصفر مائل للبهتان أو شاحب قليلاً مقارنة بـ 21K",
            hardness = "صلب جداً ومقاوم للثني",
            counterfeitWarning = "قد يتغير لونه بمرور السنوات بسبب أكسدة المعادن المرافقة (النحاس والنيكل) إذا تعرض للأحماض."
        ),
        GoldKaratInfo(
            karat = "10K",
            hallmarkNumber = "417",
            purityPercentage = 41.7,
            englishName = "10 Karat Gold",
            arabicName = "عيار 10",
            description = "أقل عيار قانوني يُصنف كذهب في الولايات المتحدة الأمريكية. يحتوي على نسبة معادن أساسية تفوق نسبة الذهب الخالص.",
            commonUsage = "خواتم التخرج، المجوهرات الاقتصادية المقاومة للصدمات.",
            colorTone = "أصفر باهت أو أسمر خفيف",
            hardness = "شديد الصلابة لكنه قد يكون عرضة للكسر تحت الضغط القوي",
            counterfeitWarning = "يحتوي على نسبة نحاس وزنك عالية تجعله يتفاعل أحياناً مع تعرق الجلد أو العطور."
        ),
        GoldKaratInfo(
            karat = "9K",
            hallmarkNumber = "375",
            purityPercentage = 37.5,
            englishName = "9 Karat Gold",
            arabicName = "عيار 9",
            description = "يحتوي على 37.5% ذهب فقط. معتمد في بريطانيا وبعض الدول الأوروبية كخيار اقتصادي قوي.",
            commonUsage = "الحلي الاقتصادية والتذكارات الأوروبية.",
            colorTone = "شاحب أو رمادي مصفر",
            hardness = "قوي وصلب للغاية",
            counterfeitWarning = "قد يُباع أحياناً بأسعار مبالغ فيها مع إخفاء حقيقة أن 62.5% من كتلته معادن عادية."
        )
    )

    fun findByNumber(input: String): GoldKaratInfo? {
        val clean = input.trim().replace("K", "").replace("k", "").replace("عيار", "").trim()
        return KARAT_LIST.firstOrNull {
            it.hallmarkNumber == clean ||
            it.karat.equals(clean + "K", ignoreCase = true) ||
            it.karat.equals(clean, ignoreCase = true) ||
            clean.contains(it.hallmarkNumber)
        }
    }
}
