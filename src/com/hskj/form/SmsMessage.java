package com.hskj.form;

public class SmsMessage {
	private int sn;
	private int customer_sn;
	private String 	customer_id;
	private String td_code;
	private String[] dest_terminal_id;
	private String src_termainal_id;
	private String msg_id;
	private String msg_content;
	private String insert_time;
	private String update_time;
	private int send_status;
	private int response_status;
	private String fail_describe;
	private String plate_msg_id;
	private int move_flag;
	private String code;//发送号码
	private int old_sn;
	private String tmp_msg_id = "";// 每个通道不同处理临时
	private String [] sns;
	private String [] old_sns;
	private String send_msg_id; //不同通道的临时msg_id
	private int sub_sn;
	private String zhdx_id;
	private String zhdx_pwd;
	private String zhdx_send_pwd;
	private String key;
	private String adc_url;
	private String adc_service_id;
	
	private String cell_code;//小号
	private String full_sp_number;
	
	private boolean isSaveLastSendInfo ;//是否记录最后的发送人信息用于处理上行
	
	private boolean isGroupLast ; // 是否是该批的最后一条
	
	private boolean has_report ;
	
	private long sgip_msg_add_time ;
	private String mobile;
	
	private String adc_user_id;
	private String adc_user_pwd;
	private int adc_sms_count;
	private int is_zjdx;
	private boolean isUseOtherTdResend = false;
	
	private int try_times;
	
	private int charge_count;
	
	private int priority;
	private int price;
	
	
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCharge_count() {
		return charge_count;
	}

	public void setCharge_count(int charge_count) {
		this.charge_count = charge_count;
	}

	public int getTry_times() {
		return try_times;
	}

	public void setTry_times(int tryTimes) {
		try_times = tryTimes;
	}

	/**
	 * @return the adc_service_id
	 */
	public String getAdc_service_id() {
		return adc_service_id;
	}
	
	public int getAdc_sms_count() {
		return adc_sms_count;
	}
	
	
	
	
	/**
	 * @return the adc_url
	 */
	public String getAdc_url() {
		return adc_url;
	}
	public String getAdc_user_id() {
		return adc_user_id;
	}
	public String getAdc_user_pwd() {
		return adc_user_pwd;
	}
	public String getCell_code() {
		return cell_code;
	}
	public String getCode() {
		return code;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public int getCustomer_sn() {
		return customer_sn;
	}
	public String[] getDest_terminal_id() {
		return dest_terminal_id;
	}
	public String getFail_describe() {
		return fail_describe;
	}
	public String getInsert_time() {
		return insert_time;
	}
	public int getIs_zjdx() {
		return is_zjdx;
	}
	public String getKey() {
		return key;
	}
	public String getMobile() {
		return mobile;
	}
	public int getMove_flag() {
		return move_flag;
	}
	public String getMsg_content() {
		return msg_content;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public int getOld_sn() {
		return old_sn;
	}
	public String[] getOld_sns() {
		return old_sns;
	}
	public String getPlate_msg_id() {
		return plate_msg_id;
	}
	public int getResponse_status() {
		return response_status;
	}
	public String getSend_msg_id() {
		return send_msg_id;
	}
	public int getSend_status() {
		return send_status;
	}
	public long getSgip_msg_add_time() {
		return sgip_msg_add_time;
	}
	public int getSn() {
		return sn;
	}
	public String[] getSns() {
		return sns;
	}
	public String getSrc_termainal_id() {
		return src_termainal_id;
	}
	public int getSub_sn() {
		return sub_sn;
	}
	public String getTd_code() {
		return td_code;
	}
	public String getTmp_msg_id() {
		return tmp_msg_id;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public String getZhdx_id() {
		return zhdx_id;
	}
	public String getZhdx_pwd() {
		return zhdx_pwd;
	}
	public String getZhdx_send_pwd() {
		return zhdx_send_pwd;
	}
	public boolean isGroupLast() {
		return isGroupLast;
	}
	public boolean isHas_report() {
		return has_report;
	}
	public boolean isSaveLastSendInfo() {
		return isSaveLastSendInfo;
	}
	public boolean isUseOtherTdResend() {
		return isUseOtherTdResend;
	}
	/**
	 * @param adc_service_id the adc_service_id to set
	 */
	public void setAdc_service_id(String adc_service_id) {
		this.adc_service_id = adc_service_id;
	}
	public void setAdc_sms_count(int adc_sms_count) {
		this.adc_sms_count = adc_sms_count;
	}
	/**
	 * @param adc_url the adc_url to set
	 */
	public void setAdc_url(String adc_url) {
		this.adc_url = adc_url;
	}
	

	public void setAdc_user_id(String adc_user_id) {
		this.adc_user_id = adc_user_id;
	}
	public void setAdc_user_pwd(String adc_user_pwd) {
		this.adc_user_pwd = adc_user_pwd;
	}
	public void setCell_code(String cell_code) {
		this.cell_code = cell_code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public void setCustomer_sn(int customer_sn) {
		this.customer_sn = customer_sn;
	}
	
	public void setDest_terminal_id(String[] dest_terminal_id) {
		this.dest_terminal_id = dest_terminal_id;
	}
	public void setFail_describe(String fail_describe) {
		this.fail_describe = fail_describe;
	}
	public void setGroupLast(boolean isGroupLast) {
		this.isGroupLast = isGroupLast;
	}
	public void setHas_report(boolean has_report) {
		this.has_report = has_report;
	}
	public void setInsert_time(String insert_time) {
		this.insert_time = insert_time;
	}
	public void setIs_zjdx(int is_zjdx) {
		this.is_zjdx = is_zjdx;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setMove_flag(int move_flag) {
		this.move_flag = move_flag;
	}
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public void setOld_sn(int old_sn) {
		this.old_sn = old_sn;
	}
	public void setOld_sns(String[] old_sns) {
		this.old_sns = old_sns;
	}
	public void setPlate_msg_id(String plate_msg_id) {
		this.plate_msg_id = plate_msg_id;
	}
	public void setResponse_status(int response_status) {
		this.response_status = response_status;
	}
	public void setSaveLastSendInfo(boolean isSaveLastSendInfo) {
		this.isSaveLastSendInfo = isSaveLastSendInfo;
	}
	public void setSend_msg_id(String send_msg_id) {
		this.send_msg_id = send_msg_id;
	}
	public void setSend_status(int send_status) {
		this.send_status = send_status;
	}
	public void setSgip_msg_add_time(long sgip_msg_add_time) {
		this.sgip_msg_add_time = sgip_msg_add_time;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public void setSns(String[] sns) {
		this.sns = sns;
	}
	public void setSrc_termainal_id(String src_termainal_id) {
		this.src_termainal_id = src_termainal_id;
	}
	public void setSub_sn(int sub_sn) {
		this.sub_sn = sub_sn;
	}
	public void setTd_code(String td_code) {
		this.td_code = td_code;
	}
	public void setTmp_msg_id(String tmp_msg_id) {
		this.tmp_msg_id = tmp_msg_id;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public void setUseOtherTdResend(boolean isUseOtherTdResend) {
		this.isUseOtherTdResend = isUseOtherTdResend;
	}
	public void setZhdx_id(String zhdx_id) {
		this.zhdx_id = zhdx_id;
	}
	public void setZhdx_pwd(String zhdx_pwd) {
		this.zhdx_pwd = zhdx_pwd;
	}
	public void setZhdx_send_pwd(String zhdx_send_pwd) {
		this.zhdx_send_pwd = zhdx_send_pwd;
	}

	public void setFull_sp_number(String full_sp_number) {
		this.full_sp_number = full_sp_number;
	}

	public String getFull_sp_number() {
		String result = "";
		if(full_sp_number != null){
			result = full_sp_number;
		}else{
			result = code + cell_code;
		}
		return result;
	}
	
}
