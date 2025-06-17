package com.learning.sqlitedatabase.Utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun Context.showToast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()
}

fun TextInputEditText.validateNotEmpty(
    errorMessage: String,
    textInputLayout: TextInputLayout,
): Boolean {
    return if (text.toString().trim().isEmpty()) {
        textInputLayout.error = errorMessage
        false
    } else {
        textInputLayout.error = null
        true
    }
}