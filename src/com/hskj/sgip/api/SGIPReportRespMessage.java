package com.hskj.sgip.api;

import java.io.IOException;
import java.io.OutputStream;







public class SGIPReportRespMessage extends SGIPMessage {
    
    public static final int LEN_SGIP_REPORTRESP = 9;
    private byte[] body = null;
    
    public SGIPReportRespMessage(SGIPReportMessage report,long node_id) throws IllegalArgumentException {
        this.header = report.getCloneMsgHeader();
        header.setTotalLength(SGIPHeader.LEN_SGIP_HEADER+ LEN_SGIP_REPORTRESP);
        header.setCommandID(SGIPMessage.ID_SGIP_REPORT_RESP, false, node_id);
        body = new byte[LEN_SGIP_REPORTRESP];
        body[0] = 0;	
    }
    
    
    protected byte[] getMsgBody() {
        return body;
    }


	@Override
	public void resp(OutputStream out,long node_id) throws IOException {
	
	}
    
}