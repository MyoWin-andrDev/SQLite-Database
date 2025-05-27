package com.learning.sqlitedatabase.model

import java.io.Serializable

data class StudentModel (val s_id : Int? = null, val s_name : String, val s_grade : String, val s_room : String, val s_gender : String , val s_fatherName : String) : Serializable