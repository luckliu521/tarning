package com.java.kaige.week4;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class CreateThreadDemo {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        int result = simpleAsyncTaskExecutorDemo();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        System.exit(0);
    }

    public static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    /**
     * 方式1：实现Thread，Join方法阻塞当前线程以等待子线程执行完毕
     *
     * @return
     */
    private static int threadDemo() {
        AtomicInteger result = new AtomicInteger();
        Thread thread = new Thread(() -> {
            System.out.println("方式1：实现Thread，Join方法阻塞当前线程以等待子线程执行完毕");
            result.set(sum()); //这是得到的返回值
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return result.intValue();
    }

    /**
     * 方式2：实现Runnable接口，Join方法阻塞当前线程以等待子线程执行完毕
     *
     * @return
     */
    private static int threadAndRunableDemo() {
        final int[] result = {0};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("方式2：实现Runnable接口，Join方法阻塞当前线程以等待子线程执行完毕");
                result[0] = sum(); //这是得到的返回值
                countDownLatch.countDown();
            }
        });
        thread.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }


    /**
     * 方式3： 通过FutureTask,实现Callable接口
     *
     * @return
     */
    private static int futureTaskDemo() {
        FutureTask<Integer> futureTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("方式3： 通过FutureTask,实现Callable接口");
                return sum();
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (futureTask.isDone()) {
            try {
                return futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 方式4： 通过ExecutorService线程池,实现Callable接口
     *
     * @return
     */
    private static int executorServicePoolDemo() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("方式4： 通过ExecutorService线程池,实现Callable接口");
                return sum();
            }
        });

        while (!future.isDone()) {
            try {
                executorService.shutdown();
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 方式5： 通过Executor线程池,实现Callable接口
     *
     * @return
     */
    private static int executorPoolDemo() {
        final int[] result = {0};
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        Executor executorPool = Executors.newSingleThreadExecutor();
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("方式5: 通过Executor线程池,实现Callable接口");
                result[0] = sum();
            }
        });

        while (result[0] == 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result[0];
    }

    /**
     * 方式7: 通过ScheduledExecutorService方式
     *
     * @return
     */
    private static int scheduledExecutorServiceDemo() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Integer result = 0;
        try {
            result = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println("方式7: 通过ScheduledExecutorService方式");
                    return sum();
                }
            }).get();

            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 方式8: 通过Spring ThreadPoolTaskExecutor方式
     * @return
     */
    private static int threadPoolTaskExecutorDemo() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        Future<Integer> future = threadPoolTaskExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("方式8: 通过ThreadPoolTaskExecutor方式");
                return sum();
            }
        });

        try {
            TimeUnit.MILLISECONDS.sleep(100);
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 方式9: 通过Spring SimpleAsyncTaskExecutor
     * @return
     */
    private static int simpleAsyncTaskExecutorDemo(){
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        Future<Integer> future = simpleAsyncTaskExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("方式9: 通过SimpleAsyncTaskExecutor");
                return sum();
            }
        });

        try {
            while (!future.isDone()) {
                return future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 方式10: 通过Spring ConcurrentTaskScheduler方式
     * @return
     */
    private static int concurrentTaskSchedulerDemo(){
        ConcurrentTaskScheduler concurrentTaskScheduler = new ConcurrentTaskScheduler();
        CountDownLatch cdt = new CountDownLatch(1);
        final int[] result = {0};
        concurrentTaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("方式10: 通过Spring ConcurrentTaskScheduler方式");
                result[0] = sum();
                cdt.countDown();
            }
        });

        try {
            cdt.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }
    /**
     * 方式11: 通过Timer方式
     *
     * @return
     */
    private static int timerDemo() {
        final int[] result = {0};
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("方式11: 通过Timer方式");
                result[0] = sum();
            }
        }, 1);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }



}
