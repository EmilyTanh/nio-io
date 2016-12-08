package com.beebank.design.factory;

public class EmailSend implements Send {

	@Override
	public void send() {
		System.out.println("这是Email发送消息！！！");
	}

}
