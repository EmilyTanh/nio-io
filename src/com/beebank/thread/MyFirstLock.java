package com.beebank.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyFirstLock{
	/*static Integer count = 1;
	final static ReentrantLock objectLock = new ReentrantLock();
	final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    *//**
     * 拿来加锁的对象
     *//*
//	private static final Object WAIT_OBJECT = new Object();


    *//**
     * 偷懒，我把异常全部抛出了。正式系统不要这么搞哦！！
     * @param args
     * @throws Exception
     *//*
    public static void main(String[] args) throws Exception {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
            	while(count < 10){
            		// 检查'对象锁'状态。
            		synchronized (count) {
            			System.out.println(Thread.currentThread().getName() + "得到了count " + count);
            			count ++;
            		}
            		lock.writeLock();
            		objectLock.lock();
            		System.out.println(Thread.currentThread().getName() + "得到了count " + count);
        			count ++;
        			objectLock.unlock();
            	}
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
            	while(count < 10){
            		// 检查'对象锁'状态。
            		
            		synchronized (count) {
            			System.out.println(Thread.currentThread().getName() + "得到了count " + count);
            			count ++;
            		}
            		
            		objectLock.lock();
            		System.out.println(Thread.currentThread().getName() + "得到了count " + count);
        			count ++;
        			objectLock.unlock();
            	}
            }
        });
        threadA.start();
        threadB.start();
    }*/
	
	/*public  int inc = 0;
    
    public synchronized void increase() {
        inc++;
    }
    
    public static void main(String[] args) {
        final MyFirstLock test = new MyFirstLock();
        for(int i=0;i<10;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<1000;j++)
                        test.increase();
                };
            }.start();
        }
        
        while(Thread.activeCount()>1)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
    }*/
	
	public  AtomicInteger inc = new AtomicInteger();
    
    public  void increase() {
        inc.getAndIncrement();
    }
    
    public static void main(String[] args) {
        final MyFirstLock test = new MyFirstLock();
        for(int i=0;i<10;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<1000;j++)
                        test.increase();
                };
            }.start();
        }
        
        while(Thread.activeCount()>1)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
    }
}
