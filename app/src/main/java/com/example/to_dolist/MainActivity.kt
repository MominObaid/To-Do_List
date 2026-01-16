package com.example.to_dolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.to_dolist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: TaskDbHelper
    private lateinit var taskAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        dbHelper = TaskDbHelper(this)
        updateUI()

        binding.buttonAdd.setOnClickListener {
            val task = binding.editTextTask.text.toString()
            if (task.isNotEmpty()) {
                dbHelper.addTask(task)
                binding.editTextTask.text.clear()
                updateUI()
            } else {
                Toast.makeText(this, "Please Enter a task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.listViewTasks.setOnItemLongClickListener { parent, _, position, _ ->
            val taskToDelete = parent.getItemAtPosition(position) as String

            AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    dbHelper.deleteTask(taskToDelete)
                    updateUI()
                    Toast.makeText(this, "$taskToDelete Deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                AlertDialog.Builder(this)
                    .setTitle("Delete All Tasks")
                    .setMessage("Are you sure you want to delete all tasks? This cannot be undone.")
                    .setPositiveButton("Yes") { _, _ ->
                        dbHelper.deleteAllTasks()
                        updateUI()
                        Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI() {
        val taskList = dbHelper.getAllTasks()
        taskAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            taskList
        )
        binding.listViewTasks.adapter = taskAdapter
    }
}
