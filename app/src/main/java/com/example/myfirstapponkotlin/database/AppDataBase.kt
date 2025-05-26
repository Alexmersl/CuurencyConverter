package com.example.myfirstapponkotlin.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfirstapponkotlin.database.StaticDao

@Database(
    entities = [ListOfCurrencies::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getStaticDao(): StaticDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "currency_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

data class StatisticInfoTuple(
    val currencyName: String,
    @ColumnInfo(name = "value") val value: String,
)
