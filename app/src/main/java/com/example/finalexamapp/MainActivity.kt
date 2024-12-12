package com.example.finalexamapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Room Database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()


        sharedPreferences = getSharedPreferences("FinalExamPrefs", Context.MODE_PRIVATE)
        nameEditText = findViewById(R.id.nameEditText)

        // Toast message
        Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show()

        //button listeners
        findViewById<Button>(R.id.buttonSnackbar).setOnClickListener {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, getString(R.string.snackbar_message), Snackbar.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.buttonScheduleTask).setOnClickListener {
            scheduleBackgroundTask()
        }

        findViewById<Button>(R.id.buttonNotification).setOnClickListener {
            showNotification()
        }

        findViewById<Button>(R.id.buttonInsertData).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            if (name.isNotEmpty()) {
                insertDataToDatabase(name)
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        //  Display the last saved user name
        findViewById<Button>(R.id.buttonFetchData).setOnClickListener {
            fetchUserPreferences()
        }

        //RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter()
        recyclerView.adapter = adapter

        // Observe LiveData
        db.userDao().getAllUsers().observe(this, Observer { users ->
            adapter.updateData(users.map { it.name })
        })
    }

    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "exam_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Exam Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for FinalExamApp notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun scheduleBackgroundTask() {
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
        Log.d("MainActivity", "Tâche en arrière-plan planifiée")
    }

    // Insert into database
    private fun insertDataToDatabase(name: String) {
        val user = User(name = name)
        Thread {
            db.userDao().insertUser(user)
            saveUserPreferences(name)
            Log.d("MainActivity", "User inserted into the database: $name")
        }.start()
    }

    // Save the user preferences in SharedPreferences
    private fun saveUserPreferences(name: String) {
        sharedPreferences.edit()
            .putString("lastUser", name)
            .apply()
    }

    // Fetch the last user from SharedPreferences
    private fun fetchUserPreferences() {
        val lastUser = sharedPreferences.getString("lastUser", "No user found")
        Toast.makeText(this, "Last User: $lastUser", Toast.LENGTH_SHORT).show()
    }
}
