<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.news.dao.*,org.news.dao.impl.*,org.news.entity.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>新闻中国</title>
	<link href="css/main.css" rel="stylesheet" type="text/css" />
</head>
  
  
<div id="header">
  <div id="top_login">
    <form action="util/user" method="post" >
      <input type="hidden" name="opr" value="login"/>
      <label> 登录名 </label>
      <input type="text" name="uname" value="" class="login_input" />
      <label> 密&#160;&#160;码 </label>
      <input type="password" name="upwd" value="" class="login_input" />
      <input type="submit" class="login_sub" value="登录" />
      <label id="error"> </label>
      <img src="images/friend_logo.gif" alt="Google" id="friend_logo" />
    </form>
  </div>
  <div id="nav">
    <div id="logo"> <img src="images/logo.jpg" alt="新闻中国" /> </div>
    <div id="a_b01"> <img src="images/a_b01.gif" alt="" /> </div>
    <!--mainnav end-->
  </div>
</div>
<div id="container">


 
  <div class="main">
    <div class="class_type"> <img src="images/class_type.gif" alt="新闻中心" /> </div>
    <div class="content">
      <ul class="classlist">
      
      
      <%
      //接受一个页号的参数pageno
      String pageno = request.getParameter("pageno");
      int pageNum = 1;
      try{
      	pageNum = Integer.parseInt(pageno);
      }catch(Exception e){
      	pageNum = 1;
      }
      
      NewsDao newsService = new NewsDaoImpl();
      
      //页数量
      int recordCount = newsService.getTotalCount();
      int pageCount = recordCount /10;
      if (recordCount % 10 > 0){  //如果不能整除，页数量还要加1
         pageCount = pageCount + 1;
      }
      
      List<News> newsList = newsService.getPageNewsList(pageNum, 10);
      for(News news: newsList){
       %>
            <li>
            <a href='util/news?opr=readNew&nid={<%=news.getNid()%>}'><%=news.getNtitle()%></a>
            <span><%=news.getNcreatedate() %></span>
            </li>
            
       <%} %>     
     
     		<li>
     		<%if (pageNum>1){ %>
     			<a href="pageindex.jsp?pageno=<%=(pageNum-1)%>">上一页</a>
     		<%}%>	
     		
     		<%if (pageNum < pageCount){ %>
     			<a href="pageindex.jsp?pageno=<%=(pageNum+1)%>">下一页</a>
     		<%} %>

				<!-- 循环生成分页链接 -->	
				当前页<%=pageNum%>共<%=pageCount%>页
				<%for(int i=1;i<=pageCount;i++){ %>
     				<a href="pageindex.jsp?pageno=<%=i%>"><%=i%></a>
     			<%} %>
     			

     		</li>
      </ul>
    </div>

  </div>
</div>
  <%@ include file="index-elements/index_bottom.html"%>
<body>1</body>
</html>
