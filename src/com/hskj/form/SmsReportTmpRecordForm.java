
package com.hskj.form;

/**
 * @author Chenlz 
 *
 */
public class SmsReportTmpRecordForm {
	private int sn;
	private String tmp_msg_id;
	private String plate_msg_id;
	private String mobile;
	private String td_code;
	private int customer_sms_sn;
	private int service_sms_sn;
	private int status;
	private String insert_time;
	private String update_time;
	private int customer_sn;
	
	
	private int send_result;
	private String [] mobiles;
	private String[] customer_sms_sns;
	private String[] service_sms_sns;
	private String src_number;
	private int sub_sn;
	private String code;
	
	
	
	private String msg_id;
	
	
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public int getSub_sn() {
		return sub_sn;
	}
	public void setSub_sn(int sub_sn) {
		this.sub_sn = sub_sn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSrc_number() {
		return src_number;
	}
	public void setSrc_number(String src_number) {
		this.src_number = src_number;
	}
	public String[] getCustomer_sms_sns() {
		return customer_sms_sns;
	}
	public void setCustomer_sms_sns(String[] customer_sms_sns) {
		this.customer_sms_sns = customer_sms_sns;
	}
	public String[] getService_sms_sns() {
		return service_sms_sns;
	}
	public void setService_sms_sns(String[] service_sms_sns) {
		this.service_sms_sns = service_sms_sns;
	}
	public int getSend_result() {
		return send_result;
	}
	public void setSend_result(int send_result) {
		this.send_result = send_result;
	}
	public String[] getMobiles() {
		return mobiles;
	}
	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getTmp_msg_id() {
		return tmp_msg_id;
	}
	public void setTmp_msg_id(String tmp_msg_id) {
		this.tmp_msg_id = tmp_msg_id;
	}
	public String getPlate_msg_id() {
		return plate_msg_id;
	}
	public void setPlate_msg_id(String plate_msg_id) {
		this.plate_msg_id = plate_msg_id;
	}
	public String getTd_code() {
		return td_code;
	}
	public void setTd_code(String td_code) {
		this.td_code = td_code;
	}
	public int getCustomer_sms_sn() {
		return customer_sms_sn;
	}
	public void setCustomer_sms_sn(int customer_sms_sn) {
		this.customer_sms_sn = customer_sms_sn;
	}
	public int getService_sms_sn() {
		return service_sms_sn;
	}
	public void setService_sms_sn(int service_sms_sn) {
		this.service_sms_sn = service_sms_sn;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(String insert_time) {
		this.insert_time = insert_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public int getCustomer_sn() {
		return customer_sn;
	}
	public void setCustomer_sn(int customer_sn) {
		this.customer_sn = customer_sn;
	}
	
	
	
}
