package com.hskj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;

import com.hskj.form.Message;
import com.hskj.form.SmsDeliverForm;
import com.hskj.form.SmsMessage;
import com.hskj.form.SmsReceiveReportForm;
import com.hskj.form.SmsReportForm;
import com.hskj.form.SmsReportTmpRecordForm;
import com.hskj.form.SmsSendLastInfoForm;
import com.hskj.log.CommonLogFactory;

import utils.DBUtil;
import utils.InsertSQL;

public class ServerDAO {

	private Log log = CommonLogFactory.getLog(this.getClass());
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 执行sql语句（带参数）
	 * @param sql
	 * @param params
	 */
	public void execute(String sql,Object[] params){
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;params != null&&i<params.length;i++){
				ps.setObject(i+1, params[i]);
			}
			ps.execute();
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps);
		}
	}
	
	
	/**
	 * 执行更新(带参数)
	 * @param sql
	 * @param params
	 */
	public int executeUpdate(String sql,Object[] params){
		
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;params != null&&i<params.length;i++){
				ps.setObject(i+1, params[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps);
		}
		
		return result;
	}


	public SmsReportTmpRecordForm queryTmpRecord(String where,Object[] params){
		
		String sql = "select sn,tmp_msg_id,plate_msg_id,mobile,td_code,customer_sms_sn,service_sms_sn,status,insert_time,update_time,sub_sn,msg_id,customer_sn from sms_report_tmp_record ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			if(where == null){
				where = "";
			}
			ps = conn.prepareStatement(sql+where);
			
			for(int i = 0;params != null&&i<params.length;i++){
				ps.setObject(i+1, params[i]);
			}
			System.out.println("query tmp: "+where);
			log.info("query tmp: "+where);
			rs = ps.executeQuery();
			if(rs.next()){
				
				SmsReportTmpRecordForm form = new SmsReportTmpRecordForm();
				form.setSn(rs.getInt("sn"));
				form.setMobile(rs.getString("mobile"));
				form.setTmp_msg_id(rs.getString("tmp_msg_id"));
				form.setPlate_msg_id(rs.getString("plate_msg_id"));
				form.setTd_code(rs.getString("td_code"));
				form.setCustomer_sms_sn(rs.getInt("customer_sms_sn"));
				form.setService_sms_sn(rs.getInt("service_sms_sn"));
				form.setStatus(rs.getInt("status"));
				form.setInsert_time(rs.getString("insert_time"));
				form.setUpdate_time(rs.getString("update_time"));
				form.setSub_sn(rs.getInt("sub_sn"));
				form.setMsg_id(rs.getString("msg_id"));
				form.setCustomer_sn(rs.getInt("customer_sn"));
				
				return form;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps,rs);
		}
		
		return null;
	}
	
	/**
	 * 插入到临时记录表
	 * @param tmpForm
	 * @return
	 */
	public void insertIntoTmpReport(ArrayList<SmsMessage>  messageList){

		String sql = "insert into sms_report_tmp_record(tmp_msg_id, plate_msg_id,mobile,td_code,customer_sms_sn,service_sms_sn,status,insert_time,update_time,sub_sn,msg_id,customer_sn,customer_id) values(?,?,?,?,?,?,?,now(),now(),?,?,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try{
			conn = dataSource.getConnection();
			InsertSQL inSql = new InsertSQL(sql);
			
			for(SmsMessage  tmpForm: messageList){
				
				if(tmpForm.getMsg_id() == null){
					tmpForm.setMsg_id("");
				}
				String mobile = "";
				for(int i = 0 ; i < tmpForm.getDest_terminal_id().length;i++){
					
					mobile = tmpForm.getDest_terminal_id()[i];
					if(mobile != null && mobile .startsWith("86")){
						mobile = mobile.substring(2);
					}
					inSql.setString(1, tmpForm.getTmp_msg_id());
					inSql.setString(2, tmpForm.getPlate_msg_id());
					inSql.setString(3, mobile);
					inSql.setString(4, tmpForm.getTd_code());
					inSql.setString(5, tmpForm.getOld_sns()[i]);
					inSql.setString(6, tmpForm.getSns()[i]);
					inSql.setInt(7, 0);
					inSql.setInt(8,  tmpForm.getSub_sn());
					inSql.setString(9,  tmpForm.getMsg_id());
					inSql.setInt(10,  tmpForm.getCustomer_sn());
					inSql.setString(11, tmpForm.getCustomer_id());
					inSql.addBatch();
				}
			}
			ps = conn.prepareStatement(inSql.getFinalSql());
			ps.executeUpdate();

		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
				DBUtil.freeConnection(conn, ps);
		}


	}
	
	public boolean insertLastSendInfo(SmsSendLastInfoForm tmpForm) {
		Connection conn = null;
		String sql = "insert into sms_last_send_info (mobile,td_code,customer_sn,code,src_terminal_id,insert_time )values(?,?,?,?,?,now())";
		try{
			conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tmpForm.getMobile());
			stmt.setString(2, tmpForm.getTd_code());
			stmt.setInt(3, tmpForm.getCustomer_sn());
			stmt.setString(4, tmpForm.getCode());
			stmt.setString(5, tmpForm.getSrc_terminal_id());


			return stmt.execute();




		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			try{
				conn.close();
			}catch (Exception e) {

			}
		}

	}
	
	public SmsSendLastInfoForm queryLastSendInfo(String mobile,String src_terminal_id ) {
		SmsSendLastInfoForm tmpForm = null;
		Connection conn = null;
		String sql = "select mobile,td_code,customer_sn,code,src_terminal_id from sms_last_send_info where mobile=?  and src_terminal_id =?";
		try{
			conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, mobile);

			stmt.setString(2, src_terminal_id);


			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				tmpForm = new SmsSendLastInfoForm();
				tmpForm.setCode(rs.getString("code"));
				tmpForm.setCustomer_sn(rs.getInt("customer_sn"));
				tmpForm.setMobile(mobile);
				tmpForm.setTd_code(rs.getString("td_code"));
				tmpForm.setSrc_terminal_id(src_terminal_id);
				
			}
			rs.close();

			return tmpForm;




		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}finally{
			try{
				conn.close();
			}catch (Exception e) {

			}
		}
	}
	
	public boolean updateLastSendInfo(SmsSendLastInfoForm tmpForm) {
		Connection conn = null;
		String sql = "update sms_last_send_info set customer_sn = ? ,code= ? where mobile=?  and src_terminal_id =? ";
		try{
			conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tmpForm.getCustomer_sn());
			stmt.setString(2, tmpForm.getCode());
			stmt.setString(3, tmpForm.getMobile());
			;
			stmt.setString(4, tmpForm.getSrc_terminal_id());


			return stmt.execute();




		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			try{
				conn.close();
			}catch (Exception e) {

			}
		}
	}

	public boolean insertSmsReportForm(SmsReportForm tmpForm) {


		Connection conn = null;
		String sql = "insert into sms_report_info (plate_msg_id,customer_sms_sn,server_sms_sn,status,send_result,result_describe,mobile,msg_id,insert_time,update_time,customer_sn)values(?,?,?,?,?,?,?,?,now(),now(),?)";
		try{
			conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tmpForm.getPlate_msg_id());
			stmt.setInt(2, tmpForm.getCustomer_sms_sn());
			stmt.setInt(3, tmpForm.getServer_sms_sn());
			stmt.setInt(4, tmpForm.getStatus());
			stmt.setInt(5, tmpForm.getSend_result());
			stmt.setString(6, tmpForm.getResult_describe());
			stmt.setObject(7, tmpForm.getMobile());
			stmt.setObject(8, tmpForm.getMsg_id());
			stmt.setObject(9, tmpForm.getCustomer_sn());
			return stmt.execute();




		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}finally{
			try{
				conn.close();
			}catch (Exception e) {

			}
		}

	}
	
	/**
	 * 抓取短信
	 * @param customer_id
	 * 			账户id
	 * @param td_code
	 * 			通道代码
	 * @param number
	 * 			数量
	 * @return
	 */
	public List<Message> fetchSms(String customer_id,String td_code,int number){
		
		List<Message> list = new ArrayList<Message>();
		String sql = "select sn,customer_sn,customer_id,td_code,dest_terminal_id,msg_id,msg_content,insert_time, update_time,send_status,response_status,fail_describe,plate_msg_id,move_flag,old_sn,code,cell_code,charge_count,priority,price from service_sms_info  where send_status = 0 and td_code = ? and  customer_id = ? order by priority desc limit ?";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, td_code);
			ps.setString(2, customer_id);
			ps.setInt(3, number);
			rs = ps.executeQuery();
			
			while(rs.next()){
				Message message = new Message();
				message.setSn(rs.getInt("sn"));
				message.setCustomer_sn(rs.getInt("customer_sn"));
				message.setCustomer_id(rs.getString("customer_id"));
				message.setTd_code(rs.getString("td_code"));
				message.setDest_terminal_id(rs.getString("dest_terminal_id"));
				message.setMsg_content(rs.getString("msg_content"));
				message.setMsg_id(rs.getString("msg_id"));
				message.setInsert_time(rs.getString("insert_time"));
				message.setUpdate_time(rs.getString("update_time"));
				message.setSend_status(rs.getInt("send_status"));
				message.setReponse_status(rs.getInt("response_status"));
				message.setFail_describe(rs.getString("fail_describe"));
				message.setPlate_msg_id(rs.getString("plate_msg_id"));
				message.setMove_flag(rs.getInt("move_flag"));
				message.setOld_sn(rs.getInt("old_sn"));
				message.setCode(rs.getString("code"));
				message.setCell_code(rs.getString("cell_code"));
				message.setCharge_count(rs.getInt("charge_count"));
				message.setPriority(rs.getInt("priority"));
				message.setPrice(rs.getInt("price"));
				
				list.add(message);
			}
			if(list.size()<1){
				return null;
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,ps,rs);
		}
		return null;
	}
	
	
	/**
	 * @param td_code
	 * @param number
	 * @return
	 */
	public List<Message> fetchSms(String td_code,int customer_sn,int number){
		
		List<Message> list = new ArrayList<Message>();
		String sql = "select sn,customer_sn,customer_id,td_code,dest_terminal_id,msg_id,msg_content,insert_time, update_time,send_status,response_status,fail_describe,plate_msg_id,move_flag,old_sn,code,cell_code,charge_count,priority,price from service_sms_info  where send_status = 0 and td_code = ? and customer_sn > ? order by customer_sn limit ?";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, td_code);
			ps.setInt(2, customer_sn);
			ps.setInt(3, number);
			rs = ps.executeQuery();
			
			while(rs.next()){
				Message message = new Message();
				message.setSn(rs.getInt("sn"));
				message.setCustomer_sn(rs.getInt("customer_sn"));
				message.setCustomer_id(rs.getString("customer_id"));
				message.setTd_code(rs.getString("td_code"));
				message.setDest_terminal_id(rs.getString("dest_terminal_id"));
				message.setMsg_content(rs.getString("msg_content"));
				message.setMsg_id(rs.getString("msg_id"));
				message.setInsert_time(rs.getString("insert_time"));
				message.setUpdate_time(rs.getString("update_time"));
				message.setSend_status(rs.getInt("send_status"));
				message.setReponse_status(rs.getInt("response_status"));
				message.setFail_describe(rs.getString("fail_describe"));
				message.setPlate_msg_id(rs.getString("plate_msg_id"));
				message.setMove_flag(rs.getInt("move_flag"));
				message.setOld_sn(rs.getInt("old_sn"));
				message.setCode(rs.getString("code"));
				message.setCell_code(rs.getString("cell_code"));
				message.setCharge_count(rs.getInt("charge_count"));
				message.setPriority(rs.getInt("priority"));
				message.setPrice(rs.getInt("price"));
				
				list.add(message);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,ps,rs);
		}
		return list;
	}
	
	

	
	/**
	 * 从receive_report_info表中抓取接收到的初始状态报告
	 * @param status
	 * @param limit
	 * @return
	 */
	public List<SmsReceiveReportForm> fetchReceiveReport(int status,int limit){
		
		List<SmsReceiveReportForm> list = new ArrayList<SmsReceiveReportForm>();
		
		String sql = "select * from receive_report_info  where status = ? limit ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setInt(2, limit);
			rs = ps.executeQuery();
			
			while(rs.next()){
				SmsReceiveReportForm form = new SmsReceiveReportForm();
				
				form.setSn(rs.getInt("sn"));
				form.setTd_code(rs.getString("td_code"));
				form.setMsg_id(rs.getString("msg_id"));
				form.setMobile(rs.getString("mobile"));
				form.setStatus(rs.getInt("status"));
				form.setResult(rs.getInt("result"));
				form.setFail_describe(rs.getString("fail_describe"));
				form.setInsert_time(rs.getString("insert_time"));
				form.setTry_times(rs.getInt("try_times"));
				
				list.add(form);
			}
				
			if(list.size()<1){
				return null;
			}
				return list;
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps, rs);
		}
		return list;
	}
	
	public List<Message> groupByCustomerIdTdCode(){
		
		List<Message> list = new ArrayList<Message>();
		String sql = "select customer_id,td_code from service_sms_info where send_status =0 group by 1,2";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				Message message = new Message();
				
				message.setCustomer_id(rs.getString("customer_id"));
				message.setTd_code(rs.getString("td_code"));
				list.add(message);
			}
			if(list.size()<1)
				return null;
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,stmt);
		}
		return null;
	}

	public List<Message> groupByCustomerIdTdCode2(){
		
		List<Message> list = new ArrayList<Message>();
		String sql = "select td_code , customer_id from customer_send_count where receive_count > send_count";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				Message message = new Message();
				
				message.setCustomer_id(rs.getString("customer_id"));
				message.setTd_code(rs.getString("td_code"));
				list.add(message);
			}
			if(list.size()<1)
				return null;
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,stmt);
		}
		return null;
	}
	
	
	public void updateSendCount(String customer_id,String td_code,int count){
		
		String sql = "update customer_send_count set send_count = send_count + ? where  customer_id = ? and td_code = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, count);
			ps.setString(2, customer_id);
			ps.setString(3, td_code);
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps);
		}
	}
	public List<String> groupByTdCode(){
		
		List<String> list = new ArrayList<String>();
		String sql = "select td_code from service_sms_info where send_status =0 group by 1";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				
				String td_code = rs.getString("td_code");
				list.add(td_code);
			}
			if(list.size()<1)
				return null;
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,stmt);
		}
		return null;
	}
	/**
	 * 跟该指定sn记录状态
	 * @param status
	 * @param sns
	 */
	public int updateStatus(int status , String sns){
		
		String sql = "update service_sms_info set send_status = ? , update_time = now() where sn in ("+sns+")";
		Connection conn = null;
		PreparedStatement ps = null;
		int i = 0;
		try{
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			i = ps.executeUpdate();

		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, ps);
		}

		
		return i;
	}
	
	public void saveReceiveReport(SmsReportForm report){
		Connection conn = null;
		
		try{
			conn = dataSource.getConnection();
			String sql = "insert into receive_report_info (td_code,msg_id,mobile,status,result , fail_describe , insert_time)values(?,?,?,?,?,?,now())";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			String mobile = report.getMobile();
			if(mobile.startsWith("+86")){
				mobile = mobile.substring(3);
			}else if(mobile.startsWith("86")){
				mobile = mobile.substring(2);
			}

			stmt.setObject(1, report.getTd_code());
			stmt.setObject(2, report.getMsg_id());
			stmt.setObject(3, mobile);
			stmt.setObject(4, 0);
			stmt.setObject(5, report.getSend_result());
			stmt.setObject(6, report.getResult_describe());
			stmt.executeUpdate();
//			log.info("td_code: "+report.getTd_code()+" mobile: "+mobile+" msg_id: "+report.getMsg_id()+" result: "+report.getSend_result()+" fail_desc: "+report.getResult_describe());
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			try{conn.close();}catch (Exception e) {}
		}
	}

	/**
	 * 保存状态报告到sms_report_info表中
	 * @param form
	 */
	public void saveRepoort(SmsReportForm form){
		
		System.out.println("插入状态报告！server_sms_sn: "+form.getServer_sms_sn());
		String sql = "insert into sms_report_info (plate_msg_id,customer_sms_sn,server_sms_sn,status,send_result,result_describe,mobile,msg_id,insert_time,update_time,customer_sn,sub_sn , report_type)values(?,?,?,?,?,?,?,?,now(),now(),?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, form.getPlate_msg_id());
			ps.setInt(2, form.getCustomer_sms_sn());
			ps.setInt(3, form.getServer_sms_sn());
			ps.setInt(4, form.getStatus());
			ps.setInt(5, form.getSend_result());
			ps.setString(6, form.getResult_describe());
			ps.setString(7, form.getMobile());
			ps.setString(8, form.getMsg_id());
			ps.setInt(9, form.getCustomer_sn());
			ps.setInt(10, form.getSub_sn());
			ps.setInt(11, 0);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,ps);
		}
	}

	
	public void saveDeliver(SmsDeliverForm form){
		
		String sql = "insert into receive_deliver_info "
			+ "(td_code,mobile,sp_number,status,customer_id,cell_code,insert_time,update_time,msg_content)"
			+ " values (?,?,?,0,'','',now(),now(),?)";
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, form.getTd_code());
			ps.setString(2, form.getSrc_terminal_id());
			ps.setString(3,form.getDest_mobile());
			ps.setString(4,form.getMsg_content());
	
			ps.execute();
//		String sql = "insert into deliver_sms_info (td_code,src_terminal_id,dest_mobile,status,customer_sn,insert_time,update_time,msg_content,pk_total,pk_number,sub_msg_id,msg_format)" +
//				"values(?,?,?,0,0,now(),now(),?,?,?,?,?)";
		
//		Connection conn = null;
//		PreparedStatement ps = null;
//		try {
//			conn = dataSource.getConnection();
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, form.getTd_code());
//			ps.setString(2, form.getSrc_terminal_id());
//			ps.setString(3,form.getDest_mobile());
//			ps.setString(4,form.getMsg_content());
//			ps.setInt(5,form.getPk_total());
//			ps.setInt(6,form.getPk_number());
//			ps.setInt(7,form.getSub_msg_id());
//			ps.setInt(8,form.getMsg_format());
//			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn,ps);
		}
		
	}
	
public List<SmsDeliverForm> findBystatusInDSInfo() {
		
		List<SmsDeliverForm> list = new ArrayList<SmsDeliverForm>();
		String sql = "select sn, src_terminal_id,customer_sn , cell_code ,dest_mobile, msg_content ,insert_time from deliver_sms_info_bak where status = 1 limit 100";
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				SmsDeliverForm deliverForm = new SmsDeliverForm();
				
				deliverForm.setSn(rs.getInt("sn"));
				deliverForm.setSrc_terminal_id(rs.getString("src_terminal_id"));
				deliverForm.setCustomer_sn(rs.getInt("customer_sn"));
				deliverForm.setCell_code(rs.getString("cell_code"));
				deliverForm.setMsg_content(rs.getString("msg_content"));
				deliverForm.setInsert_time(rs.getString("insert_time"));
				deliverForm.setDest_mobile(rs.getString("dest_mobile"));
				
				list.add(deliverForm);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}finally{
			DBUtil.freeConnection(conn, stmt, rs);
		}
		return list;
	}

	public void updateSendCount(Map<String, Integer> countMap) {
		if(countMap != null && countMap.size() > 0){
			for(String key : countMap.keySet()){
				String [] conditions = key.split("@");
				String td_code = conditions[0];
				String customer_id = conditions[1];
				Integer send_count = countMap.get(key);
				
				String sql = "update customer_send_count set send_count = send_count + " + send_count + " where td_code = '" + td_code + "' and customer_id = '" + customer_id + "'";
				this.executeUpdate(sql, null);
			}
		}
	}

/**
 * 批量插入下发记录表中
 * @param reports
 */
public void insertSubmitCatchByBatch(List<SmsMessage> smsList){

	log.debug("insert by batch");
	
	String sql = "insert ignore into  service_sms_info_catch (sn,customer_sn, customer_id, td_code, dest_terminal_id, msg_id, msg_content, insert_time, update_time, send_status, response_status, fail_describe, plate_msg_id, move_flag, old_sn, code, sub_sn, cell_code, charge_count, priority,price) values(?, ?, ?, ?, ?, ?, ?, now(), now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	Connection conn = null;
	PreparedStatement ps = null;
	
	try {
		conn = dataSource.getConnection();
		InsertSQL inSql = new InsertSQL(sql);
		
		for (int i = 0; smsList != null && i < smsList.size(); i++) {
			
			inSql.setInt(1, Integer.parseInt(smsList.get(i).getSns()[0]));
			inSql.setInt(2, smsList.get(i).getCustomer_sn());
			inSql.setString(3,smsList.get(i).getCustomer_id());
			inSql.setString(4,smsList.get(i).getTd_code());
			inSql.setString(5,smsList.get(i).getDest_terminal_id()[0]);
			inSql.setString(6,smsList.get(i).getMsg_id());
			inSql.setString(7,smsList.get(i).getMsg_content());
			inSql.setInt(8, smsList.get(i).getSend_status());
			inSql.setInt(9, smsList.get(i).getResponse_status());
			inSql.setString(10,smsList.get(i).getFail_describe());
			inSql.setString(11,smsList.get(i).getPlate_msg_id());
			inSql.setInt(12, smsList.get(i).getMove_flag());
			inSql.setInt(13, smsList.get(i).getOld_sn());
			inSql.setString(14,smsList.get(i).getCode());
			inSql.setInt(15,smsList.get(i).getSub_sn());
			inSql.setString(16,smsList.get(i).getCell_code());
			inSql.setInt(17,smsList.get(i).getCharge_count());
			inSql.setInt(18,smsList.get(i).getPriority());
			inSql.setInt(19, smsList.get(i).getPrice());
			inSql.addBatch();
		}
		
		ps = conn.prepareStatement(inSql.getFinalSql());
		ps.executeUpdate();
		
	} catch (SQLException e) {
		log.error(e.getMessage());
		e.printStackTrace();
	}finally{
		
		DBUtil.freeConnection(conn, ps);
	}
}
}
