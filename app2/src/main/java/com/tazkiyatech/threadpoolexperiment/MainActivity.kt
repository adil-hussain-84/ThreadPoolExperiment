package com.tazkiyatech.threadpoolexperiment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor

    private var currentTaskCount = 0
    private val totalTaskCount = 100

    private var startTime = 0L

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
        currentTaskCount = 0
        startTime = Date().time

        repeat(totalTaskCount) {
            executor.execute { computeFactorial() }
        }
    }

    private fun scheduleMultipleTasksInPlainThreads() {
        currentTaskCount = 0
        startTime = Date().time

        repeat(totalTaskCount) {
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

        if (currentTaskCount == totalTaskCount) {
            val totalExecutionTimeInMillis = Date().time - startTime

            Log.d("MainActivity", "Total execution time = $totalExecutionTimeInMillis")
        }
    }
}