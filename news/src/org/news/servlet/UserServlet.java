package org.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.news.dao.UserDao;
import org.news.dao.impl.UserDaoImpl;
import org.news.entity.User;

import com.alibaba.fastjson.JSON;

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 7308078748761515673L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String opr = request.getParameter("opr");
    	
    	if ("list".equals(opr)){
    		listUsers(request,response);
    	}else if ("save".equals(opr)){
    		saveUsers(request,response);
    	}else if ("delete".equals(opr)){
    		deleteUsers(request,response);
    	}else if ("modify".equals(opr)){
    		modifyUsers(request,response);
    	}else{
    		checkUser(request,response);
    	}
    	
    }

    private void deleteUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
    	String uid = request.getParameter("uid");
		
		UserDao userDao = new UserDaoImpl();
    	try {
			
			int result = userDao.deleteUser(uid);
			if (result>0){
					response.getWriter().print("删除成功");
			}else{
				response.getWriter().print("删除失败");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	response.getWriter().print("ok");
		
	}

	private void saveUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		
		UserDao userDao = new UserDaoImpl();
    	try {
			User user = new User();
			user.setUname(uname);
			user.setUpwd(upwd);
			int result = userDao.addUser(user);
			if (result>0){
					response.getWriter().print("保存成功");
			}else{
				response.getWriter().print("保存失败");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	response.getWriter().print("ok");
	}

	private void modifyUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		String uid = request.getParameter("uid");
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		
		UserDao userDao = new UserDaoImpl();
    	try {
			User user = new User();
			user.setUid(Integer.parseInt(uid));
			user.setUname(uname);
			user.setUpwd(upwd);
			int result = userDao.modifyUser(user);
			if (result>0){
					response.getWriter().print("删除成功");
			}else{
				response.getWriter().print("删除失败");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	response.getWriter().print("ok");
	}
	
	private void listUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html; charset=utf-8");
    	
    	//创建UserDao
    	UserDao userDao = new UserDaoImpl();
    	try {
			List<User> users = userDao.getAllUsers();
			String json = JSON.toJSONString(users);
			response.getWriter().print(json);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	//获取请求参数
    	String uname = request.getParameter("uname");
    	String pwd = request.getParameter("upwd");
    	
    	//创建UserDao
    	UserDao userDao = new UserDaoImpl();
    	
    	//调用userDao方法，校验用户
    	try {
			User user = userDao.findUser(uname, pwd);
	    	//根据返回值来跳转       
			if (user!= null){
				request.getSession().setAttribute("user", user);
				request.getRequestDispatcher("../newspages/admin.jsp").forward(request, response);	
			}else{
				response.sendRedirect("../index.jsp");				
			}
		} catch (SQLException e) {
			response.sendRedirect("../index.jsp");
		}    	
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.doGet(request, response);
    }

}
