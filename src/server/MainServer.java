package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hskj.dao.ServerDAO;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.queue.SmsQueue;
import com.hskj.repeatFilter.EhcacheRepeatMsgFilter;
import com.hskj.threads.scan.SmsScanerNew3;
import com.hskj.threads.send.ControlThread;
import com.hskj.threads.send.DealDeliverMessageThread;
import com.hskj.threads.send.DealPutSendFinishThread;
import com.hskj.threads.send.KeepSingleThread;

public class MainServer{
	
	private static final int PORT = 12083;
	
	private ServerSocket socket;
	
	private ServerDAO serverDAO;
	
	private AbstractApplicationContext applicationContext;
	
	private SmsScanerNew3 smsScaner;//ɨ���߳�
	
	private ArrayBlockingQueue<SmsMessage> lastSendInfoQueue; //������Ͷ���
	
	private ArrayBlockingQueue<SmsMessage> putSendFinishQueue; //������ɶ���
	
	private ArrayBlockingQueue<SmsDeliverForm> deliverMsgQueue; //������Ϣ���ն���
	
	
	
	
	/**
	 * ���캯��
	 */
	public MainServer(){
		//Ϊ���ͳ����һ����Ŀ��socket������ȷ����һ���ͳ����������״̬
		try {
			socket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�Ѿ��з��ͳ��������У���ȷ�ϲ���");

			System.exit(0);
		}
		KeepSingleThread keepSingleThread = new KeepSingleThread(socket);
		keepSingleThread.start();
		
		applicationContext = new  ClassPathXmlApplicationContext(new String[]{"classpath:spring-config.xml"});
		this.serverDAO = (ServerDAO)applicationContext.getBean("serverDAO");
		
	}
	
	public static void main(String[] args) {
		
		MainServer mainServer = new MainServer();
		mainServer.start();
	}
	
	public void start(){
		
		
		EhcacheRepeatMsgFilter.doStart();
		
		serverDAO.execute("update thread_controller set status = 1  where thread_type = 1 ", null);
		
		lastSendInfoQueue = new ArrayBlockingQueue<SmsMessage>(10000);
		putSendFinishQueue = new ArrayBlockingQueue<SmsMessage>(10000);
		deliverMsgQueue = new ArrayBlockingQueue<SmsDeliverForm>(10000);
		
		
		smsScaner = new SmsScanerNew3(serverDAO);
		
		ControlThread controlThread = applicationContext.getBean("controlThread",ControlThread.class);
		controlThread.setName("controlThread");
		controlThread.setSmsScaner(smsScaner);
		controlThread.setPutSendFinishQueue(putSendFinishQueue);
		controlThread.setDeliverMsgQueue(deliverMsgQueue);
		controlThread.setApx(applicationContext);
		controlThread.start();
		
		//����ɨ���߳�
		try{
			Thread.sleep(15000);
		}catch (Exception e) {
			
		}
		
		//��100״̬��¼���¸���Ϊ0״̬
		HashMap<String,SmsQueue> smsQueueList = smsScaner.getQueueMap();
		
		try {
			for(SmsQueue queue: smsQueueList.values()){
				
				String td_code = queue.getTd_code();
				serverDAO.execute("update service_sms_info set send_status = 0 , response_status = 0 where send_status = 100 and td_code = '"+td_code+"'", null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		smsScaner.start();
		
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
				
		//�����������ز���
		DealPutSendFinishThread dealPutSendFinishThread = new DealPutSendFinishThread(serverDAO, putSendFinishQueue, lastSendInfoQueue);
		dealPutSendFinishThread.setName("dealPutSendFinishThread");
		dealPutSendFinishThread.start();
		
		 try{
			 Thread.sleep(1000);
		 }catch (Exception e) {
			 
		 }
	
		
		//������յ�������Ϣ
		 DealDeliverMessageThread dealDeliverMsgThread = new DealDeliverMessageThread(serverDAO, deliverMsgQueue);
		 dealDeliverMsgThread.setName("dealDeliverMsgThread");
		 dealDeliverMsgThread.start();
		
		 try{
			 Thread.sleep(1000);
		 }catch (Exception e) {
			 
		 }
	
		 
		
		
		applicationContext.registerShutdownHook();
		
	}
	

}