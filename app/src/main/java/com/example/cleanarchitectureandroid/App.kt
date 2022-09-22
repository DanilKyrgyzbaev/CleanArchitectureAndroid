package com.example.cleanarchitectureandroid

import android.app.Application
import com.example.cleanarchitectureandroid.room.UserDataBase

class App : Application() {

    val database: UserDataBase by lazy { UserDataBase.getDataBase(this) }

}
