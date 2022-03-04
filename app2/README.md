# A comparison of using the ThreadPoolExecutor class as a sequential queue as opposed to the synchronized keyword

This module is a simple, single-screen Android application that compares
the [ThreadPoolExecutor](https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor)
class and the [synchronized](https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html)
keyword as a means of achieving a sequential queue.

When you run the app, you will see an interface as in the screenshot below. The text in the interface
describes the purpose of the app and the function of each of the buttons. The two tables below the screenshot
show the difference in total execution time when the factorial function is run repeatedly in
a [Single Thread Executor](https://developer.android.com/reference/java/util/concurrent/Executors#newSingleThreadExecutor())
as opposed to spawning a separate thread for each execution.

<img src="Screenshot.png" width="40%" alt="Screenshot of app">

## Executing the factorial function repeatedly in Single Thread Executor

The table below shows the total execution time when the factorial function is run repeatedly in
a [Single Thread Executor](https://developer.android.com/reference/java/util/concurrent/Executors#newSingleThreadExecutor())
.

<table>
    <tr>
        <th></th>
        <th>factorial(100)</th>
        <th>factorial(1,000)</th>
        <th>factorial(10,000)</th>
    </tr>
    <tr>
        <th>1 execution</th>
        <td>&lt; 10 milliseconds</td>
        <td>&lt; 20 milliseconds</td>
        <td>&lt; 300 milliseconds</td>
    </tr>
    <tr>
        <th>10 executions</th>
        <td>&lt; 30 milliseconds</td>
        <td>&lt; 200 milliseconds</td>
        <td>&lt; 2,000 milliseconds</td>
    </tr>
    <tr>
        <th>100 executions</th>
        <td>&lt; 100 milliseconds</td>
        <td>&lt; 1,000 milliseconds</td>
        <td>&lt; 20,000 milliseconds</td>
    </tr>
</table>

## Spawning a new Thread for each execution of the factorial function

The table below shows the total execution time when the factorial function is run repeatedly in a separate
thread. The threads are `synchronized` so that only one is running at any given time.

<table>
    <tr>
        <th></th>
        <th>factorial(100)</th>
        <th>factorial(1,000)</th>
        <th>factorial(10,000)</th>
    </tr>
    <tr>
        <th>1 execution</th>
        <td>&lt; 10 milliseconds</td>
        <td>&lt; 20 milliseconds</td>
        <td>&lt; 300 milliseconds</td>
    </tr>
    <tr>
        <th>10 executions</th>
        <td>&lt; 30 milliseconds</td>
        <td>&lt; 200 milliseconds</td>
        <td>&lt; 3,000 milliseconds</td>
    </tr>
    <tr>
        <th>100 executions</th>
        <td>&lt; 200 milliseconds</td>
        <td>&lt; 2,000 milliseconds</td>
        <td>&lt; 30,000 milliseconds</td>
    </tr>
</table>

## Conclusion

My takeaway from this app is that the difference between using the
[ThreadPoolExecutor](https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor)
class and the [synchronized](https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html)
keyword for queuing tasks in an Android application is quite inconsequential in terms of execution time. The
total execution time is lower in
the [ThreadPoolExecutor](https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor) when
the size of the task is large and/or when the number of tasks scheduled is large but the difference in
execution time is not as large as one might imagine.
