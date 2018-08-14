package org.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.news.dao.NewsDao;
import org.news.dao.UserDao;
import org.news.dao.impl.NewsDaoImpl;
import org.news.dao.impl.TopicsDaoImpl;
import org.news.dao.impl.UserDaoImpl;
import org.news.entity.News;
import org.news.entity.Topic;
import org.news.entity.User;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("serial")
public class ApiServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String opr = request.getParameter("opr");
		if ("topic".equals(opr)){
			TopicsDaoImpl dao = new TopicsDaoImpl();
			try{
				List<Topic> list = dao.getAllTopics();
				String json = JSON.toJSONString(list);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}else if ("newslist".equals(opr)){
			NewsDao dao = new NewsDaoImpl();
			String tid = request.getParameter("tid");
			try{
				List<News> list = dao.getAllnewsByTID(Integer.parseInt(tid));
				String json = JSON.toJSONString(list);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}else if ("readnews".equals(opr)){
			NewsDao dao = new NewsDaoImpl();
			String nid = request.getParameter("nid");
			try{
				News news = dao.getNewsByNID(Integer.parseInt(nid));
				String json = JSON.toJSONString(news);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}else if ("login".equals(opr)){
			UserDao dao = new UserDaoImpl();
			String uname = request.getParameter("uname");
			String upwd = request.getParameter("upwd");
			try{
				User user = dao.findUser(uname, upwd);
				String json = JSON.toJSONString(user);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}else if ("register".equals(opr)){
			UserDao dao = new UserDaoImpl();
			String uname = request.getParameter("uname");
			String upwd = request.getParameter("upwd");
			try{
				User user = new User();
				user.setUname(uname);
				user.setUpwd(upwd);
				int uid = dao.addUser(user);
				user.setUid(uid);
				String json = JSON.toJSONString(user);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
		else if ("modifyuser".equals(opr)){
			UserDao dao = new UserDaoImpl();
			String uname = request.getParameter("uname");
			String upwd = request.getParameter("upwd");
			try{
				User user = new User();
				user.setUname(uname);
				user.setUpwd(upwd);
				int uid = dao.modifyUser(user);
				user.setUid(uid);
				String json = JSON.toJSONString(user);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
		else if ("listuser".equals(opr)){
			UserDaoImpl dao = new UserDaoImpl();
			try{
				List<User> list =dao.getAllUsers();
				String json = JSON.toJSONString(list);
				out.print(json);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request, response);
		
	}

}
