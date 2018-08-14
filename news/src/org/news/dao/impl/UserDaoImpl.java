package org.news.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.news.dao.BaseDao;
import org.news.dao.UserDao;
import org.news.entity.Topic;
import org.news.entity.User;
import org.news.util.DatabaseUtil;

public class UserDaoImpl extends BaseDao implements UserDao {

    public UserDaoImpl() {
        super();
    }

    public User findUser(String uname, String password) throws SQLException {
        ResultSet rs = null;
        User user = null;
        // 根据用户名密码查找匹配的用户
        String sql = "select * from NEWS_USERS where uname=? and upwd=?";
        try {
            rs = this.executeQuery(sql, uname, password);
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUname(uname);
                user.setUpwd(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return user;
    }
    
	public List<User> getAllUsers() throws SQLException{
		 List<User> list = new ArrayList<User>();
	        ResultSet rs = null;
	        // 获取所有主题
	        String sql = "select * from NEWS_USERS";
	        try {
	            rs = executeQuery(sql);
	            User user = null;
	            while (rs.next()) {
	            	user = new User();
	            	user.setUid(rs.getInt("uid"));
	                user.setUname(rs.getString("uname"));
	                user.setUpwd(rs.getString("upwd"));
	                list.add(user);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            DatabaseUtil.closeAll(null, null, rs);
	        }
	        return list;
	}
	
	public int addUser(User user) throws SQLException{
		String sql = "insert into NEWS_USERS(UNAME,UPWD) values(?,?)";
        int result = 0;
        try {
            result = executeUpdate(sql, user.getUname(),user.getUpwd());
            if (result == 1){
            	ResultSet rs = this.executeQuery("select @@identity",new Object[]{});
            	if (rs.next()){
            		return rs.getInt(1);
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
	}
	
	public int deleteUser(String uid) throws SQLException{
		String sql = "delete from NEWS_USERS where uid=?";
        int result = 0;
        try {
            result = executeUpdate(sql, uid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
	}
	
	public int modifyUser(User user) throws SQLException{
		String sql = "update NEWS_USERS set UNAME=?,UPWD=? where uid=?";
        int result = 0;
        try {
            result = executeUpdate(sql, user.getUname(),user.getUpwd(),user.getUid());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
	}
}
