
package td.api.cmpp20.spApi;


public class CMPPTerminateMessage extends CMPPMessage {
    
    
    public CMPPTerminateMessage() {
        header = new CMPPHeader();
        header.setTotal_length(CMPPHeader.LEN_CMPP_HEADER);
        header.setCommand(CMPPMessage.ID_CMPP_TERMINATE, CMPPSequence.createSequence());
    }
    
	public CMPPTerminateMessage(CMPPHeader header, byte[] body) throws IllegalArgumentException {
		this.header = header;


		header = getCloneMsgHeader(); 
        header.setTotal_length(CMPPHeader.LEN_CMPP_HEADER);
        header.setCommand(CMPPMessage.ID_CMPP_TERMINATE, CMPPSequence.createSequence());
		
	}
    
    
    protected byte[] getMsgBody() {
        return null;
    }
    
}