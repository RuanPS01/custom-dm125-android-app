package com.aduilio.mytasks.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aduilio.mytasks.databinding.TaskListItemBinding
import com.aduilio.mytasks.entity.Task

class TaskViewHolder(private val binding: TaskListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(task: Task) {
        binding.tvTitle.text = task.title

        binding.tvDate.text = task.date?.let {
            task.date
        } ?: run {
            "-"
        }
    }
}