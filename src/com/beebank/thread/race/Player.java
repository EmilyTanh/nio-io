package com.beebank.thread.race;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Player implements Callable<Result>,Comparable<Player> {
	
	private String name;//每一个选手的姓名
	private int number;//每一个选手的编号
	private float minSpeed;//最低速度
	private Result result;//比赛结果    
	
	private Semaphore runway;//比赛跑道
	
	//确定跑道上边儿的5个人全部准备开始，最后全部都结束了才开始下一轮
	private CyclicBarrier startingGun;
	
	public Player(String name, int number, Semaphore runway) {
		super();
		this.name = name;
		this.number = number;
		this.runway = runway;
		this.minSpeed = 8f;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Semaphore getRunway() {
		return runway;
	}

	public void setRunway(Semaphore runway) {
		this.runway = runway;
	}
	
	public CyclicBarrier getStartingGun() {
		return startingGun;
	}

	public void setStartingGun(CyclicBarrier startingGun) {
		this.startingGun = startingGun;
	}

	@Override
	public Result call() throws Exception {
		try{
			this.runway.acquire();
			if(this.startingGun != null){
				// 执行到这里，说明这个选手已经拿到了跑到资源；
	            // 向发令枪表达“我已准备好”，即计数器-1
//				this.startingGun.countDown();
				System.out.println("选手" + name + "[" + number + "]，已登上跑道，等待发令！");
	            // 接下来进入“等待”状态
	            // 以便等这个发令枪所管理的所有选手上跑道了，再一起跑步
	            this.startingGun.await();
	            System.out.println("选手" + name + "[" + number + "]，跑！");
			}
			return this.doRun();
		}catch (Exception e) {
			e.printStackTrace(System.out);
		}finally {
			System.out.println("选手" + name + "[" + number + "]，比赛正常完成！");
			this.startingGun.await();
			this.runway.release();
		}
		
		// 如果执行到这里，说明发生了异常
        this.result = new Result(Float.MAX_VALUE);
        return result;
	}
	
	/** 
	 * @Title: doRun 
	 * @Description: 进入跑道进行比赛
	 * @return
	 * @author haiqing.tan
	 * 2016年9月28日
	 */
	public Result doRun() throws Exception{
		//为了保证每个运动员的比赛状态都不低于最低速度，设置一个临时速度
		//且临时速度不会低于最低速度，也不会高于14米/秒
		float presentSpeed = 0f;
		presentSpeed = this.minSpeed * (1.0f + new Random().nextFloat());
		if(presentSpeed > 14f){
			presentSpeed = 14f;
		}
		
		// 计算跑步结果(BigDecimal的使用可自行查阅资料)
        BigDecimal calculation =  new BigDecimal(100).divide(new BigDecimal(presentSpeed) , 3, RoundingMode.HALF_UP);
        float presentTime = calculation.floatValue();

        // 让线程等待presentSpeed的时间，模拟该选手跑步的过程
        synchronized (this) {
            this.wait((long)(presentTime * 1000f));
        }

        // 返回跑步结果
        this.result = new Result(presentTime);
        return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 比较两个选手的比赛结果
	 */
	@Override
	public int compareTo(Player he) { 
		Result my = this.getResult();
		Result hResult = he.getResult();
		// 如果出现了reslut为null或者targetReslut为null，说明比赛结果出现了问题
        // 当然如果真的出现这样的问题，最可能的选手中途退赛了
        if(my == null) {
            return 1;
        }
        if(hResult == null) {
            return -1;
        }

        // 耗时越少的选手，当然应该排在“成绩”队列的越前面
        if(my.getTime() < hResult.getTime()) {
            return -1;
        } else {
            return 1;
        }
	}
	
}
