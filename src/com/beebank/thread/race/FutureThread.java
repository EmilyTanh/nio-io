package com.beebank.thread.race;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class FutureThread extends Thread {

	/**
     * 选手跑步任务（Player）的执行状态对象
     */
    private Future<Result> future;

    /**
     * 跑步成绩出来后，需要操作的队列
     * （要将对应的选手加入到队列，以便依据成绩进行排序）
     */
    private PriorityBlockingQueue<Player> achievementQueue;

    /**
     * 当前进行跑步的选手
     */
    private Player player;

    public FutureThread(Future<Result> future , Player player , PriorityBlockingQueue<Player> achievementQueue) {
        this.future = future;
        this.player = player;
        this.achievementQueue = achievementQueue;
    }

    @Override
    public void run() {
        // 如果条件成立，最有可能的就是选手在比赛过程中，
        // 由于某种原因退赛了！
        if(this.future == null) {
            System.out.println("选手退赛，计分为0");
        } else {
            try {
                // 如果选手没有跑完，FutureThread将阻塞在这里
                // 当然出现跑步过程中退赛，就会抛出异常
                this.future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 运行到这里，就说明这个选手跑完了（或者退赛了）
        // 无论什么情况，都计入队列，然后通知主线程
        this.achievementQueue.put(this.player);
        synchronized (this.achievementQueue) {
            this.achievementQueue.notify();
        }
    }
}
