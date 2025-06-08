package com.learning.sqlitedatabase.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.learning.sqlitedatabase.R
import com.learning.sqlitedatabase.database.StudentDatabase
import com.learning.sqlitedatabase.databinding.ActivityAddBinding
import com.learning.sqlitedatabase.model.StudentModel
import com.learning.sqlitedatabase.myUtil.showToast

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var studentDB: StudentDatabase
    private var student: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentDB = StudentDatabase(this)
        student = intent.getParcelableExtra("student") as? StudentModel

        setupToolbar()
        populateFieldsIfNeeded()
        setupSaveButton()
    }

    private fun setupToolbar() = with(binding.tbStudentAdd) {
        title = if (student != null) "Edit Student" else "Add Student"
        setNavigationOnClickListener { finish() }
    }

    private fun populateFieldsIfNeeded() = student?.let {
        binding.etName.setText(it.studentName)
        binding.etGrade.setText(it.studentGrade)
        binding.etRoomNo.setText(it.studentRoom)
        binding.etFatherName.setText(it.studentFatherName)
        binding.rgGender.check(if (it.studentGender == "M") R.id.rbMale else R.id.rbFemale)
    }

    private fun setupSaveButton() {
        binding.btSave.setOnClickListener {
            if (areFieldsValid()) saveStudent()
        }
    }

    private fun areFieldsValid(): Boolean = with(binding) {
        when {
            etName.text.isNullOrBlank() -> {
                etName.error = "Name required"; false
            }

            etGrade.text.isNullOrBlank() -> {
                etGrade.error = "Grade required"; false
            }

            etRoomNo.text.isNullOrBlank() -> {
                etRoomNo.error = "Room No required"; false
            }

            etFatherName.text.isNullOrBlank() -> {
                etFatherName.error = "Father's name required"; false
            }

            else -> true
        }
    }

    private fun saveStudent() = with(binding) {
        val gender = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> "M"
            R.id.rbFemale -> "F"
            else -> ""
        }

        val studentData = StudentModel(
            studentId = student?.studentId,
            studentName = etName.text.toString().trim(),
            studentGrade = etGrade.text.toString().trim(),
            studentRoom = etRoomNo.text.toString().trim(),
            studentGender = gender,
            studentFatherName = etFatherName.text.toString().trim()
        )

        if (student == null) {
            studentDB.addStudent(studentData)
            showToast("${studentData.studentName}'s Record Added")
        } else {
            studentDB.updateStudent(studentData)
            showToast("${studentData.studentName}'s Record Updated")
        }
        finish()
    }
}
