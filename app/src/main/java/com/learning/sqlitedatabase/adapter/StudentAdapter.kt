package com.learning.sqlitedatabase.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.sqlitedatabase.databinding.ListItemStudentBinding
import com.learning.sqlitedatabase.model.StudentModel

class StudentAdapter (private var studentList :List<StudentModel>, private val onDeleteClick : (StudentModel) -> Unit, private val onItemLongClick : (StudentModel) -> Unit) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class  StudentViewHolder(val binding : ListItemStudentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
        StudentViewHolder(ListItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = studentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateStudent(newStudentList : List<StudentModel>){
        this.studentList = newStudentList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.binding.apply {
            val student = studentList[position]
            tvName.text = student.s_name
            tvStudentId.text = student.s_id.toString()
            tvGrade.text = student.s_grade
            tvRoom.text = student.s_room
            btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
            root.setOnLongClickListener {
                onItemLongClick(student)
                true
            }
        }
    }
}