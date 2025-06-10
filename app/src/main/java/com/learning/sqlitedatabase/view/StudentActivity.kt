package com.learning.sqlitedatabase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.sqlitedatabase.Utils.showToast
import com.learning.sqlitedatabase.adapter.StudentAdapter
import com.learning.sqlitedatabase.database.Constants
import com.learning.sqlitedatabase.database.DBInstance
import com.learning.sqlitedatabase.database.StudentDB
import com.learning.sqlitedatabase.databinding.ActivityStudentBinding
import com.learning.sqlitedatabase.model.StudentModel
import kotlinx.coroutines.launch

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    private lateinit var studentDB : StudentDB
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        setupAdapter()
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        loadStudents()
    }

    private fun initializeDatabase() {
        studentDB = DBInstance.getInstance(this)
    }

    private fun setupAdapter() {
        adapter = StudentAdapter(
            emptyList(),
            onDeleteClick = ::showDeleteConfirmation,
            onItemLongClick = ::handleLongPressed
        )
        binding.rvStudent.adapter = adapter
    }

    private fun setupViews() {
        binding.fbAddStudent.setOnClickListener {
            navigateToAddEditActivity()
        }
    }

    private fun loadStudents() {
        lifecycleScope.launch {
            val studentList = studentDB.getAllStudents()
            adapter.updateStudent(studentList)
        }
    }

    private fun showDeleteConfirmation(student: StudentModel) {
        AlertDialog.Builder(this)
            .setTitle(Constants.Dialog.DELETE_TITLE)
            .setMessage(Constants.Dialog.DELETE_MESSAGE.format(student.studentName))
            .setCancelable(true)
            .setPositiveButton(Constants.Dialog.DELETE_POSITIVE) { _, _ ->
                deleteStudent(student)
            }
            .setNegativeButton(Constants.Dialog.DELETE_NEGATIVE, null)
            .show()
    }

    private fun deleteStudent(student: StudentModel) {
        lifecycleScope.launch {
            val deletedRows = studentDB.deleteStudent(student.studentId)
            if (deletedRows) {
                showToast(Constants.Messages.DELETE_SUCCESS.format(student.studentName))
                loadStudents()
            }
        }
    }

    private fun handleLongPressed(student: StudentModel) {
        navigateToAddEditActivity(student)
    }

    private fun navigateToAddEditActivity(student: StudentModel? = null) {
        Intent(this, AddStudentActivity::class.java).apply {
            student?.let { putExtra(Constants.STUDENT_DATA, it) }
            startActivity(this)
        }
    }
}