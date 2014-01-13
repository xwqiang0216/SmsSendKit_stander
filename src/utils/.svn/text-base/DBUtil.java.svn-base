package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	public static void freeConnection( Connection conn) {
		if(null!=conn){
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}

	public static void freeConnection(Connection conn,Statement st) {
		close(st);
		freeConnection(conn);
	}

	public static void freeConnection(Connection conn,Statement st,ResultSet rs) {
		close(rs);
		close(st);
		freeConnection(conn);
	}
	private static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	private static void close(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
