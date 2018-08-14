package org.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.news.dao.TopicsDao;
import org.news.dao.impl.TopicsDaoImpl;
import org.news.entity.Topic;


public class TopicServlet extends HttpServlet {
    private static final long serialVersionUID = -8823896301195695638L;
    private TopicsDao topicDao;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	topicDao = new TopicsDaoImpl();
    	response.setContentType("text/html; charset=utf-8");
        String opr = request.getParameter("opr");
        if ("add".equals(opr)){
        	addTopic(request,response);        	
        }
        else if ("list".equals(opr)){
        	listTopic(request,response);
        }else if ("modify".equals(opr)){
        	modifyTopic(request,response);
        } else if ("del".equals(opr)){
        	deleteTopic(request,response);
        }else if ("listjson".equals(opr)){
        	listJson(request,response);
        }
    }

    
    private void listJson(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
		try {
			List<Topic> list = topicDao.getAllTopics();
			//[{"tid":1,"tname":"国内"}，"tid":2,"tname":"国际"}]
			String result = null;
			for(Topic t: list){
				String s = String.format("{\"tid\":%d,\"tname\":\"%s\"}", t.getTid(),t.getTname());
				if (result == null){
					result = "";
				}else{
					result += ",";
				}
				result = result + s;
			}
			result = "[" + result + "]";
			response.getWriter().print(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void deleteTopic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 删除主题
		// 获取参数tid
    	String tid = request.getParameter("tid");
    	
    	// 创建TopicsDao,调用deleteTopic方法
    	TopicsDao dao = new TopicsDaoImpl();
    	int result = 0;
    	try {
			result = dao.deleteTopic(Integer.parseInt(tid));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	// 返回javascript，提供提示成功/失败
    	if (result>0){
    		String s ="<script>alert('删除成功');location.href='topics?opr=list';</script>";
    		response.getWriter().println(s);
    	}else{
    		String s ="<script>alert('删除失败');location.href='topics?opr=list';</script>";
    		response.getWriter().println(s);    		
    	}
	}


	private void modifyTopic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String tid = request.getParameter("tid");
		String tname = request.getParameter("tname");
		Topic topic = new Topic();
		topic.setTid(Integer.parseInt(tid));
		topic.setTname(tname);
		
		int result =0 ;
		try {
			result = this.topicDao.updateTopic(topic);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result > 0){
			//成功
			response.getWriter().println("<script>alert('成功');location.href='topics?opr=list';</script>");
		}else{
			//失败
			response.getWriter().println("<script>alert('失败,主题重复!');location.href='topics?opr=list';</script>");
		}
	}


	private void listTopic(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 准备列表的数据
    	try {
			List<Topic> list = this.topicDao.getAllTopics();
			request.setAttribute("list", list);
			request.getRequestDispatcher("../newspages/topic_list.jsp").forward(request, response);
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	private void addTopic(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 添加
    	String tname = request.getParameter("tname");
    	int result = 0;
		try {
			result = this.topicDao.addTopic(tname);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		if (result > 0){
			//成功
			response.getWriter().println("<script>alert('成功');location.href='../newspages/topic_add.jsp';</script>");
		}else{
			//失败
			response.getWriter().println("<script>alert('失败,主题重复!');location.href='../newspages/topic_add.jsp';</script>");
		}
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.doGet(request, response);
    }

}
