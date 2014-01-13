package com.hskj.sgip.api;

import java.io.IOException;
import java.io.OutputStream;







public class SGIPDeliverRespMessage extends SGIPMessage {
    
    public static final int LEN_SGIP_DELIVERRESP = 9;
    private byte[] body = null;
    
    public SGIPDeliverRespMessage(SGIPDeliverMessage deliver,long node_id) throws IllegalArgumentException {
        this.header = deliver.getCloneMsgHeader();
        header.setTotalLength(SGIPHeader.LEN_SGIP_HEADER + LEN_SGIP_DELIVERRESP);
        header.setCommandID(SGIPMessage.ID_SGIP_DELIVER_RESP, false, node_id);
        body = new byte[LEN_SGIP_DELIVERRESP];
        body[0] = 0;	// status
    }
    
    
    protected byte[] getMsgBody() {
        return body;
    }


	@Override
	public void resp(OutputStream out, long node_id) throws IOException {
		
	}
    
}