# 第四周作业说明

* 第2题（必做）思考有多少种方式，在 main 函数启动一个新线程：
  * 见示例代码中的：[CreateThreadDemo](..\java\com\java\kaige\week4\CreateThreadDemo.java)

* 第3题（选做）列举常用的并发操作 API 和工具类：
  * API：
    * Object对象的wait/notify/notifyAll
    * Thread的sleep、yield、join、interrupt、currentThread、contextClassLoader
    * lock、unclock、volatile、synchorized、
    * await、countDown、executor
      
  * 工具类：
    * Atomic类：AtomicInteger、AtomicLong、AtomicBoolean、AtomicReference等
    * lock锁相关：Lock、Condition、ReetrantLock、ReetranReadWriteLock、StampedLock、LockSuport等    
    * 协作同步相关：Semaphore、CountDownLatch、CryclicBarrier、Phaser等
    * 线程池相关：ExecutorService、ThreadPoolExecutor、ScheduleThreadPoolExecutor等
    * 执行相关：Runable、Callble、Funture等
    * 其它：TimeUnit、
        
  
* 第4题（选做）请思考: 什么是并发? 什么是高并发? 实现高并发高可用系统需要考虑哪些 因素，对于这些你是怎么理解的?
  * 并发：指同时拥有两个或多个线程，如CPU为单核时交替执行，如CPU是多核时，每个线程多将分配一个处理器上，同时运行；
  * 高并发：同城是指能同时并行处理很多请求的场景，如电商秒杀、股票交易、12306订票等场景
  * 理解：并发可以分为并发读、并发写的场景
    * 并发读场景：可以使用缓存，如JVM缓存（如google的guava）、分布式缓存redis等；
    * 并发写场景：尽量少用锁、或用乐观锁，引入队列、缓存等
    * 并发需要先分析瓶颈，如网络带宽、数据库链接、tomcat线程池等，不同问题优化思路不同：
        * 如网络问题：可增加带宽、DNS域名解析、CDN静态资源等；
        * 如数据库链接问题：可优化应用SQL、分库分表、读写分离等
        * 如tomcat线程池问题：优化tomcat线程池、JVM参数优化、通过相关工具，分析应用的性能瓶颈
        

* 第5题（选做）请思考: 还有哪些跟并发类似 / 有关的场景和问题，有哪些可以借鉴的解决 办法
    * mysql的MVCC并发控制
    * 数据库的分库分表：与并发加锁时，把大锁拆分为多个小锁类似
    * 分布式事务、最终一致性，与 

* 第6题（必做）把多线程和并发相关知识梳理一遍，画一个脑图，截图上传到 GitHub 上。 可选工具:xmind，百度脑图，wps，MindManage，或其他。
 * 见图：[多线程和并发相关知识点](..\resources\多线程和并发相关知识点.png)
 


