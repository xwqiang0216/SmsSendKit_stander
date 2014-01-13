package td.api.smgp30.nioApi;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class CommonBean {


	private ByteBuffer headerBuffer = ByteBuffer.allocate(SMGPHeader.LEN_SMGP_HEADER);

	private ByteBuffer bodyBuffer;

	private SMGPHeader smgpHeader;

	
	private boolean isConnected;
	
	private int max_speed_per_second;
	private long last_mark_time=0;
	private int has_send_count;
	
	private boolean notReceiveSendBindRespMessage = false;//是否发送了bind消息
	private long sendBindMessageTimestamp=0L;//发送bind消息的时间戳
	
	private long sendCheckTimestamp=0L;
	
	
	private long last_send_time = 0;
	private long  current_time = 0;
	
	
	public void reset(){
		headerBuffer.clear();
		bodyBuffer = null;
	}
	
	public CommonBean(int max_speed_per_second){
		this.max_speed_per_second = max_speed_per_second;
		
	}
	
	public long getSendCheckTimestamp() {
		return sendCheckTimestamp;
	}

	public void setSendCheckTimestamp(long sendCheckTimestamp) {
		this.sendCheckTimestamp = sendCheckTimestamp;
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
			throw new Exception("invalid packet length");
		}else if(bodyBuffer == null){
			headerBuffer.flip();
			smgpHeader = new SMGPHeader(headerBuffer.array());
			
			//System.out.println("----------------------package Length: "+smgpHeader.getPacket_length()+" command_id is:"+smgpHeader.getRequest_id()+"-----------------------");
			if(smgpHeader.getPacket_length() > 65536 || smgpHeader.getPacket_length() < 0)
				throw new Exception("invalid packet length");
			
			bodyBuffer = ByteBuffer.allocate(smgpHeader.getPacket_length() - SMGPHeader.LEN_SMGP_HEADER);

		}
		
		if(bodyBuffer != null && bodyBuffer.hasRemaining() && socketChannel.read(bodyBuffer) == -1){
			throw new Exception("invalid packet length");
		}
			
		
	}
	
	public boolean isReady(){
		
		return bodyBuffer != null && bodyBuffer.hasRemaining() == false;

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

	public SMGPHeader getSmgpHeader() {
		return smgpHeader;
	}

	public void setSmgpHeader(SMGPHeader smgpHeader) {
		this.smgpHeader = smgpHeader;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
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

	


}
