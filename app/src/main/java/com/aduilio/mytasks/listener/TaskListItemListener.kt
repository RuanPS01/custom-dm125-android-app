package com.aduilio.mytasks.listener

import com.aduilio.mytasks.entity.Task

interface TaskListItemListener {

    fun onClick(task: Task)
}