package com.hskj.form;


/**
 * Descirption:对应数据表service_sms_info model
 * 
 *@author licuan<lichuan3992413@gmail.com>
 *
 *create at:   2011-5-23 下午04:00:20 
 */
public class Message {

	
	private int sn;
	private int customer_sn;
	private String customer_id;
	private String td_code;
	private String dest_terminal_id;
	private String msg_id;
	private String msg_content;
	private String insert_time;
	private String update_time;
	private int send_status;
	private int reponse_status;
	private String fail_describe;
	private String plate_msg_id;
	private int move_flag;
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
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public int getCustomer_sn() {
		return customer_sn;
	}
	public void setCustomer_sn(int customerSn) {
		customer_sn = customerSn;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getTd_code() {
		return td_code;
	}
	public void setTd_code(String tdCode) {
		td_code = tdCode;
	}
	public String getDest_terminal_id() {
		return dest_terminal_id;
	}
	public void setDest_terminal_id(String destTerminalId) {
		dest_terminal_id = destTerminalId;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msgId) {
		msg_id = msgId;
	}
	public String getMsg_content() {
		return msg_content;
	}
	public void setMsg_content(String msgContent) {
		msg_content = msgContent;
	}
	public String getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(String insertTime) {
		insert_time = insertTime;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String updateTime) {
		update_time = updateTime;
	}
	public int getSend_status() {
		return send_status;
	}
	public void setSend_status(int sendStatus) {
		send_status = sendStatus;
	}
	public int getReponse_status() {
		return reponse_status;
	}
	public void setReponse_status(int reponseStatus) {
		reponse_status = reponseStatus;
	}
	public String getFail_describe() {
		return fail_describe;
	}
	public void setFail_describe(String failDescribe) {
		fail_describe = failDescribe;
	}
	public String getPlate_msg_id() {
		return plate_msg_id;
	}
	public void setPlate_msg_id(String plateMsgId) {
		plate_msg_id = plateMsgId;
	}
	public int getMove_flag() {
		return move_flag;
	}
	public void setMove_flag(int moveFlag) {
		move_flag = moveFlag;
	}
	public int getOld_sn() {
		return old_sn;
	}
	public void setOld_sn(int oldSn) {
		old_sn = oldSn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCell_code() {
		return cell_code;
	}
	public void setCell_code(String cellCode) {
		cell_code = cellCode;
	}
	private int old_sn;
	private String code;
	private String cell_code;
}
