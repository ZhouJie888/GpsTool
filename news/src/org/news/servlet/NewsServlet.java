package org.news.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.news.dao.CommentsDao;
import org.news.dao.NewsDao;
import org.news.dao.TopicsDao;
import org.news.dao.impl.CommentsDaoImpl;
import org.news.dao.impl.NewsDaoImpl;
import org.news.dao.impl.TopicsDaoImpl;
import org.news.entity.Comment;
import org.news.entity.News;
import org.news.entity.Topic;


public class NewsServlet extends HttpServlet {
    private static final long serialVersionUID = 7679716260193021854L;

    /*
     * 实现新闻列表
     * 1、读取参数tid
     * 2、通过newsDao读取主题下的新闻，放入request,setattribute
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String op = request.getParameter("op");
    	if ("readnews".equals(op)){
    		readNews(request,response);
    		return;
    	}else if("toAddNews".equals(op)){
    		readTopics(request,response);
    		return;
    	}else if ("addNews".equals(op)){
    		try {
				addNews(request,response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return;
    	}else if ("list".equals(op)){
    		listNews(request,response);
    		return;
    	}else if ("edit".equals(op)){
    		editNews(request,response);
    		return;
    	}
    	
    	prepareListData(request, response);
    	    	
    	//跳转
    	request.getRequestDispatcher("../index.jsp").forward(request, response);
    }

	private void editNews(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException {
		// 编辑新闻前的数据准备
		String nid = request.getParameter("nid");
		NewsDao dao = new NewsDaoImpl();
		News news=null;
		try{
			news = dao.getNewsByNID(Integer.parseInt(nid));
		}catch(Exception e){
			e.printStackTrace();	
		}
		request.setAttribute("news", news);
		request.getRequestDispatcher("../newspages/news_modify.jsp").forward(request, response);
	}

	private void prepareListData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//读取标题列表
  	TopicsDao dao = new TopicsDaoImpl();
  	try {
		List<Topic> list = dao.getAllTopics();
		
		//保存列表到请求中，request setAttribute
		request.setAttribute("list", list);
		
		String tid = request.getParameter("tid");
		int tidValue;
		if (tid==null || "".equals(tid)){
			tidValue = 1;
		}else{
			tidValue = Integer.parseInt(tid);
		}
		
		//页号参数
		String pageno = request.getParameter("pageno");
		int pagenoValue = 1;
		if (pageno != null){
			pagenoValue = Integer.parseInt(pageno);
		}
		NewsDao newsDao = new NewsDaoImpl();
		List<News> list1 = newsDao.getPageNewsListByTID(pagenoValue, 5, tidValue);
		request.setAttribute("list1", list1);
		
		//读取总的页数
		int totalCount = newsDao.getNewsCountByTID(tidValue);
		if (totalCount%5 == 0){
			totalCount = totalCount/5;
		}else{
			totalCount = totalCount/5 + 1;
		}
		
		//总页数
		request.setAttribute("pageCount", totalCount);
		//页号
		request.setAttribute("pageno", pagenoValue);
		
} catch (SQLException e) {
		e.printStackTrace();
		request.getRequestDispatcher("error.jsp").forward(request, response);
}
	}

    private void listNews(HttpServletRequest request,
			HttpServletResponse response) throws IOException,ServletException {
    	
    	prepareListData(request, response);
    	
    	//跳转
    	request.getRequestDispatcher("../newspages/news_list.jsp").forward(request, response);
		
	}

	/**
     * @param request
     * @param response
     * @throws Exception
     */
    private void addNews(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		// 添加新闻
		//UploadFileServelt保存文件；创建NewsDao,接收参数，创建News的实体
    	FileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	List<FileItem> fileItems = upload.parseRequest(request);
    	News news = new News();
    	for(FileItem item : fileItems){
    		if (item.isFormField()){
    			//普通表单字段
    			if (item.getFieldName().equals("ntid")){
    				news.setNtid(Integer.parseInt(item.getString()));    				
    			}else if (item.getFieldName().equals("ntitle")){
    				news.setNtitle(item.getString());
    			}else if (item.getFieldName().equals("nauthor")){
    				news.setNauthor(item.getString());
    			}else if (item.getFieldName().equals("nsummary")){
    				news.setNsummary(item.getString());
    			}else if (item.getFieldName().equals("ncontent")){
    				news.setNcontent(item.getString());
    			}
    		}else{
    			//文件
    			String path = request.getRealPath("upload");
    			String fileName = UUID.randomUUID().toString()+item.getName();
    			item.write(new File(path+"\\"+fileName));
    			news.setNpicpath(fileName);
    		}
    	}
    	news.setNcreatedate(new java.util.Date());
    	news.setNmodifydate(new java.util.Date());
    	NewsDao newsDao = new NewsDaoImpl();
    	newsDao.addNews(news);
    	response.getWriter().println("<script>alert('ok');</script>");
	}

	private void readTopics(HttpServletRequest request,
			HttpServletResponse response) {
		// 读取标题列表
    	TopicsDao topicsDao = new TopicsDaoImpl();
    	try {
			List<Topic> list = topicsDao.getAllTopics();
			request.setAttribute("list", list);
	    	request.getRequestDispatcher("../newspages/news_add.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void readNews(HttpServletRequest request,
			HttpServletResponse response) {
		//读取某条新闻的内容和评论
		String nid = request.getParameter("nid");
		int nidValue = Integer.parseInt(nid);
		
		NewsDao newsDao = new NewsDaoImpl();
		try {
			News news = newsDao.getNewsByNID(nidValue);
			request.setAttribute("news", news);
			
			//评论
			CommentsDao commentDao = new CommentsDaoImpl();
			List<Comment> list = commentDao.getCommentsByNid(nidValue);
			request.setAttribute("list", list);
			
			request.getRequestDispatcher("../shownews.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.doGet(request, response);
    }

}
