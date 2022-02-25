# Thread Pool Experiments

This project houses a number of Android applications which each demonstrate a particular aspect of the [ThreadPoolExecutor](https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor) class.

The applications in this project are as follows:

* [app1](app1) – An analysis of the `corePoolSize`, `maximumPoolSize`, `keepAliveTime` and `workQueue` parameters available in the `ThreadPoolExecutor` constructor.
* [app2](app2) – A comparison of using the `ThreadPoolExecutor` class and the `synchronized` keyword as a means of achieving a sequential queue.
