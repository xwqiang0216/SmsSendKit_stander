package td.api.smgp30.nioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SMGPDeliverMessage extends SMGPMessage {
	public static final int LEN_SMGP_DELIVER = 77;
	private byte[] body = null;
	private byte[] msg_id;
	private int is_report;
	private int msg_format;
	private String recv_time;
	private String scr_terminal_id;
	private String dest_terminal_id;
	private int msg_length;
	private String msg_content;
	private String reserve;
    private String sub;

	private String report_msg_id;
	private String report_stat;
	private String report_err;
	private Log log = LogFactory.getLog("serverLog");
	
	private int long_msg_id = 0;//长短信id
	private int long_msg_count = 0;//长短信数量，非长短信为0
	private int long_msg_sub_sn = 0;//长短信的序号
	
	private boolean isLong = false;
	
	

	public int getLong_msg_id() {
		return long_msg_id;
	}
	public void setLong_msg_id(int long_msg_id) {
		this.long_msg_id = long_msg_id;
	}
	public int getLong_msg_count() {
		return long_msg_count;
	}
	public void setLong_msg_count(int long_msg_count) {
		this.long_msg_count = long_msg_count;
	}
	public int getLong_msg_sub_sn() {
		return long_msg_sub_sn;
	}
	public void setLong_msg_sub_sn(int long_msg_sub_sn) {
		this.long_msg_sub_sn = long_msg_sub_sn;
	}
	public boolean isLong() {
		return isLong;
	}
	public void setLong(boolean isLong) {
		this.isLong = isLong;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public int getMsg_format() {
		return msg_format;
	}
	public void setMsg_format(int msg_format) {
		this.msg_format = msg_format;
	}
	public String getRecv_time() {
		return recv_time;
	}
	public void setRecv_time(String recv_time) {
		this.recv_time = recv_time;
	}
	public int getMsg_length() {
		return msg_length;
	}
	public void setMsg_length(int msg_length) {
		this.msg_length = msg_length;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public SMGPDeliverMessage(SMGPHeader header, byte[] body) throws IllegalArgumentException {
		this.header = header;
		this.body = body;

//		byte tmp_1[] = new byte[3];
//		byte tmp_2[] = new byte[4];
//		byte tmp_3[] = new byte[3];
//		System.arraycopy(body, 72, tmp_1, 0, 3);
//		System.arraycopy(body, 75, tmp_2, 0, 4);
//		System.arraycopy(body, 79, tmp_3, 0, 3);
		 msg_id = new byte[10];
			System.arraycopy(body, 0, msg_id, 0, 10);
		is_report = body[10];
		msg_format = body[11];
		
		try {
			recv_time = new String(body,12,14,"ISO_8859_1").trim();

			scr_terminal_id = new String(body,26,21,"ISO_8859_1").trim();
			dest_terminal_id = new String(body,47,21,"ISO_8859_1").trim();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		msg_length = body[68];
		//System.out.println("message length is:"+msg_length);
		
		if(msg_length < 0)
			msg_length = 256 + msg_length;
		
		if(msg_format ==0){
			try {

				msg_content = new String(body,69,msg_length,"ISO8859_1").trim();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(msg_format ==8){ 
			
			//上行长短信
			if(body.length != LEN_SMGP_DELIVER + msg_length){
				isLong = true;
				//System.out.println("+++this is a long deliver");
				try {
//					System.out.println("+++body.length:"+body.length +" msg_length:"+ msg_length);
					
					long_msg_id = body[72];
					long_msg_count = body[73];
					long_msg_sub_sn = body[74];
					System.out.println("444444444444444444444444444"+ "   long_msg_id:"+long_msg_id+" long_msg_count:"+long_msg_count+"  long_msg_sub_sn:"+long_msg_sub_sn);
					
					msg_content = new String(body,69+6,msg_length-6,"UnicodeBigUnmarked").trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				try {
					msg_content = new String(body,69,msg_length,"UnicodeBigUnmarked").trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}	
			
			
			
		}else{
			try {

				msg_content = new String(body,69,msg_length,"GBK").trim();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(is_report == 1){
			try {
				int pos = 0;
				
				byte tmp_1[] = new byte[3];
				byte tmp_2[] = new byte[4];
				byte tmp_3[] = new byte[3];
				System.arraycopy(body, 72, tmp_1, 0, 3);
				System.arraycopy(body, 75, tmp_2, 0, 4);
				System.arraycopy(body, 79, tmp_3, 0, 3);
				
			
				    report_msg_id = BCDUtil.bcd2Str(tmp_1)+"-"+BCDUtil.bcd2Str(tmp_2)+"-"+BCDUtil.bcd2Str(tmp_3);
                    //log.info("report msg_id is:"+repo 
	                
//				    pos = msg_content.indexOf("sub:");
//				    sub = msg_content.substring(pos+4,pos+7);
				    pos = msg_content.indexOf("stat:");
	                report_stat = msg_content.substring(pos + 5, pos + 12);
	                pos = msg_content.indexOf("err:");
	                report_err = msg_content.substring(pos + 4, pos + 7);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
//		if(body.length != LEN_SMGP_DELIVER + msg_length) {
//			throw new IllegalArgumentException("login response message body: invalid size");
//		}

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
	
	public byte[] getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(byte[] msg_id) {
		this.msg_id = msg_id;
	}
	public int getIs_report() {
		return is_report;
	}
	public void setIs_report(int is_report) {
		this.is_report = is_report;
	}
	public String getScr_terminal_id() {
		return scr_terminal_id;
	}
	public void setScr_terminal_id(String scr_terminal_id) {
		this.scr_terminal_id = scr_terminal_id;
	}
	public String getDest_terminal_id() {
		return dest_terminal_id;
	}
	public void setDest_terminal_id(String dest_terminal_id) {
		this.dest_terminal_id = dest_terminal_id;
	}
	public String getMsg_content() {
		return msg_content;
	}
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	public String getReport_msg_id() {
		return report_msg_id;
	}
	public void setReport_msg_id(String report_msg_id) {
		this.report_msg_id = report_msg_id;
	}
	public String getReport_stat() {
		return report_stat;
	}
	public void setReport_stat(String report_stat) {
		this.report_stat = report_stat;
	}
	public String getReport_err() {
		return report_err;
	}
	public void setReport_err(String report_err) {
		this.report_err = report_err;
	}
	public static void debugData(String desc,byte[] data){
		System.out.println("消息总长:"+data.length +" "+desc);		
		int count=0;
	      for(int i=0;i<data.length;i++){
	    	 int b=data[i];
	    	  if(b<0){b+=256;}
	    	 String hexString= Integer.toHexString(b);
	hexString = (hexString.length() == 1) ? "0" + hexString : hexString;
	    	 System.out.print(hexString+"  ");
	    	 count++;
	    	 if(count%4==0){
	    		 System.out.print( "  ");
	    	 }
	    	 if(count%16==0){
	    		 System.out.println();
	    	 }
	      }
	      System.out.println();
    }
	
	 public int getMsgFormat() {
		 return body[11];
	 }

}
