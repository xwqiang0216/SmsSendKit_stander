package com.hskj.dao;

import java.util.List;

import com.hskj.form.ThreadStatus;





public interface IThreadStatusDAO {
	List<ThreadStatus> queryThreadStatus(String appName, int threadType);
	public void changeThreadStatus(int status,int threadId);
}
