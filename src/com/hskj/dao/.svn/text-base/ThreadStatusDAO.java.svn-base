package com.hskj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

import com.hskj.form.ThreadStatus;




/**
 * select all attributes(fields) from the threadControl table
 * @author mmc
 * @time 2011-08-23 10:43
 *
 */
public class ThreadStatusDAO implements IThreadStatusDAO {
	private DataSource dataSource;
	private static Logger logger = Logger.getLogger("serverLog");

	public List<ThreadStatus> queryThreadStatus(String appName, int threadType) {
		Connection conn =null;
		List<ThreadStatus> tdList =new ArrayList<ThreadStatus>();
		ThreadStatus threadStatus =null;
		  try {
			conn = dataSource.getConnection();
			String sql = "select sn,thread_name,thread_id,status,thread_param,thread_type,group_id from thread_controller where  thread_type = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, appName);
			pstmt.setInt(1, threadType);
			ResultSet rset = pstmt.executeQuery();
			while(rset.next()){
				threadStatus = new ThreadStatus();
				threadStatus.setThread_sn(rset.getInt("sn"));
				threadStatus.setGroup_id(rset.getInt("group_id"));
				threadStatus.setStatus(rset.getInt("status"));
				threadStatus.setThread_id(rset.getInt("thread_id"));
				threadStatus.setThread_name(rset.getString("thread_name"));
				threadStatus.setThread_param(rset.getString("thread_param"));
				threadStatus.setThread_type(rset.getInt("thread_type"));
				tdList.add(threadStatus);
			} 
		} catch (SQLException e) {
			logger.error("the ThreadStatusDAO error is:"+e);
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 
		 
		return tdList;
	}
	


	public void changeThreadStatus(int status,int threadId){
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		int thread_type = 1;
		if(status == 6){
			thread_type = 0;
		}
		
		String sql = "update thread_controller set status = ? ,thread_type = "+thread_type+ " where thread_id = ? ";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setInt(2, threadId);
			//pstmt.setString(3, appName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
