package org.news.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接与关闭工具类。
 * 
 * @author 北大青鸟
 */
public class DatabaseUtil {
    private static final String JDBC_SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=NewsManagerSystem";

	
	public static Connection getConnection(){
		try{
			Class.forName(JDBC_SQLSERVER_DRIVER);
			Connection conn = DriverManager.getConnection(DB_URL, "sa", "123456");
			return conn;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}
	
	//测试
	public static void main(String[] args){
		getConnection();
	}

    /**
     * 关闭数据库连接。
     * 
     * @param conn
     *            数据库连接
     * @param stmt
     *            Statement对象
     * @param rs
     *            结果集
     */
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        // 若结果集对象不为空，则关闭
        try {
            if (rs != null && !rs.isClosed())
                rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若Statement对象不为空，则关闭
        try {
            if (stmt != null && !stmt.isClosed())
                stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 若数据库连接对象不为空，则关闭
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
