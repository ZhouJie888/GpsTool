
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 判断请求是否有标题列表的数据,如果没有跳转到newServlet -->
<c:if test="${empty list }">
	<jsp:forward page="/util/news"></jsp:forward>
</c:if>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新闻中国</title>
<link href="css/main.css" rel="stylesheet" type="text/css" />


</head>

<body>
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
		<!-- 使用迭代标签，把标题栏的链接显示出来 -->
		<ul>
			<c:forEach items="${list}" var="topic" varStatus="status">
				<li style="float:left;margin:10px">
					<a href ="index.jsp?tid=${topic.tid}">${topic.tname }</a> 
				</li>
			</c:forEach>
		</ul>

    <div class="content">

      <ul class="classlist">

		<!-- 新闻列表 -->
		<c:forEach items="${list1}" var="news" varStatus="status">
			<li> 
				<a href="util/news?op=readnews&nid=${news.nid}">${news.ntitle}</a>
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
  </div>
  
    <%@ include file="index-elements/index_bottom.html"%>
    
</body>
</html>
