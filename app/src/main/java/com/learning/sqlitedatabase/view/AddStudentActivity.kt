package com.learning.sqlitedatabase.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.sqlitedatabase.R
import com.learning.sqlitedatabase.Utils.showToast
import com.learning.sqlitedatabase.Utils.validateNotEmpty
import com.learning.sqlitedatabase.database.Constants
import com.learning.sqlitedatabase.database.DBInstance
import com.learning.sqlitedatabase.database.StudentDB
import com.learning.sqlitedatabase.databinding.ActivityAddStudentBinding
import com.learning.sqlitedatabase.model.StudentModel
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var studentDB: StudentDB
    private var student: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        loadStudentData()
        setupViews()
    }

    private fun initializeDatabase() {
        studentDB = DBInstance.getInstance(this)
    }

    private fun loadStudentData() = with(binding){
        student = intent.getParcelableExtra<StudentModel>(Constants.STUDENT_DATA)
        tbHome.title = if (student != null) Constants.Titles.EDIT_STUDENT else Constants.Titles.ADD_STUDENT
        tbHome.setNavigationOnClickListener { finish() }
        student?.let { populateFields(it) }
    }

    private fun setupViews() {
        binding.btSave.setOnClickListener {
            if (validateFields()) {
                saveStudent()
            }
        }
    }

    private fun populateFields(student: StudentModel) {
        with(binding) {
            etName.setText(student.studentName)
            etGrade.setText(student.grade)
            etRoomNo.setText(student.roomNo)
            etFatherName.setText(student.fatherName)
            when (student.gender) {
                Constants.GENDER_MALE -> rbMale.isChecked = true
                Constants.GENDER_FEMALE -> rbFemale.isChecked = true
            }
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            val isNameValid = etName.validateNotEmpty(Constants.Validation.NAME)
            val isGradeValid = etGrade.validateNotEmpty(Constants.Validation.GRADE)
            val isRoomValid = etRoomNo.validateNotEmpty(Constants.Validation.ROOM_NO)
            val isFatherNameValid = etFatherName.validateNotEmpty(Constants.Validation.FATHER_NAME)

            val isGenderValid = if (rgGender.checkedRadioButtonId == -1) {
                tvGenderError.visibility = View.VISIBLE
                false
            } else {
                tvGenderError.visibility = View.GONE
                true
            }

            return isNameValid && isGradeValid && isRoomValid && isFatherNameValid && isGenderValid
        }
    }

    private fun saveStudent() {
        lifecycleScope.launch {
            val successMessage = if (student == null) {
                handleAddStudent()
            } else {
                handleUpdateStudent()
            }

            showToast(successMessage)
            finish()
        }
    }

    private suspend fun handleAddStudent(): String {
        val studentData = createStudentFromInput()
        return if (studentDB.addStudent(studentData)) {
            Constants.Messages.RECORD_ADDED.format(studentData.studentName)
        } else {
            Constants.Messages.ADD_FAILED
        }
    }

    private suspend fun handleUpdateStudent(): String {
        return if (student != null && updateStudent(student!!)) {
            Constants.Messages.RECORD_UPDATED.format(student!!.studentName)
        } else {
            Constants.Messages.UPDATE_FAILED
        }
    }

    private suspend fun updateStudent(studentData: StudentModel): Boolean = with(binding) {
        return studentDB.updateStudent(
            StudentModel(
                studentId = studentData.studentId,
                studentName = etName.text.toString().trim(),
                grade = etGrade.text.toString().trim(),
                roomNo = etRoomNo.text.toString().trim(),
                gender = getSelectedGender(),
                fatherName = etFatherName.text.toString().trim()
            )
        )
    }

    private fun createStudentFromInput(): StudentModel = with(binding) {
        return StudentModel(
            studentId = 0,
            studentName = etName.text.toString().trim(),
            grade = etGrade.text.toString().trim(),
            roomNo = etRoomNo.text.toString().trim(),
            gender = getSelectedGender(),
            fatherName = etFatherName.text.toString().trim()
        )
    }

    private fun getSelectedGender(): Int {
        return when (binding.rgGender.checkedRadioButtonId) {
            R.id.rbMale -> Constants.GENDER_MALE
            R.id.rbFemale -> Constants.GENDER_FEMALE
            else -> Constants.GENDER_UNSELECTED
        }
    }
}