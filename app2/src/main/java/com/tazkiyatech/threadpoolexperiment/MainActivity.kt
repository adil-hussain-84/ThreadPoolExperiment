package com.tazkiyatech.threadpoolexperiment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor

    private var currentTaskCount = 0
    private val totalTasksToSchedule = 100

    private var startTime = 0L
    private var tasksInProgress = false

    private val currentThreadName: String
        get() = Thread.currentThread().name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = Executors.newSingleThreadExecutor()

        findViewById<View>(R.id.button1).setOnClickListener { scheduleMultipleTasksInSingleThreadExecutor() }
        findViewById<View>(R.id.button2).setOnClickListener { scheduleMultipleTasksInPlainThreads() }
    }

    private fun scheduleMultipleTasksInSingleThreadExecutor() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress", Toast.LENGTH_SHORT).show()
            return
        }

        updateTextInTextView("Executing tasks...")

        tasksInProgress = true

        currentTaskCount = 0
        startTime = Date().time

        repeat(totalTasksToSchedule) {
            executor.execute { computeFactorial() }
        }
    }

    private fun scheduleMultipleTasksInPlainThreads() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress", Toast.LENGTH_SHORT).show()
            return
        }

        updateTextInTextView("Executing tasks...")

        tasksInProgress = true

        currentTaskCount = 0
        startTime = Date().time

        repeat(totalTasksToSchedule) {
            Thread { synchronized(this) { computeFactorial() } }.start()
        }
    }

    private fun computeFactorial() {
        currentTaskCount++

        Log.d("MainActivity", "$currentThreadName: Started task $currentTaskCount")

        var count = BigInteger.valueOf(1L)

        (1..5_000).forEach {
            count = count.multiply(BigInteger.valueOf(it.toLong()))
        }

        Log.d("MainActivity", "$currentThreadName: Ended task $currentTaskCount")

        if (currentTaskCount == totalTasksToSchedule) {
            val totalExecutionTimeInMillis = Date().time - startTime

            updateTextInTextView("Done. Completed all tasks in $totalExecutionTimeInMillis milliseconds")

            tasksInProgress = false
        }
    }

    private fun updateTextInTextView(text: String) {
        runOnUiThread { findViewById<TextView>(R.id.textView).text = text }
    }
}