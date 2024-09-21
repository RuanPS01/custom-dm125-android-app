package com.aduilio.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aduilio.mytasks.adapter.TasksAdapter
import com.aduilio.mytasks.databinding.ActivityMainBinding
import com.aduilio.mytasks.entity.Task
import com.aduilio.mytasks.service.RetrofitService
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("lifecycle", "Main onCreate")

        val retrfoit = RetrofitService()

        initComponents()
    }

    override fun onStart() {
        super.onStart()

        Log.e("lifecycle", "Main onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.e("lifecycle", "Main onResume")
    }

    override fun onStop() {
        super.onStop()

        Log.e("lifecycle", "Main onStop")
    }

    override fun onPause() {
        super.onPause()

        Log.e("lifecycle", "Main onPause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("lifecycle", "Main onDestroy")
    }

    private fun initComponents() {
        val tasksAdapter = TasksAdapter(this)
        binding.rvTasks.adapter = tasksAdapter
        binding.rvTasks.layoutManager = LinearLayoutManager(this)

        val tasks = ArrayList<Task>()
        tasks.add(Task(title = "Tarefa sem data"))
        for (i in 1..20) {
            tasks.add(Task(title = "Tarefa $i", date = LocalDate.now()))
        }

        tasksAdapter.setItems(tasks)

        binding.fabNewTask.setOnClickListener {
            startActivity(Intent(this, TaskFormActivity::class.java))
        }
    }
}