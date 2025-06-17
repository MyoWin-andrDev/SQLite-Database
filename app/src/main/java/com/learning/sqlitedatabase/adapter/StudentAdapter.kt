package com.learning.sqlitedatabase.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.sqlitedatabase.R
import com.learning.sqlitedatabase.Utils.Genders
import com.learning.sqlitedatabase.databinding.ListItemStudentBinding
import com.learning.sqlitedatabase.model.StudentModel

class StudentAdapter(
    private var studentList: List<StudentModel>,
    private val onDeleteClick: (StudentModel) -> Unit,
    private val onItemLongClick: (StudentModel) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ListItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) = with(binding) {
            val student = studentList[position]
            val gender =
                runCatching { Genders.valueOf(student.gender) }.getOrElse { Genders.UNSELECTED }

            tvStudentId.text = student.studentId.toString()
            tvName.text = student.studentName
            tvGrade.text = "Grade : ${student.grade}"
            tvRoomNo.text = "Room : ${student.roomNo}"

            ivGender.setImageResource(
                when (gender) {
                    Genders.MALE -> R.drawable.male
                    Genders.FEMALE -> R.drawable.femenine
                    else -> return@with
                }
            )
            btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
            main.setOnLongClickListener {
                onItemLongClick(student)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
        StudentViewHolder(
            ListItemStudentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = studentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateStudent(newStudentList: List<StudentModel>) {
        this.studentList = newStudentList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(position)
    }
}