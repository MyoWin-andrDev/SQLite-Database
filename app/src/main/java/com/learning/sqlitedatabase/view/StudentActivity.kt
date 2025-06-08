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

    private lateinit var binding: ActivityStudentBinding
    private lateinit var studentDB: StudentDatabase
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupDatabase()
        setupRecyclerView()
        setupListeners()
        loadStudents()
    }

    override fun onResume() {
        super.onResume()
        loadStudents()
    }

    private fun setupBinding() {
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupDatabase() {
        studentDB = StudentDatabase(this)
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            studentList = emptyList(),
            onDeleteClick = { student -> showDeleteConfirmation(student) },
            onEditClick = { student -> navigateToEditStudent(student) }
        )
        binding.rvStudent.adapter = adapter
    }

    private fun setupListeners() {
        binding.fbAddStudent.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun loadStudents() {
        val students = studentDB.getAllStudents()
        adapter.updateStudent(students)
    }

    private fun showDeleteConfirmation(student: StudentModel) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete ${student.studentName}'s record?")
            .setCancelable(true)
            .setPositiveButton("Delete") { _, _ ->
                if (studentDB.deleteStudent(student.studentId!!)) {
                    showToast("${student.studentName}'s record deleted successfully")
                    loadStudents()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToEditStudent(student: StudentModel) {
        Intent(this, AddActivity::class.java).also {
            it.putExtra("student", student)
            startActivity(it)
        }
    }

}
