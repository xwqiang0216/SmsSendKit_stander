package td.api.cmpp30.spApi;

import java.io.UnsupportedEncodingException;

public class CMPPDeliverMessage3 extends CMPPMessage{

    
	public static final int LEN_CMPP_DELIVER = 97;
    private byte[] body = null;
    private String report_desc;
    
    private int sub_msg_id = 0;//长短信id
	private int pk_total = 0;//长短信数量，非长短信为0
	private int pk_number = 0;//长短信的序号
    
	
    public int getSub_msg_id() {
		return sub_msg_id;
	}


	public void setSub_msg_id(int sub_msg_id) {
		this.sub_msg_id = sub_msg_id;
	}


	public int getPk_total() {
		return pk_total;
	}


	public void setPk_total(int pk_total) {
		this.pk_total = pk_total;
	}


	public int getPk_number() {
		return pk_number;
	}


	public void setPk_number(int pk_number) {
		this.pk_number = pk_number;
	}


	public CMPPDeliverMessage3(CMPPHeader header, byte[] body) throws IllegalArgumentException {
        this.header = header;
        this.body = body;
        if(body.length != LEN_CMPP_DELIVER + (body[76] & 0xff)) {
            throw new IllegalArgumentException("deliver message body: invalid size");
        }
    }
    
    
    public int isReport() {
        return body[75];
    }
    
    
    /**
     * it is available only if this message is a status report
     */
    public long getReport_no1() {
    	long result = 0;
    	if(isReport() == 1) {
	    	byte[] tmp = new byte[8];
	    	System.arraycopy(body, 77, tmp, 0, 8);
	        result = CMPPMessage.byte8ToLong(tmp, 0);
    	}
    	return result;
    }
    
    
    public int getReport_state() {
    	int result = -100;
    	if(isReport() == 1) {
    		try {
    			report_desc = new String(body, 85, 7, "ISO8859_1");
    			if("DELIVRD".equals(report_desc) || "ACCEPTD".equals(report_desc)) {
    				result = 0;
    			}
    		}catch(UnsupportedEncodingException e) { }
    	}
    	return result;
    }
    
    
    public String getReport_desc() {
        return report_desc;
    }
    
    
    public void copyMsgID(byte[] dest) {
        System.arraycopy(body, 0, dest, 0, 8);
    }
    
    
    /** get the src-terminal-id */
    public String getSrcTermid() {
        return new String(body, 42, 32).trim();
    }
    
    
    /** get the dest-terminal-id */
    public String getDestTermid() {
        return new String(body, 8, 21).trim();
    }
    
    
    /** get the message content */
    public String getMsgContent() {
        String result = null;
        if(isReport() == 0) {
        	try {
        		int format = getMsgFormat();
        		int length = body[76] & 0xff;
        		
        		if(format == 0 || format == 15) {
        			result = new String(body, 77, length, "GBK");
        		}else {
        			result = new String(body, 77, length, "UnicodeBigUnmarked");
        			//result = new String(result.getBytes("GBK"), "ISO8859_1");
        		}
    		}catch(UnsupportedEncodingException e) { }
        }
        return result;
    }
    
    
    public int getMsgFormat() {
    	return body[41];
    }
    
    
    protected byte[] getMsgBody() {
        return body;
    }
}
