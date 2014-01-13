package com.hskj.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsMessage;
import com.hskj.log.CommonLogFactory;




public class SmsQueue{
	
	private int max_size = 0;
	private String td_code;//通道代码
	private List<SmsMessage> list = null;
	private Log log = CommonLogFactory.getLog(this.getClass());
	private ServerDAO serverDAO;
	
	
	private ArrayBlockingQueue<SmsMessage> lastSendInfoQueue;//最近发送队列
	
	private ArrayBlockingQueue<SmsMessage> sendFinishReportInfoQueue;//状态报告队列
	
	
	
	public ArrayBlockingQueue<SmsMessage> getSendFinishReportInfoQueue() {
		return sendFinishReportInfoQueue;
	}

	public void setSendFinishReportInfoQueue(
			ArrayBlockingQueue<SmsMessage> sendFinishReportInfoQueue) {
		this.sendFinishReportInfoQueue = sendFinishReportInfoQueue;
	}

	public ArrayBlockingQueue<SmsMessage> getLastSendInfoQueue() {
		return lastSendInfoQueue;
	}

	public void setLastSendInfoQueue(
			ArrayBlockingQueue<SmsMessage> lastSendInfoQueue) {
		this.lastSendInfoQueue = lastSendInfoQueue;
	}
	
	public SmsQueue(int size){
		if(size != 0 ){
			list = new ArrayList<SmsMessage>(size);
			max_size = size;
		}else{
			list = new ArrayList<SmsMessage>();
			max_size = 10000;
		}

	}
	
	synchronized public SmsMessage poll(){
		try{
			if(list.size() > 0){
				//contain_size -- ;
				return list.remove(0);
			}else{
				return null;
			}
		}finally{
			notifyAll();
		}


	}
	
	public ServerDAO getServerDAO() {
		return serverDAO;
	}

	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}
	
	public List<SmsMessage> getList() {
		return list;
	}

	public void setList(List<SmsMessage> list) {
		this.list = list;
	}
	/**
	 * 添加到队列
	 * @param tmpMessage
	 * @return
	 */
	synchronized public boolean put(List<SmsMessage> smsList ){

		try{
			
			ArrayList<String> serviceSnList = new ArrayList<String>();
			if(list.size() +  smsList.size() < max_size * 0.5 ){
				
				
				for(SmsMessage tmpMessage: smsList){
					serviceSnList.add(tmpMessage.getSns()[0]);
				}
				
				int i = serverDAO.updateStatus(100, StringUtils.join(serviceSnList.toArray(), ","));//修改为批量update
				
				if(i == smsList.size()){
					for(SmsMessage tmpMessage: smsList){
						list.add(tmpMessage);
						log.info("add queue success   sn: "+tmpMessage.getSns()[0]+"    td_code: "+tmpMessage.getTd_code()+"    mobile: "+tmpMessage.getDest_terminal_id()[0]);
					}
					return true;
				}else{
					log.error("scanThread : update DB  is wrong,the count is not equals the smsList.size");
					System.out.println("scanThread : update DB  is wrong,the count is not equals the smsList.size");
				}
				

			}else{
				return false;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			notifyAll();
		}
		
		return false;
	} 
	
	
	
	
	synchronized public boolean put(SmsMessage tmpMessage ){

		try{
			if(list.size() < max_size ){

				list.add(tmpMessage);

				serverDAO.updateStatus(100, StringUtils.join(tmpMessage.getSns(),","));//将添加到队列中的对应数据状态改为100

				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			notifyAll();
		}
	}	
	

	
	
	
	/**
	 * 重新放入列表
	 * @param tmpMessage
	 */
	synchronized public void putRetryMessage(SmsMessage tmpMessage ){
		
		list.add(tmpMessage);

	} 
	

	public String getTd_code() {
		return td_code;
	}
	

	public void setTd_code(String tdCode) {
		td_code = tdCode;
	}
}