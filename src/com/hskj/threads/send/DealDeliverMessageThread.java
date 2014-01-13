package com.hskj.threads.send;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.log.CommonLogFactory;



/**
 * 长短信拼接线程
 * @author Administrator
 * @time：Apr 1, 2013 2:18:27 PM 
 */
public class DealDeliverMessageThread extends Thread{

	private ServerDAO serverDAO;
	private boolean running = true;
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue;
	private Log log = CommonLogFactory.getLog(this.getClass());
	
	
	
	public  DealDeliverMessageThread(ServerDAO serverDAO, ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue){
		this.serverDAO = serverDAO;
		this.deliverMsgQueue = deliverMsgQueue;
	}

	public void run(){
		if(log.isInfoEnabled()){
			log.info("DealDeliverMessageThread is start ");
		}
		
		SmsDeliverForm deliverForm = null;
			
		while(running){
			
			deliverForm = deliverMsgQueue.poll();
			
			try {
				
				if(deliverForm!= null){
					jointLongDeliver(deliverForm);
				}else {
					Thread.sleep(1000);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}

		if(log.isInfoEnabled()){
			log.info("DealDeliverMessageThread is ternimal ");
		}
	}
	
	
	public void jointLongDeliver(SmsDeliverForm deliverForm){
		
		System.out.println("------"+deliverForm.getSn()+"---"+deliverForm.getSub_msg_id()+"---"+deliverForm.getPk_number());
		
		serverDAO.saveDeliver(deliverForm);

			
	}
	
	
	public void shutDown(){
		running = false;
		interrupt();
	}
}
