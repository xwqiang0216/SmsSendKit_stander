package td.api.smgp30.nioApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;



public abstract class SMGPMessage {
	public static final int ID_SMGP_LOGIN = 1;
	public static final int ID_SMGP_LOGIN_RESP = 0x80000001;
	public static final int ID_SMGP_EXIT = 6;
	public static final int ID_SMGP_EXIT_RESP = 0x80000006;
	public static final int ID_SMGP_SUBMIT = 2;
	public static final int ID_SMGP_SUBMIT_RESP = 0x80000002;
	public static final int ID_SMGP_DELIVER = 3;
	public static final int ID_SMGP_DELIVER_RESP = 0x80000003;
	public static final int ID_SMGP_ACTIVE_TEST = 4;
	public static final int ID_SMGP_ACTIVE_TEST_RESP = 0x80000004;

	public static final int ID_SMGP_FORWARD = 5;
	public static final int ID_SMGP_FORWARD_RESP = 0x80000005;

	private static String getCommandIDName(int id) {
		switch (id) {
		case ID_SMGP_LOGIN:
			return "ID_SMGP_LOGIN";
		case ID_SMGP_LOGIN_RESP:
			return "ID_SMGP_LOGIN_RESP";
		case ID_SMGP_EXIT:
			return "ID_SMGP_EXIT";
		case ID_SMGP_EXIT_RESP:
			return "ID_SMGP_EXIT_RESP";
		case ID_SMGP_SUBMIT:
			return "ID_SMGP_SUBMIT";
		case ID_SMGP_SUBMIT_RESP:
			return "ID_SMGP_SUBMIT_RESP";
		case ID_SMGP_DELIVER:
			return "ID_SMGP_DELIVER";
		case ID_SMGP_DELIVER_RESP:
			return "ID_SMGP_DELIVER_RESP";
		case ID_SMGP_ACTIVE_TEST:
			return "ID_SMGP_ACTIVE_TEST";
		case ID_SMGP_ACTIVE_TEST_RESP:
			return "ID_SMGP_ACTIVE_TEST_RESP";
		case ID_SMGP_FORWARD:
			return "ID_SMGP_FORWARD";
		case ID_SMGP_FORWARD_RESP:
			return "ID_SMGP_FORWARD_RESP";
		default:
			return "UNKNOW";
		}
	}

	protected SMGPHeader header = null;



	public SMGPHeader getHeader() {
		return header;
	}

	public void setHeader(SMGPHeader header) {
		this.header = header;
	}

	/** get the command id */
	public int getRequestID() {
		return header.getRequest_id();
	}

	public int getSe() {
		return header.getSequence_id();
	}



	/**
	 * clone a message header
	 */
	public SMGPHeader getCloneMsgHeader() {
		return (SMGPHeader) header.clone();
	}

	/**
	 * this abstract method should be overrided
	 * 
	 * @return the byte array describes the current message body
	 */
	protected abstract byte[] getMsgBody();

	public abstract void resp(OutputStream out) throws IOException;


	public static  SMGPMessage create(SMGPHeader header , byte[] buffer) throws IOException {
		switch (header.getRequest_id()) {

		case SMGPMessage.ID_SMGP_LOGIN:
			return new SMGPLoginMessage(header, buffer);

		case SMGPMessage.ID_SMGP_LOGIN_RESP:
			return new SMGPLoginRespMessage(header, buffer);

		case SMGPMessage.ID_SMGP_EXIT:
			return new SMGPExitMessage(header, buffer);

		case SMGPMessage.ID_SMGP_SUBMIT_RESP:
			return new SMGPSubmitRespMessage(header,buffer);

		case SMGPMessage.ID_SMGP_DELIVER:
			return new SMGPDeliverMessage(header, buffer);
		case SMGPMessage.ID_SMGP_ACTIVE_TEST:

			return new SMGPActivceTestMessage(header, buffer);

		}
		return null;
	}
	
	public ByteBuffer getMessage() throws Exception{
		ByteBuffer messageBuffer = ByteBuffer.allocate(header.getPacket_length());
		messageBuffer.put(header.getMsgHeader());
		messageBuffer.put(getMsgBody());
		messageBuffer.flip();
		return messageBuffer;
	}


	/**
	 * construct a new {@link SMGPMessage} object according to the inputstream
	 * 
	 * @throws IOException
	 */
	public static SMGPMessage read(DataInputStream in) throws IOException {
		//int len = in.readInt();
		SMGPHeader tmp = new SMGPHeader();
		tmp.readMsgHeader(in); // get message header
		//byte[] data=new byte[len];
		//in.readFully(data);
		//debugData("接受",data);
		//byte[] tmp1=new byte[1];
		//tmp1[0]=data[8];
		//System.out.println(byte2int(tmp1));
		byte[] buffer = null;
		int body_length = tmp.getPacket_length() - SMGPHeader.LEN_SMGP_HEADER;
		if (body_length > 0) {
			if (body_length > 2000) {
				throw new SocketException("the body length overflow: "
						+ body_length);
			}
			buffer = new byte[body_length];
			// int actual_length = in.read(buffer);
			int actual_length = 0, read_bytes;
			// while(read_bytes > 0 && actual_length < body_length) {
			do {
				read_bytes = in.read(buffer, actual_length, body_length
						- actual_length);
				actual_length += read_bytes;
			} while (actual_length < body_length && read_bytes > 0);
			if (body_length != actual_length) { // get message body
				//System.out.println("read: body_length=" + body_length
				//		+ ", actual_length=" + actual_length);
				//System.out.println("head.total_length=" + tmp.getPacket_length()
				//		+ ", cmd_id=" + tmp.getRequest_id());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < body_length; i++) {
					sb.append("body[").append(i).append("] = ").append(
							buffer[i] + ", ");
				}
				//System.out.println(sb.toString());
				throw new IOException(
				"can't get actual length of message body from the inputstream");
			}
		}


		//System.out.println("get a message from SMG, the command id: "
		//		+ getCommandIDName(tmp.getRequest_id())+ "\t" + tmp.getSequence_id()) ;





		switch (tmp.getRequest_id()) {
		case SMGPMessage.ID_SMGP_LOGIN:
			return new SMGPLoginMessage(tmp, buffer);

		case SMGPMessage.ID_SMGP_LOGIN_RESP:
			return new SMGPLoginRespMessage(tmp, buffer);

		case SMGPMessage.ID_SMGP_EXIT:
			return new SMGPExitMessage(tmp, buffer);



		case SMGPMessage.ID_SMGP_SUBMIT_RESP:
			return new SMGPSubmitRespMessage(tmp,buffer);

		case SMGPMessage.ID_SMGP_DELIVER:
			return new SMGPDeliverMessage(tmp, buffer);
		case SMGPMessage.ID_SMGP_ACTIVE_TEST:

			return new SMGPActivceTestMessage(tmp, buffer);

		}
		return null;

	}

	/**
	 * write a {@link SMGPMessage} object to the SocketOutputStream
	 * 
	 * @throws IOException
	 */
	public void write(OutputStream out) throws IOException {
		byte[] tmp_head = header.getMsgHeader(); // get message header
		byte[] tmp_body = getMsgBody(); // get message body
		int length = tmp_head.length;
		if (tmp_body != null)
			length += tmp_body.length;

		byte[] message = new byte[length];
		// write message header
		System.arraycopy(tmp_head, 0, message, 0, SMGPHeader.LEN_SMGP_HEADER);
		if (tmp_body != null) {
			// write message body
			System.arraycopy(tmp_body, 0, message, SMGPHeader.LEN_SMGP_HEADER,tmp_body.length);
		}


		//System.out.println("send a message to SMG, the command id: "
		//		+ getCommandIDName(header.getRequest_id())+ "\t" + header.getSequence_id()) ;

		//out.write("start".getBytes());
		out.write(message);
		SMGPMessage.debugData("发送", message);
		//out.write("end".getBytes());
		out.flush();
	}
	public  static int byte2int(byte[] bts,int offset){
		int n = 0;
		int tem =0;
		for(int j = offset;j<offset+4;j++){
			n<<=8;
			tem=bts[j]&0xff;
			n|=tem;
		}
		return n;
	}

	/**
	 * convert 4 bytes to a Integer
	 * 
	 * @param b
	 *            the byte array, sorted from height to low
	 * @param offset
	 *            the offset value
	 * @return byte[offset], byte[offset+1], byte[offset+2], byte[offset+3]
	 */
	protected static int byte4ToInteger(byte[] b, int offset) {
		return (0xff & b[offset]) << 24 | (0xff & b[offset + 1]) << 16
		| (0xff & b[offset + 2]) << 8 | (0xff & b[offset + 3]);
	}

	/**
	 * convert a integer to 4 bytes
	 * 
	 * @param n
	 *            the integer want to be converted to bytes
	 * @return byte array sorted from height to low, the size is 4
	 */
	protected static byte[] integerToByte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}
	/**
	 * 调试消息原始数据
	 * @param dir:消息发送方向说明
	 * @param data:消息数据
	 */
	public static void debugData(String desc,byte[] data){
		//System.out.println("消息总长:"+data.length +" "+desc);		
		int count=0;
		for(int i=0;i<data.length;i++){
			int b=data[i];
			if(b<0){b+=256;}
			String hexString= Integer.toHexString(b);
			hexString = (hexString.length() == 1) ? "0" + hexString : hexString;
			// System.out.print(hexString+"  ");
			count++;
			//	    	 if(count%4==0){
			//	    		 System.out.print( "  ");
			//	    	 }
			//	    	 if(count%16==0){
			//	    		 System.out.println();
			//	    	 }
		}
		// System.out.println();
	}
	public  static int byte2int(byte[] bts){
		int n = 0;
		int tem =0;
		for(int j = 0;j<bts.length;j++){
			n<<=8;
			tem=bts[j]&0xff;
			n|=tem;
		}
		return n;
	}
}
