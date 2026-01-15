package com.example.to_dolist

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

        dbHelper = TaskDbHelper(this)
        updateUI()

        binding.buttonAdd.setOnClickListener {
            val task = binding.editTextTask.text.toString()
            if (task.isNotEmpty()){
                dbHelper.addTask(task)
                binding.editTextTask.text.clear()
                updateUI()
            }else{
                Toast.makeText(this,"Please Enter a task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.listViewTasks.setOnItemLongClickListener { parent,_, position, _->
            val taskToDelete = parent.getItemAtPosition(position) as String

            AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes"){_,_->
                    dbHelper.deleteTask(taskToDelete)
                    updateUI()
                    Toast.makeText(this,"$taskToDelete Deleted",Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No",null)
                .show()
            true
        }
    }
    private fun updateUI(){
        val taskList = dbHelper.getAllTasks()
        taskAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            taskList
        )
        binding.listViewTasks.adapter = taskAdapter
    }
}
