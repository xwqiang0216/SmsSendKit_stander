package td.api.cmpp30.spApi;

public class CMPPSubmitRespMessage3 extends CMPPMessage {
	 public static final int LEN_CMPP_SUBMITRESP = 12;
	    private byte[] body = null;
	    
	    
	    public CMPPSubmitRespMessage3(CMPPHeader header, byte[] body) throws IllegalArgumentException {
	        this.header = header;
	        this.body = body;
	        if(body.length != LEN_CMPP_SUBMITRESP) {
	            throw new IllegalArgumentException("submit response message body: invalid size");
	        }
	    }
	    
	    
	    public long getSeq_no1() {
	    	byte[] tmp = new byte[8];
	    	System.arraycopy(body, 0, tmp, 0, 8);

	    	return CMPPMessage.byte8ToLong(tmp, 0);
	    }
	    
	    
	    public int getResult() {
	    	byte[] tmp = new byte[4];
	    	System.arraycopy(body, 8, tmp, 0, 4);
	    	return CMPPMessage.byte4ToInteger(tmp, 0);
	    }
	    
	    
	    protected byte[] getMsgBody() {
	        return body;
	    }
}
