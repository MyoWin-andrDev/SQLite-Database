package com.learning.sqlitedatabase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel (
    val studentId : Int = 0,
    val studentName : String,
    val grade : String,
    val roomNo : String,
    val gender : Int,
    val fatherName : String) : Parcelable