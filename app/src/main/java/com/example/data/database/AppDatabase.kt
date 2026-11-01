package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.dao.InspectionDao
import com.example.data.model.InspectionReport
import com.example.data.model.VerificationStatus

class Converters {
    @TypeConverter
    fun fromVerificationStatus(status: VerificationStatus): String {
        return status.name
    }

    @TypeConverter
    fun toVerificationStatus(value: String): VerificationStatus {
        return try {
            VerificationStatus.valueOf(value)
        } catch (e: Exception) {
            VerificationStatus.YELLOW
        }
    }
}

@Database(
    entities = [InspectionReport::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inspectionDao(): InspectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gold_expert_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
