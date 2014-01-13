package td.api.cmpp30;


import java.util.ArrayList;

import com.hskj.form.SmsMessage;

import td.api.cmpp30.spApi.CMPPMessage;

public class Cmpp30MessageUtil {
	private SmsMessage smsMessage;
	private CMPPMessage cmppMessage;
	private ArrayList<SmsMessage> mobile_List ;
	
	
	
	
	private long submit_time;
	private int try_times;
	
	
	public int getTry_times() {
		return try_times;
	}
	public void setTry_times(int tryTimes) {
		try_times = tryTimes;
	}
	public SmsMessage getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(SmsMessage smsMessage) {
		this.smsMessage = smsMessage;
	}
	
	
	public CMPPMessage getCmppMessage() {
		return cmppMessage;
	}
	public void setCmppMessage(CMPPMessage cmppMessage) {
		this.cmppMessage = cmppMessage;
	}
	public long getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(long submit_time) {
		this.submit_time = submit_time;
	}
	public ArrayList<SmsMessage> getMobile_List() {
		return mobile_List;
	}
	public void setMobile_List(ArrayList<SmsMessage> mobile_List) {
		this.mobile_List = mobile_List;
	}
}
