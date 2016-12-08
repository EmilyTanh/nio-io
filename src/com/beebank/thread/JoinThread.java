package com.beebank.thread;

import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 这个线程用来测试join的执行
 * @author yinwenjie
 */
public class JoinThread implements Runnable {

    public static void main(String[] args) throws Exception {
        /*
         * 启动一个子线程joinThread，然后等待子线程joinThread运行完成后
         * 这个线程再继续运行
         * */
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        Thread joinThread = new Thread(new JoinThread());
        joinThread.start();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
        try {
            // 等待，知道joinThread执行完成后，main线程才继续执行
            joinThread.join();
            
            joinThread.sleep(2000);
            System.out.println("线程" + id + "继续执行！" + new Date().getTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        System.err.println("线程" + id + "启动成功，准备进入等待状态（5秒）");

        // 使用sleep方法，模型这个线程执行业务代码的过程
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        	System.err.println(e.getMessage());
        }

        //执行到这里，说明线程被唤醒了
        System.err.println("线程" + id + "执行完成！" + new Date().getTime());
    }
}
