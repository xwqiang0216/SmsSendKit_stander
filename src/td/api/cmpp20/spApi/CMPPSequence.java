
package td.api.cmpp20.spApi;


public class CMPPSequence {
    
    private final static int MIN_SEQ = 0;
    private final static int MAX_SEQ = 0x7fffffff;
    
    private static int seq_index;
    
    public static synchronized int createSequence() {
        if(seq_index == MAX_SEQ) {
            seq_index = MIN_SEQ;
        }
        
        return ++seq_index;	// sequence increment step one
    }
    
}