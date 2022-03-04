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
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor

    private var numTasksStarted = AtomicInteger(0)
    private var numTasksCompleted = AtomicInteger(0)
    private var startTime = 0L
    private var timeTakenToScheduleAllTasks = 0L
    private var tasksInProgress = false

    private val currentThreadName: String
        get() = Thread.currentThread().name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        findViewById<View>(R.id.button1).setOnClickListener { executeTasksInFixedThreadPool() }
        findViewById<View>(R.id.button2).setOnClickListener { executeTasksInPlainThreads() }
    }

    private fun executeTasksInFixedThreadPool() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress.", Toast.LENGTH_SHORT).show()
            return
        }

        val sizeOfEachTask = getSizeOfEachTask() ?: run {
            Toast.makeText(this, "Error: Please enter the size of each task.", Toast.LENGTH_SHORT).show()
            return
        }

        val totalNumberOfTasksToExecute = getTotalNumberOfTasksToExecute() ?: run {
            Toast.makeText(
                this,
                "Error: Please enter the total number of tasks to execute.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        tasksInProgress = true
        numTasksStarted.set(0)
        numTasksCompleted.set(0)
        startTime = Date().time

        repeat(totalNumberOfTasksToExecute) {
            executor.execute { doSomeWork(sizeOfEachTask, totalNumberOfTasksToExecute) }
        }

        timeTakenToScheduleAllTasks = Date().time - startTime

        updateTextInTextView("Tasks scheduled in $timeTakenToScheduleAllTasks milliseconds.\nAwaiting completion...")
    }

    private fun executeTasksInPlainThreads() {
        if (tasksInProgress) {
            Toast.makeText(this, "Error: Tasks already in progress.", Toast.LENGTH_SHORT).show()
            return
        }

        val sizeOfEachTask = getSizeOfEachTask() ?: run {
            Toast.makeText(this, "Error: Please enter the size of each task.", Toast.LENGTH_SHORT).show()
            return
        }

        val totalNumberOfTasksToExecute = getTotalNumberOfTasksToExecute() ?: run {
            Toast.makeText(
                this,
                "Error: Please enter the total number of tasks to execute.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        tasksInProgress = true
        numTasksStarted.set(0)
        numTasksCompleted.set(0)
        startTime = Date().time

        repeat(totalNumberOfTasksToExecute) {
            Thread { doSomeWork(sizeOfEachTask, totalNumberOfTasksToExecute) }.start()
        }

        timeTakenToScheduleAllTasks = Date().time - startTime

        updateTextInTextView("Tasks scheduled in $timeTakenToScheduleAllTasks milliseconds.\nAwaiting completion...")
    }

    private fun doSomeWork(inputForFactorialFunction: Int, totalNumberOfTasks: Int) {
        val numTasksStarted = numTasksStarted.incrementAndGet()

        Log.d("MainActivity", "$currentThreadName: Started task $numTasksStarted")

        computeFactorial(inputForFactorialFunction)

        val numTasksCompleted = numTasksCompleted.incrementAndGet()

        Log.d("MainActivity", "$currentThreadName: Ended task $numTasksCompleted")

        if (numTasksCompleted == totalNumberOfTasks) {
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