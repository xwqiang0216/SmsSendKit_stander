package com.hskj.threads.send;

import java.util.concurrent.ArrayBlockingQueue;

import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.threads.scan.SmsScanerNew3;


public interface IControlService {
	public void doShutDown();
	public void doStart();
	public void doInit(String param , int thread_id,SmsScanerNew3 smsScaner,ArrayBlockingQueue<SmsMessage> sendFinishQueue, ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue);
}
