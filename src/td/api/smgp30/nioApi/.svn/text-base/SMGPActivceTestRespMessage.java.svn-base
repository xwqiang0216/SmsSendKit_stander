package td.api.smgp30.nioApi;

import java.io.IOException;
import java.io.OutputStream;

public class SMGPActivceTestRespMessage extends SMGPMessage {

	public SMGPActivceTestRespMessage(SMGPActivceTestMessage test) {
		header = test.getCloneMsgHeader();
		header.setPacket_length(12);
		header.setRequest_id(ID_SMGP_ACTIVE_TEST_RESP, false);
	}
	
	@Override
	protected byte[] getMsgBody() {
		
		return new byte[0];
	}

	@Override
	public void resp(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}

}
