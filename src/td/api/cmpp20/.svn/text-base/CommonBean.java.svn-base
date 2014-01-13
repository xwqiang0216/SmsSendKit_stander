package td.api.cmpp20;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import td.api.cmpp20.spApi.CMPPHeader;
import utils.ByteUtil;


public class CommonBean {


	private ByteBuffer headerBuffer = ByteBuffer.allocate(CMPPHeader.LEN_CMPP_HEADER);

	private ByteBuffer bodyBuffer;

	private CMPPHeader cmppHeader;

	private boolean notReceiveSendBindRespMessage = false;//是否发送了bind消息
	private long sendBindMessageTimestamp=0L;//发送bind消息的时间戳
	private boolean isConnected;
	private long sendCheckTimestamp=0L;
	
	private int max_speed_per_second;
	private long last_mark_time=0;
	private long last_send_time = 0;
	private long  current_time = 0;
	private int has_send_count;
	
	private long active_time = 0L;
	private int active_count =0; 
	
	public void reset(){
		headerBuffer.clear();
		bodyBuffer = null;
	}
	
	public long getSendCheckTimestamp() {
		return sendCheckTimestamp;
	}

	public void setSendCheckTimestamp(long sendCheckTimestamp) {
		this.sendCheckTimestamp = sendCheckTimestamp;
	}

	public boolean isNotReceiveSendBindRespMessage() {
		return notReceiveSendBindRespMessage;
	}

	public void setNotReceiveSendBindRespMessage(
			boolean notReceiveSendBindRespMessage) {
		this.notReceiveSendBindRespMessage = notReceiveSendBindRespMessage;
	}

	public long getSendBindMessageTimestamp() {
		return sendBindMessageTimestamp;
	}

	public void setSendBindMessageTimestamp(long sendBindMessageTimestamp) {
		this.sendBindMessageTimestamp = sendBindMessageTimestamp;
	}

	public CommonBean(int max_speed_per_second){
		this.max_speed_per_second = max_speed_per_second;
		
	}
	
//	public boolean canSend(){
//		
//		if(System.currentTimeMillis() - last_mark_time > 1000){
//			has_send_count = 0;
//			last_mark_time = System.currentTimeMillis();
//		}
//		
//		return has_send_count < max_speed_per_second;
//			
//		
//	}
	
	public boolean activeTime(){
		if(System.currentTimeMillis() - active_time > 20000){
			active_count = active_count + 1; 
			active_time = System.currentTimeMillis();
			return true;
		}else{
			return false;
		}
	}
	
	
	public boolean canSend(int sleep_time){
		
		current_time = System.currentTimeMillis();
		if(current_time - last_send_time >=sleep_time){
			last_send_time = current_time;
			return true;
		}else{
			return false;
		}
		
	}
	
	
	public void send(int count){
		has_send_count += count;
	}

	public void read(SocketChannel socketChannel) throws Exception{

		
		if(headerBuffer.hasRemaining() && socketChannel.read(headerBuffer) == -1){
			throw new Exception("invalid packet length {[commonBean = " + this + "]-- HEADER:  " + this.getHeaderBuffer() + "[" + ByteUtil.bytesToHexString(this.getHeaderBuffer()) + "]}");
		}else if(bodyBuffer == null){
			headerBuffer.flip();
			cmppHeader = new CMPPHeader(headerBuffer.array());
			
			if(cmppHeader.getTotal_length() > 65536 || cmppHeader.getTotal_length() < 0)
				throw new Exception("invalid packet length  {[commonBean = " + this + "]-- HEADER:  " + this.getHeaderBuffer() + "[" + ByteUtil.bytesToHexString(this.getHeaderBuffer()) + "]}\n" +
						"head_total_length:" +  cmppHeader.getTotal_length() +"  seq:"+cmppHeader.getSequence_id()+"   command_id:" +cmppHeader.getCommand_id());
			
			bodyBuffer = ByteBuffer.allocate(cmppHeader.getTotal_length() - CMPPHeader.LEN_CMPP_HEADER);

		}
		
		if(bodyBuffer != null && bodyBuffer.hasRemaining() && socketChannel.read(bodyBuffer) == -1){
			throw new Exception("invalid packet length {[commonBean = " + this + "]-- HEADER:  " + this.getHeaderBuffer() + "[" + ByteUtil.bytesToHexString(this.getHeaderBuffer()) + "]-- BODY: " + this.getBodyBuffer() + "[" + ByteUtil.bytesToHexString(this.getBodyBuffer()) + "]}");
		}
			
		
	}
	
	public boolean isReady(){
		
		return bodyBuffer != null && bodyBuffer.hasRemaining() == false;

	}



	public long getActive_time() {
		return active_time;
	}

	public void setActive_time(long active_time) {
		this.active_time = active_time;
	}

	public ByteBuffer getHeaderBuffer() {
		return headerBuffer;
	}

	public void setHeaderBuffer(ByteBuffer headerBuffer) {
		this.headerBuffer = headerBuffer;
	}

	public ByteBuffer getBodyBuffer() {
		return bodyBuffer;
	}

	public void setBodyBuffer(ByteBuffer bodyBuffer) {
		this.bodyBuffer = bodyBuffer;
	}



	public int getActive_count() {
		return active_count;
	}

	public void setActive_count(int active_count) {
		this.active_count = active_count;
	}

	public CMPPHeader getCmppHeader() {
		return cmppHeader;
	}

	public void setCmppHeader(CMPPHeader cmppHeader) {
		this.cmppHeader = cmppHeader;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	


}
