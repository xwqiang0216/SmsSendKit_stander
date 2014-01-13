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

import com.hskj.dao.IThreadStatusDAO;
import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.form.SmsReportForm;
import com.hskj.log.CommonLogFactory;
import com.hskj.queue.SmsQueue;
import com.hskj.repeatFilter.EhcacheRepeatMsgFilter;
import com.hskj.threads.scan.SmsScanerNew3;


import td.api.cmpp30.Cmpp30MessageUtil;
import td.api.cmpp30.CommonBean;
import td.api.cmpp30.spApi.CMPPActiveMessage;
import td.api.cmpp30.spApi.CMPPActiveRespMessage;
import td.api.cmpp30.spApi.CMPPConnectMessage;
import td.api.cmpp30.spApi.CMPPConnectRespMessage3;
import td.api.cmpp30.spApi.CMPPDeliverMessage3;
import td.api.cmpp30.spApi.CMPPDeliverRespMessage3;
import td.api.cmpp30.spApi.CMPPMessage;
import td.api.cmpp30.spApi.CMPPSubmitMessage3;
import td.api.cmpp30.spApi.CMPPSubmitRespMessage3;
import utils.EncodingUtils;
import utils.StatusUtil;



public class CmppSendByNioThreadSingle30  extends Thread implements IControlService{
	
	private boolean running = false;
	private SmsQueue queue;
	private ArrayBlockingQueue<SmsMessage> putSendFinishQueue;
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue;
	
	private Log log = CommonLogFactory.getLog(this.getClass());
	
	private IThreadStatusDAO threadStatusDAO;
	private ServerDAO serverDAO;

	private String td_code;
	private String cmpp_host;
	private int cmpp_port;
	private String cmpp_service_id;
	private String cmpp_ent_code;
	private String cmpp_pwd;
	
	private int qm = 0;
	private int max_speed = 50;//默认50
	private int max_allow_connect = 1;//Nio最大连接数
	private int has_connect = 0;//当前连接数
	
	private int thread_id;
	private int msg_id = 0;
	private HashMap<Integer,Cmpp30MessageUtil> waitSubmitRespMap = new HashMap<Integer,Cmpp30MessageUtil>();
	
	private long last_check_time ;
	
	
	public void doInit(String param, int thread_id, SmsScanerNew3 smsScaner, ArrayBlockingQueue<SmsMessage> putSendFinishQueue, ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue) {
		String [] params = param.split("#");
		this.td_code = params[0];
		this.cmpp_host = params[1];
		this.cmpp_port = Integer.parseInt(params[2]);
		this.cmpp_service_id = params[3];
		this.cmpp_ent_code = params[4];
		this.cmpp_pwd = params[5];
		
		this.qm = Integer.parseInt(params[6]);
		this.max_speed = Integer.parseInt(params[7]);
		this.max_allow_connect = Integer.parseInt(params[8]);
	
		this.thread_id = thread_id;
		
		this.putSendFinishQueue = putSendFinishQueue;
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
		start();
	}

	public void run(){
		threadStatusDAO.changeThreadStatus(3, thread_id);
		Selector selector = null;
		try{
			selector = Selector.open();
		}catch (Exception e) {
			e.printStackTrace();
		}

		SmsMessage tmpForm = null;
		
		System.out.println(td_code+"--SendThread  is start ");

		SelectionKey key = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while(running){
			try{

				if( has_connect < max_allow_connect ){
					has_connect++;
					SocketChannel socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
					socketChannel.connect(new InetSocketAddress(cmpp_host,cmpp_port));
				}

				if(selector.select(1) > 0){

					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

					while(keys.hasNext()){
						try{
							key = keys.next();

							SocketChannel socketChannel = (SocketChannel)key.channel();
							

							if(key.isConnectable()){
								if(socketChannel.finishConnect()){
									
									socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
									System.out.println(td_code+" try connect to server");
									continue;
								}


							}
							CommonBean commonBean = (CommonBean)key.attachment();




							if(key.isReadable() && commonBean != null){
								
								commonBean.setSendCheckTimestamp(System.currentTimeMillis());
								commonBean.read(socketChannel);

								if(commonBean.isReady()){


									CMPPMessage message = CMPPMessage.create(commonBean.getCmppHeader(), commonBean.getBodyBuffer().array());

									if(message.getCommand_id() == CMPPMessage.ID_CMPP_CONNECT_RESP){

										CMPPConnectRespMessage3 tmp = (CMPPConnectRespMessage3)message;
										if(tmp.getConnect_status() == 0){
											
											commonBean.setNotReceiveSendBindRespMessage(false);
											commonBean.setSendBindMessageTimestamp(System.currentTimeMillis());
											commonBean.setConnected(true);
											
											System.out.println(td_code + "   login success  " + tmp.getConnect_status());
										}else{
											System.out.println(td_code +"  login fail status :" +tmp.getConnect_status());
											throw new Exception("login fail");
										}


									}

									if(message.getCommand_id() == CMPPMessage.ID_CMPP_SUBMIT_RESP){



										CMPPSubmitRespMessage3 submitResp = (CMPPSubmitRespMessage3)message;

										Cmpp30MessageUtil u = waitSubmitRespMap.remove(submitResp.getCloneMsgHeader().getSequence_id());
										if( u != null){
											SmsMessage tmpMessage = u.getSmsMessage();
											if(submitResp.getResult() == 0 ){
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(1000);
												tmpMessage.setFail_describe("");
												tmpMessage.setTmp_msg_id(String.valueOf(submitResp.getSeq_no1()));

												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getCode()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is 0");

												}
											}else{
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(submitResp.getResult());
												tmpMessage.setFail_describe("2");
												tmpMessage.setTmp_msg_id("");
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getCode()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is fail "+submitResp.getResult());

												}
											}
											System.out.println(sdf.format(System.currentTimeMillis())+" " +td_code+"    receive and find submitResp for seq "+submitResp.getCloneMsgHeader().getSequence_id() + " status :"+submitResp.getResult());
											 //log.info("00000000000000"+td_code+" send resp: "+ submitResp.getSeq_no1()+"_"+submitResp.getSequence_id() +"  status:"+submitResp.getResult());
											tmpMessage.setHas_report(true);
											
											putSendFinishQueue.add(tmpMessage);
										}else{
											log.info(sdf.format(System.currentTimeMillis())+" " +td_code+"receive but not find submitResp for seq "+submitResp.getCloneMsgHeader().getSequence_id() + " status :"+submitResp.getResult());
										}


									}


									if(message.getCommand_id() == CMPPMessage.ID_CMPP_DELIVER){

										CMPPDeliverMessage3 deliver = (CMPPDeliverMessage3)message;
										log.info(td_code+"                cmpp report: msg_id:"+deliver.getReport_no1()+"   desc:"+deliver.getReport_state()+" mobile:"+deliver.getSrcTermid()+"  err:"+deliver.getReport_desc());
										CMPPDeliverRespMessage3 deliverResp = new CMPPDeliverRespMessage3(deliver);
										socketChannel.write(deliverResp.getMessage());
										
										if(deliver.isReport() == 1){
//											int report_state = -1;
//											String fail_describe = "";
//											if(deliver.getReport_state() == 0){
//												report_state = 0;
//											}else{
//												fail_describe = deliver.getReport_state()+"_"+deliver.getReport_desc();
//											}

											String mobile = deliver.getSrcTermid();
											if(mobile.startsWith("86")){
												mobile = mobile.substring(2);
											}
											SmsReportForm report = new SmsReportForm();
											report.setMsg_id(String.valueOf(deliver.getReport_no1()));
											report.setTd_code(td_code);
											report.setSend_result(deliver.getReport_state());
											report.setResult_describe(deliver.getReport_desc());
											report.setMobile(mobile);
											
											log.info(td_code+" report from "+deliver.getSrcTermid() + " to "+deliver.getDestTermid()+ " "+deliver.getReport_desc());
											System.out.println(td_code+" report from "+deliver.getSrcTermid() + " to "+deliver.getDestTermid()+ " "+deliver.getReport_desc());
											serverDAO.saveReceiveReport(report);

										}else{

											String mobile = deliver.getSrcTermid();
											if(mobile.startsWith("86")){
												mobile = mobile.substring(2);
											}
											SmsDeliverForm deliverForm = new SmsDeliverForm();
											deliverForm.setDest_mobile(deliver.getDestTermid());
											deliverForm.setSrc_terminal_id(mobile);
											deliverForm.setTd_code(td_code);
											deliverForm.setMsg_content(deliver.getMsgContent());
											
											deliverForm.setPk_number(deliver.getPk_number());
											deliverForm.setPk_total(deliver.getPk_total());
											deliverForm.setSub_msg_id(deliver.getSub_msg_id());
											
											deliverMsgQueue.put(deliverForm);

											System.out.println(td_code+" deliver from "+deliver.getSrcTermid() + " to "+deliver.getDestTermid()+ " "+deliver.getMsgContent());
											log.info(td_code+" deliver from "+deliver.getSrcTermid() + " to "+deliver.getDestTermid()+ " "+deliver.getMsgContent());
										}

									}


									if(message.getCommand_id() == CMPPMessage.ID_CMPP_ACTIVE){
										CMPPActiveMessage testMessage = (CMPPActiveMessage)message;

										CMPPActiveRespMessage testRespMessage = new CMPPActiveRespMessage(testMessage);
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
									CMPPConnectMessage login = new CMPPConnectMessage(cmpp_ent_code,cmpp_pwd,0);
									socketChannel.write(login.getMessage());
									
									comb.setNotReceiveSendBindRespMessage(true);
									comb.setSendBindMessageTimestamp(System.currentTimeMillis());
									comb.setSendCheckTimestamp(System.currentTimeMillis());
									
									System.out.println( td_code   + "  =====send login message");
								}
								
								
								if(commonBean != null && commonBean.canSend(1000/(max_speed/max_allow_connect)) && commonBean.isConnected() && waitSubmitRespMap.size() < 50 * has_connect){
									tmpForm = queue.poll();
									if(tmpForm != null){
										
										String keyName = tmpForm.getMsg_id()+"_" +tmpForm.getMobile()+"_" + tmpForm.getMsg_content();
										Object obj = EhcacheRepeatMsgFilter.doFilter(EncodingUtils.MD5(keyName), 60*5, 2);
										
										if(obj == null){
											
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
													
													CMPPSubmitMessage3 submit = new CMPPSubmitMessage3(tmpForm.getCode(),tmpForm.getDest_terminal_id()[0],longMsg.get(i-1),cmpp_service_id,longMsg.size(),i,msg_id);
													SmsMessage longSmsMessage = (SmsMessage)BeanUtils.cloneBean(tmpForm);
													
													socketChannel.write(submit.getMessage());
													
													Cmpp30MessageUtil u = new Cmpp30MessageUtil();
													u.setCmppMessage(submit);
													u.setSmsMessage(longSmsMessage);
													u.setTry_times(u.getTry_times() + 1);
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
												
												CMPPSubmitMessage3 submit = new CMPPSubmitMessage3(tmpForm.getCode(),tmpForm.getDest_terminal_id()[0],tmpForm.getMsg_content(),cmpp_service_id);
												
												socketChannel.write(submit.getMessage());
												Cmpp30MessageUtil u = new Cmpp30MessageUtil();
												u.setCmppMessage(submit);
												u.setSmsMessage(tmpForm);
												u.setTry_times(u.getTry_times() + 1);
												u.setSubmit_time(System.currentTimeMillis());
												waitSubmitRespMap.put(submit.getCloneMsgHeader().getSequence_id(), u);
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpForm.getCode()+" to "+ tmpForm.getDest_terminal_id()[0]+" result is  已经提交！");
												}
											}
										}else{
												tmpForm.setSend_status(1);
												tmpForm.setResponse_status(999);
												putSendFinishQueue.put(tmpForm);
												log.error("the same message :" + keyName);
											
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
							System.out.println(td_code + "  connect count has reduce ,count is:"+has_connect);
							key.channel().close();
							key.cancel();

							sleep(3000);
						}finally{
							keys.remove();

						}
					}

				}else{
					try{
						Thread.sleep(200);
					}catch (Exception e) {

					}
				}


				if(System.currentTimeMillis() - last_check_time > 20000){
					last_check_time = System.currentTimeMillis();
					Iterator<Cmpp30MessageUtil> its = waitSubmitRespMap.values().iterator();
					while(its.hasNext()){
						Cmpp30MessageUtil u = its.next();
						if(System.currentTimeMillis() - u.getSubmit_time() > 60000){
							SmsMessage tmpMessage = u.getSmsMessage();
							
							
							if(tmpMessage.getTry_times() >=1){
							
							tmpMessage.setSend_status(1);
							tmpMessage.setResponse_status(2);
							
							tmpMessage.setFail_describe("try_times > 1");
							if(log.isInfoEnabled()){
								log.info(" td_code: "+td_code+"  dest_terminal: "+tmpMessage.getDest_terminal_id()[0]+"  --try_times >1 ");

							}
							tmpMessage.setHas_report(true);
							putSendFinishQueue.add(tmpMessage);
							}else{
								tmpMessage.setTry_times(tmpMessage.getTry_times()+1);
								queue.putRetryMessage(tmpMessage);
							}
							its.remove();
						}
					}
				}
				Thread.sleep(1);
			}catch (Exception e) {
				
				e.printStackTrace();

			}
		}
		
		try {
			Iterator<Cmpp30MessageUtil> its = waitSubmitRespMap.values().iterator();
			while(its.hasNext()){
				Cmpp30MessageUtil u = its.next();
				tmpForm = u.getSmsMessage();
				tmpForm.setSend_status(1);
				tmpForm.setResponse_status(1000);
				putSendFinishQueue.add(tmpForm);
				
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

	
	public IThreadStatusDAO getThreadStatusDAO() {
		return threadStatusDAO;
	}

	public void setThreadStatusDAO(IThreadStatusDAO threadStatusDAO) {
		this.threadStatusDAO = threadStatusDAO;
	}

	public ServerDAO getServerDAO() {
		return serverDAO;
	}

	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}

	public void doShutDown() {
		shutDown();
		
	}

	public void shutDown(){
		this.running = false;
		Thread.currentThread().interrupt();

	}

}

