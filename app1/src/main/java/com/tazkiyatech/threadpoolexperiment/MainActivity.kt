package com.tazkiyatech.threadpoolexperiment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {

    private lateinit var executor1: Executor
    private lateinit var executor2: Executor
    private lateinit var executor3: Executor
    private lateinit var executor4: Executor

    private var taskCount = 1

    private val currentThreadName: String
        get() = Thread.currentThread().name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor1 = ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS, LinkedBlockingQueue(2))
        executor2 = ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS, SynchronousQueue())
        executor3 = Executors.newFixedThreadPool(3)
        executor4 = Executors.newCachedThreadPool()

        findViewById<View>(R.id.button1).setOnClickListener { executeRunnableIn(executor1) }
        findViewById<View>(R.id.button2).setOnClickListener { executeRunnableIn(executor2) }
        findViewById<View>(R.id.button3).setOnClickListener { executeRunnableIn(executor3) }
        findViewById<View>(R.id.button4).setOnClickListener { executeRunnableIn(executor4) }
    }

    private fun executeRunnableIn(executor: Executor) {
        try {
            executor.execute(createRunnable())
        } catch (e: RejectedExecutionException) {
            Log.e("MainActivity", "${e.message}")
            Toast.makeText(this, "Rejected execution. See Logcat for details.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createRunnable(): Runnable {
        val taskID = taskCount++

        return Runnable {
            Log.d("MainActivity", "$currentThreadName: Starting task $taskID")
            SystemClock.sleep(3000L)
            Log.d("MainActivity", "$currentThreadName: Ending task $taskID")
        }
    }
}