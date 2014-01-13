package td.api.smgp30.nioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SMGPSubmitRespMessage extends SMGPMessage{
	public static final int LEN_SGMGP_SUBMIT = 14;
	private byte[] body = null;
	private String msg_id;
	private int status;
	private Log log = LogFactory.getLog("serverLog");
	
	public SMGPSubmitRespMessage(SMGPHeader header, byte[] body) throws IllegalArgumentException {
		this.header = header;
		this.body = body;
		String tmp_msg_id = null;
		try {
			tmp_msg_id = new String(body,0,10,"ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte tmp_1[] = new byte[3];
		byte tmp_2[] = new byte[4];
		byte tmp_3[] = new byte[3];
		System.arraycopy(body, 0, tmp_1, 0, 3);
		System.arraycopy(body, 3, tmp_2, 0, 4);
		System.arraycopy(body, 7, tmp_3, 0, 3); 
		/*byte tmp_1[] = new byte[4];
	    System.arraycopy(tmp_msg_id.substring(0, 3).getBytes(), 0, tmp_1, 1, 3);
	   
	    byte tmp_2[] = new byte[4];
	    tmp_2 = tmp_msg_id.substring(3, 7).getBytes();
	    
	    byte tmp_3[] = new byte[4];
	    System.arraycopy(tmp_msg_id.substring(7, 10).getBytes(), 0, tmp_3, 1, 3);*/
	    msg_id = BCDUtil.bcd2Str(tmp_1)+"-"+BCDUtil.bcd2Str(tmp_2)+"-"+BCDUtil.bcd2Str(tmp_3);
	    //log.info("get msg_id is:"+msg_id);
	   // msg_id =byte4ToInteger(tmp_1,0)+"-"+byte4ToInteger(tmp_2,0)+"-"+byte4ToInteger(tmp_3,0); 
		
		status = byte4ToInteger(body, 10);
		
		if(body.length != LEN_SGMGP_SUBMIT) {
			throw new IllegalArgumentException("login response message body: invalid size");
		}
	}
	
	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	protected byte[] getMsgBody() {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public void resp(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
