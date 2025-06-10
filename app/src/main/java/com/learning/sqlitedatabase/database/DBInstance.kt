package com.learning.sqlitedatabase.database

import android.content.Context
import com.learning.sqlitedatabase.database.StudentDB

object DBInstance {
    private var INSTANCE: StudentDB? = null

    fun getInstance(context: Context): StudentDB {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: StudentDB(context.applicationContext).also { INSTANCE = it }
        }
    }
}