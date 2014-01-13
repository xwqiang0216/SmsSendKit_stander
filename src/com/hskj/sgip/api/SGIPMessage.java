package com.hskj.sgip.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;





public abstract class SGIPMessage {

	public static final int ID_SGIP_BIND = 1;
	public static final int ID_SGIP_BIND_RESP = 0x80000001;
	public static final int ID_SGIP_UNBIND = 2;
	public static final int ID_SGIP_UNBIND_RESP = 0x80000002;
	public static final int ID_SGIP_SUBMIT = 3;
	public static final int ID_SGIP_SUBMIT_RESP = 0x80000003;
	public static final int ID_SGIP_DELIVER = 4;
	public static final int ID_SGIP_DELIVER_RESP = 0x80000004;
	public static final int ID_SGIP_REPORT = 5;
	public static final int ID_SGIP_REPORT_RESP = 0x80000005;

	private static String getCommandIDName(int id) {
		switch (id) {
		case ID_SGIP_BIND:
			return "ID_SGIP_BIND";
		case ID_SGIP_BIND_RESP:
			return "ID_SGIP_BIND_RESP";
		case ID_SGIP_UNBIND:
			return "ID_SGIP_UNBIND";
		case ID_SGIP_UNBIND_RESP:
			return "ID_SGIP_UNBIND_RESP";
		case ID_SGIP_SUBMIT:
			return "ID_SGIP_SUBMIT";
		case ID_SGIP_SUBMIT_RESP:
			return "ID_SGIP_SUBMIT_RESP";
		case ID_SGIP_DELIVER:
			return "ID_SGIP_DELIVER";
		case ID_SGIP_DELIVER_RESP:
			return "ID_SGIP_DELIVER_RESP";
		case ID_SGIP_REPORT:
			return "ID_SGIP_REPORT";
		case ID_SGIP_REPORT_RESP:
			return "ID_SGIP_REPORT_RESP";

		default:
			return "UNKNOW";
		}
	}
	
	public static  SGIPMessage create(SGIPHeader header , byte[] buffer) throws IOException {
		switch (header.getCommanID()) {

		case SGIPMessage.ID_SGIP_SUBMIT_RESP:
			return new SGIPSubmitRespMessage(header, buffer);

		case SGIPMessage.ID_SGIP_DELIVER:
			return new SGIPDeliverMessage(header, buffer);

		case SGIPMessage.ID_SGIP_BIND:
			return new SGIPBindMessage(header, buffer);

		case SGIPMessage.ID_SGIP_UNBIND:
			return new SGIPUnbindMessage(header);

		case SGIPMessage.ID_SGIP_BIND_RESP:
			return new SGIPBindRespMessage(header, buffer);

		case SGIPMessage.ID_SGIP_UNBIND_RESP:
			return new SGIPUnbindRespMessage(header);

		case SGIPMessage.ID_SGIP_REPORT:
			return new SGIPReportMessage(header, buffer);

		}
		return null;
	} 

	
	
	
	
	
	protected SGIPHeader header = null;

	public ByteBuffer getMessage() throws Exception{
		ByteBuffer messageBuffer = ByteBuffer.allocate(header.getTotalLength());
		messageBuffer.put(header.getMsgHeader());
		messageBuffer.put(getMsgBody());
		messageBuffer.flip();
		return messageBuffer;
	}
	

	public SGIPHeader getHeader() {
		return header;
	}

	public void setHeader(SGIPHeader header) {
		this.header = header;
	}

	/** get the command id */
	public int getCommandID() {
		return header.getCommanID();
	}

	public int getSeq_no1() {
		return header.getSeq_no1();
	}

	public int getSeq_no2() {
		return header.getSeq_no2();
	}

	public int getSeq_no3() {
		return header.getSeq_no3();
	}

	public SGIPHeader getCloneMsgHeader() {
		return (SGIPHeader) header.clone();
	}

	protected abstract byte[] getMsgBody();

	public abstract void resp(OutputStream out, long node_id) throws IOException;

	public static SGIPMessage read(InputStream in) throws IOException {
		SGIPHeader tmp = new SGIPHeader();
		tmp.readMsgHeader(in); // get message header

		byte[] buffer = null;
		int body_length = tmp.getTotalLength() - SGIPHeader.LEN_SGIP_HEADER;
		if (body_length > 0) {
			if (body_length > 2000) {
				throw new SocketException("the body length overflow: "
						+ body_length);
			}
			buffer = new byte[body_length];
			int actual_length = 0, read_bytes;
			do {
				read_bytes = in.read(buffer, actual_length, body_length
						- actual_length);
				actual_length += read_bytes;
			} while (actual_length < body_length && read_bytes > 0);
			if (body_length != actual_length) { // get message body
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < body_length; i++) {
					sb.append("body[").append(i).append("] = ").append(
							buffer[i] + ", ");
				}
				throw new IOException(
						"can't get actual length of message body from the inputstream");
			}
		}



		switch (tmp.getCommanID()) {
		case SGIPMessage.ID_SGIP_SUBMIT_RESP:
			return new SGIPSubmitRespMessage(tmp, buffer);

		case SGIPMessage.ID_SGIP_DELIVER:
			return new SGIPDeliverMessage(tmp, buffer);

		case SGIPMessage.ID_SGIP_BIND:
			return new SGIPBindMessage(tmp, buffer);

		case SGIPMessage.ID_SGIP_UNBIND:
			return new SGIPUnbindMessage(tmp);

		case SGIPMessage.ID_SGIP_BIND_RESP:
			return new SGIPBindRespMessage(tmp, buffer);

		case SGIPMessage.ID_SGIP_UNBIND_RESP:
			return new SGIPUnbindRespMessage(tmp);

		case SGIPMessage.ID_SGIP_REPORT:
			return new SGIPReportMessage(tmp, buffer);
		}
		
		return null;
	}

	/**
	 * write a {@link SGIPMessage} object to the SocketOutputStream
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
		System.arraycopy(tmp_head, 0, message, 0, SGIPHeader.LEN_SGIP_HEADER);
		if (tmp_body != null) {
			// write message body
			System.arraycopy(tmp_body, 0, message, SGIPHeader.LEN_SGIP_HEADER,
					tmp_body.length);
		}

		//System.out.println("send a message to SMG, the command id: "
		//			+ getCommandIDName(header.getCommanID())+ "\t" + header.getSeq_no1() + ":" + header.getSeq_no2() + ":" + header.getSeq_no3()) ;
		
		out.write(message);
		out.flush();
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
}