package com.example.cleanarchitectureandroid.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cleanarchitectureandroid.model.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {

        @Volatile
        private var INSTANCE: UserDataBase? = null
        fun getDataBase(context: Context): UserDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "item_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
