package com.beebank.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class MyFirstReadWriteLock {

	public static void main(String[] args) {
		ReentrantReadWriteLock objectLock = new ReentrantReadWriteLock();
		Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				WriteLock lock = objectLock.writeLock();
				lock.lock();
				System.err.println("thread1做了一些写操作》》》");
				lock.unlock();
			}
		});
		
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				WriteLock lock = objectLock.writeLock();
				lock.lock();
				System.err.println("thread2做了一些写操作》》》");
//				lock.unlock();
				
				//同一个线程对同一个对象在进行写锁之后进行读锁
				ReadLock lock1 = objectLock.readLock();
				lock1.lock();
				System.out.println("thread2做了一些读操作》》》");
				lock1.unlock();
				lock.unlock();
			}
		});
		
		Thread thread3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ReadLock lock = objectLock.readLock();
				lock.lock();
				System.out.println("thread3做了一些读操作》》》");
				lock.unlock();
				//同一个线程对同一个对象使用写锁
				WriteLock lock2 = objectLock.writeLock();
				lock2.lock();
				System.out.println("thread3做了一些写操作》》》");
				lock2.unlock();
				
//				lock.unlock();
			}
		});
		
		thread1.start();
		
//		thread1.interrupt();

		thread2.start();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		thread3.start();
	}

}
