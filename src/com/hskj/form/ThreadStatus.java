package com.hskj.form;



public class ThreadStatus {
	private int thread_sn;
	private String thread_name;//�����ļ��е�id
	private int thread_id;//ÿ�������߳��Լ���id�����ظ�
	private int status;//0�ر�  1 ׼���������� 2 �����߳���������  3������  4׼���ر�  5�����߳����ڹر� 6�ѹر�
	private String thread_param;//������Ϣ
	private int thread_type; //0ɨ���߳� 1�����߳�  2��sendŲ��catch��dealFileToCatch��3���������̣߳�dealFileToCatch��4���ж��ļ���receive_deliver(dealDeliverSaveThread)
		//5״̬������ļ���receive_report_info��dealReportSaveThread�� 6 �������̷߳ŵ�histroy�����̣߳�dealDeliverMsgToHistory�� 7������ڵ�û�л�����״̬���棬ʱ��������ǰ�ģ�ʵʱ����dealCatchExpireThread��
		//8��ȡ���ͨ������Ϣ����ļ� ��checkedMsgToFileThread�� 9״̬����ƥ�䴦���̣߳�dealReportThread��
	private int group_id;//����ɨ���̸߳�ֵ����thread_id
	
	
	public int getThread_sn() {
		return thread_sn;
	}
	public void setThread_sn(int thread_sn) {
		this.thread_sn = thread_sn;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int groupId) {
		group_id = groupId;
	}
	public String getThread_param() {
		return thread_param;
	}
	public void setThread_param(String threadParam) {
		thread_param = threadParam;
	}
	
	public String getThread_name() {
		return thread_name;
	}
	public void setThread_name(String threadName) {
		thread_name = threadName;
	}
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int threadId) {
		thread_id = threadId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getThread_type() {
		return thread_type;
	}
	public void setThread_type(int threadType) {
		thread_type = threadType;
	}
	
	
	
}
