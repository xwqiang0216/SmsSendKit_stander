package td.api.smgp30.nioApi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class SMGPHeader implements Cloneable{
	public final static int LEN_SMGP_HEADER = 12;
	private static int squence_index;
	private final int MIN_SEQ = 0;
	private final int MAX_SEQ = 0x7fffffff;
	private int packet_length;
	private int request_id;
	private int sequence_id;
	private static Lock lock = new ReentrantLock(); 

	private void createSequence(){
		lock.lock();
		try{
			if(squence_index == MAX_SEQ){
				squence_index = MIN_SEQ;
			}else{
				squence_index++;
			}
			sequence_id = squence_index;
		}finally{
			lock.unlock();
		}
	}

	public SMGPHeader(){
		
	}
	
	public SMGPHeader(byte [] buffer){
		packet_length = SMGPMessage.byte4ToInteger(buffer, 0);
		request_id	 = 	SMGPMessage.byte4ToInteger(buffer, 4);
		sequence_id = SMGPMessage.byte4ToInteger(buffer, 8);
	}
	
	
	
	
	public int getPacket_length() {
		return packet_length;
	}


	public void setPacket_length(int packet_length) {
		this.packet_length = packet_length;
	}


	public void setRequest_id(int request_id , boolean is_create_seq) {
		this.request_id = request_id;
		if(is_create_seq){
			createSequence();
		}
	}


	public int getRequest_id() {
		return request_id;
	}



	public int getSequence_id() {
		return sequence_id;
	}


	public void setSequence_id(int sequence_id) {
		this.sequence_id = sequence_id;
	}


	public static int getLEN_SMGP_HEADER() {
		return LEN_SMGP_HEADER;
	}


	public byte[] getMsgHeader() throws IOException {
		
		ByteBuffer buffer = ByteBuffer.allocate(LEN_SMGP_HEADER);
		buffer.putInt(packet_length);
		buffer.putInt(request_id);
		buffer.putInt(sequence_id);
		
		return buffer.array();
		/*
		ByteArrayOutputStream out = new ByteArrayOutputStream(LEN_SMGP_HEADER);
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeInt(packet_length);
		dos.writeInt(request_id);
		dos.writeInt(sequence_id);


		return out.toByteArray();
		*/
	}


	public void readMsgHeader(InputStream in) throws IOException {
		byte[] buffer = new byte[LEN_SMGP_HEADER];
		//int actual_length = in.read(buffer);
		int actual_length = 0, read_bytes;
		//while(read_bytes > 0 && actual_length < LEN_SGIP_HEADER) {
		do {
			read_bytes = in.read(buffer, actual_length, LEN_SMGP_HEADER-actual_length);
			actual_length += read_bytes;
		}while(actual_length < LEN_SMGP_HEADER && read_bytes > 0);
		if(LEN_SMGP_HEADER != actual_length) {
			if(actual_length == -1) {
				throw new SocketException("get the end of the inputStream, maybe the connection is broken");
			}else {
				//System.out.println("head: actual_length=" + actual_length + ", LEN_SGIP_HEADER=" + LEN_SMGP_HEADER);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < LEN_SMGP_HEADER; i++) {
					sb.append("head[").append(i).append("] = ").append(buffer[i] + ", ");
				}
				//System.out.println(sb.toString());
				throw new IOException("can't get actual length of message header from the inputstream:" + actual_length);
			}
		}

		/*if(SGIPMessage.log.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < LEN_SGIP_HEADER; i++) {
                sb.append("\nhead[").append(i).append("] = ").append(buffer[i]);
        	}
            SGIPMessage.log.debug(sb.toString());
        }*/
		SMGPMessage.debugData("sdfs",buffer);
		packet_length = SMGPMessage.byte4ToInteger(buffer, 0);
		request_id	 = 	SMGPMessage.byte4ToInteger(buffer, 4);
		sequence_id = SMGPMessage.byte4ToInteger(buffer, 8);

	}

	public void reCreateSeq(){
		createSequence();
	}

	protected Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
