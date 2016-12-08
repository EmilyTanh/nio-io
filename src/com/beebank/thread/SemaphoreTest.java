package com.beebank.thread;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void main(String[] args) {
		Semaphore semp = new Semaphore(5 , false);

        // 我们创建10个线程，并通过0-9的index进行编号
        for(int index = 0 ; index < 10 ; index++) {
            Thread semaphoreThread = new Thread(new SemaphoreRunnableNonfair(semp , index));
            semaphoreThread.start();
        }
	}

	/**
     * 测试Semaphore的非公平模式
     * @author haiqing.tan
     */
    private static class SemaphoreRunnableNonfair implements Runnable {

        private Semaphore semp;

        /**
         * 编号
         */
        private Integer index;

        public SemaphoreRunnableNonfair(Semaphore semp , Integer index) {
            this.semp = semp;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                System.out.println("线程" + this.index + "等待信号。。。。。。");
                this.semp.acquire();
//                if(this.semp.tryAcquire()){
                //当出现这句代码，当semp正在获得许可的请求中，接收到当前线程停止，会报InterruptedException错误
//                Thread.currentThread().interrupt();
                // 停止一段时间，模拟业务处理过程
                synchronized(this) {
                    System.out.println("index 为 " + this.index + " 的线程，获得信号，开始处理业务");
                    this.wait(5000);
                }
//                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.out);
            } finally {
                // 最后都要释放这个信号/证书
                this.semp.release();
            }
        }
    }
}
