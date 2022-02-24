package com.tazkiyatech.threadpoolexperiment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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
    private var startTime = 0L
    private var timeTakenToScheduleAllTasks = 0L
    private var tasksInProgress = false

    private val currentThreadName: String
        get() = Thread.currentThread().name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = Executors.newSingleThreadExecutor()

        findViewById<View>(R.id.button1).setOnClickListener { executeTasksInSingleThreadExecutor() }
        findViewById<View>(R.id.button2).setOnClickListener { executeTasksInPlainOldSynchronizedThreads() }
    }

    private fun executeTasksInSingleThreadExecutor() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress", Toast.LENGTH_SHORT).show()
            return
        }

        val sizeOfEachTask = getSizeOfEachTask() ?: return
        val totalNumberOfTasksToExecute = getTotalNumberOfTasksToExecute() ?: return

        tasksInProgress = true
        currentTaskCount = 0
        startTime = Date().time

        repeat(totalNumberOfTasksToExecute) {
            executor.execute { doSomeWork(sizeOfEachTask, totalNumberOfTasksToExecute) }
        }

        timeTakenToScheduleAllTasks = Date().time - startTime

        updateTextInTextView("Tasks scheduled in $timeTakenToScheduleAllTasks milliseconds.\nAwaiting completion...")
    }

    private fun executeTasksInPlainOldSynchronizedThreads() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress", Toast.LENGTH_SHORT).show()
            return
        }

        val sizeOfEachTask = getSizeOfEachTask() ?: return
        val totalNumberOfTasksToExecute = getTotalNumberOfTasksToExecute() ?: return

        updateTextInTextView("Executing tasks...")

        tasksInProgress = true
        currentTaskCount = 0
        startTime = Date().time

        repeat(totalNumberOfTasksToExecute) {
            Thread { synchronized(this) { doSomeWork(sizeOfEachTask, totalNumberOfTasksToExecute) } }.start()
        }

        timeTakenToScheduleAllTasks = Date().time - startTime

        updateTextInTextView("Tasks scheduled in $timeTakenToScheduleAllTasks milliseconds.\nAwaiting completion...")
    }

    private fun doSomeWork(inputForFactorialFunction: Int, totalNumberOfTasks: Int) {
        currentTaskCount++

        Log.d("MainActivity", "$currentThreadName: Started task $currentTaskCount")

        computeFactorial(inputForFactorialFunction)

        Log.d("MainActivity", "$currentThreadName: Ended task $currentTaskCount")

        if (currentTaskCount == totalNumberOfTasks) {
            val totalExecutionTimeInMillis = Date().time - startTime

            updateTextInTextView("Tasks scheduled in $timeTakenToScheduleAllTasks milliseconds.\nTasks completed in $totalExecutionTimeInMillis milliseconds")

            tasksInProgress = false
        }
    }

    private fun computeFactorial(n: Int): BigInteger {
        var result = BigInteger.valueOf(1L)

        (1..n).forEach {
            result = result.multiply(BigInteger.valueOf(it.toLong()))
        }

        return result
    }

    private fun updateTextInTextView(text: String) {
        runOnUiThread { findViewById<TextView>(R.id.statusTextView).text = text }
    }

    private fun getTotalNumberOfTasksToExecute(): Int? {
        val text = findViewById<EditText>(R.id.totalTasksToExecuteEditText).text

        return try {
            text.toString().toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun getSizeOfEachTask(): Int? {
        val text = findViewById<EditText>(R.id.sizeOfEachTaskEditText).text

        return try {
            text.toString().toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }
}