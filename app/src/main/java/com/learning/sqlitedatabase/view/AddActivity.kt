package com.learning.sqlitedatabase.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.learning.sqlitedatabase.database.StudentDatabase
import com.learning.sqlitedatabase.databinding.ActivityAddBinding
import com.learning.sqlitedatabase.model.StudentModel
import com.learning.sqlitedatabase.myUtil.showToast

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    private lateinit var studentDB : StudentDatabase
    private var student : StudentModel? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            studentDB = StudentDatabase(this@AddActivity)
            student = intent.getSerializableExtra("student") as? StudentModel

            if (student != null) {
                tvTitle.text = "Edit Student"
                populateFields()
            } else {
                tvTitle.text = "Add Student"
            }
            binding.btnSave.setOnClickListener {
                if (validateFields()) {
                    saveStudent()
                }
            }
        }
    }

    private fun populateFields() {
        student?.let {
            binding.etName.setText(it.s_name)
            binding.etGrade.setText(it.s_grade)
            binding.etRoomNo.setText(it.s_room)
            binding.etGender.setText(it.s_gender)
            binding.etFatherName.setText(it.s_fatherName)
        }
    }

    private fun validateFields(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.etName.error = "Name required"
            return false
        }
        if (binding.etGrade.text.toString().trim().isEmpty()) {
            binding.etGrade.error = "Grade required"
            return false
        }
        if (binding.etRoomNo.text.toString().trim().isEmpty()) {
            binding.etRoomNo.error = "Room No required"
            return false
        }
        if (binding.etGender.text.toString().trim().isEmpty()) {
            binding.etGender.error = "Gender required"
            return false
        }
        if (binding.etFatherName.text.toString().trim().isEmpty()) {
            binding.etFatherName.error = "Father's name required"
            return false
        }
        return true
    }

    private fun saveStudent() {
        binding.apply {
            val newStudentRecord = StudentModel(
                s_id = student?.s_id,
                s_name = etName.text.toString().trim(),
                s_grade = etGrade.text.toString().trim(),
                s_room = etRoomNo.text.toString().trim(),
                s_gender = etGender.text.toString().trim(),
                s_fatherName = etFatherName.text.toString().trim()
            )
            if (student == null) {
                studentDB.addStudent(newStudentRecord)
                showToast("${newStudentRecord.s_name}'s Record Added")
            } else {
                studentDB.updateStudent(newStudentRecord)
                showToast("${newStudentRecord.s_name}'s Record Updated")
            }
            finish()
        }
    }
}