package com.hskj.threads.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsMessage;
import com.hskj.log.CommonLogFactory;





/**
 * @author 1007025
 *
 */
public class DealPutSendFinishThread extends Thread{

	private ServerDAO serverDAO;
	private boolean running = true;
	private ArrayBlockingQueue<SmsMessage> sendFinishQueue;
	private Log log = CommonLogFactory.getLog(this.getClass());
	
	public  DealPutSendFinishThread(ServerDAO serverDAO , ArrayBlockingQueue<SmsMessage> sendFinishQueue,ArrayBlockingQueue<SmsMessage> lastSendInfoQueue){
		this.serverDAO = serverDAO;
		this.sendFinishQueue = sendFinishQueue;
	}

	
	class DealSendFinishQueueThread extends Thread{
		private Map<String, Integer> countMap = new HashMap<String, Integer>();
		
		public void dealSendFinishQueue(ArrayList<SmsMessage> messageList,ArrayList<Integer> snList){
			
			
			serverDAO.insertSubmitCatchByBatch(messageList);
			serverDAO.insertIntoTmpReport(messageList);
			serverDAO.execute("delete from  service_sms_info  where sn in (" + StringUtils.join(snList.toArray(),",") +")" , null);
			messageList.clear();
			snList.clear();
			
		}
		
		
		public void run(){
			
			SmsMessage tmpForm = null;
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>();
			ArrayList<Integer> snList = new ArrayList<Integer>();
			
			int i = 0;
			while(running){
				tmpForm = sendFinishQueue.poll();
				
				if(null != tmpForm){
					//-----------------添加统计数据维护
					String key = tmpForm.getTd_code() + "@" + tmpForm.getCustomer_id();
					countInTheMap(key);
					i++;
					
					if(i > 999){
						serverDAO.updateSendCount(countMap);
						countMap.clear();
						i = 0;
					}
					
					snList.add(Integer.parseInt(tmpForm.getSns()[0]));
					messageList.add(tmpForm);
				}else{
					
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					serverDAO.updateSendCount(countMap);
					countMap.clear();
					i = 0;
					if(snList.size() != 0 && messageList.size() != 0){
						dealSendFinishQueue(messageList,snList);
					}
				}
				
				if(messageList.size() > 1000 && snList.size() > 1000){
					dealSendFinishQueue(messageList,snList);
				}
			}
		}
		
		private void countInTheMap(String key) {
			if(!countMap.containsKey(key)){
				countMap.put(key, 0);
			}
			countMap.put(key, countMap.get(key) + 1);
		}
	}
	
	
	public void run(){
		if(log.isInfoEnabled()){
			log.info("DealPutSendFinishThread is start ");
		}
		
//		for(int i = 0;i<3;i++){
			new DealSendFinishQueueThread().start();
//		}
		
		if(log.isInfoEnabled()){
			log.info("DealPutSendFinishThread is start ");
		}
	}
	
	
	
	public void shutDown(){
		running = false;
		interrupt();
	}
}
