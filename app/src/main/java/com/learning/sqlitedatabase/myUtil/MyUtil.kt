package com.learning.sqlitedatabase.myUtil

import android.content.Context
import android.widget.Toast

fun Context.showToast(value : String){
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()
}