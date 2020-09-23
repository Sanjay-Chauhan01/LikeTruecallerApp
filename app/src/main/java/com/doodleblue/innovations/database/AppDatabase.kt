package com.doodleblue.innovations.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doodleblue.innovations.database.dao.ContactDao
import com.doodleblue.innovations.model.ContactData

@Database(entities = [ContactData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val contactDataDao: ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}