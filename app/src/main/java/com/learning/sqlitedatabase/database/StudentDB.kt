package com.learning.sqlitedatabase.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learning.sqlitedatabase.database.Constants.Database.DATABASE_NAME
import com.learning.sqlitedatabase.database.Constants.Database.DATABASE_VERSION
import com.learning.sqlitedatabase.database.Constants.Database.TABLE_STUDENT
import com.learning.sqlitedatabase.model.StudentModel

class StudentDB (context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(database: SQLiteDatabase) {
        with(Constants.TableColumns){
            database.execSQL("""
            CREATE TABLE $TABLE_STUDENT( 
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT UNIQUE COLLATE NOCASE,
                $COLUMN_GRADE TEXT,
                $COLUMN_ROOM TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_FATHER_NAME TEXT)
        """.trimIndent())
        }
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        with(Constants.TableColumns){
            database.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
            onCreate(database)
        }
    }

    fun addStudent(student: StudentModel): Boolean = with(Constants.TableColumns) {
        return writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, student.studentName)
                put(COLUMN_GRADE, student.grade)
                put(COLUMN_ROOM, student.roomNo)
                put(COLUMN_GENDER, student.gender)
                put(COLUMN_FATHER_NAME, student.fatherName)
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
    }

    fun getAllStudents(): List<StudentModel> = with(Constants.TableColumns) {
        val students = mutableListOf<StudentModel>()

        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENT", null)

            cursor.use {
                while (it.moveToNext()) {
                    students.add(StudentModel(
                        studentId = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        studentName = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                        grade = it.getString(it.getColumnIndexOrThrow(COLUMN_GRADE)),
                        roomNo = it.getString(it.getColumnIndexOrThrow(COLUMN_ROOM)),
                        gender = it.getInt(it.getColumnIndexOrThrow(COLUMN_GENDER)),
                        fatherName = it.getString(it.getColumnIndexOrThrow(COLUMN_FATHER_NAME))
                    ))
                }
            }
            cursor.close()
            db.close()
        }
        return students
    }

    fun updateStudent(student: StudentModel): Boolean = with(Constants.TableColumns){
        return writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, student.studentName)
                put(COLUMN_GRADE, student.grade)
                put(COLUMN_ROOM, student.roomNo)
                put(COLUMN_GENDER, student.gender)
                put(COLUMN_FATHER_NAME, student.fatherName)
            }

            try {
                db.update(
                    TABLE_STUDENT,
                    values,
                    "$COLUMN_ID = ?",
                    arrayOf(student.studentId.toString())
                )
                true
            } catch (e: Exception) {
                false
            }
            finally {
                db.close()
            }
        }
    }

    fun deleteStudent(id: Int): Boolean = with(Constants.TableColumns) {
        return writableDatabase.use { db ->
            try {
                db.delete(
                    TABLE_STUDENT,
                    "$COLUMN_ID = ?",
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
}