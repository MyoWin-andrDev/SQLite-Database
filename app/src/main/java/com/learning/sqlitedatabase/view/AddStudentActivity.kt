package com.learning.sqlitedatabase.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.sqlitedatabase.R
import com.learning.sqlitedatabase.Utils.Constants
import com.learning.sqlitedatabase.Utils.Genders
import com.learning.sqlitedatabase.Utils.Localization
import com.learning.sqlitedatabase.Utils.showToast
import com.learning.sqlitedatabase.Utils.validateNotEmpty
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

    private fun loadStudentData() = with(binding) {
        student = intent.getParcelableExtra<StudentModel>(Constants.STUDENT_DATA)
        tbHome.title = if (student != null) Localization.editStudent else Localization.addStudent
        tbHome.setNavigationOnClickListener { finish() }
        student?.let { populateFields(it) }
    }

    private fun setupViews() = with(binding) {
        btSave.setOnClickListener {
            if (validateFields()) {
                saveStudent()
            }
        }
    }

    private fun populateFields(student: StudentModel) = with(binding) {
        val gender =
            runCatching { Genders.valueOf(student.gender) }.getOrElse { Genders.UNSELECTED }

        etName.setText(student.studentName)
        etGrade.setText(student.grade)
        etRoomNo.setText(student.roomNo)
        etFatherName.setText(student.fatherName)
        when (gender) {
            Genders.MALE -> rbMale.isChecked = true
            Genders.FEMALE -> rbFemale.isChecked = true
            Genders.UNSELECTED -> {}
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            val isNameValid = etName.validateNotEmpty(Localization.nameErrorMsg, nameInputLayout)
            val isGradeValid = etGrade.validateNotEmpty(Localization.gradeErrorMsg, gradeInputLayout)
            val isRoomValid = etRoomNo.validateNotEmpty(Localization.roomNoErrorMsg, roomInputLayout)
            val isFatherNameValid = etFatherName.validateNotEmpty(Localization.fatherNameErrorMsg, fatherNameInputLayout)

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

    private fun handleAddStudent(): String {
        val studentData = createStudentFromInput()
        return if (studentDB.addStudent(studentData)) {
            Localization.recordAdded.format(studentData.studentName)
        } else {
            Localization.addFailed
        }
    }

    private fun handleUpdateStudent(): String {
        return if (student != null && updateStudent(student!!)) {
            Localization.recordUpdated.format(student!!.studentName)
        } else {
            Localization.updateFailed
        }
    }

    private fun updateStudent(studentData: StudentModel): Boolean = with(binding) {
        return studentDB.updateStudent(
            StudentModel(
                studentId = studentData.studentId,
                studentName = etName.text.toString().trim(),
                grade = etGrade.text.toString().trim(),
                roomNo = etRoomNo.text.toString().trim(),
                gender = getSelectedGender().name,
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
            gender = getSelectedGender().name,
            fatherName = etFatherName.text.toString().trim()
        )
    }

    private fun getSelectedGender(): Genders {
        return when (binding.rgGender.checkedRadioButtonId) {
            R.id.rbMale -> Genders.MALE
            R.id.rbFemale -> Genders.FEMALE
            else -> Genders.UNSELECTED
        }
    }
}