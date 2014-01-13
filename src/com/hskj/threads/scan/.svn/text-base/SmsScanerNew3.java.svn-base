package com.hskj.threads.scan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.hskj.dao.ServerDAO;
import com.hskj.form.Message;
import com.hskj.form.SmsMessage;
import com.hskj.log.CommonLogFactory;
import com.hskj.queue.SmsQueue;



/**
 * @author 1007025
 *
 */
public class SmsScanerNew3 extends Thread{

	private Log log = CommonLogFactory.getLog(this.getClass());

	private boolean running ;
	private ServerDAO serverDAO;
	private HashMap<String, SmsQueue> queueMap = new HashMap<String,SmsQueue>();
	private HashMap<String,Integer> tdCustomerSnMap = new HashMap<String, Integer>(); 
	public SmsScanerNew3(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
		running = true;
	}


	public void run(){

		log.info("SmsScanerNew2Thread is start");
		while(running){
			
			try {
				Set<String> keys = queueMap.keySet();
				String[] tdArray = new String[keys.size()];
				keys.toArray(tdArray);
				
				if(tdArray.length >0){
					for(String td_code :tdArray){
						if(tdCustomerSnMap.get(td_code) == null){
							tdCustomerSnMap.put(td_code, 0);
						}
						List<Message> messages = serverDAO.fetchSms(td_code,tdCustomerSnMap.get(td_code),1000);
						
						if(messages.size() > 0){
							tdCustomerSnMap.put(td_code, messages.get(messages.size() - 1).getCustomer_sn());
							
							List<SmsMessage> list = process(messages);//加工数据，可添加群发加工
							
							boolean isSuccess = queueMap.get(td_code).put(list);
							if(!isSuccess){
								System.out.println("===========queue full,  td_code: "+td_code+"=============queueSize:" + queueMap.get(td_code).getList().size());
							}
						}
						
						if(messages.size() < 1000){
							tdCustomerSnMap.put(td_code, 0);
						}
					}
				}
				
				
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		log.info("SmsScanServerThread is terminal");
	}




	/**
	 * 对原始的数据进行加工
	 * @param messages
	 * @return
	 */

	private List<SmsMessage> process(List<Message> messages){

		List<SmsMessage> list = new ArrayList<SmsMessage>();

		if(messages==null)
			return null;

		for(Message message:messages){

			SmsMessage tmpSmsMessage = new SmsMessage();
			tmpSmsMessage.setMobile(message.getDest_terminal_id());
			String [] mobiles = new String[1];
			mobiles[0] = message.getDest_terminal_id();
			tmpSmsMessage.setDest_terminal_id(mobiles);
			String [] sns = new String[1];
			sns[0] = String.valueOf(message.getSn());
			String [] old_sns = new String[1];
			tmpSmsMessage.setSns(sns);
			old_sns[0] = String.valueOf(message.getOld_sn());
			tmpSmsMessage.setOld_sns(old_sns);
			tmpSmsMessage.setTmp_msg_id(new Random().nextInt(65536)+"");
			tmpSmsMessage.setCode(message.getCode());
			tmpSmsMessage.setSrc_termainal_id("");
			tmpSmsMessage.setTd_code(message.getTd_code());
			tmpSmsMessage.setMsg_content(message.getMsg_content());
			tmpSmsMessage.setPlate_msg_id(message.getPlate_msg_id());
			tmpSmsMessage.setCustomer_sn(message.getCustomer_sn());
			tmpSmsMessage.setCustomer_id(message.getCustomer_id());
			tmpSmsMessage.setMsg_id(message.getMsg_id());
			tmpSmsMessage.setCell_code(message.getCell_code());
			tmpSmsMessage.setCharge_count(message.getCharge_count());
			tmpSmsMessage.setInsert_time(message.getInsert_time());
			tmpSmsMessage.setPriority(message.getPriority());
			tmpSmsMessage.setPrice(message.getPrice());

			list.add(tmpSmsMessage);
		}

		if(list.size()<1)
			return null;

		return list;
	}


	public ServerDAO getServerDAO() {
		return serverDAO;
	}

	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}

	public void shutDown(){
		running = false;
		Thread.currentThread().interrupt();
	}



	public HashMap<String, SmsQueue> getQueueMap() {
		return queueMap;
	}


	public void setQueueMap(HashMap<String, SmsQueue> queueMap) {
		this.queueMap = queueMap;
	}
}
