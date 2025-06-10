package com.learning.sqlitedatabase.Utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

fun Context.showToast(value : String){
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()
}

fun TextInputEditText.validateNotEmpty(errorMessage: String): Boolean {
    return if (text.toString().trim().isEmpty()) {
        error = errorMessage
        false
    } else {
        error = null
        true
    }
}