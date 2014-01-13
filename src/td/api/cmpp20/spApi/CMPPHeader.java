
package td.api.cmpp20.spApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;


public class CMPPHeader implements Cloneable {
    
    public final static int LEN_CMPP_HEADER = 12;
    
    private int total_length;
    private int command_id;
    private int sequence_id;
    
    
    public void setTotal_length(int len) {
        total_length = len;
    }
    
    
    public int getTotal_length() {
        return total_length;
    }
    
    
    public void setCommand(int id, int sequence) {
        command_id = id;
        sequence_id = sequence;
    }
    
    
    public void setCommand_id(int id) {
        command_id = id;
    }
    
    
    public int getCommand_id() {
        return command_id;
    }
    
    
    public int getSequence_id() {
        return sequence_id;
    }
    
    
    public void setSequence_id(int sequence) {
        sequence_id = sequence;
    }
    
    
	public CMPPHeader(byte [] buffer){
		total_length = CMPPMessage.byte4ToInteger(buffer, 0);
		command_id	 = 	CMPPMessage.byte4ToInteger(buffer, 4);
		sequence_id = CMPPMessage.byte4ToInteger(buffer, 8);
	}
    
    
    
    public CMPPHeader() {
		super();
	}
    
    public byte[] getMsgHeader() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(LEN_CMPP_HEADER);
        out.write(CMPPMessage.integerToByte(total_length), 0, 4);
        out.write(CMPPMessage.integerToByte(command_id), 0, 4);
        out.write(CMPPMessage.integerToByte(sequence_id), 0, 4);
        return out.toByteArray();
    }
    
    
    public void readMsgHeader(InputStream in) throws IOException {
        byte[] buffer = new byte[LEN_CMPP_HEADER];
        int actual_length = CMPPMessage.read(in, buffer);
        if(LEN_CMPP_HEADER != actual_length) {
        	CMPPMessage.log.debug("head: actual_length=" + actual_length + ", LEN_CMPP_HEADER=" + LEN_CMPP_HEADER);
        	if(actual_length == -1) {
        		throw new SocketException("get the end of the inputStream, maybe the connection is broken");
        	}else {
        		throw new IOException("can't get actual length of message header from the inputstream:" + actual_length);
        	}
        }
       
        total_length = CMPPMessage.byte4ToInteger(buffer, 0);
        command_id = CMPPMessage.byte4ToInteger(buffer, 4);
        sequence_id = CMPPMessage.byte4ToInteger(buffer, 8);
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