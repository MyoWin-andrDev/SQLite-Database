package com.learning.sqlitedatabase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel(
    val studentId: Int? = null,
    val studentName: String,
    val studentGrade: String,
    val studentRoom: String,
    val studentGender: String,
    val studentFatherName: String
) : Parcelable