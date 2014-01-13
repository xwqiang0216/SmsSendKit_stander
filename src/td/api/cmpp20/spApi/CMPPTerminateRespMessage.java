
package td.api.cmpp20.spApi;


public class CMPPTerminateRespMessage extends CMPPMessage {
    
    public CMPPTerminateRespMessage(CMPPHeader header) {
        this.header = header;
    }
    
    protected byte[] getMsgBody() {
        return null;
    }
    
}