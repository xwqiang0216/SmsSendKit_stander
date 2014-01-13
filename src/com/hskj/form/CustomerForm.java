/**
 * 
 */
package com.hskj.form;

/**
 * @author Chenlz 
 *
 */
public class CustomerForm {
	private int sn ;
	private String customer_name;
	private String customer_id;
	private String customer_pwd;
	private String customer_ip;
	private int status;
	private String insert_time;
	private String update_time;
	private int sms_count;
	private int is_success_charge ;
	private int is_deliver_charge;
	
	
	
	
	
	public int getIs_success_charge() {
		return is_success_charge;
	}
	public void setIs_success_charge(int is_success_charge) {
		this.is_success_charge = is_success_charge;
	}
	public int getIs_deliver_charge() {
		return is_deliver_charge;
	}
	public void setIs_deliver_charge(int is_deliver_charge) {
		this.is_deliver_charge = is_deliver_charge;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_pwd() {
		return customer_pwd;
	}
	public void setCustomer_pwd(String customer_pwd) {
		this.customer_pwd = customer_pwd;
	}
	public String getCustomer_ip() {
		return customer_ip;
	}
	public void setCustomer_ip(String customer_ip) {
		this.customer_ip = customer_ip;
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
	public int getSms_count() {
		return sms_count;
	}
	public void setSms_count(int sms_count) {
		this.sms_count = sms_count;
	}
	
	
	
	
}
