package com.beebank.thread.race;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * 测试跑步成绩的类
 * @author admin
 *
 */
public class OneTrack {

	private static final String[] PLAYERNAMES = new String[]{"鸣人","佐助"
	        ,"雏田","小樱","卡卡西","我爱罗","自来也","宁次","鹿丸","井野","丁次"};

    /**
     * 报名队列（非线程安全）
     */
    private List<Player> signupPlayers = new LinkedList<Player>();

    /**
     * 初赛结果队列（有排序功能，且线程安全）
     */
    private PriorityBlockingQueue<Player> preliminaries = new PriorityBlockingQueue<Player>();

    /**
     * 决赛结果队列（有排序功能，且线程安全）
     */
    private PriorityBlockingQueue<Player> finals = new PriorityBlockingQueue<Player>();

    public void track() {
        /*
         * 赛跑分为以下几个阶段进行；
         * 
         * 1、报名
         * 2、初赛，10名选手，分成两组，每组5名选手。
         * 分两次进行初赛（因为场地只有5条赛道，只有拿到进场许可的才能使用赛道，进行比赛）
         * 
         * 3、决赛：初赛结果将被写入到一个队列中进行排序，只有成绩最好的前五名选手，可以参加决赛。
         * 
         * 4、决赛结果的前三名将分别作为冠亚季军被公布出来
         * */

        //1、================报名
        // 这就是跑道，需求上说了只有5条跑道，所以只有5个permits。
        Semaphore runway = new Semaphore(5);
        this.signupPlayers.clear();
        for(int index = 0 ; index < OneTrack.PLAYERNAMES.length ; ) {
            Player player = new Player(OneTrack.PLAYERNAMES[index], ++index , runway);
            this.signupPlayers.add(player);
        }
        
        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>初赛开始》》》》》》》》》》》》》》》》》》");
        //2、================进行初赛
        // 这是赛道的数量
        int runwayCount = 5;
        // 这是裁判
        ExecutorService refereeService = Executors.newFixedThreadPool(5);
        // 这是发令枪
        CyclicBarrier startingGun = null;
        for(int index = 0;index < this.signupPlayers.size();index++){
        	/*
             * 这是发令枪，发令枪的使用规则是：
             * 1、最多5位选手听从一把发令枪的命令（因为跑道最多就是5条）
             * 2、如果剩余的没有比赛的选手不足5人，则这些人听从一把发令枪的命令
             * */
            if(index % runwayCount == 0) {
                startingGun =  this.signupPlayers.size() - index > runwayCount?
                	new CyclicBarrier(runwayCount):
                	new CyclicBarrier(this.signupPlayers.size() - index);
            }
            
            // 获取这个选手
            Player player = this.signupPlayers.get(index);
            // 设置选手关注的发令枪
            player.setStartingGun(startingGun);
            // 提交给裁判组准备执行
            Future<Result> future = refereeService.submit(player);

            // 开始一个选手的跑步动作状态监控
            new FutureThread(future, player, this.preliminaries).start();
        }
        
        //! 只有当PLAYERNAMES.length位选手的成绩都产生了，才能进入决赛，这很重要
        while(this.preliminaries.size() < OneTrack.PLAYERNAMES.length) {
            try {
                synchronized (this.preliminaries) {
                    this.preliminaries.wait();
                }
            } catch(InterruptedException e) {
                e.printStackTrace(System.out);
            }
        }

        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>决赛开始》》》》》》》》》》》》》》》》》》");
        // 3、============决赛(只有初赛结果的前5名可以参见)
        //决赛的发令枪
        startingGun = new CyclicBarrier(5);
        for(int index = 0 ; index < 5 ; index++) {
            Player player = this.preliminaries.poll();
            //重新设置选手关注的发令枪
            player.setStartingGun(startingGun);
            //提交给裁判组准备执行
            Future<Result> future = refereeService.submit(player);
            //开始一个选手的跑步动作状态监控
            new FutureThread(future, player, this.finals).start();
        }
        //! 只有当5位选手的决赛成绩都产生了，才能到下一步：公布成绩
        while(this.finals.size() < 5) {
            try {
                synchronized (this.finals) {
                    this.finals.wait();
                }
            } catch(InterruptedException e) {
                e.printStackTrace(System.out);
            }
        }
        
        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>公布决赛成绩》》》》》》》》》》》》》》》》》》");
        // 4、============公布决赛成绩（前三名）
        for(int index = 0 ; index < 3 ; index++) {
            Player player = this.finals.poll();
            switch (index) {
            case 0:
                System.out.println("第一名："  + player.getName() + "[" + player.getNumber() + "]，成绩：" + player.getResult().getTime() + "秒");
                break;
            case 1:
                System.out.println("第二名："  + player.getName() + "[" + player.getNumber() + "]，成绩：" + player.getResult().getTime() + "秒");
                break;
            case 2:
                System.out.println("第三名："  + player.getName() + "[" + player.getNumber() + "]，成绩：" + player.getResult().getTime() + "秒");
                break;
            default:
                break;
            }
        }
    }

    public static void main(String[] args) throws RuntimeException {
        new OneTrack().track();
    }

}
