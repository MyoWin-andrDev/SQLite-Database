package com.learning.sqlitedatabase.database

object Constants {

    const val STUDENT_DATA = "STUDENT_DATA"
    const val GENDER_MALE = 1
    const val GENDER_FEMALE = 0
    const val GENDER_UNSELECTED = -1

    //Database
    object Database {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Student.db"
        const val TABLE_STUDENT = "tbl_user"
    }

    // Column names
    object TableColumns {
        const val COLUMN_ID = "s_id"
        const val COLUMN_NAME = "s_name"
        const val COLUMN_GRADE = "s_grade"
        const val COLUMN_ROOM = "s_room"
        const val COLUMN_GENDER = "s_gender"
        const val COLUMN_FATHER_NAME = "s_fatherName"
    }

    object Dialog {
        const val DELETE_TITLE = "Delete Confirmation"
        const val DELETE_MESSAGE = "Are you sure you want to delete %s?"
        const val DELETE_POSITIVE = "Delete"
        const val DELETE_NEGATIVE = "Cancel"
    }

    object Validation {
        const val NAME = "Name"
        const val GRADE = "Grade"
        const val ROOM_NO = "Room No"
        const val FATHER_NAME = "Father's Name"
    }

    object Messages {
        const val RECORD_ADDED = "%s's Record Added"
        const val RECORD_UPDATED = "%s's Record Updated"
        const val ADD_FAILED = "Could not add student"
        const val UPDATE_FAILED = "Could not update student"
        const val DELETE_SUCCESS = "%s's records deleted successfully"
        const val GENERAL_ERROR = "Something went wrong"
    }

    object Titles {
        const val ADD_STUDENT = "Add Student"
        const val EDIT_STUDENT = "Edit Student"
    }
}