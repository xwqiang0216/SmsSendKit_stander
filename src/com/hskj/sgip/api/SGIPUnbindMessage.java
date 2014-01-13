package com.hskj.sgip.api;

import java.io.IOException;
import java.io.OutputStream;






/**
 * @author Chenlz
 */
public class SGIPUnbindMessage extends SGIPMessage {

	public SGIPUnbindMessage(long node_id) {
		header = new SGIPHeader();
		header.setTotalLength(SGIPHeader.LEN_SGIP_HEADER);
		header.setCommandID(SGIPMessage.ID_SGIP_UNBIND, true, node_id);
	}

	public SGIPUnbindMessage(SGIPHeader header) {
		this.header = header;
	}

	protected byte[] getMsgBody() {
		return null;
	}

	@Override
	public void resp(OutputStream out,long node_id) throws IOException {
		new SGIPUnbindRespMessage(this,node_id).write(out);
		
	}

}