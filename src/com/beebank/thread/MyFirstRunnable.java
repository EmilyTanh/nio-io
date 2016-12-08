package com.beebank.thread;

public class MyFirstRunnable implements Runnable {
	
	private String name;
	
	public MyFirstRunnable(String name) {
		super();
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println("当前线程" + this.name + "正在运行....");
	}

	public static void main(String[] args) {
		new Thread(new MyFirstRunnable("bbbb")).start();
	}

}
