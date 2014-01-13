
package com.hskj.sgip.api;

import java.io.IOException;
import java.io.OutputStream;







public class SGIPUnbindRespMessage extends SGIPMessage {    
    
    public SGIPUnbindRespMessage(SGIPHeader header) {
        this.header = header;
    }
    
    
    public SGIPUnbindRespMessage(SGIPUnbindMessage unbind,long node_id) {
        this.header = unbind.getCloneMsgHeader();
        header.setTotalLength(SGIPHeader.LEN_SGIP_HEADER);
        header.setCommandID(SGIPMessage.ID_SGIP_UNBIND_RESP, false , node_id);
    }
    
    
    protected byte[] getMsgBody() {
        return null;
    }


	@Override
	public void resp(OutputStream out,  long node_id) throws IOException {
				
	}
    
}