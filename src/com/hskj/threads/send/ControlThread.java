package com.hskj.threads.send;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.hskj.dao.IThreadStatusDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.form.ThreadStatus;
import com.hskj.threads.scan.SmsScanerNew3;

import utils.StatusUtil;

public class ControlThread extends Thread{
	
	private Log log = LogFactory.getLog("serverLog");
	
	private boolean running = true;
	private ApplicationContext apx;
	private IThreadStatusDAO threadStatusDAO;
	private SmsScanerNew3 smsScaner;
	private ArrayBlockingQueue<SmsMessage> putSendFinishQueue;
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue;

	private HashMap<Integer,IControlService> controlMap = new HashMap<Integer, IControlService>();
	
	
	public void run() {
		System.out.println("ControlThread is start");
		log.info("--- ControlThread is start ---");
		
		while (running) {
			try{
				List<ThreadStatus> list = threadStatusDAO.queryThreadStatus(StatusUtil.APP_NAME, 1);
				for (ThreadStatus t : list) {
					
					System.out.println("---thread_sn:"+t.getThread_sn()+"  ---"+"线程类型:"+t.getThread_name()+"  ---status:"+t.getStatus());

					if (t.getStatus() == 1) {// 准备启动
						
						threadStatusDAO.changeThreadStatus(2, t.getThread_id() );// 更新该线程数据库中的状态为正在启动
						IControlService controlService = apx.getBean(t.getThread_name(), IControlService.class);
						
						if (controlService != null) {
							controlService.doInit(t.getThread_param(),t.getThread_id(),smsScaner,putSendFinishQueue,deliverMsgQueue);
							controlMap.put(t.getThread_id(), controlService);
							controlService.doStart();
						}

					}else if(t.getStatus()==4){//准备关闭

						if(controlMap.containsKey(t.getThread_id())){
							
							threadStatusDAO.changeThreadStatus(5, t.getThread_id());//更新数据库中该线程状态为正在关闭
							IControlService controlService = controlMap.remove(t.getThread_id());
							if(controlService != null){
								controlService.doShutDown();
							}
							
						}

					}

				}

			}catch (Exception e) {
				e.printStackTrace();
			}

			try {

				Thread.sleep(5000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		System.out.println("ControlThread is terminal");
	}

	public void shutDown(){
		running = false;
		interrupt();
	}

	public IThreadStatusDAO getThreadStatusDAO() {
		return threadStatusDAO;
	}

	public void setThreadStatusDAO(IThreadStatusDAO threadStatusDAO) {
		this.threadStatusDAO = threadStatusDAO;
	}
	public ApplicationContext getApx() {
		return apx;
	}

	public void setApx(ApplicationContext apx) {
		this.apx = apx;
	}


	public SmsScanerNew3 getSmsScaner() {
		return smsScaner;
	}

	public void setSmsScaner(SmsScanerNew3 smsScaner) {
		this.smsScaner = smsScaner;
	}

	public ArrayBlockingQueue<SmsMessage> getPutSendFinishQueue() {
		return putSendFinishQueue;
	}

	public void setPutSendFinishQueue(
			ArrayBlockingQueue<SmsMessage> putSendFinishQueue) {
		this.putSendFinishQueue = putSendFinishQueue;
	}

	public ArrayBlockingQueue<SmsDeliverForm> getDeliverMsgQueue() {
		return deliverMsgQueue;
	}

	public void setDeliverMsgQueue(
			ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue) {
		this.deliverMsgQueue = deliverMsgQueue;
	}
	
	
	
}
