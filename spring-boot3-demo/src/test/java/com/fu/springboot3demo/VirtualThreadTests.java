package com.fu.springboot3demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


/**
 * JDK21虚拟线程测试类
 * 创建日期：2024-05-09
 */
@Slf4j
public class VirtualThreadTests {

    /**
     * 方式一：创建一个执行器，为每个任务启动一个新的虚拟线程
     */
    @Test
    void testVirtualThread1() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println("task: " + i);
                return i;
            }));
        }
    }

    /**
     * 方式二：
     * 1、创建虚拟线程并自动执行
     * 2、创建不自动启动的线程，手动执行
     */
    @Test
    void testVirtualThread2() {
        // 创建虚拟线程并自动执行
        var virtual = Thread.ofVirtual().name("pack1");
        virtual.start(() -> System.out.printf("%s - 自动执行任务完成\n", Thread.currentThread().getName()));
        // 创建不自动启动的线程
        var thread = virtual.unstarted(() -> System.out.printf("%s - 手动执行任务完成\n", Thread.currentThread().getName()));
        // 手动启动虚拟线程
        thread.start();
        // 打印线程对象：VirtualThread[#21,pack]/runnable
        System.out.println("VirtualThread1: " + thread);
    }

    /**
     * 方式三：通过 ThreadFactory 工厂创建
     */
    @Test
    void testVirtualThread3() {
        ThreadFactory threadFactory = Thread.ofVirtual().name("threadFactory").factory();
        threadFactory.newThread(() -> System.out.printf("%s - 通过工厂创建执行任务完成", Thread.currentThread().getName())).start();
    }

    /**
     * 方式四：直接通过Thread静态方法创建
     * 该方式无法指定线程名称，线程名称默认为：null
     */
    @Test
    void testVirtualThread4() {
        Thread.startVirtualThread(() -> System.out.printf("线程ID：%s - 任务执行完成", Thread.currentThread().threadId()));
    }

    /**
     * 虚拟线程join()函数的作用：内部虚拟线程阻塞外部虚拟线程，并且直到内部虚拟线程执行完，才继续往下执行外部虚拟线程的剩余代码。
     * 注意：即使内部虚拟线程抛出异常，也不会阻止外部虚拟线程继续往下执行。因为内部虚拟线程抛出的异常不会往外部虚拟线程抛！！！
     */
    @Test
    void testVirtualThread5() throws InterruptedException {
        Thread.startVirtualThread(() -> {
            System.out.printf("0.我是虚拟线程？%s\n", Thread.currentThread().isVirtual());
            System.out.printf("1.我是第1个虚拟线程%s...\n", Thread.currentThread().threadId());
            Thread.startVirtualThread(() -> System.out.printf("3.我是第2个虚拟线程%s...\n", Thread.currentThread().threadId()));
            System.out.printf("2.不阻塞第1个虚拟线程，不等第2个虚拟线程执行完，第1个虚拟线程继续往下执行剩余代码，当前虚拟线程ID:%s\n", Thread.currentThread().threadId());
        });
        Thread.sleep(1000L);//休眠1秒，查看结果。
        System.out.println("======== 分割线 ========");
        Thread.startVirtualThread(() -> {
            System.out.printf("1.我是第1个虚拟线程%s...\n", Thread.currentThread().threadId());
            Thread thread2 = Thread.startVirtualThread(() -> System.out.printf("2.我是第2个虚拟线程%s...\n", Thread.currentThread().threadId()));
            try {
                //等待线程2执行完，才往下执行。
                thread2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("3.阻塞第1个虚拟线程，等第2个虚拟线程执行完，第1个虚拟线程才继续往下执行剩余代码，当前线程ID:%s\n", Thread.currentThread().threadId());
        });
    }

    /**
     * 结构化并发（预览功能）
     * 当一个子任务发生错误时，其它的子任务会在未完成的情况下取消
     */
    @Test
    void testStructuredTaskScope() throws InterruptedException {
        /*try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> fork1 = scope.fork(VirtualThreadTests::task1);
            StructuredTaskScope.Subtask<String> fork2 = scope.fork(VirtualThreadTests::task2);
            StructuredTaskScope.Subtask<String> fork3 = scope.fork(VirtualThreadTests::task3);
            scope.join();
            System.out.println(fork1);
            System.out.println(fork2);
            System.out.println(fork3);
        }*/
    }

    private static String task1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "task1 ok";
    }

    private static String task2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println(1 / 0);
        return "task2 ok";
    }

    private static String task3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "task3 ok";
    }

}
