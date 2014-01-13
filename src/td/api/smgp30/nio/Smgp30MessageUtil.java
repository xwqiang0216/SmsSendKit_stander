package td.api.smgp30.nio;


import com.hskj.form.SmsMessage;

import td.api.smgp30.nioApi.SMGPMessage;

public class Smgp30MessageUtil {
	private SmsMessage smsMessage;
	private SMGPMessage smgpMessage;
	private long submit_time;
	private int tryTimes;
	
	
	public int getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
	public SmsMessage getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(SmsMessage smsMessage) {
		this.smsMessage = smsMessage;
	}
	public SMGPMessage getSmgpMessage() {
		return smgpMessage;
	}
	public void setSmgpMessage(SMGPMessage smgpMessage) {
		this.smgpMessage = smgpMessage;
	}
	public long getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(long submit_time) {
		this.submit_time = submit_time;
	}
	
	
}
