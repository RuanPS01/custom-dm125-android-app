package com.ruan.mytasks.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ruan.mytasks.R
import com.ruan.mytasks.databinding.TaskListItemBinding
import com.ruan.mytasks.entity.Task
import com.ruan.mytasks.listener.TaskItemClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewHolder(
    private val context: Context,
    private val binding: TaskListItemBinding,
    private val listener: TaskItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(task: Task) {
        binding.tvTitle.text = task.title

        if (task.completed) {
            binding.viewIndicator.setBackgroundResource(R.color.gray_300)
            binding.tvTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_300))
        } else {
            val today = LocalDate.now()
            val taskDate = task.date

            binding.viewIndicator.setBackgroundResource(R.color.primary)
            binding.tvTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            when {
                taskDate == null || taskDate.isAfter(today) -> {
                    // Tarefas sem data ou com data futura
                    binding.tvTitle.setBackgroundResource(R.color.primary)

                }
                taskDate.isEqual(today) -> {
                    // Tarefas que vencem hoje
                    binding.tvTitle.setBackgroundResource(R.color.yellow_700)
                }
                taskDate.isBefore(today.minusDays(1)) -> {
                    // Tarefas vencidas (antes de ontem)
                    binding.tvTitle.setBackgroundResource(R.color.red_700)
                }
                else -> {
                    // Tarefas no prazo (hoje ou futuro)
                    binding.tvTitle.setBackgroundResource(R.color.primary)
                }
            }

        }

        task.date?.let { date ->
            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            binding.tvDate.setText(formattedDate)
        } ?: run {
            binding.tvDate.setText("--/--/----")
        }

        task.time?.let { time ->
            val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))
            binding.tvTime.setText(formattedTime)
        } ?: run {
            binding.tvTime.setText("--:--")
        }

        binding.root.setOnClickListener {
            listener.onClick(task)
        }

        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(ContextCompat.getString(context, R.string.mark_as_completed)).setOnMenuItemClickListener {
                listener.onMarkAsCompleteClick(adapterPosition, task)
                true
            }
            menu.add(ContextCompat.getString(context, R.string.share)).setOnMenuItemClickListener {
                listener.onShareClick(task)
                true
            }
        }
    }
}