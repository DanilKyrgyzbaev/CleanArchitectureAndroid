package com.example.cleanarchitectureandroid

import android.app.Application
import com.example.cleanarchitectureandroid.room.UserDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    val database: UserDataBase by lazy { UserDataBase.getDataBase(this) }

}
