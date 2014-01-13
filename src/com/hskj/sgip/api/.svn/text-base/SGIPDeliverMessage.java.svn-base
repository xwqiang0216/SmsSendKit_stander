package com.hskj.sgip.api;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;





public class SGIPDeliverMessage extends SGIPMessage {
    
    public static final int LEN_SGIP_DELIVER = 57;
    private byte[] body = null;
    private int msg_length;
    private int msg_encode;
    
    private int TP_udhi ;
	private int long_msg_id;//长短信id
	private int long_msg_count;//长短信数量，非长短信为0
	private int long_msg_sub_sn;//长短信的序号
	
	
    
    
    public int getMsg_encode() {
		return msg_encode;
	}


	public void setMsg_encode(int msg_encode) {
		this.msg_encode = msg_encode;
	}


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


	public SGIPDeliverMessage(SGIPHeader header, byte[] body) throws IllegalArgumentException {
        this.header = header;
        this.body = body;
        msg_length = SGIPMessage.byte4ToInteger(body, 45);
        msg_encode = body[44];
        TP_udhi = body[43];
        
        
        if(body.length != LEN_SGIP_DELIVER + msg_length) {
        }
    }
    
    
    /** get the src-terminal-id */
    public String getSrcTermid() {
        return new String(body, 0, 21).trim();
    }
    
    
    /** get the dest-terminal-id */
    public String getDestTermid() {
        return new String(body, 21, 21).trim();
    }
    
    
    /** get the message content */
    public String getMsgContent() {
        String content = null;
        
        if(TP_udhi == 1 || body[49] == 0x05){//处理长短信上行 49,50,51位置      分别对应 05 00 03 
        	
        	long_msg_id = body[52];
        	long_msg_count = body[53];
        	long_msg_sub_sn = body[54];
        	
        	System.out.println("==============长短信上行=====================id:count:sn  "  + long_msg_id+":" +long_msg_count+":" +long_msg_sub_sn);
        	
        	try {
        		if(msg_encode == 15) {			// GBK
        			content = new String(body, 49+6, msg_length-6, "GBK");
        			//content = new String(content.getBytes("GBK"), "ISO8859_1");
        		}else if(msg_encode == 8) {		// UCS2
        			content = new String(body, 49+6, msg_length-6, "UnicodeBigUnmarked");
        			//content = new String(content.getBytes("GBK"), "ISO8859_1");
        		}else {
        			content = new String(body, 49+6, msg_length-6, "ISO8859_1");
        			content = new String(content.getBytes("ISO8859_1"), "GBK");
        		}
        	}catch(UnsupportedEncodingException e) {
        		e.printStackTrace();
        	}
        	
        	System.out.println("#####################   " + content);
        }else{
        	try {
        		if(msg_encode == 15) {			// GBK
        			content = new String(body, 49, msg_length, "GBK");
        			//content = new String(content.getBytes("GBK"), "ISO8859_1");
        		}else if(msg_encode == 8) {		// UCS2
        			content = new String(body, 49, msg_length, "UnicodeBigUnmarked");
        			//content = new String(content.getBytes("GBK"), "ISO8859_1");
        		}else {
        			content = new String(body, 49, msg_length, "ISO8859_1");
        			content = new String(content.getBytes("ISO8859_1"), "GBK");
        		}
        	}catch(UnsupportedEncodingException e) {
        		e.printStackTrace();
        	}
        	
        }
        
        return content;
    }
    
    
    /** get the link id */
    public String getLinkID() {
        return new String(body, 49 + msg_length, 8).trim();
    }
    
    
    protected byte[] getMsgBody() {
        return body;
    }


	@Override
	public void resp(OutputStream out, 
			long node_id) throws IOException {
		//System.out.println("SGIP deliver: " + getMsgContent() + " from "
		//		+ getSrcTermid() + " to " + getDestTermid());
		//server.deliverMessage(this);
		new SGIPDeliverRespMessage(this, node_id).write(out);
	}

}