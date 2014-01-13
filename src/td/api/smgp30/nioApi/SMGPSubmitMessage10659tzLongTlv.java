package td.api.smgp30.nioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;




public class SMGPSubmitMessage10659tzLongTlv extends SMGPMessage {		
	public static final int LEN_SGMGP_SUBMIT = 114+12;
	private byte[] body = null;

	private int subType = 6;//�·�����
	private int is_need_report = 1;//�Ƿ���Ҫ״̬����
	private int priority = 0;
	private String service_id="help";//ҵ�����
	private String fee_type = "01" ;//�շ�����
	private String fee_code = "0"; //�ʷѴ���
	private int msg_format = 8;//����Ϣ��ʽ
	private String valid_date = "";//��Чʱ��;
	private String at_time = "";//��ʱ����
	private String src_terminal_id = "";//�����û�����
	private String charge_terminal_id = "";//�Ʒ��û�����
	private int dest_terminal_count  = 1;//�����û�����
	private String dest_terminal_id = "";//�����û�����
	private int msg_length = 0;//�������ݳ���

	private String reserve ;//�����ֶ�

	public SMGPSubmitMessage10659tzLongTlv(String src_terminal_id,String dest_terminal_id ,String msg_content , String service_id, String spid) throws UnsupportedEncodingException{
		this.dest_terminal_id = dest_terminal_id;
		this.src_terminal_id = src_terminal_id;
		this.msg_length = msg_content.getBytes("UnicodeBigUnmarked").length;
//		this.charge_terminal_id = src_terminal_id;
		this.charge_terminal_id = dest_terminal_id;
		this.service_id = service_id;
		
		String tmpMobile[] = dest_terminal_id.split(",");
		body = new byte[LEN_SGMGP_SUBMIT + msg_length + 21 * tmpMobile.length];
		body[0] = (byte)subType;
		body[1] = (byte)is_need_report;
		body[2] = (byte)priority;
		byte[] tmp = service_id.getBytes("GBK");
		System.arraycopy(tmp, 0, body, 3, tmp.length);
		tmp = fee_type.getBytes();
		System.arraycopy(tmp, 0, body, 13, tmp.length);
		tmp = fee_code.getBytes();
		System.arraycopy(tmp, 0, body, 15, tmp.length);
		String fixedFee = "0";
		tmp = fixedFee.getBytes();
		System.arraycopy(tmp, 0, body, 21, tmp.length);
		
		body[27] = (byte)msg_format;
		
		//tmp = valid_date.getBytes();
		//System.arraycopy(tmp, 0, body, 28, tmp.length);
		//tmp = at_time.getBytes();
		//System.arraycopy(tmp, 0, body, 45, tmp.length);
		
		tmp = this.src_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 62, tmp.length);
		tmp = charge_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 83, tmp.length);
		body[104] = (byte)tmpMobile.length;
        int i = 0;
        for(i = 0; i < tmpMobile.length; i++)
            System.arraycopy(tmpMobile[i].getBytes(), 0, body, 105 + i * 21, tmpMobile[i].length());
		
        
        int loc = 105 + i * 21;
		
		
        
		body[loc] = (byte)msg_length;

		tmp = msg_content.getBytes("UnicodeBigUnmarked");
		System.arraycopy(tmp, 0, body, loc + 1	, tmp.length);
		
//		System.out.println("locλ�ã�"+loc);
//		System.out.println("body length:"+body.length);
		
		//loc = loc+tmp.length;
		loc = loc + 1 + tmp.length + 8;
		//TLV MsgSrc
		tmp = integerToByte(0x0010);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		String SPID = spid;
		
		tmp = integerToByte(8);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		tmp = SPID.getBytes("GBK");
		System.arraycopy(tmp, 0, body, loc	, tmp.length);
		//debugData("body:",body);
		header = new SMGPHeader();
		header.setPacket_length(body.length + SMGPHeader.LEN_SMGP_HEADER);
		header.setRequest_id(SMGPMessage.ID_SMGP_SUBMIT, true);


	}
	
	public SMGPSubmitMessage10659tzLongTlv(String src_terminal_id,String dest_terminal_id ,String msg_content , String service_id, String spid,int total,int index,int msg_id) throws UnsupportedEncodingException{
		this.dest_terminal_id = dest_terminal_id;
		this.src_terminal_id = src_terminal_id;
		this.msg_length = msg_content.getBytes("UnicodeBigUnmarked").length;
		this.charge_terminal_id = dest_terminal_id;
		this.service_id = service_id;
		
		String tmpMobile[] = dest_terminal_id.split(",");
		body = new byte[LEN_SGMGP_SUBMIT + msg_length + 6 + 15 + 21 * tmpMobile.length ];
		
		//System.out.println("body length is:"+(LEN_SGMGP_SUBMIT + msg_length + 6 + 15 + 21 * tmpMobile.length ));
		body[0] = (byte)subType;
		body[1] = (byte)is_need_report;
		body[2] = (byte)priority;
		byte[] tmp = service_id.getBytes("GBK");
		System.arraycopy(tmp, 0, body, 3, tmp.length);
		tmp = fee_type.getBytes();
		System.arraycopy(tmp, 0, body, 13, tmp.length);
		tmp = fee_code.getBytes();
		System.arraycopy(tmp, 0, body, 15, tmp.length);
		String fixedFee = "0";
		tmp = fixedFee.getBytes();
		System.arraycopy(tmp, 0, body, 21, tmp.length);
		
		body[27] = (byte)msg_format;
		
		//tmp = valid_date.getBytes();
		//System.arraycopy(tmp, 0, body, 28, tmp.length);
		//tmp = at_time.getBytes();
		//System.arraycopy(tmp, 0, body, 45, tmp.length);
		
		tmp = this.src_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 62, tmp.length);
		tmp = charge_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 83, tmp.length);
		body[104] = (byte)tmpMobile.length;
        int i = 0;
        for(i = 0; i < tmpMobile.length; i++)
            System.arraycopy(tmpMobile[i].getBytes(), 0, body, 105 + i * 21, tmpMobile[i].length());
		
        
        int loc = 105 + i * 21;
		
		msg_length = msg_length+6;
		
		body[loc] = (byte)msg_length;
        
		tmp = msg_content.getBytes("UnicodeBigUnmarked");
		body[loc + 1] = 0x05;
		body[loc + 2] = 0x00;
		body[loc + 3] = 0x03;
		body[loc + 4] = (byte)msg_id;
		body[loc + 5] = (byte)total;
		body[loc + 6] = (byte)index;
		System.arraycopy(tmp, 0, body, loc + 7	, tmp.length);
		
		loc = loc+7+tmp.length+8;
		//�������ŵ�TP_udhi TLV
		tmp= integerToByte(0x0002);
		System.arraycopy(tmp, 2, body, loc, 2);
		loc = loc+2;
		
		tmp=integerToByte(1);
		System.arraycopy(tmp, 2, body, loc	, 2);
		loc = loc+2;
		tmp= integerToByte(1);
		System.arraycopy(tmp, 3, body, loc	, 1);
		
		loc  = loc +1;
		//�������ŵ�PkTotal TLV
		tmp= integerToByte(0x0009);
		System.arraycopy(tmp, 2, body, loc, 2);
		loc = loc+2;
		tmp=integerToByte(1);
		System.arraycopy(tmp, 2, body, loc	, 2);
		loc = loc+2;
		tmp= integerToByte(total);
		System.arraycopy(tmp, 3, body, loc	, 1);
		loc = loc +1;
		//�������ŵ�PkNumber TLV
		tmp= integerToByte(0x000a);
		System.arraycopy(tmp, 2, body, loc, 2);
		loc = loc+2;
		tmp=integerToByte(1);
		System.arraycopy(tmp, 2, body, loc	, 2);
		loc = loc+2;
		tmp= integerToByte(index);
		System.arraycopy(tmp, 3, body, loc	, 1);
		loc = loc +1;
		
		
		tmp = integerToByte(0x0010);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		String SPID = spid;
		
		tmp = integerToByte(8);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		tmp = SPID.getBytes("GBK");
		System.arraycopy(tmp, 0, body, loc	, tmp.length);
		//System.out.println("locλ�ã�"+loc);
		//System.out.println("body length:"+body.length);
		
		/*//loc = loc+tmp.length;
		loc = loc + 1 + tmp.length + 8;
		//TLV MsgSrc
		tmp = integerToByte(0x0010);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		String SPID = "12169999";
		
		tmp = integerToByte(8);
		System.arraycopy(tmp, 2, body, loc	, 2);
		
		loc = loc + 2;
		
		tmp = SPID.getBytes("GBK");
		System.arraycopy(tmp, 0, body, loc	, tmp.length);
		loc  = loc+tmp.length;
		*/
		//�������ŵ�TP_udhi TLV
		
		
/*		byte []tmpInt = integerToByte(0x02);
		body[body.length - 5] = tmpInt[2];
		body[body.length - 4] = tmpInt[3];
		tmpInt = integerToByte(0x01);
		body[body.length - 3] = (byte)1;
		body[body.length - 2] = tmpInt[2];
		body[body.length - 1] = tmpInt[3];
		

		//�������ŵ�PkTotal TLV
		tmpInt = integerToByte(0x09);
		body[body.length - 10] = tmpInt[2];
		body[body.length - 9] = tmpInt[3];
		tmpInt = integerToByte(0x01);
		body[body.length - 8] = (byte)total;
		body[body.length - 7] = tmpInt[2];
		body[body.length - 6] = tmpInt[3];
		
		
		//�������ŵ�PkNumber TLV
		tmpInt = integerToByte(0x0a);
		body[body.length - 15] = tmpInt[2];
		body[body.length - 14] = tmpInt[3];
		tmpInt = integerToByte(0x01);
		body[body.length - 13] = (byte)index;
		body[body.length - 12] = tmpInt[2];
		body[body.length - 11] = tmpInt[3];
		
		*/
		
//		debugData("body:",body);
		header = new SMGPHeader();
		header.setPacket_length(body.length + SMGPHeader.LEN_SMGP_HEADER);
		header.setRequest_id(SMGPMessage.ID_SMGP_SUBMIT, true);


	}
	
	
	
	public static void debugData(String desc,byte[] data){
		int count=0;
	      for(int i=0;i<data.length;i++){
	    	 int b=data[i];
	    	  if(b<0){b+=256;}
	    	 String hexString= Integer.toHexString(b);
	hexString = (hexString.length() == 1) ? "0" + hexString : hexString;
	    	 System.out.print(hexString+"  ");
	    	 count++;
	    	 if(count%4==0){
	    		 System.out.print( "  ");
	    	 }
	    	 if(count%16==0){
	    		 System.out.println();
	    	 }
	      }
	      System.out.println();
    }
	@Override
	protected byte[] getMsgBody() {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public void resp(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}

}
