package com.beebank.design.factory;

public class SendFactory {
	public Send sendMes(String symb){
		if("sms".equals(symb)){
			return new SmsSend();
		}else if("email".equals(symb)){
			return new EmailSend();
		}else{
			System.err.println("输入错误");
			return null;
		}
	}
}
