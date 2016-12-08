package com.beebank.thread;

public class MyFirstThread extends Thread {

	private String name;
	
    
    public MyFirstThread(String name) {
		super();
		this.name = name;
	}

	@Override
    public void run() {
        System.out.println("线程（" + name + "）做了一些事情，然后结束了。");
    }

    public static void main(String[] args) throws Exception {
        new MyFirstThread("aaaa").start();
    }
}