
package td.api.cmpp30.spApi;


public class CMPPActiveMessage extends CMPPMessage {
    
    
    /**
     * construct a new Active test message
     */
    public CMPPActiveMessage() {
        header = new CMPPHeader();
        header.setTotal_length(CMPPHeader.LEN_CMPP_HEADER);
        header.setCommand(CMPPMessage.ID_CMPP_ACTIVE, CMPPSequence.createSequence());
    }
    
	public CMPPActiveMessage(CMPPHeader header, byte[] body) throws IllegalArgumentException {
		this.header = header;


		header = getCloneMsgHeader();
        header.setTotal_length(CMPPHeader.LEN_CMPP_HEADER);
        header.setCommand(CMPPMessage.ID_CMPP_ACTIVE, CMPPSequence.createSequence());
	}
    /**
     * get the active message from server(SMG)
     */
    public CMPPActiveMessage(CMPPHeader header) {
        this.header = header;
    }
    
    
    protected byte[] getMsgBody() {
        return null;
    }
    
}