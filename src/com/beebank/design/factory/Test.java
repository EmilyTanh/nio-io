package com.beebank.design.factory;

public class Test {

	public static void main(String[] args) {
		SendFactory factory = new SendFactory();
		Send send = factory.sendMes("xxx");
		send.send();
	}

}
