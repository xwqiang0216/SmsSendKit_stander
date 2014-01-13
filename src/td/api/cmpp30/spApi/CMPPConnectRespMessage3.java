package td.api.cmpp30.spApi;

public class CMPPConnectRespMessage3 extends CMPPMessage {
	 public static final int LEN_CMPP_CONNECTRESP = 21;
	    private byte[] body = null;
	
	    public CMPPConnectRespMessage3(CMPPHeader header, byte[] body) throws IllegalArgumentException {
	        this.header = header;
	        this.body = body;
	        if(body.length != LEN_CMPP_CONNECTRESP) {
	            throw new IllegalArgumentException("connect response message body: invalid size");
	        }
	    }
	    
	    
	    public int getConnect_status() {
	    	byte[] tmp = new byte[4];
	    	System.arraycopy(body, 0, tmp, 0, 4);
	    	return CMPPMessage.byte4ToInteger(tmp, 0);
	    }
	    
	    
	    public int getVersion() {
	        return body[20];
	    }
	    
	    
	    protected byte[] getMsgBody() {
	        return body;
	    }
	
}
