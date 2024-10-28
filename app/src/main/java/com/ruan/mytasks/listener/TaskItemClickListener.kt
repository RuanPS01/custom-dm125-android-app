package com.ruan.mytasks.listener

import com.ruan.mytasks.entity.Task

interface TaskItemClickListener {

    fun onClick(task: Task)

    fun onMarkAsCompleteClick(position: Int, task: Task)

    fun onShareClick(task: Task)
}