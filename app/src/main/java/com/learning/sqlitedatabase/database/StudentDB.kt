package com.learning.sqlitedatabase.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learning.sqlitedatabase.model.StudentModel


class StudentDB(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Student.db"
        const val TABLE_STUDENT = "tbl_user"


        const val GENDER_MALE = 1
        const val GENDER_FEMALE = 0
        const val GENDER_UNSELECTED = -1

        const val S_ID = "s_id"
        const val S_NAME = "s_name"
        const val S_GRADE = "s_grade"
        const val S_ROOM = "s_room"
        const val S_GENDER = "s_gender"
        const val S_FATHER_NAME = "s_fatherName"

    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE $TABLE_STUDENT( 
                $S_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $S_NAME TEXT UNIQUE COLLATE NOCASE,
                $S_GRADE TEXT,
                $S_ROOM TEXT,
                $S_GENDER TEXT,
                $S_FATHER_NAME TEXT)
        """.trimIndent()
        )
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(database)
    }

    fun addStudent(student: StudentModel): Boolean = writableDatabase.use { db ->
        val values = ContentValues().apply {
            put(S_NAME, student.studentName)
            put(S_GRADE, student.grade)
            put(S_ROOM, student.roomNo)
            put(S_GENDER, student.gender)
            put(S_FATHER_NAME, student.fatherName)
        }

        try {
            db.insert(TABLE_STUDENT, null, values)
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()

        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENT", null)

            cursor.use {
                while (it.moveToNext()) {
                    students.add(
                        StudentModel(
                            studentId = it.getInt(it.getColumnIndexOrThrow(S_ID)),
                            studentName = it.getString(it.getColumnIndexOrThrow(S_NAME)),
                            grade = it.getString(it.getColumnIndexOrThrow(S_GRADE)),
                            roomNo = it.getString(it.getColumnIndexOrThrow(S_ROOM)),
                            gender = it.getString(it.getColumnIndexOrThrow(S_GENDER)),
                            fatherName = it.getString(it.getColumnIndexOrThrow(S_FATHER_NAME))
                        )
                    )
                }
            }
            cursor.close()
            db.close()
        }
        return students
    }

    fun updateStudent(student: StudentModel): Boolean = writableDatabase.use { db ->
        val values = ContentValues().apply {
            put(S_NAME, student.studentName)
            put(S_GRADE, student.grade)
            put(S_ROOM, student.roomNo)
            put(S_GENDER, student.gender)
            put(S_FATHER_NAME, student.fatherName)
        }

        try {
            db.update(
                TABLE_STUDENT,
                values,
                "$S_ID = ?",
                arrayOf(student.studentId.toString())
            )
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }


    fun deleteStudent(id: Int): Boolean = writableDatabase.use { db ->
        try {
            db.delete(
                TABLE_STUDENT,
                "$S_ID = ?",
                arrayOf(id.toString())
            )
            true
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}