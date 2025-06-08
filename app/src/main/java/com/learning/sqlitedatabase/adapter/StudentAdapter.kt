package com.learning.sqlitedatabase.adapter

import android.annotation.SuppressLint
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.learning.sqlitedatabase.R
import com.learning.sqlitedatabase.databinding.ListItemStudentBinding
import com.learning.sqlitedatabase.model.StudentModel

class StudentAdapter(
    private var studentList: List<StudentModel>,
    private val onDeleteClick: (StudentModel) -> Unit,
    private val onEditClick: (StudentModel) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ListItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentModel) {
            val genderIcon = if (student.studentGender == "M") {
                R.drawable.baseline_male_24
            } else {
                R.drawable.baseline_female_24
            }

            binding.apply {
                tvName.text = student.studentName
                tvGradeValue.text = toBoldText("Grade", student.studentGrade)
                tvRoomNo.text = toBoldText("Room", student.studentRoom)
                ivgender.setImageResource(genderIcon)

                btDelete.setOnClickListener { onDeleteClick(student) }
                btEdit.setOnClickListener { onEditClick(student) }
            }
        }

        private fun toBoldText(label: String, value: String): Spanned {
            return HtmlCompat.fromHtml(
                "$label : <b>$value</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemStudentBinding.inflate(inflater, parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position])
    }

    override fun getItemCount(): Int = studentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateStudent(newStudentList: List<StudentModel>) {
        studentList = newStudentList
        notifyDataSetChanged()
    }
}
