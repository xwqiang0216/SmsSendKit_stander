package com.hskj.threads.send;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;

import com.hskj.dao.ServerDAO;
import com.hskj.dao.ThreadStatusDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.form.SmsReportForm;
import com.hskj.log.CommonLogFactory;
import com.hskj.queue.SmsQueue;
import com.hskj.repeatFilter.EhcacheRepeatMsgFilter;
import com.hskj.threads.scan.SmsScanerNew3;


import td.api.smgp30.nio.Smgp30MessageUtil;
import td.api.smgp30.nioApi.CommonBean;
import td.api.smgp30.nioApi.SMGPActivceTestMessage;
import td.api.smgp30.nioApi.SMGPActivceTestRespMessage;
import td.api.smgp30.nioApi.SMGPDeliverMessage;
import td.api.smgp30.nioApi.SMGPDeliverRespMessage;
import td.api.smgp30.nioApi.SMGPLoginMessage;
import td.api.smgp30.nioApi.SMGPLoginRespMessage;
import td.api.smgp30.nioApi.SMGPMessage;
import td.api.smgp30.nioApi.SMGPSubmitMessage10659tzLongTlv;
import td.api.smgp30.nioApi.SMGPSubmitRespMessage;
import utils.EncodingUtils;
import utils.StatusUtil;




public class Smgp30nioSendThread extends Thread implements IControlService{

	private boolean running = false;
	private SmsQueue queue;
	
	private Log log = CommonLogFactory.getLog(this.getClass());

	
	private int msg_id = 0;
	private HashMap<Integer,Smgp30MessageUtil> waitSubmitRespMap = new HashMap<Integer,Smgp30MessageUtil>();
	
	private ArrayBlockingQueue<SmsMessage> sendFinishQueue;
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue;
	
	private ServerDAO serverDAO;
	private ThreadStatusDAO threadStatusDAO;
	
	private long last_check_time ;
	
	private int thread_id;
	
	private String td_code;
	private String smgp_gate_host;
	private int smgp_gate_port;
	private String smgp_user;
	private String smgp_pwd;
	private String smgp_corp_code;
	private String smgp_service_id;
	
	private int max_allow_connect = 1;//Nio最大连接数
	private int has_connect = 0;//当前连接数
	private int max_speed = 50;//默认50
	private int qm = 0;
	

	public void doInit(String param, int thread_id, SmsScanerNew3 smsScaner,ArrayBlockingQueue<SmsMessage> sendFinishQueue,ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue) {
		
		this.thread_id = thread_id;
		
		String [] params = param.split("#");
//		for(int i=0;i<params.length;i++){
//			System.out.println("--"+params[i]+"--");
//		}
		
		td_code = params[0];
		smgp_gate_host = params[1];
		smgp_gate_port = Integer.parseInt(params[2]);
		smgp_user = params[3];
		smgp_pwd = params[4];
		smgp_corp_code = params[5];
		smgp_service_id = params[6];
		
		qm = Integer.parseInt(params[7]);
		max_speed = Integer.parseInt(params[8]);
		max_allow_connect = Integer.parseInt(params[9]);
		
		this.sendFinishQueue = sendFinishQueue;
		this.deliverMsgQueue = deliverMsgQueue; 
		
		if(smsScaner.getQueueMap().get(td_code) == null){
			queue = new SmsQueue(4000);
			queue.setTd_code(td_code);
			queue.setServerDAO(serverDAO);
			smsScaner.getQueueMap().put(td_code, queue);
		}else{
			queue = smsScaner.getQueueMap().get(td_code);
			
		}
		
	}

	public void doStart() {
		running = true;
		serverDAO.execute("update service_sms_info set send_status = 0 , response_status = 0 where send_status = 100 and td_code = '"+td_code+"'", null);
		start();
		
	}
	
	@Override
	public void run() {

		threadStatusDAO.changeThreadStatus(3, thread_id);
		log.info("thread  "+thread_id+" change status :3 ");
		System.out.println( td_code+"  SendThread  is start ");
		
		Selector selector = null;
		try{
			selector = Selector.open();
		}catch (Exception e) {
			e.printStackTrace();
		}

		SmsMessage tmpForm = null;
		

		SelectionKey key = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while(running){
			try{

				if( has_connect < max_allow_connect ){
					has_connect++;
					SocketChannel socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
					socketChannel.connect(new InetSocketAddress(smgp_gate_host,smgp_gate_port));
				}

				if(selector.select() > 0){

					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

					while(keys.hasNext()){
						try{
							key = keys.next();
							SocketChannel socketChannel = (SocketChannel)key.channel();
							
							if(key.isConnectable()){
								if(socketChannel.finishConnect()){
									
									socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
									System.out.println("try connect to server");
									continue;
								}

							}
							CommonBean commonBean = (CommonBean)key.attachment();

							if(key.isReadable() && commonBean != null){

								commonBean.read(socketChannel);
								commonBean.setSendCheckTimestamp(System.currentTimeMillis());

								if(commonBean.isReady()){

									SMGPMessage message = SMGPMessage.create(commonBean.getSmgpHeader(), commonBean.getBodyBuffer().array());

									if(message.getRequestID() == SMGPMessage.ID_SMGP_LOGIN_RESP){

										SMGPLoginRespMessage tmp = (SMGPLoginRespMessage)message;
										if(tmp.getResult() == 0){
											commonBean.setConnected(true);
											
											commonBean.setNotReceiveSendBindRespMessage(false);
											commonBean.setSendBindMessageTimestamp(System.currentTimeMillis());
											
											System.out.println(td_code + " login success :" + tmp.getResult());
											log.info(td_code + "  login success :" +tmp.getResult());
										}else{
											System.out.println(td_code + "  login fail status :" +tmp.getResult());
											log.info(td_code + "  login fail status :" +tmp.getResult());
											throw new Exception("login fail");
										}


									}

									if(message.getRequestID() == SMGPMessage.ID_SMGP_SUBMIT_RESP){

										SMGPSubmitRespMessage submitResp = (SMGPSubmitRespMessage)message;

										log.info("  Get submit_resp the seq is:"+submitResp.getHeader().getSequence_id());
										Smgp30MessageUtil u = waitSubmitRespMap.remove(submitResp.getHeader().getSequence_id());
										if( u != null){
											SmsMessage tmpMessage = u.getSmsMessage();
											
//											log.info("-------------"+td_code+" send sms from "+tmpMessage.getFull_sp_number()+" to "+ tmpMessage.getDest_terminal_id()[0] +" wait response");
											
											if(submitResp.getStatus() == 0 ){
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(1000);
												tmpMessage.setFail_describe("提交成功");
												tmpMessage.setTmp_msg_id(submitResp.getMsg_id());
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getFull_sp_number()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is 0");
												}
												
											}else{
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(2);
												tmpMessage.setFail_describe(submitResp.getStatus()+"_提交失败");
												tmpMessage.setTmp_msg_id("");
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getFull_sp_number()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is fail "+submitResp.getStatus());
												}
											}
											System.out.println(sdf.format(System.currentTimeMillis())+" " +td_code+" receive and find submitResp for seq "+submitResp.getHeader().getSequence_id() + " status :"+submitResp.getStatus());
											tmpMessage.setHas_report(true);
											sendFinishQueue.put(tmpMessage);
										}else{
											System.out.println(sdf.format(System.currentTimeMillis())+" " +td_code+" receive but NOT find submitResp for seq "+submitResp.getHeader().getSequence_id() + " status :"+submitResp.getStatus());
										}

									}


									if(message.getRequestID() == SMGPMessage.ID_SMGP_DELIVER){

										SMGPDeliverMessage deliver = (SMGPDeliverMessage)message;
										log.info(td_code+"    smgp report: msg_id:"+deliver.getReport_msg_id()+"  desc:"+deliver.getReport_stat()+"  mobile:"+deliver.getScr_terminal_id()+"  err:"+deliver.getReport_err());
										SMGPDeliverRespMessage deliverResp = new SMGPDeliverRespMessage(deliver);
										socketChannel.write(deliverResp.getMessage());

										if(deliver.getIs_report() == 1){
//											int report_state = -1;
//											String fail_describe = "";
//											if(deliver.getReport_stat().equalsIgnoreCase("DELIVRD")){
//												report_state = 0;
//											}else{
//												fail_describe = deliver.getReport_stat()+"_"+deliver.getReport_err() +"_" +deliver.getDest_terminal_id();
//											}


											SmsReportForm report = new SmsReportForm();
											report.setMsg_id(deliver.getReport_msg_id());
											report.setTd_code(td_code);
											try {
												report.setSend_result(Integer.parseInt(deliver.getReport_err().trim()));
											} catch (Exception e) {
												report.setSend_result(0);
												e.printStackTrace();
												
											}
											report.setResult_describe(deliver.getReport_stat());
											report.setMobile(deliver.getScr_terminal_id());
											
											log.info( td_code +"  手机号: "+deliver.getScr_terminal_id()+" report "+ "  msg_id: "+deliver.getReport_msg_id()+"  status： " + deliver.getReport_err()+"  err: " + deliver.getReport_err());
											
											serverDAO.saveReceiveReport(report);

										}else{

											SmsDeliverForm deliverForm = new SmsDeliverForm();
											deliverForm.setDest_mobile(deliver.getDest_terminal_id());
											deliverForm.setSrc_terminal_id(deliver.getScr_terminal_id());
											deliverForm.setTd_code(td_code);
											deliverForm.setMsg_content(deliver.getMsg_content());
											
											deliverForm.setPk_number(deliver.getLong_msg_sub_sn());
											deliverForm.setPk_total(deliver.getLong_msg_count());
											deliverForm.setSub_msg_id(deliver.getLong_msg_id());
											deliverForm.setMsg_format(deliver.getMsgFormat());
//											System.out.println(td_code+" deliver from "+deliver.getScr_terminal_id() + " to "+deliver.getDest_terminal_id()+ " "+deliver.getMsg_content());
											log.info("deliverForm   pk_number: "+deliverForm.getPk_number()+"  pk_total: "+deliverForm.getPk_total()+" sub_msg_id: "+deliverForm.getSub_msg_id() 
													+ td_code+"  from "+deliverForm.getDest_mobile() + " to "+deliverForm.getSrc_terminal_id()+ " msg_content: "+deliverForm.getMsg_content());
											
											deliverMsgQueue.put(deliverForm);
//											serverDAO.saveDeliver(deliverForm);
										}

									}

									if(message.getRequestID() == SMGPMessage.ID_SMGP_ACTIVE_TEST){
										SMGPActivceTestMessage testMessage = (SMGPActivceTestMessage)message;

										SMGPActivceTestRespMessage testRespMessage = new SMGPActivceTestRespMessage(testMessage);
										socketChannel.write(testRespMessage.getMessage());
										System.out.println(td_code+" send testResp message");
									}

									commonBean.reset();
								}
								
								
							}

							if(key.isWritable()){
								
								if(commonBean == null){
									
									CommonBean comb = new CommonBean(max_speed);
									key.attach(comb);
									SMGPLoginMessage login = new SMGPLoginMessage(2, smgp_user, smgp_pwd , smgp_corp_code);
									socketChannel.write(login.getMessage());
									
									comb.setNotReceiveSendBindRespMessage(true);
									comb.setSendBindMessageTimestamp(System.currentTimeMillis());
									comb.setSendCheckTimestamp(System.currentTimeMillis());
									
									System.out.println(  td_code + " send login message");
								}
								
								
								if(commonBean != null && commonBean.canSend(1000/(max_speed/max_allow_connect)) && commonBean.isConnected() && waitSubmitRespMap.size() < 50 * has_connect){
									commonBean.send(1);
									tmpForm = queue.poll();
									
									if(tmpForm != null){
										
										
										String keyName = tmpForm.getMsg_id()+"_" +tmpForm.getMobile()+"_" + tmpForm.getMsg_content();
										Object obj = EhcacheRepeatMsgFilter.doFilter(EncodingUtils.MD5(keyName), 60*5, 2);
										if(obj == null){
											
											log.info("td_code:"+td_code+"  queue   poll   sn: "+tmpForm.getSns()[0]+"   mobile: "+tmpForm.getDest_terminal_id()[0]+" queue poll"  +"  waitSubmitRespMap:" + waitSubmitRespMap.size());
											
											tmpForm.setSrc_termainal_id(tmpForm.getFull_sp_number());
											
											if(tmpForm.getMsg_content().length() > (70-qm)){
												
												String content = tmpForm.getMsg_content();
												boolean hasLeft = true;
												ArrayList<String> longMsg = new ArrayList<String>();
												
												while(hasLeft){
													boolean condition1 = content.length() > 67 - qm;    
													boolean condition2 = content.length() > 67;
													if(condition2){
														longMsg.add(content.substring(0, 67));
														content = content.substring(67);
													}else if (!condition2 && condition1){
														longMsg.add(content.substring(0, 67 - qm));
														content = content.substring(67 - qm);
													}else{
														longMsg.add(content);
														hasLeft = false;
													}
												}
												
												
												for(int i = 1;i<=longMsg.size();i++){
													SMGPSubmitMessage10659tzLongTlv submit = new SMGPSubmitMessage10659tzLongTlv(tmpForm.getFull_sp_number(),tmpForm.getDest_terminal_id()[0],longMsg.get(i-1),smgp_service_id,smgp_corp_code,longMsg.size(),i,msg_id);
													SmsMessage longSmsMessage = (SmsMessage)BeanUtils.cloneBean(tmpForm);
													
													socketChannel.write(submit.getMessage());
													
													Smgp30MessageUtil u = new Smgp30MessageUtil();
													u.setSmgpMessage(submit);
													u.setSmsMessage(longSmsMessage);
													u.setTryTimes(u.getTryTimes() + 1);
													u.setSubmit_time(System.currentTimeMillis());
													waitSubmitRespMap.put(submit.getCloneMsgHeader().getSequence_id(), u);
													
													log.info("   td_code: "+tmpForm.getTd_code()+"  sn: "+tmpForm.getSns()[0]+"   mobile: "+tmpForm.getDest_terminal_id()[0]+ " wait  response  seq:" + submit.getCloneMsgHeader().getSequence_id());
													
													Thread.sleep(1000/(max_speed/max_allow_connect));
													
												}
												
												
												if(msg_id ==127){
													msg_id = 0;
												}else {
													msg_id ++;
												}
												
											}else{
												SMGPSubmitMessage10659tzLongTlv submit = new SMGPSubmitMessage10659tzLongTlv(tmpForm.getFull_sp_number(),tmpForm.getDest_terminal_id()[0] ,tmpForm.getMsg_content(),smgp_service_id,smgp_corp_code);
												SmsMessage longSmsMessage = (SmsMessage)BeanUtils.cloneBean(tmpForm);
												socketChannel.write(submit.getMessage());
												Smgp30MessageUtil u = new Smgp30MessageUtil();
												u.setSmsMessage(longSmsMessage);
												u.setSmgpMessage(submit);
												u.setTryTimes(u.getTryTimes() + 1);
												u.setSubmit_time(System.currentTimeMillis());
												waitSubmitRespMap.put(submit.getHeader().getSequence_id(), u);
												
												log.info(" --waitSubmitRespMap customer_id: "+tmpForm.getCustomer_id()+" dest_terminal_id: "+tmpForm.getDest_terminal_id()[0]);
											}
											
										}else{
											
											log.error("the same message :" + keyName);
											tmpForm.setSend_status(1);
											tmpForm.setResponse_status(999);
											sendFinishQueue.put(tmpForm);
											
										}
										
									}
								}
								
								
							}

							
							if(commonBean != null && System.currentTimeMillis() - commonBean.getSendCheckTimestamp() > 60000){
								log.info(td_code+"  not  message over 60s ");
								throw new Exception(td_code+" not  message over 60s");
							}
							
							
							if(commonBean != null&&commonBean.isNotReceiveSendBindRespMessage()== true && System.currentTimeMillis() - commonBean.getSendBindMessageTimestamp() > 60000){
								log.info(td_code+" send bind message over 60s not receive resp");
								throw new Exception(td_code+" send bind message over 60s not receive resp");
							}
							
							
						}catch (Exception e) {
							e.printStackTrace();
							if(has_connect > 0)
								has_connect--;
							System.out.println( td_code + "  connect count has reduce ,count is: "+has_connect);
							key.channel().close();
							key.cancel();
							sleep(3000);
						}finally{
							keys.remove();

						}
					}

				}else{
					try{
						sleep(200);
					}catch (Exception e) {

					}
				}


				if(System.currentTimeMillis() - last_check_time > 20000){
					last_check_time = System.currentTimeMillis();
					Iterator<Smgp30MessageUtil> its = waitSubmitRespMap.values().iterator();
					
					while(its.hasNext()){
						Smgp30MessageUtil u = its.next();
						if(System.currentTimeMillis() - u.getSubmit_time() > 60000){
							SmsMessage tmpMessage = u.getSmsMessage();
							
							tmpMessage.setSend_status(1);
							tmpMessage.setResponse_status(1000);

							tmpMessage.setFail_describe("submit time > 60000ms");
							tmpMessage.setHas_report(true);
							log.info(" td_code: "+td_code+"  dest_terminal: "+tmpMessage.getDest_terminal_id()[0]+"  --submit time > 60000ms");
							sendFinishQueue.put(tmpMessage);
							
							its.remove();
						}

					}
				}
				
				sleep(1);
			}catch (Exception e) {
				
				e.printStackTrace();

			}
		}
		
		try {
			Iterator<Smgp30MessageUtil> its = waitSubmitRespMap.values().iterator();
			while(its.hasNext()){
				Smgp30MessageUtil u = its.next();
				tmpForm = u.getSmsMessage();
				tmpForm.setSend_status(1);
				tmpForm.setResponse_status(1000);
				sendFinishQueue.add(tmpForm);
				
				its.remove();
			}
			
			
//			tmpForm = queue.poll();
//			while(tmpForm != null){
//				
//				serverDAO.updateStatus(0, StringUtils.join(tmpForm.getSns(),","));//跟该发送状态为0
//				tmpForm = queue.poll();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		threadStatusDAO.changeThreadStatus(6, thread_id);
		System.out.println(td_code+"--SendThread is terminal!");
		
		
	}


	public int getMax_allow_connect() {
		return max_allow_connect;
	}


	public void setMax_allow_connect(int maxAllowConnect) {
		max_allow_connect = maxAllowConnect;
	}

	public void shutDown(){
		this.running = false;
		interrupt();
	}

	public void doShutDown() {
		shutDown();
	}

	public ServerDAO getServerDAO() {
		return serverDAO;
	}

	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}

	public ThreadStatusDAO getThreadStatusDAO() {
		return threadStatusDAO;
	}

	public void setThreadStatusDAO(ThreadStatusDAO threadStatusDAO) {
		this.threadStatusDAO = threadStatusDAO;
	}
	
	

}
