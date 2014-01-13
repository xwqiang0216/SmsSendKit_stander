package com.hskj.threads.send;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hskj.dao.ServerDAO;
import com.hskj.dao.ThreadStatusDAO;
import com.hskj.form.Message;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.log.CommonLogFactory;
import com.hskj.queue.SmsQueue;
import com.hskj.repeatFilter.EhcacheRepeatMsgFilter;
import com.hskj.sgip.api.CommonBean;
import com.hskj.sgip.api.SGIPBindMessage;
import com.hskj.sgip.api.SGIPBindRespMessage;
import com.hskj.sgip.api.SGIPMessage;
import com.hskj.sgip.api.SGIPSubmitLNLTMessage;
import com.hskj.sgip.api.SGIPSubmitMessage;
import com.hskj.sgip.api.SGIPSubmitRespMessage;
import com.hskj.sgip.api.SGIPUnbindMessage;
import com.hskj.sgip.api.SGIPUnbindRespMessage;
import com.hskj.sgip.api.SgipMessageUtil;
import com.hskj.threads.scan.SmsScanerNew3;

import utils.EncodingUtils;
import utils.StatusUtil;


/**
 * Descirption:sgip发送程序
 * 
 *@author licuan<lichuan3992413@gmail.com>
 *
 */
public class SgipLNLTSendThread extends Thread implements IControlService{
	
	
	private Log log = CommonLogFactory.getLog(this.getClass());

	private boolean running = false;
	
	private SmsQueue queue;
	private int msg_id = 0;
	private ServerDAO serverDAO;
	private ThreadStatusDAO threadStatusDAO;
	
	private long last_check_time ;
	private long last_check_time_flag;
	
	private SgipDeliverThread sgipDeliverThread;
	
	private ArrayBlockingQueue<SmsMessage> sendFinishQueue;
	private HashMap<Integer,SgipMessageUtil> waitSubmitRespMap = new HashMap<Integer,SgipMessageUtil>();
	private int thread_id;
	private String td_code;
	private String sgip_host;
	private int sgip_port;
	private String sgip_user;
	private String sgip_pwd;
	private String corp_id;
	private String server_id;
	private int qm;
	private int speed; 
	private int max_connect;
	private int has_connect = 0;//当前连接数
	
	private long node_id;
	private String sp_number;
	private int local_port;
	
	public void doInit(String param, int thread_id, SmsScanerNew3 smsScaner, ArrayBlockingQueue<SmsMessage> sendFinishQueue, ArrayBlockingQueue<SmsDeliverForm> delvierMsgQueue) {
		
		// param : td_code#sgip_host#sgip_port#sgip_user#sgip_pwd#server_id#corp_id#qm#speed#connect#node_id#sp_number#local_port
		
		String params[] = param.split("#");
		
		this.td_code = params[0];
		this.sgip_host = params[1];
		this.sgip_port = Integer.parseInt(params[2]);
		this.sgip_user = params[3];
		this.sgip_pwd = params[4];
		this.server_id = params[5];
		this.corp_id = params[6];
		this.qm = Integer.parseInt(params[7]);
		this.speed = Integer.parseInt(params[8]);
		this.max_connect = Integer.parseInt(params[9]);
		this.node_id = Long.parseLong(params[10]);
		this.sp_number = params[11];
		this.local_port = Integer.parseInt(params[12]);
		
		this.sendFinishQueue = sendFinishQueue;
		
		this.thread_id = thread_id;
		
		if(smsScaner.getQueueMap().get(td_code) == null){
			
			queue = new SmsQueue(4000);
			queue.setTd_code(td_code);
			queue.setServerDAO(serverDAO);
			smsScaner.getQueueMap().put(td_code, queue);
		}else{
			queue = smsScaner.getQueueMap().get(td_code);
			
		}
		

		/*
		 * 启动上行状态报告处理程序
		 */
		sgipDeliverThread = new SgipDeliverThread(server_id,serverDAO,td_code,node_id,local_port,delvierMsgQueue);
		sgipDeliverThread.start();
	}
	public void doStart() {
		running = true;
		start();
		serverDAO.execute("update service_sms_info set send_status = 0 , response_status = 0 where send_status = 100 and td_code = '"+td_code+"'", null);
	}

	public void run(){

		Selector selector = null;
		SelectionKey key = null;
		try{
			selector = Selector.open();
		}catch (Exception e) {
			e.printStackTrace();
		}
		SmsMessage tmpForm = null;

		System.out.println("yw_code  :"+ td_code +"    SendThread  is start ");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		//根据thread_id更新status为已经启动
		threadStatusDAO.changeThreadStatus(3, thread_id);// 更改状态为启动成功

		
		while(running){
			try{

				if( has_connect < max_connect && queue != null){
					has_connect++;
					SocketChannel socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
					socketChannel.connect(new InetSocketAddress(sgip_host,sgip_port));
					System.out.println("yw_code: "+td_code+" has_connect : "+ has_connect+"  max_allow_connect :"+ max_connect);
				}
				if(selector.select() > 0&&selector.isOpen()){
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

									SGIPMessage message = SGIPMessage.create(commonBean.getSgipHeader(), commonBean.getBodyBuffer().array());

									if(message.getCommandID() == SGIPMessage.ID_SGIP_BIND_RESP){

										SGIPBindRespMessage tmp = (SGIPBindRespMessage)message;
										if(tmp.getResult() == 0){
											
											commonBean.setNotReceiveSendBindRespMessage(false);
											commonBean.setSendBindMessageTimestamp(System.currentTimeMillis());
											
											commonBean.setConnected(true);
											System.out.println("yw_code  :"+ td_code +"  login success:"+tmp.getResult());
										}else{
											System.out.println("yw_code  :"+ td_code +"  login fail status :" +tmp.getResult());
											throw new Exception("login fail");
										}

									}

									if(message.getCommandID() == SGIPMessage.ID_SGIP_SUBMIT_RESP){

										SGIPSubmitRespMessage submitResp = (SGIPSubmitRespMessage)message;

										SgipMessageUtil u = waitSubmitRespMap.remove(submitResp.getCloneMsgHeader().getSeq_no3());
										
										if( u != null){
											SmsMessage tmpMessage = u.getMessage();
											
											if(submitResp.getResult() == 0 ){
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(1000);
												tmpMessage.setFail_describe("提交成功");
												tmpMessage.setTmp_msg_id(submitResp.getSeq_no1()+"_"+submitResp.getSeq_no2()+"_"+submitResp.getSeq_no3());
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getCode()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is 0");
												}
												
											}else{
												tmpMessage.setSend_status(1);
												tmpMessage.setResponse_status(2);
												tmpMessage.setFail_describe(submitResp.getResult()+"_提交失败");
												tmpMessage.setTmp_msg_id("");
												if(log.isInfoEnabled()){
													log.info(td_code+" send sms from "+tmpMessage.getCode()+" to "+ tmpMessage.getDest_terminal_id()[0]+" result is fail "+submitResp.getResult());
												}
											}
											System.out.println(sdf.format(System.currentTimeMillis())+" " +td_code+" receive and find submitResp for seq "+submitResp.getSeq_no1()+"_"+submitResp.getSeq_no2()+"_"+submitResp.getSeq_no3() + " status :"+submitResp.getResult());
											tmpMessage.setHas_report(true);
											sendFinishQueue.put(tmpMessage);
										}else{
											System.out.println(sdf.format(System.currentTimeMillis())+" " +td_code+" receive but NOT find submitResp for seq "+submitResp.getSeq_no1()+"_"+submitResp.getSeq_no2()+"_"+submitResp.getSeq_no3() + " status :"+submitResp.getResult());
										}


									}



									if(message.getCommandID() == SGIPMessage.ID_SGIP_UNBIND){
										
										SgipMessageUtil messageUtil = new SgipMessageUtil();
										
										SGIPUnbindRespMessage unbindResp = new SGIPUnbindRespMessage((SGIPUnbindMessage)message,node_id);
										
										messageUtil.setSgipMessage(unbindResp);
										//socketChannel.write(unbindResp.getMessage());
										
										throw new Exception(td_code+"  the  server send unbind message ");
									}

									commonBean.reset();
								}
								
								
							}

							if(key.isWritable()){
								
								if(commonBean == null){
									CommonBean comb = new CommonBean(speed);
									key.attach(comb);
									SGIPBindMessage bind = new SGIPBindMessage(1,sgip_user,sgip_pwd,node_id);
									socketChannel.write(bind.getMessage());
									
									comb.setNotReceiveSendBindRespMessage(true);
									comb.setSendBindMessageTimestamp(System.currentTimeMillis());
									
									comb.setSendCheckTimestamp(System.currentTimeMillis());
									
									System.out.println("yw_code  :" + td_code +"  send login message");
								}
								
								
								if(commonBean != null && commonBean.canSend(1000/(speed/max_connect)) && commonBean.isConnected() && waitSubmitRespMap.size() < 50 * has_connect){
									commonBean.send(1);
							
									tmpForm = (SmsMessage) queue.poll();
									
									
									if(tmpForm != null){
	
										String keyName = tmpForm.getMsg_id()+"_" +tmpForm.getMobile()+"_" + tmpForm.getMsg_content();
										Object obj = EhcacheRepeatMsgFilter.doFilter(EncodingUtils.MD5(keyName), 60*5, 2);
										if(obj == null){
											
											tmpForm.setSaveLastSendInfo(false);
											tmpForm.setHas_report(true);
											String mobile = "";
											String mobile_tmp = tmpForm.getMobile();//加86下发
											if(tmpForm.getMobile().startsWith("86")){
												mobile = tmpForm.getMobile().substring(2, tmpForm.getMobile().length());
											}else{
												mobile = tmpForm.getMobile();
											}	
											tmpForm.setMobile(mobile);
											
											
											
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
													
													SGIPSubmitLNLTMessage submit = new SGIPSubmitLNLTMessage(tmpForm.getCode(),mobile_tmp,longMsg.get(i-1),corp_id,server_id,node_id,longMsg.size(),i,msg_id);
													SmsMessage longSmsMessage = (SmsMessage)BeanUtils.cloneBean(tmpForm);
													socketChannel.write(submit.getMessage());
													SgipMessageUtil u = new SgipMessageUtil();
													u.setSgipMessage(submit);
													u.setMessage(longSmsMessage);
													u.setTry_times(u.getTry_times() + 1);
													u.setSubmit_time(System.currentTimeMillis());
													waitSubmitRespMap.put(submit.getCloneMsgHeader().getSeq_no3(), u);
													
													log.info("   td_code: "+tmpForm.getTd_code()+"  sn: "+tmpForm.getSns()[0]+"   mobile: "+tmpForm.getDest_terminal_id()[0]+ " wait  response  seq:" + submit.getCloneMsgHeader().getSeq_no3());
													
													Thread.sleep(1000/(speed/max_connect));
												}
												
												if(msg_id ==127){
													msg_id = 0;
												}else {
													msg_id ++;
												}
												
												
											}else{
												
												SGIPSubmitLNLTMessage submit = new SGIPSubmitLNLTMessage(tmpForm.getCode(), mobile_tmp, tmpForm.getMsg_content() , corp_id,server_id,node_id);
												socketChannel.write(submit.getMessage());
												SgipMessageUtil u = new SgipMessageUtil();
												u.setSgipMessage(submit);
												u.setMessage(tmpForm);
												u.setTry_times(u.getTry_times() + 1);
												u.setSubmit_time(System.currentTimeMillis());
												waitSubmitRespMap.put(submit.getCloneMsgHeader().getSeq_no3(), u);
											}
										}else{
											
											tmpForm.setSend_status(1);
											tmpForm.setResponse_status(999);
											sendFinishQueue.put(tmpForm);
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
							if(has_connect > 0){
								has_connect--;
								System.out.println("has_connect reduce ,test_connect not reduce!");
							}
							System.out.println("connect count has reduce ,count is:"+has_connect+" yw_code:"+td_code);
							try {
								key.channel().close();
								key.cancel();
							} catch (Exception e1) {
								System.out.println("############################################################# " +td_code);
								e1.printStackTrace();
							}
							sleep(3000);
							
						}finally{
							keys.remove();

						}
					}

				}else{
					try{
						sleep(1000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}


				if(System.currentTimeMillis() - last_check_time > 20000){
					last_check_time = System.currentTimeMillis();
					Iterator<SgipMessageUtil> its = waitSubmitRespMap.values().iterator();
					while(its.hasNext()){
						SgipMessageUtil u = its.next();
						if(System.currentTimeMillis() - u.getSubmit_time() > 60000){
							SmsMessage tmpMessage = u.getMessage();
							
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
			Iterator<SgipMessageUtil> its = waitSubmitRespMap.values().iterator();
			while(its.hasNext()){
				SgipMessageUtil u = its.next();
				tmpForm = u.getMessage();
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


	



	public void shutDown(){
		
		this.running = false;
		sgipDeliverThread.shutDown();
		Thread.currentThread().interrupt();

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
