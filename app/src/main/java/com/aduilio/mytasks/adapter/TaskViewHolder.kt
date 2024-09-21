package com.aduilio.mytasks.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aduilio.mytasks.databinding.TaskListItemBinding
import com.aduilio.mytasks.entity.Task
import com.aduilio.mytasks.listener.TaskListItemListener

class TaskViewHolder(
    private val binding: TaskListItemBinding,
    private val listener: TaskListItemListener
) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(task: Task) {
        binding.tvTitle.text = task.title

        binding.tvDate.text = task.date?.let {
            task.date.toString()
        } ?: run {
            "-"
        }

        binding.root.setOnClickListener {
            listener.onClick(task)
        }
    }
}