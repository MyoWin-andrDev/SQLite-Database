package com.learning.sqlitedatabase.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learning.sqlitedatabase.model.StudentModel

class StudentDatabase( val context : Context) : SQLiteOpenHelper(context, "Student.db", null, 1 ){
    lateinit var db : SQLiteDatabase
    companion object{
        val TBL_STUDENT = "tbl_user"
    }

    override fun onCreate(database : SQLiteDatabase?) {
        database?.execSQL(("""
            CREATE TABLE $TBL_STUDENT( 
                s_id INTEGER PRIMARY KEY AUTOINCREMENT ,
                s_name TEXT UNIQUE COLLATE NOCASE ,
                s_grade TEXT , s_room TEXT , s_gender TEXT ,
                s_fatherName TEXT) """).trimIndent())
    }

    override fun onUpgrade(database : SQLiteDatabase?, p1: Int, p2: Int) {
        database?.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT ")
    }

    fun addStudent(student: StudentModel): Boolean{
        db = writableDatabase
        val values = ContentValues().apply {
            put("s_name", student.s_name)
            put("s_grade", student.s_grade)
            put("s_room", student.s_room)
            put("s_gender", student.s_gender)
            put("s_fatherName", student.s_fatherName)
        }
        return try{
            db.insert(TBL_STUDENT, null, values)
            true
        }
        catch (e : Exception){
            false
        }
        finally {
            db.close()
        }

    }

    fun getAllStudents(): List<StudentModel> {
        val students = mutableListOf<StudentModel>()
        db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_STUDENT", null)
        cursor.use {
            if (it.moveToFirst()) {
                while (!it.isAfterLast) {
                    students.add(StudentModel(
                        s_id = it.getInt(it.getColumnIndexOrThrow("s_id")),
                        s_name = it.getString(it.getColumnIndexOrThrow("s_name")),
                        s_grade = it.getString(it.getColumnIndexOrThrow("s_grade")),
                        s_room = it.getString(it.getColumnIndexOrThrow("s_room")),
                        s_gender = it.getString(it.getColumnIndexOrThrow("s_gender")),
                        s_fatherName = it.getString(it.getColumnIndexOrThrow("s_fatherName"))
                    ))
                    it.moveToNext()
                }
            }
        }
        return students
    }

    fun updateStudent(student: StudentModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("s_name", student.s_name)
            put("s_grade", student.s_grade)
            put("s_room", student.s_room)
            put("s_gender", student.s_gender)
            put("s_fatherName", student.s_fatherName)
        }
        return try{
            db.update(TBL_STUDENT, values, "s_id = ?", arrayOf((student.s_id).toString()))
            true
        }catch (e : Exception){
            false
        }finally {
            db.close()
        }
    }

    fun deleteStudent(id: Int): Boolean {
        val db = writableDatabase
        return try {
            db.delete(TBL_STUDENT, "s_id = ?", arrayOf(id.toString()))
            true
        }catch (e : Exception){
            false
        }finally {
            db.close()
        }
    }
}