package com.hskj.sgip.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;


public class SGIPHeader implements Cloneable {
    
    public final static int LEN_SGIP_HEADER = 20;
    
    private int total_length;
    private int command_id;
    private int seq_no1, seq_no2, seq_no3;
    
    
    
    public SGIPHeader() {
		super();
	}


	public void setTotalLength(int len) {
        total_length = len;
    }
    
    
    public int getTotalLength() {
        return total_length;
    }
   
    
    public void setCommandID(int id, boolean createSequence,long node_id) {
        command_id = id;
        if(createSequence) {
            buildSequence(node_id);
        }
    }
    
    
    public int getCommanID() {
        return command_id;
    }
    
    
	public SGIPHeader(byte [] buffer){
		total_length = SGIPMessage.byte4ToInteger(buffer, 0);
		command_id	 = 	SGIPMessage.byte4ToInteger(buffer, 4);
        seq_no1 = SGIPMessage.byte4ToInteger(buffer, 8);
        seq_no2 = SGIPMessage.byte4ToInteger(buffer, 12);
        seq_no3 = SGIPMessage.byte4ToInteger(buffer, 16);
	}
    
    public void buildSequence(long node_id) {
        SGIPSequence seq = SGIPSequence.createSequence();
        seq_no1 = seq.getSeq_no1(node_id);
        seq_no2 = seq.getSeq_no2();
        seq_no3 = seq.getSeq_no3();
    }
    
    
    public int getSeq_no1() {
        return seq_no1;
    }
    
    
    public int getSeq_no2() {
        return seq_no2;
    }
    
    
    public int getSeq_no3() {
        return seq_no3;
    }
    
    
    public byte[] getMsgHeader() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(LEN_SGIP_HEADER);
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(total_length);
        dos.writeInt(command_id);
        dos.writeInt((int)seq_no1);
        dos.writeInt(seq_no2);
        dos.writeInt(seq_no3);
        return out.toByteArray();
    }
    
    
    public void readMsgHeader(InputStream in) throws IOException {
        byte[] buffer = new byte[LEN_SGIP_HEADER];
        int actual_length = 0, read_bytes;
        do {
        	read_bytes = in.read(buffer, actual_length, LEN_SGIP_HEADER-actual_length);
        	actual_length += read_bytes;
        }while(actual_length < LEN_SGIP_HEADER && read_bytes > 0);
        if(LEN_SGIP_HEADER != actual_length) {
        	if(actual_length == -1) {
        		throw new SocketException("get the end of the inputStream, maybe the connection is broken");
        	}else {
               StringBuffer sb = new StringBuffer();
    	        for(int i = 0; i < LEN_SGIP_HEADER; i++) {
    	            sb.append("head[").append(i).append("] = ").append(buffer[i] + ", ");
    	    	}
    	   	throw new IOException("can't get actual length of message header from the inputstream:" + actual_length);
        	}
        }
        
        
        total_length = SGIPMessage.byte4ToInteger(buffer, 0);
        command_id = SGIPMessage.byte4ToInteger(buffer, 4);
        seq_no1 = SGIPMessage.byte4ToInteger(buffer, 8);
        seq_no2 = SGIPMessage.byte4ToInteger(buffer, 12);
        seq_no3 = SGIPMessage.byte4ToInteger(buffer, 16);
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