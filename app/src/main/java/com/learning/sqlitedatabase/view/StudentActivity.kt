package com.learning.sqlitedatabase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.learning.sqlitedatabase.adapter.StudentAdapter
import com.learning.sqlitedatabase.database.StudentDatabase
import com.learning.sqlitedatabase.databinding.ActivityStudentBinding
import com.learning.sqlitedatabase.model.StudentModel
import com.learning.sqlitedatabase.myUtil.showToast

class StudentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStudentBinding
    private lateinit var studentDB : StudentDatabase
    private lateinit var adapter : StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            studentDB = StudentDatabase(this@StudentActivity)
            adapter = StudentAdapter(emptyList(),
                onDeleteClick = {student -> showDeleteConfirmation(student)},
                onItemLongClick = {student -> handleLongPressed(student)})
            rvStudent.adapter = adapter
            loadStudents()
            binding.fbAddStudent.setOnClickListener{
                Intent(this@StudentActivity, AddActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadStudents()
    }
    private fun loadStudents(){
        val studentList = studentDB.getAllStudents()
        adapter.updateStudent(studentList)
    }
    private fun showDeleteConfirmation(student: StudentModel){
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure to delete?")
            .setCancelable(true)
            .setPositiveButton("Delete"){_,_ ->
                if(studentDB.deleteStudent(student.s_id!!)) {
                    showToast("${student.s_name}'s records deleted successfully")
                }
                loadStudents()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun handleLongPressed(student : StudentModel){
        Intent(this@StudentActivity, AddActivity::class.java).also {
            it.putExtra("student",student)
            startActivity(it)
        }
    }
}