/**
 * 
 */
package com.hskj.form;


/**
 * Descirption:对应sms_report_info表
 * 
 *@author licuan<lichuan3992413@gmail.com>
 *
 *create at:   2011-5-25 下午04:23:05 
 */
public class SmsReportForm {
	
	private int sn;
	private int customer_sms_sn;
	private int server_sms_sn;
	private int status;
	private int send_result;
	private String result_describe;
	private String plate_msg_id;
	private int sub_sn;
	private String msg_id;
	private String mobile;
	private int customer_sn;
	private String td_code;
	private String seq;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getTd_code() {
		return td_code;
	}

	public void setTd_code(String td_code) {
		this.td_code = td_code;
	}

	public int getCustomer_sn() {
		return customer_sn;
	}

	public void setCustomer_sn(int customer_sn) {
		this.customer_sn = customer_sn;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getSub_sn() {
		return sub_sn;
	}

	public void setSub_sn(int sub_sn) {
		this.sub_sn = sub_sn;
	}

	public String getPlate_msg_id() {
		return plate_msg_id;
	}

	public void setPlate_msg_id(String plate_msg_id) {
		this.plate_msg_id = plate_msg_id;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public int getCustomer_sms_sn() {
		return customer_sms_sn;
	}

	public void setCustomer_sms_sn(int customer_sms_sn) {
		this.customer_sms_sn = customer_sms_sn;
	}

	public int getServer_sms_sn() {
		return server_sms_sn;
	}

	public void setServer_sms_sn(int server_sms_sn) {
		this.server_sms_sn = server_sms_sn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSend_result() {
		return send_result;
	}

	public void setSend_result(int send_result) {
		this.send_result = send_result;
	}

	public String getResult_describe() {
		return result_describe;
	}

	public void setResult_describe(String result_describe) {
		this.result_describe = result_describe;
	}

}
