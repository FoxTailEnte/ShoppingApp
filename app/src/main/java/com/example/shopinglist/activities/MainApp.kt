package com.example.shopinglist.activities

import android.app.Application
import com.example.shopinglist.database.MainDataBase

class MainApp: Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
}