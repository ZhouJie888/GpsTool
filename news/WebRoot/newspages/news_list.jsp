
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加主题--管理后台</title>
<link href="../css/admin.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div id="header">
  <div id="welcome">
    欢迎使用新闻管理系统！</div>
  <div id="nav">
    <div id="logo"><img src="../images/logo.jpg" alt="新闻中国" /></div>
    <div id="a_b01"><img src="../images/a_b01.gif" alt="" /></div>
  </div>
</div>
<div id="admin_bar">
  <div id="status">管理员： 登录  &#160;&#160;&#160;&#160; <a href="#">login out</a></div>
  <div id="channel"> </div>
</div>

  <div class="main">
  <%@ include file="console_element/left.html" %>
  
  <div id="opt_area">

		<!-- 使用迭代标签，把标题栏的链接显示出来 -->
			<c:forEach items="${list}" var="topic" varStatus="status">
				<li style="float:left;margin:10px">
					<a href ="../util/news?op=list&tid=${topic.tid}">${topic.tname }</a> 
				</li>
			</c:forEach>


	<ul class="classlist">
		<!-- 新闻列表 -->
		<c:forEach items="${list1}" var="news" varStatus="status">
			<li> 
				<a href="../util/news?op=edit&nid=${news.nid}">编辑</a>
				<a href="../util/news?op=del&nid=${news.nid}">删除</a>
				${news.ntitle}
				${news.ncreatedate} 
			</li>
		</c:forEach>
		
      </ul>
      
      <p align = "center">
      	第${pageno}页/共${pageCount}页  
      <c:if test="${pageno > 1}">
      	<a href ="index.jsp?tid=${param.tid}&pageno=${pageno - 1}">上一页</a> 
      </c:if>
      
      <c:if test="${pageno < pageCount}">
      	<a href="index.jsp?tid=${param.tid}&pageno=${pageno + 1}">下一页</a>
      </c:if>
      </p>
    </div>
</div>

  
   <%@ include file="console_element/bottom.html" %>
    
</body>
</html>
