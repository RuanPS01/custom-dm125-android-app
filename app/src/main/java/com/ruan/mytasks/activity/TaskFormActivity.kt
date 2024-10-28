package com.ruan.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ruan.mytasks.databinding.ActivityTaskFormBinding
import com.ruan.mytasks.entity.Task
import com.ruan.mytasks.service.TaskService
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.TimePicker
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding

    private val taskService: TaskService by viewModels()

    private var taskId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("lifecycle", "TaskForm onCreate")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initComponents()
        setValues()
    }

    private fun openCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                binding.etDate.setText(
                    (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun openTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                binding.etTime.setText(
                    String.format("%02d:%02d", selectedHour, selectedMinute)
                )
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun initComponents() {
        binding.btSave.setOnClickListener {
            binding.tilTitle.error = null
            binding.tilDate.error = null
            binding.tilTime.error = null

            // Verificar se os campos estão preenchidos
            val title = binding.etTitle.text.toString()
            val dateInput = binding.etDate.text.toString()
            val timeInput = binding.etTime.text.toString()

            var isValid = true

            if (title.isEmpty()) {
                binding.tilTitle.error = "Título é obrigatório"
                isValid = false
            }
            if (dateInput.isEmpty()) {
                binding.tilDate.error = "Data é obrigatória"
                isValid = false
            }
            if (timeInput.isEmpty()) {
                binding.tilTime.error = "Hora é obrigatória"
                isValid = false
            }

            // Se algum campo obrigatório estiver vazio, interrompe a execução
            if (!isValid) return@setOnClickListener


            val date = if (dateInput.isNotEmpty()) LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("d/M/yyyy")) else null
            val time = if (timeInput.isNotEmpty()) LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm")) else null

            val task = Task(
                id = taskId,
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                date = date,
                time = time
            )

            taskService.save(task).observe(this) { responseDto ->
                if (responseDto.isError) {
                    Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                } else {
                    finish()
                }
            }
        }

        binding.etDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openCalendar()
            }
        }

        binding.etTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openTimePicker()
            }
        }

        binding.etDate.setOnClickListener {
            openCalendar()
        }

        binding.etTime.setOnClickListener {
            openTimePicker()
        }
    }

    @Suppress("deprecation")
    private fun setValues() {
        supportActionBar?.title = "Nova Tarefa"
        (intent.extras?.getSerializable("task") as Task?)?.let { task ->
            taskId = task.id
            if (task.id != null) {
               supportActionBar?.title = "Editar Tarefa"
            }

            binding.etTitle.setText(task.title)
            binding.etDescription.setText(task.description)

            task.date?.let { date ->
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                binding.etDate.setText(formattedDate)
            }

            task.time?.let { time ->
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))
                binding.etTime.setText(formattedTime) // assumindo que você tem um EditText para time
            }

            if (task.completed) {
                binding.btSave.visibility = View.INVISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Log.e("lifecycle", "TaskForm onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.e("lifecycle", "TaskForm onResume")
    }

    override fun onStop() {
        super.onStop()

        Log.e("lifecycle", "TaskForm onStop")
    }

    override fun onPause() {
        super.onPause()

        Log.e("lifecycle", "TaskForm onPause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("lifecycle", "TaskForm onDestroy")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}