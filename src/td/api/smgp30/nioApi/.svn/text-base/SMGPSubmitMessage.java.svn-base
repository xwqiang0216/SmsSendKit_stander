package td.api.smgp30.nioApi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;




public class SMGPSubmitMessage extends SMGPMessage {
	public static final int LEN_SGMGP_SUBMIT = 114;
	private byte[] body = null;

	private int subType = 6;//下发类型
	private int is_need_report = 1;//是否需要状态报告
	private int priority = 0;
	private String service_id="help";//业务代码
	private String fee_type = "00" ;//收费类型
	private String fee_code = "0"; //资费代码
	private int msg_format = 15;//短消息格式
	private String valid_date = "";//有效时间;
	private String at_time = "";//定时发送
	private String src_terminal_id = "";//发送用户号码
	private String charge_terminal_id = "";//计费用户代码
	private int dest_terminal_count  = 1;//接收用户数量
	private String dest_terminal_id = "";//接收用户号码
	private int msg_length = 0;//短信内容长度

	private String reserve ;//保留字段

	public SMGPSubmitMessage(String src_terminal_id,String dest_terminal_id ,String msg_content , String service_id) throws UnsupportedEncodingException{
		this.dest_terminal_id = dest_terminal_id;
		this.src_terminal_id = src_terminal_id;
		this.msg_length = msg_content.getBytes("GBK").length;
		this.charge_terminal_id = src_terminal_id;
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

		tmp = msg_content.getBytes("GBK");
		System.arraycopy(tmp, 0, body, loc + 1	, tmp.length);

		header = new SMGPHeader();
		header.setPacket_length(body.length + SMGPHeader.LEN_SMGP_HEADER);
		header.setRequest_id(SMGPMessage.ID_SMGP_SUBMIT, true);


	}
	/*
	public SMGPSubmitMessage(String src_terminal_id,String dest_terminal_id ,String msg_content) throws UnsupportedEncodingException{
		this.dest_terminal_id = dest_terminal_id;
		this.src_terminal_id = src_terminal_id;
		this.msg_length = msg_content.getBytes("GBK").length;
		this.charge_terminal_id = src_terminal_id;
		
		body = new byte[LEN_SGMGP_SUBMIT+msg_length];
		body[0] = (byte)subType;
		body[1] = (byte)is_need_report;
		body[2] = (byte)priority;
		byte[] tmp = service_id.getBytes("GBK");
		System.arraycopy(tmp, 0, body, 3, tmp.length);
		tmp = fee_type.getBytes();
		System.arraycopy(tmp, 0, body, 13, tmp.length);
		tmp = fee_code.getBytes();
		System.arraycopy(tmp, 0, body, 15, tmp.length);
		body[21] = (byte)msg_format;
		
		tmp = valid_date.getBytes();
		//System.arraycopy(tmp, 0, body, 22, tmp.length);
		//tmp = at_time.getBytes();
		//System.arraycopy(tmp, 0, body, 39, tmp.length);
		
		tmp = this.src_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 56, tmp.length);
		tmp = charge_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 77, tmp.length);
		body[98] = 1;
		tmp = dest_terminal_id.getBytes();
		System.arraycopy(tmp, 0, body, 99	, tmp.length);
		body[120] = (byte)msg_length;

		tmp = msg_content.getBytes();
		System.arraycopy(tmp, 0, body, 121	, tmp.length);

		header = new SMGPHeader();
		header.setPacket_length(body.length + SMGPHeader.LEN_SMGP_HEADER);
		header.setRequest_id(SMGPMessage.ID_SMGP_SUBMIT, true);


	}
*/
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
