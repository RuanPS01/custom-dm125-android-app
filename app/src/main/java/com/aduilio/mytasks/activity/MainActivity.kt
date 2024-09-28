package com.aduilio.mytasks.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aduilio.mytasks.R
import com.aduilio.mytasks.adapter.TasksAdapter
import com.aduilio.mytasks.databinding.ActivityMainBinding
import com.aduilio.mytasks.entity.Task
import com.aduilio.mytasks.fragment.PreferenceFragment
import com.aduilio.mytasks.listener.TaskListItemListener
import com.aduilio.mytasks.service.TaskService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var tasksAdapter: TasksAdapter

    private val taskService: TaskService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("lifecycle", "Main onCreate")

        initComponents()

        askNotificationPermission()
    }

    override fun onStart() {
        super.onStart()

        Log.e("lifecycle", "Main onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.e("lifecycle", "Main onResume")

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PreferenceFragment.DAILY_NOTIFICATION_KEY, false)
        Log.e("pref", "O valor da configuração é: $pref")

        readTasks()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, PreferenceActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initComponents() {
        tasksAdapter = TasksAdapter(this, object : TaskListItemListener {
            override fun onClick(task: Task) {
                val intent = Intent(this@MainActivity, TaskFormActivity::class.java)
                intent.putExtra("task", task)
                startActivity(intent)
            }
        })
        binding.rvTasks.adapter = tasksAdapter
        binding.rvTasks.layoutManager = LinearLayoutManager(this)

        binding.fabNewTask.setOnClickListener {
            startActivity(Intent(this, TaskFormActivity::class.java))
        }
    }

    private fun readTasks() {
        taskService.readAll().observe(this) { responseDto ->
            if (responseDto.isError) {
                Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
            } else {
                responseDto.value?.let { tasks ->
                    tasksAdapter.setItems(tasks)
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    AlertDialog.Builder(this)
                            .setMessage(R.string.notification_permission_rationale)
                            .setPositiveButton(
                                R.string.accept
                            ) { _, _ ->
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            .setNegativeButton(R.string.not_accept, null)
                            .show()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.e("permission", "Permission dada: $isGranted")
        }
}