package com.hskj.form;



public class ThreadStatus {
	private int thread_sn;
	private String thread_name;//配置文件中的id
	private int thread_id;//每个发送线程自己的id不能重复
	private int status;//0关闭  1 准备启动运行 2 控制线程正在启动  3已启动  4准备关闭  5控制线程正在关闭 6已关闭
	private String thread_param;//参数信息
	private int thread_type; //0扫描线程 1发送线程  2从send挪到catch表（dealFileToCatch）3处理上行线程（dealFileToCatch）4上行读文件到receive_deliver(dealDeliverSaveThread)
		//5状态报告读文件到receive_report_info（dealReportSaveThread） 6 将上行线程放到histroy表中线程（dealDeliverMsgToHistory） 7处理过期的没有回来的状态报告，时间是两天前的，实时处理（dealCatchExpireThread）
		//8读取审核通过的信息打成文件 （checkedMsgToFileThread） 9状态报告匹配处理线程（dealReportThread）
	private int group_id;//对于扫描线程该值等于thread_id
	
	
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
