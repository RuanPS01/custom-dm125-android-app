package com.aduilio.mytasks.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aduilio.mytasks.databinding.TaskListItemBinding
import com.aduilio.mytasks.entity.Task
import com.aduilio.mytasks.listener.TaskListItemListener

class TasksAdapter(
    private val context: Context,
    private val listener: TaskListItemListener
) : RecyclerView.Adapter<TaskViewHolder>() {

    private val tasks = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.e("adapter", "Criando um TaskViewHolder")

        val binding = TaskListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskViewHolder(binding, listener)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, position: Int) {
        Log.e("adapter", "Populando um TaskViewHolder")

        val task = tasks[position]
        taskViewHolder.setValues(task)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Task>) {
        tasks.clear()
        tasks.addAll(items)

        notifyDataSetChanged()
    }
}