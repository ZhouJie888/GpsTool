package org.news.dao;


import java.sql.SQLException;
import java.util.List;

import org.news.entity.User;

public interface UserDao{
	//查找是否登录成功
	public User findUser(String uname, String password)  throws SQLException;	
	public List<User> getAllUsers() throws SQLException;
	public int addUser(User user) throws SQLException;
	public int deleteUser(String uid) throws SQLException;
	public int modifyUser(User user) throws SQLException;
}