package com.hskj.threads.send;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsReportForm;
import com.hskj.log.CommonLogFactory;
import com.hskj.sgip.api.SGIPBindMessage;
import com.hskj.sgip.api.SGIPBindRespMessage;
import com.hskj.sgip.api.SGIPDeliverMessage;
import com.hskj.sgip.api.SGIPDeliverRespMessage;
import com.hskj.sgip.api.SGIPMessage;
import com.hskj.sgip.api.SGIPReportMessage;
import com.hskj.sgip.api.SGIPReportRespMessage;
import com.hskj.sgip.api.SGIPUnbindMessage;
import com.hskj.sgip.api.SGIPUnbindRespMessage;




/**
 * Descirption:sgip上行状态报告接收程序
 * 
 *@author licuan<lichuan3992413@gmail.com>
 *
 */
public class SgipDeliverThread extends Thread {
	
	private Log log = CommonLogFactory.getLog(this.getClass());
	
	private boolean running = false;
	private ServerSocket serverSocket ; 
	private ServerDAO serverDAO;
	private String td_code;
	private long node_id;
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue;
	private String service_id;
	
	public SgipDeliverThread(String service_id,ServerDAO serverDAO,String td_code,long node_id,int local_port,ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue){
		
		this.serverDAO = serverDAO;
		this.td_code = td_code;
		this.node_id = node_id;
		this.deliverMsgQueue = deliverMsgQueue;
		this.service_id = service_id;
		
		try {
			serverSocket = new ServerSocket(local_port);
			running = true;
		} catch (IOException e) {
			System.out.println("============td_code:" + td_code +"  local_port:"+local_port);
			running = false;
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		log.info(td_code+" deliverThread  is start ");

		while(running){
			try{
				if(serverSocket != null){

					Socket st = serverSocket.accept();
					System.out.println("--- "+td_code+" a socket is connect  "  + st.getInetAddress());
					if(st != null){
						deliverThread receive = new deliverThread(st,this);
						receive.setName(td_code+"_receive_"+System.currentTimeMillis());
						receive.start();
					}


				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			try{
				sleep(200);
			}catch (Exception e) {
				
			}
		}
		log.info(td_code+" deliver server is terminal ");
	}


	private class deliverThread extends Thread{
		private boolean tmpRunning;
		private Socket socket;
		private InputStream in;
		private OutputStream out;
		private SgipDeliverThread sgipDeliverThread;

		public deliverThread(Socket socket , SgipDeliverThread sgipDeliverThread){
			this.socket = socket;
			try {
				this.socket.setSoTimeout(10000);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			tmpRunning = true;
			this.sgipDeliverThread = sgipDeliverThread;
			
		}

		public void run(){
			try {
				in = socket.getInputStream();
				out = socket.getOutputStream();
			} catch (IOException e) {
				tmpRunning = false;
				e.printStackTrace();
			}
			while(tmpRunning && sgipDeliverThread.isRunning()){
				try{
					SGIPMessage message = SGIPMessage.read(in);

					if(message.getCommandID() == SGIPMessage.ID_SGIP_BIND){
                        log.info("TD_CODE "+td_code+" requir bind!");

						SGIPBindMessage bindMessage = (SGIPBindMessage)message;

						new SGIPBindRespMessage(bindMessage,node_id).write(out);

					}else if(message.getCommandID() == SGIPMessage.ID_SGIP_DELIVER){
						SGIPDeliverMessage deliverMessage = (SGIPDeliverMessage)message;
						
						System.out.println("--- "+td_code+" deliver resp");
						new SGIPDeliverRespMessage(deliverMessage,node_id).write(out);
						String mobile = deliverMessage.getSrcTermid();
						if(mobile .startsWith("86")){
							mobile = mobile.substring(2);
						}
						SmsDeliverForm deliver = new SmsDeliverForm();
						deliver.setDest_mobile(deliverMessage.getDestTermid().trim());
						deliver.setSrc_terminal_id(mobile);
						if(service_id.trim().equals("api")){
							deliver.setTd_code("");
						}else{
							deliver.setTd_code(td_code);
						}
						deliver.setMsg_content(deliverMessage.getMsgContent());
						deliver.setSub_msg_id(deliverMessage.getLong_msg_id());
						deliver.setPk_number(deliverMessage.getLong_msg_sub_sn());
						deliver.setPk_total(deliverMessage.getLong_msg_count());
						deliver.setMsg_format(deliverMessage.getMsg_encode());
						log.info("deliverForm   pk_number: "+deliver.getPk_number()+"  pk_total: "+deliver.getPk_total()+" sub_msg_id: "+deliver.getSub_msg_id() +" service_id:"+service_id+"  td_code:"
								+ deliver.getTd_code()+"  from "+deliver.getDest_mobile() + " to "+deliver.getSrc_terminal_id()+ " msg_content: "+deliver.getMsg_content());
						
//						serverDAO.saveDeliver(deliver);
						deliverMsgQueue.put(deliver);

						
					}else if(message.getCommandID() == SGIPMessage.ID_SGIP_REPORT){
						SGIPReportMessage reportMessage = (SGIPReportMessage)message;
						if(log.isInfoEnabled()){
						log.info("--- "+td_code+" get report :" +reportMessage.getUserNumber()+" result: "+reportMessage.getReport_state()+
								" Msg_id: "+(reportMessage.getReport_no1()+"_"+reportMessage.getReport_no2()+"_"+reportMessage.getReport_no3())+" describe: " +reportMessage.getReport_Err());
						}
						System.out.println("--- "+td_code+" report resp");
						new SGIPReportRespMessage(reportMessage,node_id).write(out);
						
						String mobile = reportMessage.getUserNumber().trim();
						if(mobile .startsWith("86")){
							mobile = mobile.substring(2);
						}
						SmsReportForm report = new SmsReportForm();
						report.setMsg_id((reportMessage.getReport_no1()+"_"+reportMessage.getReport_no2()+"_"+reportMessage.getReport_no3()));
						report.setTd_code(td_code);
						report.setSend_result(reportMessage.getReport_state());
						report.setResult_describe(reportMessage.getReport_Err());
						report.setMobile(mobile);
						serverDAO.saveReceiveReport(report);
						
					}else if(message.getCommandID() == SGIPMessage.ID_SGIP_UNBIND){
						new SGIPUnbindRespMessage((SGIPUnbindMessage)message,node_id).write(out);
						
						out.flush();
						tmpRunning = false;

					}else{
						tmpRunning = false;

					}
				}catch (Exception e) {
					e.printStackTrace();
					tmpRunning = false;
				}
				
			}
			try{
				in.close();
			}catch (Exception e) {

			}
			try{
				out.close();
			}catch (Exception e) {

			}
			try{
				socket.close();
			}catch (Exception e) {

			}
		}

		
	}
	public void shutDown(){
		this.running = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		interrupt();

	}

	public boolean isRunning() {
		return running;
	}

}
