# Thread Pool Experiments

This project houses a number of Android applications which each demonstrate a particular aspect of
the [ThreadPoolExecutor](https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor)
class.

The applications in this project are as follows:

* [app1](app1) – An analysis of the `corePoolSize`, `maximumPoolSize`, `keepAliveTime` and `workQueue`
  parameters available in the `ThreadPoolExecutor` constructor.
* [app2](app2) – A performance comparison of using a single thread executor for executing tasks serially as
  opposed to the `synchronized` keyword.
* [app3](app3) – A performance comparison of using a fixed thread pool for executing tasks concurrently as
  opposed to an unbounded thread pool.
  