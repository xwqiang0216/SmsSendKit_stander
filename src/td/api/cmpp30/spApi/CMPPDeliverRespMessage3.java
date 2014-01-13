package td.api.cmpp30.spApi;

public class CMPPDeliverRespMessage3  extends CMPPMessage {

    
    public static final int LEN_CMPP_DELIVERRESP = 12;
    private byte[] body = null;
    
    public CMPPDeliverRespMessage3(CMPPDeliverMessage3 deliver) throws IllegalArgumentException {
        this.header = deliver.getCloneMsgHeader();
        header.setTotal_length(CMPPHeader.LEN_CMPP_HEADER + LEN_CMPP_DELIVERRESP);
        header.setCommand_id(CMPPMessage.ID_CMPP_DELIVER_RESP);
        body = new byte[LEN_CMPP_DELIVERRESP];
        deliver.copyMsgID(body);  
        byte[] result = new byte[4];
        result[0]=0;
        result[1]=0;
        result[2]=0;
        result[3]=0;
		System.arraycopy(result, 0, body,8, result.length);
    }
    
    
    protected byte[] getMsgBody() {
        return body;
    }
}
