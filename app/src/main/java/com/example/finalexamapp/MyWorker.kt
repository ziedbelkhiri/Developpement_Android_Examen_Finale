package com.example.finalexamapp

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // Perform background task here
        Log.d("MyWorker", "Tâche en arrière-plan exécutée")
        return Result.success()
    }
}
