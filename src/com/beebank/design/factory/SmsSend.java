package com.beebank.design.factory;

public class SmsSend implements Send {

	@Override
	public void send() {
		System.out.println("这是sms发送消息！！");
	}

}
