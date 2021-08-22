# 第二周作业说明

* 第1题（选做）：使用GCLogAnalysis.java演练GC，在windows上，基于Xmx1g、Xms1g内存运行时如下：
  * Serial GC: 内存分配速率达到4.18 gb/sec，年轻代对象提升到老年代速率达到1.02 gb/sec，每次GC平均时间在32ms，总GC时间480ms，GC次数15
  * Parallel GC: 内存分配速率达到3.89 gb/sec，年轻代对象提升到老年代速率达到1.05 gb/sec，每次GC平均时间在21.7ms，总GC时间520ms，GC次数24
  * CMS GC:  内存分配速率达到3.35 gb/sec，年轻代对象提升到老年代速率达到1.01 gb/sec，每次GC平均时间在28.1ms，总GC时间320ms，GC次数12
  * G1 GC:  内存分配速率达到3.38 gb/sec，堆内存持续上升，每次GC平均时间在8.21ms，总GC时间132ms，GC次数12
  * 所有GC运行的分配速率都远大于提升速率，且堆内存持续上升，其中G1的每次GC时间最短，

* 第2题（选做）：使用压测工具演练gateway，使用wrk工具在windows上，基于Xmx1g、Xms1g内存运行压测：
  * CMS GC: 最大可以到41个并发
  * G1 GC: 最大可以到50个并发

* 第4题（必做）：不同 GC 和堆内存的总结：见[\week2\jvm总结.md](\work\2.6-jvm总结.md)

* 第6题（必做）：写代码使用 HttpClient或OkHttp访问：见[com.java.training.week2.HttpClientWork.java](..\java\com\java\training\week2\HttpClientWork.java)

