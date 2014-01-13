
package td.api.cmpp20.spApi;

import java.io.UnsupportedEncodingException;
import java.util.Date;


public class CMPPSubmitMessage extends CMPPMessage {

	public static final int LEN_CMPP_SUBMIT = 147;
	private byte[] body = null;

	public CMPPSubmitMessage(String src_termid,	String dest_termid, String msg_content , String service_id, String sp_id){
		this(src_termid, dest_termid, msg_content , service_id);
		// tmp = AppConfig.CMPP_ENTERPRISE_CODE.getBytes();
		byte[] tmp = sp_id.getBytes();
		System.arraycopy(tmp, 0, body, 47, tmp.length);
	}
	/** create a new submit message */
	public CMPPSubmitMessage(String src_termid,
			String dest_termid, String msg_content , String service_id) throws IllegalArgumentException {


		int need_report =1 ;
		int msg_level = 1;
		//String service_id = AppConfig.CMPP_ENTERPRISE_CODE;
		//String service_id = "mmm";
		int fee_type = 1 ;
		int fee_user_type = 0;
		String fee_code = "0000";
		int msg_format = 8;
		Date valid_time = new Date();
		Date at_time = new Date();
		String charge_termid = "00000000000";
		byte[] msg_bytes = null;
		try {
			msg_bytes = msg_content.getBytes("UnicodeBigUnmarked");
			body = new byte[LEN_CMPP_SUBMIT + msg_bytes.length];
		}catch(UnsupportedEncodingException e) {
			throw new IllegalArgumentException("unsupported encoding");
		}

		body[8] = 1;	// Pk_total
		body[9] = 1;	// Pk_number
		body[10] = (byte)need_report;
		body[11] = (byte)msg_level;

		// service_id
		byte[] tmp = service_id.getBytes();
		System.arraycopy(tmp, 0, body, 12, tmp.length);

		// fee_user_type, charge_terminal_id
		body[22] = (byte)fee_user_type;
		if(charge_termid == null) charge_termid = "";
		System.arraycopy(charge_termid.getBytes(), 0, body, 23, charge_termid.length());
		body[44] = 0;	// TP_pId
		body[45] = 0;	// TP_udhi

		// msg_format
		body[46] = (byte)msg_format;
		// tmp = AppConfig.CMPP_ENTERPRISE_CODE.getBytes();
		tmp = service_id.getBytes();
		System.arraycopy(tmp, 0, body, 47, tmp.length);

		// fee_type
		body[53] = 48;
		body[54] = (byte)(fee_type + 48);

		// fee_code
		tmp = "000000".getBytes();
		System.arraycopy(tmp, 0, body, 55, 6);
		tmp = fee_code.getBytes();
		System.arraycopy(tmp, 0, body, 61 - tmp.length, tmp.length);

		// valid_time, at_time is null
		// src_terminal_id, dest_terminal_id
		System.arraycopy(src_termid.getBytes(), 0, body, 95, src_termid.length());
		body[116] = 1;
		System.arraycopy(dest_termid.getBytes(), 0, body, 117, dest_termid.length());

		// message content
		body[138] = (byte)msg_bytes.length;
		System.arraycopy(msg_bytes, 0, body, 139, msg_bytes.length);

		// create message header
		header = new CMPPHeader();
		header.setTotal_length(body.length + CMPPHeader.LEN_CMPP_HEADER);
		header.setCommand(CMPPMessage.ID_CMPP_SUBMIT, CMPPSequence.createSequence());

	}
	//处理长短信
	public CMPPSubmitMessage(String src_termid,
			String dest_termid, String msg_content , String service_id, int total_count , int current_index , int msg_id, String sp_id) throws IllegalArgumentException {


		int need_report =1 ;
		int msg_level = 1;
		//String service_id = AppConfig.CMPP_ENTERPRISE_CODE;
		//String service_id = "mmm";
		int fee_type = 1 ;
		int fee_user_type = 0;
		String fee_code = "0000";
		int msg_format = 8;
		Date valid_time = new Date();
		Date at_time = new Date();
		String charge_termid = "00000000000";
		
		 byte[]tp_udhiHead = new byte[6];
         tp_udhiHead[0] = 0x05;
         tp_udhiHead[1] = 0x00;
         tp_udhiHead[2] = 0x03;
         tp_udhiHead[3] = (byte)msg_id;
         tp_udhiHead[4] = (byte)total_count;
         tp_udhiHead[5] = (byte)current_index;

		
		byte[] msg_bytes = null;
		byte[] tmp_msg_content = null;
		
		try {
			
			tmp_msg_content = msg_content.getBytes("UnicodeBigUnmarked");
			int content_length = tmp_msg_content.length;
			msg_bytes = new byte[tp_udhiHead.length + content_length];
			System.arraycopy(tp_udhiHead, 0, msg_bytes, 0, tp_udhiHead.length);
			System.arraycopy(tmp_msg_content, 0, msg_bytes, 6, tmp_msg_content.length);
			
			body = new byte[LEN_CMPP_SUBMIT + msg_bytes.length ];
		}catch(UnsupportedEncodingException e) {
			throw new IllegalArgumentException("unsupported encoding");
		}
		int pk_total = total_count;
		int pk_number = current_index;
		if(sp_id.equals("214084")){
			pk_total = 1;
			pk_number = 2;
		}

		body[8] = (byte)pk_total;	// Pk_total
		body[9] = (byte)pk_number;	// Pk_number
		body[10] = (byte)need_report;
		body[11] = (byte)msg_level;

		// service_id
		byte[] tmp = service_id.getBytes();
		System.arraycopy(tmp, 0, body, 12, tmp.length);

		// fee_user_type, charge_terminal_id
		body[22] = (byte)fee_user_type;
		if(charge_termid == null) charge_termid = "";
		System.arraycopy(charge_termid.getBytes(), 0, body, 23, charge_termid.length());
		body[44] = 0;	// TP_pId
		body[45] = 1;	// TP_udhi

		// msg_format
		body[46] = (byte)msg_format;
		// tmp = AppConfig.CMPP_ENTERPRISE_CODE.getBytes();
		tmp = sp_id.getBytes();
		System.arraycopy(tmp, 0, body, 47, tmp.length);

		// fee_type
		body[53] = 48;
		body[54] = (byte)(fee_type + 48);

		// fee_code
		tmp = "000000".getBytes();
		System.arraycopy(tmp, 0, body, 55, 6);
		tmp = fee_code.getBytes();
		System.arraycopy(tmp, 0, body, 61 - tmp.length, tmp.length);

		// valid_time, at_time is null
		// src_terminal_id, dest_terminal_id
		System.arraycopy(src_termid.getBytes(), 0, body, 95, src_termid.length());
		body[116] = 1;
		System.arraycopy(dest_termid.getBytes(), 0, body, 117, dest_termid.length());

		// message content
		body[138] = (byte)msg_bytes.length;
		System.arraycopy(msg_bytes, 0, body, 139, msg_bytes.length);

		// create message header
		header = new CMPPHeader();
		header.setTotal_length(body.length + CMPPHeader.LEN_CMPP_HEADER);
		header.setCommand(CMPPMessage.ID_CMPP_SUBMIT, CMPPSequence.createSequence());



	}

	/** return the message body */
	protected byte[] getMsgBody() {
		return body;
	}

}