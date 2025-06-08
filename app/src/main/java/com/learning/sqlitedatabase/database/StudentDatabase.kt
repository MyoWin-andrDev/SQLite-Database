package com.learning.sqlitedatabase.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learning.sqlitedatabase.model.StudentModel

class StudentDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Student.db"
        private const val DATABASE_VERSION = 1
        const val TBL_STUDENT = "tbl_user"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TBL_STUDENT (
                s_id INTEGER PRIMARY KEY AUTOINCREMENT,
                s_name TEXT UNIQUE COLLATE NOCASE,
                s_grade TEXT,
                s_room TEXT,
                s_gender TEXT,
                s_fatherName TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }

    fun addStudent(student: StudentModel): Boolean {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("s_name", student.studentName)
                put("s_grade", student.studentGrade)
                put("s_room", student.studentRoom)
                put("s_gender", student.studentGender)
                put("s_fatherName", student.studentFatherName)
            }
            return try {
                val result = db.insert(TBL_STUDENT, null, values)
                result != -1L
            } catch (e: Exception) {
                false
            }
        }
    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT * FROM $TBL_STUDENT", null)
            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        students.add(
                            StudentModel(
                                studentId = it.getInt(it.getColumnIndexOrThrow("s_id")),
                                studentName = it.getString(it.getColumnIndexOrThrow("s_name")),
                                studentGrade = it.getString(it.getColumnIndexOrThrow("s_grade")),
                                studentRoom = it.getString(it.getColumnIndexOrThrow("s_room")),
                                studentGender = it.getString(it.getColumnIndexOrThrow("s_gender")),
                                studentFatherName = it.getString(it.getColumnIndexOrThrow("s_fatherName"))
                            )
                        )
                    } while (it.moveToNext())
                }
            }
        }
        return students
    }


    fun updateStudent(student: StudentModel): Boolean {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("s_name", student.studentName)
                put("s_grade", student.studentGrade)
                put("s_room", student.studentRoom)
                put("s_gender", student.studentGender)
                put("s_fatherName", student.studentFatherName)
            }
            return try {
                val rowsUpdated = db.update(TBL_STUDENT, values, "s_id = ?", arrayOf(student.studentId.toString()))
                rowsUpdated > 0
            } catch (e: Exception) {
                false
            }
        }
    }


    fun deleteStudent(id: Int): Boolean {
        writableDatabase.use { db ->
            return try {
                val rowsDeleted = db.delete(TBL_STUDENT, "s_id = ?", arrayOf(id.toString()))
                rowsDeleted > 0
            } catch (e: Exception) {
                false
            }
        }
    }

}